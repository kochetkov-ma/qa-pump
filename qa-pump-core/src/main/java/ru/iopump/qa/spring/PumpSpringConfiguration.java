package ru.iopump.qa.spring;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

import static ru.iopump.qa.constants.PumpConstants.*;
import static ru.iopump.qa.constants.PumpVariables.CONF_DIR_KEY;

@Slf4j
@Configuration
@PropertySource(factory = TypesafePropertySourceFactory.class,
        value = "classpath:${" + CONF_DIR_KEY + ":" + CONF_DIR_DEFAULT + "}/" + CONF_FILE_DEFAULT)
public class PumpSpringConfiguration {

    public PumpSpringConfiguration() {
        log.info("[CONFIGURATION] Base configuration class '{}' has been created", getClass());
    }
}
