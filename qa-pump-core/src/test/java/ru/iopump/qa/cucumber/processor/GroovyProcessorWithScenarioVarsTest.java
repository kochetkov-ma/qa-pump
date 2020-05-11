package ru.iopump.qa.cucumber.processor;

import com.google.common.collect.Maps;
import org.junit.Before;
import org.mockito.Mockito;
import ru.iopump.qa.component.groovy.GroovyScript;

public class GroovyProcessorWithScenarioVarsTest {

    private GroovyProcessor processor;
    private ProcessingContext context;
    private GroovyScript script;

    @Before
    public void setUp() {
        this.context = Mockito.mock(ProcessingContext.class);
        Mockito.when(context.getBingMap()).thenReturn(Maps.newHashMap());
        this.processor = new GroovyProcessor(context, false);
        this.script = GroovyScript.create();
    }
}