package io.cucumber.junit;

import static ru.iopump.qa.constants.PumpConfigKeys.FEATURE_THREADS;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import java.util.Optional;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.junit.runners.model.InitializationError;
import org.junit.runners.model.RunnerScheduler;
import ru.iopump.qa.annotation.PumpOptions;
import ru.iopump.qa.util.VarUtil;

/**
 * Feature multi-thread runner for JUnit.
 * Parallelize Features on JUnit runner level using {@link RunnerScheduler} with {@link ExecutorService} with fixed thread pool.
 * All related scenarios execute sequentially by the Feature's thread.
 */
@Slf4j
public class PumpFeatureParallel extends Pump {

    public static final int FEATURE_THREADS_DEFAULT = 4;
    @Setter
    @Getter
    private int threadCount = FEATURE_THREADS_DEFAULT;

    public PumpFeatureParallel(Class<?> testClass) throws InitializationError {
        super(testClass);
        setScheduler(runnerScheduler(threadCount, threadFactory()));
    }

    protected void parseOptions(Class<?> testClass) {
        setThreadCount(VarUtil.get(FEATURE_THREADS)
            .map(Integer::parseInt)
            .or(() -> Optional.ofNullable(testClass.getAnnotation(PumpOptions.class)).map(PumpOptions::featureThreads))
            .orElse(FEATURE_THREADS_DEFAULT)
        );
    }

    private static RunnerScheduler runnerScheduler(int threadCount, ThreadFactory factory) {
        return new RunnerScheduler() {
            private final ExecutorService executorService = Executors.newFixedThreadPool(threadCount, factory);

            public void schedule(Runnable childStatement) {
                executorService.submit(childStatement);
            }

            public void finished() {
                try {
                    executorService.shutdown();
                    executorService.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    e.printStackTrace(System.err);
                }
            }
        };
    }

    private static ThreadFactory threadFactory() {
        return new ThreadFactoryBuilder().setNameFormat("feature-pool-%d").build();
    }
}