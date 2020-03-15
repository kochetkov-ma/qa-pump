package ru.iopump.qa.cucumber;

import io.cucumber.core.backend.CucumberBackendException;
import io.cucumber.core.backend.ObjectFactory;
import io.cucumber.core.cli.Main;
import io.cucumber.core.options.Constants;
import io.cucumber.spring.SpringFactory;
import io.github.classgraph.ClassGraph;
import io.github.classgraph.ClassInfoList;
import io.github.classgraph.ScanResult;
import java.util.Collection;
import lombok.NonNull;
import ru.iopump.qa.annotation.PumpApi;
import ru.iopump.qa.constants.PumpConfigKeys;
import ru.iopump.qa.exception.EmptyException;
import ru.iopump.qa.exception.PumpException;
import ru.iopump.qa.spring.PumpConfiguration;
import ru.iopump.qa.util.Str;
import ru.iopump.qa.util.VarUtil;

/**
 * QA Pump ObjectFactory. It decorate cucumber Spring object factory {@link SpringFactory}.
 * It adds only one configuration class extended {@link PumpConfiguration} or use default.
 */
@PumpApi("Cucumber Object Factory")
public final class PumpObjectFactory implements ObjectFactory {
    private static boolean suppressFail = Boolean.parseBoolean(
        VarUtil.getOrDefault(PumpConfigKeys.DONT_REFRESH_CONTEXT_ON_SPRING_ERROR_KEY, "true"));
    private static boolean loaded;
    private static Exception failed;
    private final ObjectFactory delegate;

    public PumpObjectFactory() {
        this.delegate = new SpringFactory();
        this.delegate.addClass(pumpSpringConfigurationClass());
        loaded = true;
    }

    @NonNull
    private static Class<? extends PumpConfiguration> pumpSpringConfigurationClass() {
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
    public static <T> Collection<Class<T>> findImplementations(@NonNull Class<T> abstractClass) {
        try (ScanResult scanResult = new ClassGraph().enableAllInfo()
            .blacklistClasses(DefaultPumpConfiguration.class.getName())
            .scan()) {
            final ClassInfoList controlClasses = scanResult.getSubclasses(abstractClass.getName());
            return controlClasses
                .filter(classInfo -> !classInfo.isAbstract())
                .loadClasses(abstractClass);
        }
    }

    public static void checkObjectFactoryLoaded() {
        if (!loaded) {
            throw PumpException.of("Qa Pump Cucumber Object Loader is not loaded!\n" +
                    "You must add '{}' via Cucumber options." +
                    "\nInstructions:\n{}\n" +
                    "Old way: JDK SPI (ServiceLoader) - see JavaDoc '{}'\n",
                PumpObjectFactory.class, getInstruction(), ObjectFactory.class);
        }
    }

    private static String getInstruction() {
        return Str.frm("For Junit Cucumber runner: adjust '@CucumberOptions' value 'objectFactory' to '{}'\n" +
                "In common case: adjust system / env / 'cucumber.properties' property '{}' to '{}'\n" +
                "Also you can use Cucumber CLI options '--object-factory' - details in the file 'Usage.txt' located in classpath " +
                "or see JavaDoc for '{}'",
            PumpObjectFactory.class.getName(),
            Constants.OBJECT_FACTORY_PROPERTY_NAME,
            PumpObjectFactory.class.getName(),
            Main.class);
    }

    @Override
    public void start() {
        if (failed != null && suppressFail) {
            Thread.currentThread().interrupt();
            throw new EmptyException("Context failed earlier on '{}'", failed.getClass().getSimpleName());
        }
        try {
            delegate.start();
        } catch (Exception contextCreatingException) {
            failed = contextCreatingException;
            throw contextCreatingException;
        }
    }

    @Override
    public void stop() {
        delegate.stop();
    }

    @Override
    public boolean addClass(Class<?> glueClass) {
        return delegate.addClass(glueClass);
    }

    @Override
    public <T> T getInstance(Class<T> glueClass) {
        if (failed != null && suppressFail) {
            Thread.currentThread().interrupt();
            throw new EmptyException("Context failed earlier on '{}'", failed.getClass().getSimpleName());
        }
        try {
            return delegate.getInstance(glueClass);
        } catch (CucumberBackendException beanCreatingException) {
            failed = beanCreatingException;
            throw beanCreatingException;
        }
    }

    public static class DefaultPumpConfiguration extends PumpConfiguration {
    }
}
