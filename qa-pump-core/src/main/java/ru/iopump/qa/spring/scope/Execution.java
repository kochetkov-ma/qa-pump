package ru.iopump.qa.spring.scope;

import static ru.iopump.qa.spring.scope.RunnerType.CUCUMBER_SINGLE_THREAD;
import static ru.iopump.qa.spring.scope.RunnerType.PUMP;
import static ru.iopump.qa.util.Str.frm;

import io.cucumber.junit.Pump;
import io.cucumber.junit.PumpFeatureParallel;
import io.cucumber.junit.PumpScenarioParallel;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicBoolean;
import javax.annotation.Nullable;
import lombok.NonNull;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import ru.iopump.qa.cucumber.event.TestExecutionStart;

@SuppressWarnings("RedundantModifiersUtilityClassLombok")
@UtilityClass
@Slf4j
public class Execution {
    private static final AtomicBoolean STARTED = new AtomicBoolean(false);
    private static final PostponeApplicationEventPublisher eventPublisher = new PostponeApplicationEventPublisher();

    private static RunnerType RUNNER_TYPE;

    private static volatile FeatureSpec firstFeature; //NOPMD
    private static volatile FeatureSpec lastFeature; //NOPMD

    public static void setEventPublisher(@NonNull ApplicationEventPublisher eventPublisher) {
        Execution.eventPublisher.redirectTo(eventPublisher);
    }

    public static Optional<FeatureSpec> getFirstFeature() {
        return Optional.ofNullable(firstFeature);
    }

    public static Optional<FeatureSpec> getLastFeature() {
        return Optional.ofNullable(lastFeature);
    }

    public static void startIfNot(@Nullable FeatureSpec firstFeature) {
        boolean notStarted = !STARTED.getAndSet(true);
        if (notStarted) {
            Execution.firstFeature = firstFeature;
            eventPublisher.publishEvent(new TestExecutionStart(firstFeature));
        }
    }

    public static void assumedLastFeature(@Nullable FeatureSpec lastFeature) {
        Execution.lastFeature = lastFeature;
    }

    public static void assumedStop() {
        STARTED.getAndSet(false);
    }

    public static boolean isStarted() {
        return STARTED.get();
    }

    public static RunnerType getRunner() {
        return RUNNER_TYPE;
    }

    public static void setRunner(@Nullable RunnerType newRunnerType) {
        synchronized (STARTED) {
            RUNNER_TYPE = newRunnerType;
        }
    }

    public static boolean isRunner(@Nullable RunnerType expectedRunnerType) {
        synchronized (STARTED) {
            return RUNNER_TYPE == expectedRunnerType;
        }
    }

    public static boolean setRunnerIfEmpty(@NonNull RunnerType newRunnerType) {
        synchronized (STARTED) {
            if (log.isDebugEnabled()) {
                log.debug("Try set runner. Argument: {}. Current value: {}", newRunnerType, getRunner());
            }
            if (RUNNER_TYPE == null) {
                RUNNER_TYPE = newRunnerType;
                return true;
            } else {
                return false;
            }
        }
    }

    public static void checkRunner() {
        final RunnerType runner = RUNNER_TYPE;
        final boolean isUsingSupportedRunners = (runner == CUCUMBER_SINGLE_THREAD || runner == PUMP);

        if (!isUsingSupportedRunners) {
            throw new IllegalStateException(frm(
                "You use unsupported Runner '{}'.\n" +
                    "Pump Framework support only two Cucumber Runners: {} and {}\n" +
                    "You should run your tests via @RunWith({}) or @RunWith({}} or @RunWith({}) with as JUnit tests.\n" +
                    "Also you can use Cucumber Main CLI with Pump Hook CoreCucumberHook.class " +
                    "- just add pkg 'ru.iopump.qa.glue' to your glue",
                runner,
                CUCUMBER_SINGLE_THREAD,
                PUMP,
                Pump.class.getName(),
                PumpFeatureParallel.class.getName(),
                PumpScenarioParallel.class.getName()
            ));
        }
    }

    public static void checkIfStarted() {
        final boolean isStarted = STARTED.get();

        if (!isStarted) {
            throw new IllegalStateException(frm(
                "Execution must be started. Probably we are using unsupported runner or there is pump internal error\n" +
                    "Pump Framework support only two Cucumber Runners: {} and {}\n" +
                    "You should run your tests via @RunWith({}) or @RunWith({}} or @RunWith({}) with as JUnit tests.\n" +
                    "Also you can use Cucumber Main CLI with Pump Hook CoreCucumberHook.class " +
                    "- just add pkg 'ru.iopump.qa.glue' to your glue",
                CUCUMBER_SINGLE_THREAD,
                PUMP,
                Pump.class.getName(),
                PumpFeatureParallel.class.getName(),
                PumpScenarioParallel.class.getName()
            ));
        }
    }
}