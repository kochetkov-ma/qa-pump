package ru.iopump.qa.spring;

import lombok.extern.slf4j.Slf4j;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import ru.iopump.qa.cucumber.PumpObjectFactory;
import ru.iopump.qa.spring.scope.Execution;
import ru.iopump.qa.spring.scope.FeatureCodeScope;
import ru.iopump.qa.spring.scope.RunnerType;

@Slf4j
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {PumpConfiguration.class})
@ActiveProfiles("test")
public class PumpConfigurationDefaultProfileTest {

    @Autowired
    private ApplicationContext applicationContext;

    @BeforeClass
    public static void beforeClass() {
        Execution.setRunner(RunnerType.CUCUMBER_SINGLE_THREAD); // Avoid internal asserts
        new PumpObjectFactory(); // Avoid internal asserts
        FeatureCodeScope.initScope(); // Avoid internal asserts
    }

    @AfterClass
    public static void afterClass() {
        Execution.setRunner(null);
    }

    @Test
    public void loadTestConfig() {
        final String[] appName = applicationContext.getEnvironment().getActiveProfiles();
        log.info("{}", (Object[]) appName);
    }
}