package ru.iopump.qa.component.vars;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.io.Closeable;
import java.util.concurrent.atomic.AtomicBoolean;
import org.junit.Before;
import org.junit.Test;

public class FeatureVarsTest {

    private FeatureVars featureVars;

    @Before
    public void setUp() {
        featureVars = new FeatureVars();
    }

    @Test
    public void put() {
        featureVars.put("key1", 1);
        featureVars.put("key2", "string");
        featureVars.put("key2", null);
        featureVars.put("", new Object());

        assertThat(featureVars.get("key1")).isEqualTo(1);
        assertThat(featureVars.get("key2")).isEqualTo(null);
        assertThat(featureVars.get("")).isNotNull();
        assertThatThrownBy(() -> featureVars.put(null, new Object())).isInstanceOf(NullPointerException.class);
    }

    @Test
    public void remove() {
    }

    @Test
    public void get() {
    }

    @Test
    public void getAll() {
    }

    @Test
    public void close() {
        AtomicBoolean closed = new AtomicBoolean();
        featureVars.put("key1", (Closeable) () -> closed.set(true));
        featureVars.put("key2", (Closeable) () -> {throw new RuntimeException();});
        featureVars.close();

        assertThat(closed.get()).isTrue();
    }
}