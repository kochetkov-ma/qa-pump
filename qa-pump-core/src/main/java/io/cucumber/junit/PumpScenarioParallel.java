package io.cucumber.junit;

import static ru.iopump.qa.constants.PumpConfigKeys.SCENARIO_THREADS;

import io.cucumber.core.options.RuntimeOptions;
import java.util.Optional;
import java.util.concurrent.ExecutorService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.NotImplementedException;
import org.junit.runners.model.InitializationError;
import org.junit.runners.model.RunnerScheduler;
import ru.iopump.qa.annotation.PumpOptions;
import ru.iopump.qa.util.VarUtil;

/**
 * Experimental multi-thread runner.
 * Parallelize Features on JUnit runner level using {@link RunnerScheduler} with {@link ExecutorService} with fixed thread pool.
 * Then parallelize Scenarios inside their feature thread using cucumber {@link io.cucumber.core.runtime.Runtime} possibilities
 * configured with {@link RuntimeOptions#getThreads()} as well {@link ExecutorService}
 * inside Cucumber {@link io.cucumber.core.runtime.Runtime}.
 */
@Slf4j
public final class PumpScenarioParallel extends PumpFeatureParallel {
    public static final int SCENARIO_THREADS_DEFAULT = 2;

    public PumpScenarioParallel(Class<?> testClass) throws InitializationError {
        super(testClass);
        throw new NotImplementedException("This runner is not implemented yet. Work in progress. Use 'PumpFeatureParallel.class'");
    }

    protected void parseOptions(Class<?> testClass) {
        setThreadCount(VarUtil.get(SCENARIO_THREADS)
            .map(Integer::parseInt)
            .or(() -> Optional.ofNullable(testClass.getAnnotation(PumpOptions.class)).map(PumpOptions::scenarioThreads))
            .orElse(SCENARIO_THREADS_DEFAULT)
        );

        super.parseOptions(testClass);
    }
}