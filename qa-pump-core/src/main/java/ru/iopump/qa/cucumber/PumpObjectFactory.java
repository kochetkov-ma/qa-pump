package ru.iopump.qa.cucumber;

import static ru.iopump.qa.cucumber.SpringContextLoader.instance;
import static ru.iopump.qa.util.Str.frm;

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
import ru.iopump.qa.spring.scope.Execution;
import ru.iopump.qa.spring.scope.FeatureCodeScope;
import ru.iopump.qa.spring.scope.RunnerType;
import ru.iopump.qa.util.Str;
import ru.iopump.qa.util.VarUtil;

/**
 * QA Pump ObjectFactory. It decorate cucumber Spring object factory {@link SpringFactory}.
 * It adds only one configuration class extended {@link PumpConfiguration} or use default.
 */
@PumpApi("Cucumber Object Factory")
public final class PumpObjectFactory implements ObjectFactory {
    private static final Object INIT_LOCK = new Object();
    private static final boolean suppressFail = Boolean.parseBoolean(
        VarUtil.getOrDefault(PumpConfigKeys.DONT_REFRESH_CONTEXT_ON_SPRING_ERROR_KEY, "true"));
    private static boolean loaded;
    private static Throwable failed;
    private static Thread CREATED_THREAD; //NOPMD

    /**
     * Cucumber create runner on new feature-thread
     * Only one thread using for every feature in single-thread mode
     * But many threads for multi-thread mode. And this ObjectFactor support only Pump runners for multi-thread mode
     */
    public PumpObjectFactory() {
        System.out.println("[PUMP] PumpObjectFactory created from thread " + Thread.currentThread()); //NOPMD
        init();
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

    @SuppressWarnings("ConstantConditions")
    @Override
    public void start() {
        failCheck();
        try {
            instance().startSpringContext();
        } catch (Throwable contextCreatingException) { //NOPMD
            failed = contextCreatingException;
            if (contextCreatingException instanceof RuntimeException) { //NOPMD
                throw (RuntimeException) contextCreatingException;
            } else if (contextCreatingException instanceof Error) { //NOPMD
                throw (Error) contextCreatingException;
            } else if (contextCreatingException instanceof InterruptedException) { //NOPMD
                Thread.currentThread().interrupt();
                throw PumpException.of(contextCreatingException);
            } else {
                throw PumpException.of(contextCreatingException);
            }
        }
    }

    @Override
    public void stop() {
        instance().stopGlue();
    }

    @Override
    public boolean addClass(Class<?> glueClass) {
        return instance().addClass(glueClass);
    }

    @Override
    public <T> T getInstance(Class<T> glueClass) {
        failCheck();
        try {
            return instance().getInstance(glueClass);
        } catch (CucumberBackendException beanCreatingException) {
            failed = beanCreatingException;
            throw beanCreatingException;
        }
    }

    //region Private methods
    private static void checkSingleThread() {
        if (CREATED_THREAD != null && CREATED_THREAD != Thread.currentThread()) {
            throw new IllegalStateException(frm(
                "Probably you have run Cucumber test execution in multi-thread mode.\n" +
                    "Pump Framework support this mode only via Pump JUnit runners PumpFeatureParallel.class or PumpScenarioParallel.class" +
                    " or Pump.class sub-classes.\n" +
                    "We assume it because your initial thread for this hook class is not equals current thread.\n" +
                    "Please, use Pump JUnit Runners or disable multi-thread mode.\n" +
                    "INIT_THREAD={} but Thread.currentThread={}", CREATED_THREAD, Thread.currentThread()
            ));
        }
    }

    /**
     * For internal using.
     * Unfreeze context between JUnit Pump Runner classes executions.
     * Use via reflection only.
     */
    @SuppressWarnings("unused")
    private static void resetContextUnsafeInternal() { //NOPMD
        System.out.println("[PUMP] PumpObjectFactory reset in thread " + Thread.currentThread()); //NOPMD
        loaded = false;
        failed = null; //NOPMD
        SpringContextLoader.dispose();
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

    private static void init() {
        // Pump Runner set Execution runner earlier by itself.
        // If no runner then using Cucumber TestNG or Cucumber JUnit or MianCLI or other extranl Runner
        // For that runner supported only single-thread mode. It means only one PageFactory will be created.
        //
        // Only if runner is not set yet.
        // Set runner as CUCUMBER_SINGLE_THREAD
        // It means this execution run without any PumpRunners or other supported external runners.
        // May be Main CLI or other external Cucumber runner is using now.

        if (!Execution.isRunner(RunnerType.PUMP)) {
            checkSingleThread(); // If not PUMP runner thread must be the same

            if (Execution.isStarted()) {
                // Stop previous execution
                FeatureCodeScope.getInstance().stop(); // Stop current instance in thread
                FeatureCodeScope.stopScope(); // Stop static
                Execution.assumedStop(); // Stop execution
                Execution.setRunner(null); // Clear runner
                resetContextUnsafeInternal(); // Reset context
            }

            if (Execution.setRunnerIfEmpty(RunnerType.CUCUMBER_SINGLE_THREAD)) { // If not PUMP runner thread must be the same
                FeatureCodeScope.initScope(); // Init execution and feature scope
                CREATED_THREAD = Thread.currentThread(); // Set created thread
            }

        } else {
            CREATED_THREAD = null; // Set NULL for Pump runners
        }

        loaded = true; //NOPMD
    }

    private void failCheck() {
        if (failed != null && suppressFail) {
            Thread.currentThread().interrupt();
            throw new EmptyException("Context failed earlier on '{}'", failed.getClass().getSimpleName());
        }
    }
    //endregion
}
