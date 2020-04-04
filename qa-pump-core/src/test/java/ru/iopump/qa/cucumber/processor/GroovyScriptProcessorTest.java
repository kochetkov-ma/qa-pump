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
import ru.iopump.qa.component.TestContext;
import ru.iopump.qa.component.groovy.GroovyEvaluatorImpl;
import ru.iopump.qa.cucumber.processor.GroovyScriptProcessor;
import ru.iopump.qa.cucumber.processor.ProcessResult;
import ru.iopump.qa.exception.ProcessorException;

public class GroovyScriptProcessorTest {

    private GroovyScriptProcessor processor;
    private TestContext context;

    @Before
    public void setUp() {
        this.context = Mockito.mock(TestContext.class);
        Mockito.when(context.snapshot()).thenReturn(Maps.newHashMap());
        this.processor = new GroovyScriptProcessor(new GroovyEvaluatorImpl(context), false);
    }

    @Test
    public void process() {
        assertThat(processor.process("'string1' + '-' + 'string2'"))
            .returns("string1-string2", p -> p.getResult().orElse(null))
            .returns("string1-string2", ProcessResult::getResultAsString)
            .returns(null, p -> p.getProcessException().orElse(null));

        assertThat(processor.process("null"))
            .returns(null, p -> p.getResult().orElse(null))
            .returns("null", ProcessResult::getResultAsString)
            .returns(null, p -> p.getProcessException().orElse(null));

        assertThat(processor.process("null"))
            .returns(null, p -> p.getResult().orElse(null))
            .returns("null", ProcessResult::getResultAsString)
            .returns(null, p -> p.getProcessException().orElse(null));

        assertThat(processor.process("[1:1]"))
            .returns(ImmutableMap.of(1, 1), p -> p.getResult().orElse(null))
            .returns("{1=1}", ProcessResult::getResultAsString)
            .returns(null, p -> p.getProcessException().orElse(null));

        assertThat(processor.process("\"${new Integer(1)}\""))
            .returns("1", p -> p.getResult().orElse(null))
            .returns("1", ProcessResult::getResultAsString)
            .returns(null, p -> p.getProcessException().orElse(null));
    }

    @Test
    public void processWithProblems() {
        assertThat(processor.process("propertyNotExist")).satisfies(result ->
        {
            assertThat(result.getResult()).isEmpty();
            assertThat(result.getResultAsString()).isEqualTo("propertyNotExist");
            assertThat(result.getProcessException()).containsInstanceOf(MissingPropertyException.class);
            assertThat(result.isNullResult()).isFalse();
        });

        assertThat(processor.process("methodNotExists()")).satisfies(result ->
        {
            assertThat(result.getResult()).isEmpty();
            assertThat(result.getResultAsString()).isEqualTo("methodNotExists()");
            assertThat(result.getProcessException()).containsInstanceOf(MissingMethodException.class);
            assertThat(result.isNullResult()).isFalse();
        });

        assertThatThrownBy(() -> processor.process("\"${propertyNotExist}\""))
            .isInstanceOf(ProcessorException.class);

        assertThat(processor.process("${propertyNotExist}")).satisfies(result ->
        {
            assertThat(result.getResult()).isEmpty();
            assertThat(result.getResultAsString()).isEqualTo("${propertyNotExist}");
            assertThat(result.getProcessException()).containsInstanceOf(MissingPropertyException.class);
            assertThat(result.isNullResult()).isFalse();
        });

        assertThat(processor.process("${methodNotExists()}")).satisfies(result ->
        {
            assertThat(result.getResult()).isEmpty();
            assertThat(result.getResultAsString()).isEqualTo("${methodNotExists()}");
            assertThat(result.getProcessException()).containsInstanceOf(MissingMethodException.class);
            assertThat(result.isNullResult()).isFalse();
        });

        assertThatThrownBy(() -> processor.process("\"${propertyNotExist}\""))
            .isInstanceOf(ProcessorException.class);

        assertThatThrownBy(() -> processor.process("throw new RuntimeException()"))
            .isInstanceOf(ProcessorException.class);

    }

    @Test
    public void processWithProblemsStrict() {
        this.processor = new GroovyScriptProcessor(new GroovyEvaluatorImpl(context), true);

        assertThatThrownBy(() -> processor.process("propertyNotExist"))
            .isInstanceOf(ProcessorException.class);
        assertThatThrownBy(() -> processor.process("methodNotExists()"))
            .isInstanceOf(ProcessorException.class);
        assertThatThrownBy(() -> processor.process("\"${propertyNotExist}\""))
            .isInstanceOf(ProcessorException.class);
        assertThatThrownBy(() -> processor.process("${propertyNotExist}"))
            .isInstanceOf(ProcessorException.class);
        assertThatThrownBy(() -> processor.process("${methodNotExists()}"))
            .isInstanceOf(ProcessorException.class);
        assertThatThrownBy(() -> processor.process("\"${propertyNotExist}\""))
            .isInstanceOf(ProcessorException.class);
        assertThatThrownBy(() -> processor.process("throw new RuntimeException()"))
            .isInstanceOf(ProcessorException.class);
    }
}