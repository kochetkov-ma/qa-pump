package ru.iopump.qa.component;


import static ru.iopump.qa.util.Str.frm;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.ContextClosedEvent;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import ru.iopump.qa.cucumber.event.FeatureStart;
import ru.iopump.qa.cucumber.event.ScenarioStart;
import ru.iopump.qa.cucumber.event.TestExecutionStart;
import ru.iopump.qa.spring.scope.FeatureCodeScope;

@Component
@RequiredArgsConstructor
@Slf4j
public class DefaultListeners {

    @EventListener
    public void onContextRefresh(ContextRefreshedEvent event) {
        FeatureCodeScope.setApplicationEventPublisher(event.getApplicationContext());
    }

    @EventListener
    public void onContextClosed(ContextClosedEvent event) {
        if (FeatureCodeScope.isStarted()) {
            FeatureCodeScope.getInstance().stop();
            FeatureCodeScope.stopExecution();
        }
        System.out.println(frm(
            "INFO: Test execution has been finished at '{}'\n",
            FeatureCodeScope.getLastFeature()
        ));
    }

    @EventListener
    public void onTestStart(TestExecutionStart testExecutionStart) {
        System.out.println(
            frm("INFO: Test execution has been started from first feature: '{}'\n", testExecutionStart.getFirstFeature())
        );
    }

    @EventListener
    public void onFeatureStart(FeatureStart featureStart) {
        System.out.println(frm("INFO: Feature '{}' has been started now\n", featureStart.getFeature()));
    }

    @EventListener
    public void onScenarioStart(ScenarioStart scenarioStart) {
        System.out.println(
            frm("INFO: Scenario '{} - {}' has been started now\n",
                scenarioStart.getScenario().getId(),
                scenarioStart.getScenario().getName()
            )
        );
    }
}