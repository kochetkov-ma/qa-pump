package ru.iopump.qa.component.vars;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.time.LocalDateTime;
import lombok.extern.slf4j.Slf4j;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import ru.iopump.qa.component.vars.predefined.BuiltInStaticVars;
import ru.iopump.qa.exception.TestVarException;
import ru.iopump.qa.util.Str;

@Slf4j
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {StaticVars.class, BuiltInStaticVars.class})
public class StaticVarsTest {

    @Autowired
    private StaticVars staticVars;

    @BeforeClass
    public static void beforeClass() {
        System.setProperty("pump.bind.static", "#");
    }

    @AfterClass
    public static void afterClass() {
        System.clearProperty("pump.bind.static");
    }

    @Test
    public void remove() {
        assertThatThrownBy(() -> staticVars.remove("test")).isInstanceOf(UnsupportedOperationException.class);
    }

    @Test
    public void put() {
        assertThatThrownBy(() -> staticVars.put("test", "test")).isInstanceOf(UnsupportedOperationException.class);
    }

    @Test
    public void getNotExists() {
        assertThatThrownBy(() -> staticVars.get("not exists")).isInstanceOf(TestVarException.class);
        assertThatThrownBy(() -> staticVars.get(null)).isInstanceOf(NullPointerException.class);
    }

    @Test
    public void get() {
        assertThat(staticVars.get("now")).isNotNull().isInstanceOf(LocalDateTime.class);
        assertThat(staticVars.get("NULL")).isNull();
        assertThat(staticVars.get("started_time")).isEqualTo(LocalDateTime.MIN);

    }

    @Test
    public void bindName() {
        assertThat(staticVars.bindName()).isEqualTo("#");
    }

    @Test
    public void map() {
        log.info(Str.toPrettyString(staticVars.snapshot()));
        assertThat(staticVars.snapshot()).hasSize(8); // May change. Persist it on add new predefined values
    }
}