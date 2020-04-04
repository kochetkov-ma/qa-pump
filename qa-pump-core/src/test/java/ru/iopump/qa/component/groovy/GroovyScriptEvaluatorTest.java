package ru.iopump.qa.component.groovy;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.google.common.collect.ImmutableMap;
import groovy.lang.MissingMethodException;
import groovy.lang.MissingPropertyException;
import org.junit.Test;

public class GroovyScriptEvaluatorTest {

    @Test
    public void evaluateWithBind() {
        var eval = new GroovyScriptEvaluator();

        assertThat(eval.evaluate("key1 + 1", ImmutableMap.of("key1", 1, "key2", "value")))
            .isEqualTo(2);

        assertThat(eval.evaluate("\"${key1 + 1} ${key2.repeat 2}\"", ImmutableMap.of("key1", 1, "key2", "value")))
            .isEqualTo("2 valuevalue");

        assertThat(eval.evaluate("key2.repeat 2", ImmutableMap.of("key1", 1, "key2", "value")))
            .isEqualTo("valuevalue");
    }

    @Test
    public void evaluate() {
        var eval = new GroovyScriptEvaluator();

        assertThat(eval.evaluate("'string1' + '-' + 'string2'"))
            .isEqualTo("string1-string2");
        assertThat(eval.evaluate("null"))
            .isEqualTo(null);
        assertThat(eval.evaluate("'null'"))
            .isEqualTo("null");
        assertThat(eval.evaluate("\"null\""))
            .isEqualTo("null");
        assertThat(eval.evaluate("[1:1]"))
            .isEqualTo(ImmutableMap.of(1, 1));
        assertThat(eval.evaluate(""))
            .isEqualTo(null);

        assertThat(eval.evaluate("\"${new Integer(1)}\""))
            .asString()
            .isEqualTo("1");
        assertThat(eval.evaluate("\"\"\"${new Integer(1)}\n${new Integer(2)}\"\"\""))
            .asString()
            .isEqualTo("1\n2");
        assertThat(eval.evaluate("'${new Integer(1)}'"))
            .asString()
            .isEqualTo("${new Integer(1)}");
        assertThat(eval.evaluate("'''${new Integer(1)}\n${new Integer(2)}'''"))
            .asString()
            .isEqualTo("${new Integer(1)}\n${new Integer(2)}");
    }

    @Test
    public void evaluateWithExceptions() {
        var eval = new GroovyScriptEvaluator();

        assertThatThrownBy(() -> eval.evaluate("propertyNotExist"))
            .isInstanceOf(MissingPropertyException.class);

        assertThatThrownBy(() -> eval.evaluate("methodNotExists()"))
            .isInstanceOf(MissingMethodException.class);
    }
}