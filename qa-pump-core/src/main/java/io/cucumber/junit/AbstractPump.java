package io.cucumber.junit;


import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.joor.Reflect;
import org.junit.runner.Description;
import org.junit.runner.manipulation.Filter;
import org.junit.runner.manipulation.InvalidOrderingException;
import org.junit.runner.manipulation.NoTestsRemainException;
import org.junit.runner.manipulation.Orderer;
import org.junit.runner.manipulation.Sorter;
import org.junit.runner.notification.RunListener;
import org.junit.runner.notification.RunNotifier;
import org.junit.runners.ParentRunner;
import org.junit.runners.model.InitializationError;
import org.junit.runners.model.RunnerScheduler;
import ru.iopump.qa.cucumber.PumpObjectFactory;
import ru.iopump.qa.spring.scope.FeatureCodeScope;
import ru.iopump.qa.spring.scope.FeatureSpec;
import ru.iopump.qa.spring.scope.RunnerType;

@Slf4j
abstract class AbstractPump<DELEGATE extends ParentRunner<ParentRunner<?>>> extends ParentRunner<ParentRunner<?>> {

    protected final DELEGATE cucumberDelegate;

    public AbstractPump(Class<?> testClass) throws InitializationError {
        super(testClass);
        FeatureCodeScope.stopExecution(); // Reset Feature Context
        Reflect.onClass(PumpObjectFactory.class).call("resetContextUnsafeInternal").get(); // Reset Spring Factory
        FeatureCodeScope.checkRunnerOrSet(null, RunnerType.PUMP_JUNIT); // Add new Runner and start Test execution
        cucumberDelegate = newCucumberDelegate(testClass);
    }

    protected abstract DELEGATE newCucumberDelegate(Class<?> testClass) throws InitializationError;

    @Override
    public void setScheduler(RunnerScheduler scheduler) {
        cucumberDelegate.setScheduler(scheduler);
    }

    @Override
    public Description getDescription() {
        return cucumberDelegate.getDescription();
    }

    @Override
    public void filter(Filter filter) throws NoTestsRemainException {
        cucumberDelegate.filter(filter);
    }

    @Override
    public void sort(Sorter sorter) {
        cucumberDelegate.sort(sorter);
    }

    @Override
    public void order(Orderer orderer) throws InvalidOrderingException {
        cucumberDelegate.order(orderer);
    }

    @Override
    public int testCount() {
        return cucumberDelegate.testCount();
    }

    @Override
    public void run(RunNotifier notifier) {
        notifier.addListener(listener());
        cucumberDelegate.run(notifier);
    }

    @NonNull
    protected RunListener listener() {
        return new RunListener() {

            @Override
            public void testSuiteStarted(Description description) {
                if (description.getTestClass() != null) {
                    log.info("[JUNIT] ROOT JUNIT RUNNER STARTED - " + description);
                } else {
                    FeatureSpec featureSpec = FeatureSpec.fromDescription(description);
                    log.info("[JUNIT] {} {}", "testSuiteStarted", featureSpec);
                    FeatureCodeScope.getInstance().start(featureSpec);
                }
            }

            @Override
            public void testSuiteFinished(Description description) {
                if (description.getTestClass() != null) {
                    log.info("[JUNIT] ROOT JUNIT RUNNER AND LAST FEATURE FINISHED - " + description);
                    FeatureCodeScope.stopExecution();
                } else {
                    log.info("[JUNIT] {} {}", "testSuiteFinished", FeatureCodeScope.getInstance().getActiveFeature());
                    FeatureCodeScope.getInstance().stop();
                }
            }
        };
    }
}