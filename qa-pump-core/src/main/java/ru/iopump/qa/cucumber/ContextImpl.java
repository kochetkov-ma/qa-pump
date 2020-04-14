package ru.iopump.qa.cucumber;

import io.cucumber.core.backend.ObjectFactory;
import io.cucumber.spring.SpringFactory;
import io.github.classgraph.ClassGraph;
import io.github.classgraph.ClassInfoList;
import io.github.classgraph.ScanResult;
import java.util.Collection;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicBoolean;
import lombok.NonNull;
import ru.iopump.qa.exception.PumpException;
import ru.iopump.qa.spring.PumpConfiguration;
import ru.iopump.qa.util.Str;

enum ContextImpl {
    INSTANCE;

    private final AtomicBoolean frozen = new AtomicBoolean();
    private ObjectFactory delegate;
    private CountDownLatch latch;

    ContextImpl() {
        latch = new CountDownLatch(1);
        delegate = new SpringFactory();
        delegate.addClass(pumpSpringConfigurationClass());
    }

    void startSpringContext() throws InterruptedException {
        if (frozen.compareAndSet(false, true)) { // freeze context
            delegate.start();
            latch.countDown(); // Open latch on ready context
        } else {
            latch.await(); // Wait for context starting
        }
    }

    void stopGlue() {
        delegate.stop();
    }

    boolean addClass(Class<?> glueClass) {
        if (frozen.get()) {
            throw new IllegalStateException("You cannot add glue after Spring Context init. Context has been frozen whole execution");
        }
        return delegate.addClass(glueClass);
    }

    <T> T getInstance(Class<T> glueClass) {
        return delegate.getInstance(glueClass);
    }

    void resetUnsafeInternal() {
        delegate = new SpringFactory();
        latch = new CountDownLatch(1);
        frozen.set(false);
    }

    @NonNull
    static Class<? extends PumpConfiguration> pumpSpringConfigurationClass() {
        Collection<Class<PumpConfiguration>> cfgClasses = findImplementations(PumpConfiguration.class);
        if (cfgClasses.isEmpty()) {
            System.out.println("\nThere are no USER classes extended " + PumpConfiguration.class +
                ". Loaded DEFAULT QA Pump configuration: " + DefaultPumpConfiguration.class + "\n");
            return DefaultPumpConfiguration.class;
        }
        if (cfgClasses.size() > 1) {
            throw PumpException.of("Several QA Pump configuration classes found. " +
                    "Keep the only in classpath, please:\n{}",
                Str.toPrettyString(cfgClasses)
            );
        }
        var cfg = cfgClasses.iterator().next();
        System.out.println("\n[PUMP] Using USER Qa Pump configuration: " + cfg + "\n");
        return cfgClasses.iterator().next();
    }

    @NonNull
    static <T> Collection<Class<T>> findImplementations(@SuppressWarnings("SameParameterValue") @NonNull Class<T> abstractClass) {
        try (ScanResult scanResult = new ClassGraph().enableAllInfo()
            .blacklistClasses(DefaultPumpConfiguration.class.getName())
            .scan()) {
            final ClassInfoList controlClasses = scanResult.getSubclasses(abstractClass.getName());
            return controlClasses
                .filter(classInfo -> !classInfo.isAbstract())
                .loadClasses(abstractClass);
        }
    }

    public static class DefaultPumpConfiguration extends PumpConfiguration {
    }
}
