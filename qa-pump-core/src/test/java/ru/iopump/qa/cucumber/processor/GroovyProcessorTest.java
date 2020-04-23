package ru.iopump.qa.cucumber.processor;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import groovy.lang.MissingMethodException;
import groovy.lang.MissingPropertyException;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import ru.iopump.qa.component.TestVars;
import ru.iopump.qa.component.groovy.GroovyScript;
import ru.iopump.qa.exception.ProcessorException;

public class GroovyProcessorTest {

    private GroovyProcessor processor;
    private TestVars context;
    private GroovyScript script;

    @Before
    public void setUp() {
        this.context = Mockito.mock(TestVars.class);
        Mockito.when(context.snapshot()).thenReturn(Maps.newHashMap());
        this.processor = new GroovyProcessor(context, false);
        this.script = GroovyScript.create();
    }

    @Test
    public void process() {
        assertThat(processor.process("'string1' + '-' + 'string2'", script))
            .returns("string1-string2", p -> p.getResult().orElse(null))
            .returns("string1-string2", ProcessResult::getResultAsString)
            .returns(null, p -> p.getProcessException().orElse(null));

        assertThat(processor.process("null", script))
            .returns(null, p -> p.getResult().orElse(null))
            .returns("null", ProcessResult::getResultAsString)
            .returns(null, p -> p.getProcessException().orElse(null));

        assertThat(processor.process("null", script))
            .returns(null, p -> p.getResult().orElse(null))
            .returns("null", ProcessResult::getResultAsString)
            .returns(null, p -> p.getProcessException().orElse(null));

        assertThat(processor.process("[1:1]", script))
            .returns(ImmutableMap.of(1, 1), p -> p.getResult().orElse(null))
            .returns("{1=1}", ProcessResult::getResultAsString)
            .returns(null, p -> p.getProcessException().orElse(null));

        assertThat(processor.process("\"${new Integer(1)}\"", script))
            .returns("1", p -> p.getResult().orElse(null))
            .returns("1", ProcessResult::getResultAsString)
            .returns(null, p -> p.getProcessException().orElse(null));
    }

    @Test
    public void processWithProblems() {
        assertThat(processor.process("propertyNotExist", script)).satisfies(result ->
        {
            assertThat(result.getResult()).isEmpty();
            assertThat(result.getResultAsString()).isEqualTo("propertyNotExist", script);
            assertThat(result.getProcessException()).containsInstanceOf(MissingPropertyException.class);
            assertThat(result.isNullResult()).isFalse();
        });

        assertThat(processor.process("methodNotExists()", script)).satisfies(result ->
        {
            assertThat(result.getResult()).isEmpty();
            assertThat(result.getResultAsString()).isEqualTo("methodNotExists()", script);
            assertThat(result.getProcessException()).containsInstanceOf(MissingMethodException.class);
            assertThat(result.isNullResult()).isFalse();
        });

        assertThatThrownBy(() -> processor.process("\"${propertyNotExist}\"", script))
            .isInstanceOf(ProcessorException.class);

        assertThat(processor.process("${propertyNotExist}", script)).satisfies(result ->
        {
            assertThat(result.getResult()).isEmpty();
            assertThat(result.getResultAsString()).isEqualTo("${propertyNotExist}", script);
            assertThat(result.getProcessException()).containsInstanceOf(MissingPropertyException.class);
            assertThat(result.isNullResult()).isFalse();
        });

        assertThat(processor.process("${methodNotExists()}", script)).satisfies(result ->
        {
            assertThat(result.getResult()).isEmpty();
            assertThat(result.getResultAsString()).isEqualTo("${methodNotExists()}", script);
            assertThat(result.getProcessException()).containsInstanceOf(MissingMethodException.class);
            assertThat(result.isNullResult()).isFalse();
        });

        assertThatThrownBy(() -> processor.process("\"${propertyNotExist}\"", script))
            .isInstanceOf(ProcessorException.class);

        assertThatThrownBy(() -> processor.process("throw new RuntimeException()", script))
            .isInstanceOf(ProcessorException.class);

    }

    @Test
    public void processWithProblemsStrict() {
        this.processor = new GroovyProcessor(context, true);

        assertThatThrownBy(() -> processor.process("propertyNotExist", script))
            .isInstanceOf(ProcessorException.class);
        assertThatThrownBy(() -> processor.process("methodNotExists()", script))
            .isInstanceOf(ProcessorException.class);
        assertThatThrownBy(() -> processor.process("\"${propertyNotExist}\"", script))
            .isInstanceOf(ProcessorException.class);
        assertThatThrownBy(() -> processor.process("${propertyNotExist}", script))
            .isInstanceOf(ProcessorException.class);
        assertThatThrownBy(() -> processor.process("${methodNotExists()}", script))
            .isInstanceOf(ProcessorException.class);
        assertThatThrownBy(() -> processor.process("\"${propertyNotExist}\"", script))
            .isInstanceOf(ProcessorException.class);
        assertThatThrownBy(() -> processor.process("throw new RuntimeException()", script))
            .isInstanceOf(ProcessorException.class);
    }
}