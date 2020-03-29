package ru.iopump.qa.user.component;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class TestBean {
    public TestBean() {
        log.info("TestBean was created");
    }
}
