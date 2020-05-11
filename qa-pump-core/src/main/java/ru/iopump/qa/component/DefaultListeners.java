package ru.iopump.qa.component;


import static ru.iopump.qa.util.Str.frm;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.joor.Reflect;
import org.springframework.context.event.ContextClosedEvent;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import ru.iopump.qa.cucumber.PumpObjectFactory;
import ru.iopump.qa.cucumber.event.FeatureFinish;
import ru.iopump.qa.cucumber.event.FeatureStart;
import ru.iopump.qa.cucumber.event.ScenarioStart;
import ru.iopump.qa.cucumber.event.TestExecutionStart;
import ru.iopump.qa.spring.scope.Execution;
import ru.iopump.qa.spring.scope.FeatureCodeScope;

@SuppressWarnings("unused")
@Component
@RequiredArgsConstructor
@Slf4j
public class DefaultListeners {

    @EventListener
    public void onContextRefresh(ContextRefreshedEvent event) {
        FeatureCodeScope.setEventPublisher(event.getApplicationContext());
        Execution.setEventPublisher(event.getApplicationContext());
    }

    @EventListener
    public void onContextClosed(ContextClosedEvent mandatoryArg) {
        if (Execution.isStarted()) {
            FeatureCodeScope.getInstance().stop(); // Stop current instance in thread
            FeatureCodeScope.stopScope(); // Stop static
            Execution.assumedStop(); // Stop execution
            Reflect.onClass(PumpObjectFactory.class).call("resetContextUnsafeInternal").get();
        }
        System.out.println(frm( //NOPMD
            "INFO: Test execution has been finished at '{}'\n",
            Execution.getLastFeature()
        ));
    }

    @EventListener
    public void onTestStart(TestExecutionStart testExecutionStart) {
        System.out.println( //NOPMD
            frm("INFO: Test execution has been started from first feature: '{}'\n", testExecutionStart.getFirstFeature())
        );
    }

    @EventListener
    public void onFeatureStart(FeatureStart featureStart) {
        System.out.println(frm("INFO: Feature '{}' has been started now\n", featureStart.getFeature())); //NOPMD
    }

    @EventListener
    public void onFeatureFinish(FeatureFinish featureFinish) {
        System.out.println(frm("INFO: Feature '{}' has been finished now\n", featureFinish.getFeature())); //NOPMD
    }

    @EventListener
    public void onScenarioStart(ScenarioStart scenarioStart) {
        System.out.println( //NOPMD
            frm("INFO: Scenario '{} - {}' has been started now\n",
                scenarioStart.getScenario().getId(),
                scenarioStart.getScenario().getName()
            )
        );
    }
}