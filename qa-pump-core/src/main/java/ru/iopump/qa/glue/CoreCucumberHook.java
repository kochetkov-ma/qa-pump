package ru.iopump.qa.glue;

import io.cucumber.java.Before;
import io.cucumber.java.Scenario;
import java.util.Collection;
import lombok.NonNull;
import org.springframework.context.ApplicationEventPublisher;
import ru.iopump.qa.component.vars.FeatureVars;
import ru.iopump.qa.component.vars.ScenarioVars;
import ru.iopump.qa.cucumber.event.ScenarioStart;
import ru.iopump.qa.spring.scope.Execution;
import ru.iopump.qa.spring.scope.FeatureCodeScope;
import ru.iopump.qa.spring.scope.FeatureSpec;
import ru.iopump.qa.spring.scope.RunnerType;

/**
 * Internal Main Cucumber Hook.
 * With Scenario Scope.
 */
public class CoreCucumberHook {

    private final ApplicationEventPublisher eventPublisher;
    private final Collection<String> directBindings;
    private final ScenarioVars scenarioVars;
    private final FeatureVars featureVars;

    public CoreCucumberHook(ApplicationEventPublisher eventPublisher,
                            Collection<String> directBindings,
                            ScenarioVars scenarioVars,
                            FeatureVars featureVars) {
        this.eventPublisher = eventPublisher;
        this.directBindings = directBindings;
        this.scenarioVars = scenarioVars;
        this.featureVars = featureVars;
        Execution.checkRunner();
    }

    @Before(value = "@StepVars", order = 0)
    public void extractStepVars() {
        directBindings.add(scenarioVars.bindName());
    }

    @Before(value = "@FeatureVars", order = 1)
    public void extractFeatureVars() {
        directBindings.add(featureVars.bindName());
    }

    @Before(order = Integer.MIN_VALUE)
    public void mainBefore(Scenario cucumberScenario) {

        /* Check Runner If no Pump.class junit runner then use CUCUMBER_SINGLE_THREAD and handle Feature Scope here */
        if (Execution.isRunner(RunnerType.CUCUMBER_SINGLE_THREAD)) {
            singleThreadMainCliFeatureScopeHandler(cucumberScenario);
        }
        /* Additional check. Just in case */
        Execution.checkIfStarted();
        /* Fire Spring event on Scenario start. Only after Feature handling above */
        eventPublisher.publishEvent(new ScenarioStart(cucumberScenario));

    }


    //// STATIC INTERNAL ////

    //region Private methods
    private static void singleThreadMainCliFeatureScopeHandler(Scenario cucumberScenario) {
        FeatureCodeScope.getInstance().getActiveFeature()
            .ifPresentOrElse(f -> {
                    if (isNewFeature(f, cucumberScenario)) {
                        FeatureCodeScope.getInstance().stop(); // Stop previous feature scope
                        FeatureCodeScope.getInstance().start(FeatureSpec.fromScenario(cucumberScenario));
                    }
                },
                () -> FeatureCodeScope.getInstance().start(FeatureSpec.fromScenario(cucumberScenario))
            );
    }

    private static boolean isNewFeature(@NonNull FeatureSpec featureFromPrevScenario, @NonNull Scenario newScenario) {
        return !featureFromPrevScenario.getUri().equalsIgnoreCase(newScenario.getUri().toString());
    }
    //endregion
}
