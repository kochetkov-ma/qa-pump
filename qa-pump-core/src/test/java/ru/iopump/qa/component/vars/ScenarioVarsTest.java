package ru.iopump.qa.component.vars;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import lombok.extern.slf4j.Slf4j;
import org.junit.Before;
import org.junit.Test;
import ru.iopump.qa.exception.TestVarException;

@Slf4j
public class ScenarioVarsTest {

    private ScenarioVars scenarioVars;

    @Before
    public void setUp() {
        scenarioVars = new ScenarioVars();
    }

    @Test
    public void put() {
        assertThatThrownBy(() -> scenarioVars.put(null, "not null")).isInstanceOf(NullPointerException.class);

        scenarioVars.put("", null);
        assertThat(scenarioVars.map()).containsEntry("", null);
    }

    @Test
    public void remove() {
        assertThat(scenarioVars.remove("not_exists")).isNull();
        scenarioVars.put("", null);
        assertThat(scenarioVars.remove("")).isNull();
        assertThat(scenarioVars.map()).doesNotContainKeys("");
    }



    @Test
    public void get() {
        scenarioVars.put("", null);
        assertThat(scenarioVars.get("")).isNull();
        assertThatThrownBy(() -> scenarioVars.get("not_exists")).isInstanceOf(TestVarException.class);
    }

    @Test
    public void snapshot() {
        scenarioVars.put("", null);
        assertThat(scenarioVars.snapshot()).hasSize(1);
    }

    @Test
    public void testToString() {
        scenarioVars.put("", null);
        scenarioVars.put("object", new Object());
        log.info("{}", scenarioVars);
    }

    @Test
    public void bindName() {
        assertThat(scenarioVars.bindName()).isEqualTo("scenario");
    }
}