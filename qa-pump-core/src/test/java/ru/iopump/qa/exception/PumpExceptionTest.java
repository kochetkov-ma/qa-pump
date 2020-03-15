package ru.iopump.qa.exception;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;

public class PumpExceptionTest {

    @Test
    public void create() {
        assertThat(new PumpException("", null, ""))
            .isInstanceOf(PumpException.class)
            .hasMessage("")
            .hasNoCause();

        assertThat(new PumpException(new RuntimeException()))
            .isInstanceOf(PumpException.class)
            .hasMessage("java.lang.RuntimeException")
            .hasCauseInstanceOf(RuntimeException.class);

        assertThat(PumpException.of(new RuntimeException()))
            .isInstanceOf(PumpException.class)
            .hasMessage("java.lang.RuntimeException")
            .hasCauseInstanceOf(RuntimeException.class);

        assertThat(PumpException.of().initCause(new RuntimeException()))
            .isInstanceOf(PumpException.class)
            .hasMessage(null)
            .hasCauseInstanceOf(RuntimeException.class);
    }

    @Test
    public void ofCause() {
        assertThat(PumpException.of(new Exception()))
            .isInstanceOf(PumpException.class)
            .hasMessage("java.lang.Exception")
            .hasCauseInstanceOf(Exception.class);

        assertThat(PumpException.of(null))
            .isInstanceOf(PumpException.class)
            .hasMessage(null)
            .hasNoCause();
    }

    @Test
    public void of() {
        assertThat(PumpException.of())
            .isInstanceOf(PumpException.class)
            .hasMessage(null)
            .hasNoCause();
    }

    @Test
    public void ofWithArgs() {
        assertThat(PumpException.of("{}", "test"))
            .isInstanceOf(PumpException.class)
            .hasMessage("test")
            .hasNoCause();

        assertThat(PumpException.of(null, "test"))
            .isInstanceOf(PumpException.class)
            .hasMessage(null)
            .hasNoCause();

        assertThat(PumpException.of(null, (Object[]) null))
            .isInstanceOf(PumpException.class)
            .hasMessage(null)
            .hasNoCause();
    }

    @Test
    public void withCause() {
        assertThat(PumpException.of().withCause(new IllegalArgumentException()))
            .isInstanceOf(PumpException.class)
            .hasMessage("java.lang.IllegalArgumentException")
            .hasCauseInstanceOf(IllegalArgumentException.class);

        assertThat(PumpException.of().withCause(null))
            .isInstanceOf(PumpException.class)
            .hasMessage(null)
            .hasNoCause();
    }

    @Test
    public void withMsg() {
        assertThat(PumpException.of("no").withCause(new IllegalArgumentException()).withMsg("new"))
            .isInstanceOf(PumpException.class)
            .hasMessage("new")
            .hasCauseInstanceOf(IllegalArgumentException.class);
    }
}