package ru.iopump.qa.exception;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;

public class PumpCoreExceptionTest {

    @Test
    public void of() {
        assertThat(PumpCoreException.of())
            .isInstanceOf(PumpCoreException.class)
            .hasMessage(null)
            .hasNoCause();
    }

    @Test
    public void ofWithArgs() {
        assertThat(PumpCoreException.of("{}", "test"))
            .isInstanceOf(PumpCoreException.class)
            .hasMessage("test")
            .hasNoCause();

        assertThat(PumpCoreException.of(null, "test"))
            .isInstanceOf(PumpCoreException.class)
            .hasMessage(null)
            .hasNoCause();

        assertThat(PumpCoreException.of(null, (Object[]) null))
            .isInstanceOf(PumpCoreException.class)
            .hasMessage(null)
            .hasNoCause();
    }
}