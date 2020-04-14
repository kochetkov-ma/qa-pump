package ru.iopump.qa.spring.scope;

import com.google.common.collect.Lists;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Supplier;
import javax.annotation.Nullable;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.joor.Reflect;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationEventPublisher;
import ru.iopump.qa.cucumber.PumpObjectFactory;
import ru.iopump.qa.cucumber.event.FeatureStart;
import ru.iopump.qa.cucumber.event.TestExecutionStart;

@Slf4j
public class FeatureCodeScope {

    private static final AtomicBoolean STARTED = new AtomicBoolean(false);
    private static final List<ApplicationEvent> beforeEventPublisher = Lists.newCopyOnWriteArrayList();

    private static ApplicationEventPublisher eventPublisher;
    private static Supplier<FeatureCodeScope> contextSupplier;
    private static Runnable contextRemove;

    private static volatile FeatureSpec firstFeature; // don't clear on stop
    private static volatile FeatureSpec lastFeature; // don't clear on stop

    private final Map<String, Object> objects = new HashMap<>();
    private final Map<String, Runnable> callbacks = new HashMap<>();

    private FeatureSpec activeFeature;
    private int counter;

    //// STATIC ////
    public static boolean checkRunnerOrSet(@Nullable RunnerType expectedRunnerType,
                                           @NonNull RunnerType newRunnerType) {

        synchronized (STARTED) {
            if (FeatureScope.RUNNER_TYPE == expectedRunnerType) {
                FeatureScope.RUNNER_TYPE = newRunnerType;
                if (newRunnerType == RunnerType.PUMP_JUNIT) {
                    final ThreadLocal<FeatureCodeScope> context = ThreadLocal.withInitial(FeatureCodeScope::new);
                    contextSupplier = context::get;
                    contextRemove = context::remove;
                } else {
                    final FeatureCodeScope context = new FeatureCodeScope();
                    contextSupplier = () -> context;
                    contextRemove = () -> {
                    };
                }
                return true;
            } else {
                return false;
            }
        }
    }

    public static FeatureCodeScope getInstance() {
        synchronized (STARTED) {
            return contextSupplier.get();
        }
    }

    public static boolean isStarted() {
        return STARTED.get();
    }


    public static void stopExecution() {
        STARTED.set(false);
        beforeEventPublisher.clear();
        eventPublisher = null;
        contextSupplier = null;
        contextRemove = null;
        FeatureScope.RUNNER_TYPE = null;
    }

    public static void setApplicationEventPublisher(ApplicationEventPublisher eventPublisher) {
        synchronized (beforeEventPublisher) {
            FeatureCodeScope.eventPublisher = eventPublisher;
            beforeEventPublisher.forEach(eventPublisher::publishEvent);
            beforeEventPublisher.clear();
        }
    }

    public static Optional<FeatureSpec> getFirstFeature() {
        return Optional.ofNullable(firstFeature);
    }

    public static Optional<FeatureSpec> getLastFeature() {
        return Optional.ofNullable(lastFeature);
    }

    //// INSTANCE ////


    public FeatureCodeScope() {
        log.debug("FeatureCodeScope created with thread " + Thread.currentThread());
    }

    public void start(@NonNull FeatureSpec featureSpec) {
        synchronized (beforeEventPublisher) {
            if (STARTED.compareAndSet(false, true)) {
                firstFeature = featureSpec;
                lastFeature = null;

                /* Fire Spring event on Test Execution start */
                if (FeatureCodeScope.eventPublisher != null) {
                    eventPublisher.publishEvent(new TestExecutionStart(featureSpec));
                } else {
                    beforeEventPublisher.add(new TestExecutionStart(featureSpec));
                }
            }

            /* Fire Spring event on Feature start */
            if (FeatureCodeScope.eventPublisher != null) {
                eventPublisher.publishEvent(new FeatureStart(featureSpec));
            } else {
                beforeEventPublisher.add(new FeatureStart(featureSpec));
            }
        }
        activeFeature = featureSpec;
        counter++;
    }

    public Optional<FeatureSpec> getActiveFeature() {
        return Optional.ofNullable(activeFeature);
    }

    public String getId() {
        return "cucumber_feature_" + counter;
    }

    public void stop() {
        activeFeature.setStopTime(LocalDateTime.now());
        lastFeature = activeFeature;
        for (Runnable callback : callbacks.values()) {
            callback.run();
        }
        callbacks.clear();
        cleanUp();
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

    private void cleanUp() {
        activeFeature = null;
        contextRemove.run();
        objects.values().parallelStream().forEach(value -> {
            if (value instanceof DisposableBean) {
                try {
                    ((DisposableBean) value).destroy();
                } catch (Exception e) {
                    log.error("FeatureContext DisposableBean bean destroy error: " + value, e);
                }
            } else if (value instanceof AutoCloseable) {
                try {
                    ((AutoCloseable) value).close();
                } catch (Exception e) {
                    log.error("FeatureScope AutoCloseable bean destroy error: " + value, e);
                }
            }
        });
        objects.clear();
    }

    public void registerDestructionCallback(String name, Runnable callback) {
        callbacks.put(name, callback);
    }
}
