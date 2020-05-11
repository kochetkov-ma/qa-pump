package ru.iopump.qa.spring.scope;

import com.google.common.base.Preconditions;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.Supplier;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.context.ApplicationEventPublisher;
import ru.iopump.qa.cucumber.event.FeatureFinish;
import ru.iopump.qa.cucumber.event.FeatureStart;

@Slf4j
public class FeatureCodeScope {

    private static final PostponeApplicationEventPublisher eventPublisher = new PostponeApplicationEventPublisher();
    private static Supplier<FeatureCodeScope> contextSupplier;
    private static Runnable contextRemove;

    private final Map<String, Object> objects = new HashMap<>(); //NOPMD
    private final Map<String, Runnable> callbacks = new HashMap<>(); //NOPMD

    private FeatureSpec activeFeature;
    private int counter;

    public FeatureCodeScope() {
        if (log.isDebugEnabled()) {
            log.debug("FeatureCodeScope created with thread " + Thread.currentThread());
        }
    }

    //// STATIC ////
    public static void initScope() {
        Execution.checkRunner(); // Runner has already been sat

        final RunnerType activeRunner = Execution.getRunner();
        if (activeRunner == RunnerType.PUMP) {
            final ThreadLocal<FeatureCodeScope> context = ThreadLocal.withInitial(FeatureCodeScope::new);
            contextSupplier = context::get;
            contextRemove = context::remove;
        } else {
            final FeatureCodeScope context = new FeatureCodeScope();
            contextSupplier = () -> context;
            contextRemove = () -> {
            };
        }
    }

    public static FeatureCodeScope getInstance() {
        Preconditions.checkState(contextRemove != null,
            "FeatureCodeScope must be initialized by initScope() before");

        return contextSupplier.get();
    }

    public static void stopScope() {
        contextSupplier = null; //NOPMD
        contextRemove = null; //NOPMD
    }

    public static void setEventPublisher(@NonNull ApplicationEventPublisher eventPublisher) {
        FeatureCodeScope.eventPublisher.redirectTo(eventPublisher);
    }

    //// INSTANCE ////
    public void start(@NonNull FeatureSpec featureSpec) {

        Execution.startIfNot(featureSpec); // Start on FIRST feature or do nothing
        activeFeature = featureSpec; // Set active feature in thread
        eventPublisher.publishEvent(new FeatureStart(featureSpec)); // Fire Spring event on Feature start
        counter++;
    }

    public Optional<FeatureSpec> getActiveFeature() {
        return Optional.ofNullable(activeFeature);
    }

    public String getId() {
        return "cucumber_feature_" + counter;
    }

    public void stop() {

        activeFeature.setStopTime(LocalDateTime.now()); // Visit active feature
        Execution.assumedLastFeature(activeFeature); // This feature may be last or not
        callbacks.values().forEach(Runnable::run); // Execute all internal callbacks
        eventPublisher.publishEvent(new FeatureFinish(activeFeature));
        cleanUp(); // Clear references
    }

    public Object get(String name) {
        return objects.get(name);
    }

    public void put(String name, Object object) {
        objects.put(name, object);
    }

    public Object remove(String name) {
        callbacks.remove(name);
        return objects.remove(name);
    }

    public void registerDestructionCallback(String name, Runnable callback) {
        callbacks.put(name, callback);
    }

    //region Private methods
    private void cleanUp() {
        activeFeature = null; //NOPMD

        callbacks.clear();
        contextRemove.run();
        objects.values().parallelStream().forEach(value -> {
            if (value instanceof DisposableBean) {
                try {
                    ((DisposableBean) value).destroy();
                } catch (Exception e) { //NOPMD
                    if (log.isErrorEnabled()) {
                        log.error("FeatureContext DisposableBean bean destroy error: " + value, e);
                    }
                }
            } else if (value instanceof AutoCloseable) {
                try {
                    ((AutoCloseable) value).close();
                } catch (Exception e) { //NOPMD
                    if (log.isErrorEnabled()) {
                        log.error("FeatureScope AutoCloseable bean destroy error: " + value, e);
                    }
                }
            }
        });
        objects.clear();
    }
    //endregion
}