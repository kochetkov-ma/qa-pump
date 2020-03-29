package ru.iopump.qa.spring;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@Slf4j
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {PumpConfiguration.class})
public class PumpConfigurationTestProfileTest {

    @Autowired
    private Environment environment;
    @Value("${name}")
    private String name;

    @Test
    public void loadCoreConfig() {
        log.info("name = {}", name);
        log.info("environment = {}", environment);
    }
}