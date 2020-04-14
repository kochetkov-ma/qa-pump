package ru.iopump.qa.annotation;

import io.cucumber.junit.Pump;
import io.cucumber.junit.PumpFeatureParallel;
import io.cucumber.junit.PumpScenarioParallel;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Additional Cucumber runner options for {@link Pump} or sub-classes as well as {@link PumpFeatureParallel}.
 */
@Documented
@Inherited
@Retention(RetentionPolicy.RUNTIME)
@Target( {ElementType.TYPE})
public @interface PumpOptions {
    /**
     * Only for parallel runners.
     * Set amount of Feature threads.
     * It doesn't work in base {@link Pump} - only for {@link PumpFeatureParallel} and sub-classes.
     */
    int featureThreads() default PumpFeatureParallel.FEATURE_THREADS_DEFAULT;

    /**
     * Only for {@link PumpScenarioParallel}.
     * Set amount of Scenario threads inside each features thread.
     * The total number of threads will be {@link PumpOptions#featureThreads()} * scenarioThreads.
     */
    int scenarioThreads() default PumpFeatureParallel.FEATURE_THREADS_DEFAULT;
}
