package ru.iopump.qa.spring;

import static org.springframework.core.env.StandardEnvironment.SYSTEM_ENVIRONMENT_PROPERTY_SOURCE_NAME;
import static org.springframework.core.env.StandardEnvironment.SYSTEM_PROPERTIES_PROPERTY_SOURCE_NAME;
import static ru.iopump.qa.constants.PumpConfigKeys.CONF_DIR_KEY;
import static ru.iopump.qa.constants.PumpConfigKeys.SPRING_ACTIVE_PROFILE_KEY;
import static ru.iopump.qa.constants.PumpConfigKeys.USER_COMPONENT_PACKAGE_KEY;
import static ru.iopump.qa.constants.PumpConstants.CONF_DIR_DEFAULT;
import static ru.iopump.qa.constants.PumpConstants.getDefaultConfigName;
import static ru.iopump.qa.constants.PumpInternalConstants.COMPONENT_SCAN_PACKAGE_EXTRA_DEFAULT;
import static ru.iopump.qa.constants.PumpInternalConstants.COMPONENT_SCAN_PACKAGE_MAIN_DEFAULT;
import static ru.iopump.qa.constants.PumpInternalConstants.COMPONENT_SCAN_PACKAGE_USER_DEFAULT;

import com.typesafe.config.Config;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import javax.annotation.Nullable;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.Environment;
import org.springframework.core.env.PropertySource;
import org.springframework.core.env.PropertySources;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.support.EncodedResource;
import org.springframework.test.context.ContextConfiguration;
import ru.iopump.qa.annotation.PumpApi;
import ru.iopump.qa.exception.PumpException;
import ru.iopump.qa.util.Str;

/**
 * Main class for Pump Framework configuration.</br>
 * By default Qa Pump Framework use only this configuration class and this is enough for most situations.</br>
 * If you want configure Qa Pump Framework yourself extend this class and use {@link ContextConfiguration} annotation.
 * For more details see {@link io.cucumber.spring.SpringFactory} - you can use all this configuration possibilities</br>
 * But we we recommend use only {@link ContextConfiguration} with you child configuration class.</br>
 */
@Slf4j
@PumpApi("Main class. Spring configuration")
@ContextConfiguration(classes = PumpConfiguration.class, initializers = PumpConfiguration.class)
@ComponentScan( {
    COMPONENT_SCAN_PACKAGE_MAIN_DEFAULT,
    COMPONENT_SCAN_PACKAGE_EXTRA_DEFAULT,
    "${" + USER_COMPONENT_PACKAGE_KEY + ":" + COMPONENT_SCAN_PACKAGE_USER_DEFAULT + "}"
})
public class PumpConfiguration implements ApplicationContextInitializer<ConfigurableApplicationContext> {

    public PumpConfiguration() {
        log.info("[CONFIGURATION] Base configuration class '{}' has been created", getClass());
    }

    /**
     * Configure spring {@link Environment} to use 3 types of configurations:
     * <p>in priority ordering (first is highest)<ul>
     * <li>Spring SystemEnvironment
     * <li>Spring SystemProperties
     * <li>Pump Typesafe configuration {@link com.typesafe.config.ConfigFactory}
     * </ul><p>
     */
    @Override
    public void initialize(@NonNull ConfigurableApplicationContext applicationContext) {
        log.info("[CONFIGURATION] Initializing context '{}'...", applicationContext.getClass());

        final ConfigurableEnvironment environment = applicationContext.getEnvironment();
        var configurationDir = environment.getProperty(CONF_DIR_KEY, CONF_DIR_DEFAULT);
        var confName = getDefaultConfigName(environment.getProperty(SPRING_ACTIVE_PROFILE_KEY));
        var resource = new ClassPathResource(configurationDir + "/" + confName);

        var source = new EncodedResource(resource, StandardCharsets.UTF_8);
        PropertySource<Config> propertySource;
        try {
            propertySource = PumpConfigUtils.getConfigFactory().createPropertySource("typesafe", source);
        } catch (IOException e) {
            throw PumpException.of("Error loading typesafe configuration", e);
        }
        environment.getPropertySources().addLast(propertySource);

        printSystemEnvironment(environment.getPropertySources());
        printSystemProperties(environment.getPropertySources());
    }

    //// PRIVATE

    private static void printSystemEnvironment(@Nullable PropertySources propertySources) {
        if (propertySources == null) {
            return;
        }
        final PropertySource<?> source = propertySources.get(SYSTEM_ENVIRONMENT_PROPERTY_SOURCE_NAME);
        if (source == null) {
            return;
        }
        //noinspection rawtypes
        log.debug("[CONFIGURATION] Spring System Environment Config\n{}", Str.toPrettyString((Map) source.getSource()));
    }

    private static void printSystemProperties(@Nullable PropertySources propertySources) {
        if (propertySources == null) {
            return;
        }
        final PropertySource<?> source = propertySources.get(SYSTEM_PROPERTIES_PROPERTY_SOURCE_NAME);
        if (source == null) {
            return;
        }
        //noinspection rawtypes
        log.debug("[CONFIGURATION] Spring System Properties Config\n{}", Str.toPrettyString((Map) source.getSource()));
    }
}
