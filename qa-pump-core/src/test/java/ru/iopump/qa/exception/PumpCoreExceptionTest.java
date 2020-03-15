package ru.iopump.qa.exception;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;

public class PumpCoreExceptionTest {

    @Test
    public void create() {
        assertThat(new PumpCoreException("", null, ""))
            .isInstanceOf(PumpCoreException.class)
            .hasMessage("")
            .hasNoCause();

        assertThat(new PumpCoreException(new RuntimeException()))
            .isInstanceOf(PumpCoreException.class)
            .hasMessage("java.lang.RuntimeException")
            .hasCauseInstanceOf(RuntimeException.class);

        assertThat(PumpCoreException.of(new RuntimeException()))
            .isInstanceOf(PumpCoreException.class)
            .hasMessage("java.lang.RuntimeException")
            .hasCauseInstanceOf(RuntimeException.class);

        assertThat(PumpCoreException.of().initCause(new RuntimeException()))
            .isInstanceOf(PumpCoreException.class)
            .hasMessage(null)
            .hasCauseInstanceOf(RuntimeException.class);
    }

    @Test
    public void ofCause() {
        assertThat(PumpCoreException.of(new Exception()))
            .isInstanceOf(PumpCoreException.class)
            .hasMessage("java.lang.Exception")
            .hasCauseInstanceOf(Exception.class);

        assertThat(PumpCoreException.of(null))
            .isInstanceOf(PumpCoreException.class)
            .hasMessage(null)
            .hasNoCause();
    }

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

    @Test
    public void withCause() {
        assertThat(PumpCoreException.of().withCause(new IllegalArgumentException()))
            .isInstanceOf(PumpCoreException.class)
            .hasMessage("java.lang.IllegalArgumentException")
            .hasCauseInstanceOf(IllegalArgumentException.class);

        assertThat(PumpCoreException.of().withCause(null))
            .isInstanceOf(PumpCoreException.class)
            .hasMessage(null)
            .hasNoCause();
    }

    @Test
    public void withMsg() {
        assertThat(PumpCoreException.of("no").withCause(new IllegalArgumentException()).withMsg("new"))
            .isInstanceOf(PumpCoreException.class)
            .hasMessage("new")
            .hasCauseInstanceOf(IllegalArgumentException.class);
    }
}