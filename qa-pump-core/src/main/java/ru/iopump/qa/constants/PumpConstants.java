package ru.iopump.qa.constants;

import static io.cucumber.spring.CucumberTestContext.SCOPE_CUCUMBER_GLUE;
import static ru.iopump.qa.spring.scope.FeatureScope.SCOPE_PUMP_FEATURE;

import org.apache.commons.lang3.StringUtils;
import ru.iopump.qa.annotation.PumpApi;

@PumpApi("Constants")
public final class PumpConstants {
    public PumpConstants() {
        throw new AssertionError("utility class");
    }

    /**
     * Default configuration folder in classpath resources.
     */
    public static final String CONF_DIR_DEFAULT = "/configuration";

    /**
     * Use as bean scope @Scope(PumpConstants.FEATURE_SCOPE).
     * Bean will life
     */
    public static final String FEATURE_SCOPE = SCOPE_PUMP_FEATURE;

    /**
     * Use as bean scope @Scope(PumpConstants.SCENARIO_SCOPE)
     */
    public static final String SCENARIO_SCOPE = SCOPE_CUCUMBER_GLUE;


    /**
     * Default configuration file name.
     * Prefix = 'pump-'.
     * Main name = {@link PumpConfigKeys#SPRING_ACTIVE_PROFILE_KEY}.
     * Extension = '.conf'.
     * Full name = Prefix + Main name + Extension -> 'pump-default.conf' when Spring Profile = 'default'.
     * Full default path will be '/configuration/pump-default.conf' in classpath resources.
     */
    public static String getDefaultConfigName(String activeProfile) {
        activeProfile = StringUtils.isBlank(activeProfile) ? "default" : activeProfile;
        return "pump-" + activeProfile + ".conf";
    }
}
