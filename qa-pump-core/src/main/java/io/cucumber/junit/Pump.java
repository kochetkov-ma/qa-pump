package io.cucumber.junit;


import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.junit.runner.Description;
import org.junit.runner.notification.RunNotifier;
import org.junit.runners.ParentRunner;
import org.junit.runners.model.InitializationError;
import org.junit.runners.model.Statement;

@Slf4j
public class Pump extends AbstractPump<Cucumber> {

    public Pump(Class<?> testClass) throws InitializationError {
        super(testClass);
    }

    @Override
    protected Cucumber newCucumberDelegate(Class<?> testClass) throws InitializationError {
        return new Cucumber(testClass);
    }

    @Override
    protected List<ParentRunner<?>> getChildren() {
        return cucumberDelegate.getChildren();
    }

    @Override
    protected Description describeChild(ParentRunner<?> child) {
        return cucumberDelegate.describeChild(child);
    }

    @Override
    protected void runChild(ParentRunner<?> child, RunNotifier notifier) {
        cucumberDelegate.runChild(child, notifier);
    }

    @Override
    protected Statement childrenInvoker(RunNotifier notifier) {
        return cucumberDelegate.childrenInvoker(notifier);
    }
}