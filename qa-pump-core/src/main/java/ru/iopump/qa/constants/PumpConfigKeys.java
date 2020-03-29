package ru.iopump.qa.constants;

import static org.springframework.core.env.AbstractEnvironment.ACTIVE_PROFILES_PROPERTY_NAME;

import lombok.experimental.UtilityClass;
import org.springframework.core.env.AbstractEnvironment;
import ru.iopump.qa.annotation.PumpApi;

/**
 * Main configuration constants.
 */
@PumpApi("Configuration's keys")
@UtilityClass
public class PumpConfigKeys {
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

    /**
     * You may set this Variable key via System / Env / TypeSafe configuration.
     * It add your package to scan Spring Components as well as internal packages 'ru.iopump.qa.component' and 'ru.iopump.qa.step'.
     * See Spring annotation {@link org.springframework.context.annotation.ComponentScan} - value or basePackages attributes.
     * Default value = 'ru.iopump.qa.user.component'.
     */
    public final static String USER_COMPONENT_PACKAGE_KEY = "pump.user.bean.package";

    /**
     * System or environment variable key.
     * Set via -Dpump.env.single.context='' or export into your OS env variables.
     * Used before spring context created.
     * Suppress multiple recreating context on spring exception.
     * Generally recreating Spring context lust multiplies stacktrace in console without positive effect.
     * And this setting show only one stacktrace and skip other tries.
     * Default = true.
     */
    public final static String DONT_REFRESH_CONTEXT_ON_SPRING_ERROR_KEY = "pump.env.single.context";
}
