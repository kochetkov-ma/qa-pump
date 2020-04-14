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

class SpringContextLoader {
    private static SpringContextLoader INSTANCE;

    private final AtomicBoolean init = new AtomicBoolean();
    private final ObjectFactory delegate;
    private final CountDownLatch latch;

    static synchronized SpringContextLoader instance() {
        if (INSTANCE == null) {
            INSTANCE = new SpringContextLoader();
        }
        return INSTANCE;
    }

    static synchronized void dispose() {
        if (INSTANCE != null) {
            INSTANCE.stopGlue();
        }
        INSTANCE = null;
    }

    SpringContextLoader() {
        latch = new CountDownLatch(1);
        delegate = new SpringFactory();
        delegate.addClass(pumpSpringConfigurationClass());
    }

    void startSpringContext() throws InterruptedException {
        if (init.compareAndSet(false, true)) { // freeze context
            System.out.println("[PUMP] SpringContextLoader context is starting from thread " + Thread.currentThread());
            delegate.start();
            System.out.println("[PUMP] SpringContextLoader context has been started from thread " + Thread.currentThread());
            latch.countDown(); // Open latch on ready context
        } else {
            latch.await(); // Wait for context starting
            System.out.println("[PUMP] SpringContextLoader context has been already started from thread " + Thread.currentThread());
        }
    }

    void stopGlue() {
        delegate.stop();
    }

    boolean addClass(Class<?> glueClass) {
        if (init.get()) return false; // context frozen
        return delegate.addClass(glueClass);
    }

    <T> T getInstance(Class<T> glueClass) {
        return delegate.getInstance(glueClass);
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
