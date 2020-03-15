package ru.iopump.qa;

import static org.assertj.core.api.Assertions.assertThat;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.slf4j.MarkerFactory;

@Slf4j
public class InitTest {

    @Test
    public void test() {
        log.info("[TESTING] First Test is OK");
        assertThat(true).isTrue();
    }
}
