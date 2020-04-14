package ru.iopump.qa.constants;

import static org.springframework.core.env.AbstractEnvironment.ACTIVE_PROFILES_PROPERTY_NAME;

import io.cucumber.junit.PumpFeatureParallel;
import io.cucumber.junit.PumpScenarioParallel;
import org.springframework.core.env.AbstractEnvironment;
import ru.iopump.qa.annotation.PumpApi;
import ru.iopump.qa.annotation.PumpOptions;
import ru.iopump.qa.cucumber.transformer.Transformer;

/**
 * Main configuration constants.
 */
@PumpApi("Configuration's keys")
public final class PumpConfigKeys {
    public PumpConfigKeys() {
        throw new AssertionError("utility class");
    }

    /**
     * Value type {@link String}.
     * Default = {@link PumpConstants#CONF_DIR_DEFAULT}.
     * You can override default configuration directory {@link PumpConstants#CONF_DIR_DEFAULT}.
     * <p>Example: <pre>-Dpump.configuration.dir=[configuration directory in classpath]</pre>
     *
     * @see PumpConstants#CONF_DIR_DEFAULT
     */
    public static final String CONF_DIR_KEY = "pump.configuration.dir";

    /**
     * Value type {@link String}.
     * Default = empty string.
     * Variable key - Spring Active Profile.
     * Alias of Spring variable key {@link AbstractEnvironment#ACTIVE_PROFILES_PROPERTY_NAME}.
     * <p>Example: <pre>-Dspring.profiles.active=[profile name]</pre>
     *
     * @see AbstractEnvironment#ACTIVE_PROFILES_PROPERTY_NAME
     */
    public static final String SPRING_ACTIVE_PROFILE_KEY = ACTIVE_PROFILES_PROPERTY_NAME;

    /**
     * Value type {@link String}.
     * Default = 'ru.iopump.qa.user.component'.
     * You may set this Variable key via System / Env / TypeSafe configuration.
     * It add your package to scan Spring Components as well as internal packages 'ru.iopump.qa.component' and 'ru.iopump.qa.step'.
     * See Spring annotation {@link org.springframework.context.annotation.ComponentScan} - value or basePackages attributes.
     */
    public static final String USER_COMPONENT_PACKAGE_KEY = "pump.user.bean.package";

    /**
     * Value type {@link Boolean}.
     * Default = true.
     * System or environment variable key.
     * Set via -Dpump.env.single.context='' or export into your OS env variables.
     * Used before spring context created.
     * Suppress multiple recreating context on spring exception.
     * Generally recreating Spring context lust multiplies stacktrace in console without positive effect.
     * And this setting show only one stacktrace and skip other tries.
     */
    public static final String DONT_REFRESH_CONTEXT_ON_SPRING_ERROR_KEY = "pump.env.single.context";

    /**
     * Value type {@link Boolean}.
     * Default = false.
     * true - don't use fail-safe algorithm.
     * false - use fail-safe algorithm during processing, has less sensitive to wrong gherkin arg string from feature-files.
     */
    public static final String PROCESSOR_STRICT = "pump.processor.strict";

    /**
     * Value type {@link Boolean}.
     * Default = true.
     * true - use universal fail-safe {@link Transformer} if no transformers found by target argument type in StepDef method.
     * false - don't use and throw exception if no transformers found by target argument type in StepDef method.
     */
    public static final String TRANSFORMER_LAST_RESORT = "transformer.last.resort";

    /**
     * Value type {@link Integer}.
     * Default = {@link PumpFeatureParallel#FEATURE_THREADS_DEFAULT}.
     *
     * @see PumpOptions#featureThreads()
     * @see PumpFeatureParallel
     */
    public static final String FEATURE_THREADS = "pump.feature.threads";

    /**
     * Value type {@link Integer}.
     * Default = {@link PumpScenarioParallel#SCENARIO_THREADS_DEFAULT}.
     *
     * @see PumpOptions#scenarioThreads()
     * @see PumpScenarioParallel
     */
    public static final String SCENARIO_THREADS = "pump.scenario.threads";
}