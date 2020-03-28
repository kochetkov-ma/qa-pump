package ru.iopump.qa.cucumber;

import io.cucumber.core.backend.ObjectFactory;
import io.cucumber.core.cli.Main;
import io.cucumber.core.options.Constants;
import io.cucumber.spring.SpringFactory;
import io.github.classgraph.ClassGraph;
import io.github.classgraph.ClassInfoList;
import io.github.classgraph.ScanResult;
import java.util.Collection;
import lombok.NonNull;
import ru.iopump.qa.exception.PumpCoreException;
import ru.iopump.qa.spring.PumpSpringConfiguration;
import ru.iopump.qa.util.Str;

/**
 * QA Pump ObjectFactory. It decorate cucumber Spring object factory {@link SpringFactory}.
 * It adds only one configuration class extended {@link PumpSpringConfiguration} or use default.
 */
public final class PumpObjectFactory implements ObjectFactory {
    private static boolean loaded;
    private final ObjectFactory delegate;

    public PumpObjectFactory() {
        this.delegate = new SpringFactory();
        this.delegate.addClass(pumpSpringConfigurationClass());
        loaded = true;
    }

    @Override
    public void start() {
        delegate.start();
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
        return delegate.getInstance(glueClass);
    }

    @NonNull
    private static Class<? extends PumpSpringConfiguration> pumpSpringConfigurationClass() {
        Collection<Class<PumpSpringConfiguration>> cfgClasses = findImplementations(PumpSpringConfiguration.class);
        if (cfgClasses.isEmpty()) {
            System.out.println("\nThere are no USER classes extended " + PumpSpringConfiguration.class +
                ". Loaded DEFAULT QA Pump configuration: " + DefaultPumpSpringConfiguration.class + "\n");
            return DefaultPumpSpringConfiguration.class;
        }
        if (cfgClasses.size() > 1) {
            throw PumpCoreException.of("Several QA Pump configuration classes found. " +
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
            .blacklistClasses(DefaultPumpSpringConfiguration.class.getName())
            .scan()) {
            final ClassInfoList controlClasses = scanResult.getSubclasses(abstractClass.getName());
            return controlClasses
                .filter(classInfo -> !classInfo.isAbstract())
                .loadClasses(abstractClass);
        }
    }

    public static void checkObjectFactoryLoaded() {
        if (!loaded) {
            throw PumpCoreException.of("Qa Pump Cucumber Object Loader is not loaded!\n" +
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

    public static class DefaultPumpSpringConfiguration extends PumpSpringConfiguration {
    }
}
