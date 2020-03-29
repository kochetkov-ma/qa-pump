package ru.iopump.qa.spring;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@Slf4j
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {PumpConfiguration.class})
@ActiveProfiles("test")
public class PumpConfigurationDefaultProfileTest {

    @Autowired
    private ApplicationContext applicationContext;

    @Test
    public void loadTestConfig() {
        final String[] appName = applicationContext.getEnvironment().getActiveProfiles();
        log.info("{}", (Object[]) appName);
    }
}