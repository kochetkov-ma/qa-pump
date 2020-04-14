package ru.iopump.qa.glue;

import static ru.iopump.qa.util.Str.frm;

import io.cucumber.java.Before;
import io.cucumber.java.Scenario;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import ru.iopump.qa.cucumber.event.ScenarioStart;
import ru.iopump.qa.spring.scope.FeatureCodeScope;
import ru.iopump.qa.spring.scope.FeatureSpec;
import ru.iopump.qa.spring.scope.RunnerType;

@RequiredArgsConstructor
public class CoreCucumberHook {
    public static final Thread INIT_THREAD = Thread.currentThread();
    private final ApplicationEventPublisher eventPublisher;

    @Before(order = Integer.MIN_VALUE)
    public void mainBefore(Scenario cucumberScenario) {

        /* Check Runner If no Pump.class junit runner then use RunnerType.CLI_SINGLE_THREAD and handle Feature Scope here */
        if (FeatureCodeScope.checkRunnerOrSet(null, RunnerType.CUCUMBER_SINGLE_THREAD)) {
            checkSingleThread();
            singleThreadMainCliFeatureScopeHandler(cucumberScenario);
        }

        /* Fire Spring event on Scenario start. Only after Feature handling above */
        eventPublisher.publishEvent(new ScenarioStart(cucumberScenario));
    }

    //// STATIC INTERNAL ////

    private static void singleThreadMainCliFeatureScopeHandler(Scenario cucumberScenario) {
        FeatureCodeScope.getInstance().getActiveFeature()
            .ifPresentOrElse(f -> {
                    if (isNewFeature(f, cucumberScenario)) {
                        FeatureCodeScope.getInstance().stop();
                        FeatureCodeScope.getInstance().start(FeatureSpec.fromScenario(cucumberScenario));
                    }
                },
                () -> FeatureCodeScope.getInstance().start(FeatureSpec.fromScenario(cucumberScenario))
            );
    }

    private static void checkSingleThread() {
        if (INIT_THREAD != Thread.currentThread()) {
            throw new IllegalStateException(frm(
                "Probably you have run Cucumber test execution in multi-thread mode.\n" +
                    "Pump Framework support this mode only via Pump JUnit runners PumpFeatureParallel.class or PumpScenarioParallel.class" +
                    " or Pump.class sub-classes.\n" +
                    "We assume it because your initial thread for this hook class is not equals current thread.\n" +
                    "Please, use Pump JUnit Runners or disable multi-thread mode.\n" +
                    "INIT_THREAD={} but Thread.currentThread={}", INIT_THREAD, Thread.currentThread()
            ));
        }
    }

    private static boolean isNewFeature(@NonNull FeatureSpec featureFromPrevScenario, @NonNull Scenario newScenario) {
        return !featureFromPrevScenario.getUri().equalsIgnoreCase(newScenario.getUri().toString());
    }
}
