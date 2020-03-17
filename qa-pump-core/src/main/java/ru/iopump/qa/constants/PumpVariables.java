package ru.iopump.qa.constants;

import lombok.experimental.UtilityClass;
import org.springframework.core.env.AbstractEnvironment;

import static org.springframework.core.env.AbstractEnvironment.ACTIVE_PROFILES_PROPERTY_NAME;

@UtilityClass
public class PumpVariables {
    /**
     * You can override default configuration directory {@link PumpConstants#CONF_DIR_DEFAULT}.
     * <p>Example: <pre>-Dpump.configuration.dir=[configuration directory in classpath]</pre>
     *
     * @see PumpConstants#CONF_DIR_DEFAULT
     */
    public final static String CONF_DIR_KEY = "pump.configuration.dir";
    /**
     * Variable key - Spring Active Profile.
     * Alias of Spring variable key {@link AbstractEnvironment#ACTIVE_PROFILES_PROPERTY_NAME}.
     * <p>Example: <pre>-Dspring.profiles.active=[profile name]</pre>
     *
     * @see AbstractEnvironment#ACTIVE_PROFILES_PROPERTY_NAME
     */
    public final static String SPRING_ACTIVE_PROFILE_KEY = ACTIVE_PROFILES_PROPERTY_NAME;
}
