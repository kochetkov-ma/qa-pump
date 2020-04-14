package ru.iopump.qa.cucumber;

import static ru.iopump.qa.cucumber.ContextImpl.INSTANCE;

import io.cucumber.core.backend.CucumberBackendException;
import io.cucumber.core.backend.ObjectFactory;
import io.cucumber.core.cli.Main;
import io.cucumber.core.options.Constants;
import io.cucumber.spring.SpringFactory;
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

    private static final boolean suppressFail = Boolean.parseBoolean(
        VarUtil.getOrDefault(PumpConfigKeys.DONT_REFRESH_CONTEXT_ON_SPRING_ERROR_KEY, "true"));
    private static boolean loaded;
    private static Throwable failed;

    public PumpObjectFactory() {
        loaded = true;
    }

    @SuppressWarnings("ConstantConditions")
    @Override
    public void start() {
        failCheck();
        try {
            INSTANCE.startSpringContext();
        } catch (Throwable contextCreatingException) {
            failed = contextCreatingException;
            if (contextCreatingException instanceof RuntimeException) {
                throw (RuntimeException) contextCreatingException;
            } else if (contextCreatingException instanceof Error) {
                throw (Error) contextCreatingException;
            } else if (contextCreatingException instanceof InterruptedException) {
                Thread.currentThread().interrupt();
                throw PumpException.of(contextCreatingException);
            } else {
                throw PumpException.of(contextCreatingException);
            }
        }
    }

    @Override
    public void stop() {
        INSTANCE.stopGlue();
    }

    @Override
    public boolean addClass(Class<?> glueClass) {
        return INSTANCE.addClass(glueClass);
    }

    @Override
    public <T> T getInstance(Class<T> glueClass) {
        failCheck();
        try {
            return INSTANCE.getInstance(glueClass);
        } catch (CucumberBackendException beanCreatingException) {
            failed = beanCreatingException;
            throw beanCreatingException;
        }
    }

    private void failCheck() {
        if (failed != null && suppressFail) {
            Thread.currentThread().interrupt();
            throw new EmptyException("Context failed earlier on '{}'", failed.getClass().getSimpleName());
        }
    }

    /**
     * For internal using.
     * Unfreeze context between JUnit Pump Runner classes executions.
     * Use via reflection only.
     */
    private static void resetContextUnsafeInternal() {
        loaded = false;
        failed = null;
        INSTANCE.resetUnsafeInternal();
    }

    //// STATIC ////
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
}
