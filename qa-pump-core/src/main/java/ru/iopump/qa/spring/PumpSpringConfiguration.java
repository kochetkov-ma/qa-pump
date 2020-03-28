package ru.iopump.qa.spring;

import static org.springframework.core.env.StandardEnvironment.SYSTEM_ENVIRONMENT_PROPERTY_SOURCE_NAME;
import static org.springframework.core.env.StandardEnvironment.SYSTEM_PROPERTIES_PROPERTY_SOURCE_NAME;
import static ru.iopump.qa.constants.PumpConstants.CONF_DIR_DEFAULT;
import static ru.iopump.qa.constants.PumpConstants.getDefaultConfigName;
import static ru.iopump.qa.constants.PumpVariables.CONF_DIR_KEY;
import static ru.iopump.qa.constants.PumpVariables.SPRING_ACTIVE_PROFILE_KEY;
import static ru.iopump.qa.spring.config.PumpConfigUtils.getConfigFactory;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import javax.annotation.Nullable;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.Environment;
import org.springframework.core.env.PropertySource;
import org.springframework.core.env.PropertySources;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.support.EncodedResource;
import org.springframework.test.context.ContextConfiguration;
import ru.iopump.qa.annotation.PumpApi;
import ru.iopump.qa.util.Str;

/**
 * Main class for Pump Framework configuration.</br>
 * By default Qa Pump Framework use only this configuration class and this is enough for most situations.</br>
 * If you want configure Qa Pump Framework yourself extend this class and use {@link ContextConfiguration} annotation.
 * For more details see {@link io.cucumber.spring.SpringFactory} - you can use all this configuration possibilities</br>
 * But we we recommend use only {@link ContextConfiguration} with you child configuration class.</br>
 *
 */
@Slf4j
@PumpApi("Glue Spring configuration")
@ContextConfiguration(classes = PumpSpringConfiguration.class)
public class PumpSpringConfiguration implements ApplicationContextInitializer<ConfigurableApplicationContext> {

    public PumpSpringConfiguration() {
        log.info("[CONFIGURATION] Base configuration class '{}' has been created", getClass());
    }

    /**
     * This {@link BeanFactoryPostProcessor} configure spring {@link Environment} to use 3 types of configurations:
     * <p>in priority ordering (first is highest)<ul>
     * <li>Spring SystemEnvironment
     * <li>Spring SystemProperties
     * <li>Pump Typesafe configuration
     * </ul><p>
     */
    @Bean
    public static BeanFactoryPostProcessor typesafePropertySourcePostProcessor(@NonNull ConfigurableEnvironment environment) throws IOException {

        var configurationDir = environment.getProperty(CONF_DIR_KEY, CONF_DIR_DEFAULT);
        var confName = getDefaultConfigName(environment.getProperty(SPRING_ACTIVE_PROFILE_KEY));
        var resource = new ClassPathResource(configurationDir + "/" + confName);

        var source = new EncodedResource(resource, StandardCharsets.UTF_8);
        var propertySource = getConfigFactory().createPropertySource("typesafe", source);
        environment.getPropertySources().addLast(propertySource);

        printSystemEnvironment(environment.getPropertySources());
        printSystemProperties(environment.getPropertySources());

        final PropertySourcesPlaceholderConfigurer ppc = new PropertySourcesPlaceholderConfigurer();
        ppc.setPropertySources(environment.getPropertySources());

        return ppc;
    }

    ///////////////////
    ///// PRIVATE /////
    ///////////////////

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

    @Override
    public void initialize(ConfigurableApplicationContext applicationContext) {
        log.info("[CONFIGURATION] initialize");
    }
}
