package ru.iopump.qa.cucumber.processor;

import static org.assertj.core.api.Assertions.assertThat;

import com.google.common.collect.ImmutableMap;
import java.util.List;
import org.junit.Before;
import org.junit.Test;
import ru.iopump.qa.component.vars.FeatureVars;
import ru.iopump.qa.component.vars.ScenarioVars;
import ru.iopump.qa.component.vars.SharedVars;

public class ProcessingContextTest {

    private ProcessingContext processingContext;
    private ScenarioVars scenarioVars;
    private FeatureVars featureVars;
    private SharedVars sharedVars;

    @Before
    public void setUp() {
        scenarioVars = new ScenarioVars();
        featureVars = new FeatureVars();
        sharedVars = new SharedVars();
    }

    @Test
    public void complexTest() {
        var object_1 = new Object();
        var object_2 = new Object();
        scenarioVars.put("object_1", object_1);
        scenarioVars.put("string_1", "string_1 value");

        featureVars.put("object_1", object_2);
        featureVars.put("string_2", "string_2 value");

        sharedVars.put("object_1", object_1);
        sharedVars.put("string_2", "string_2 value");

        var pBeans = List.of(
            scenarioVars,
            featureVars,
            sharedVars
        );
        processingContext = new ProcessingContext(() -> pBeans, null);

        // Results
        var objectMap = processingContext.getBingMap();
        assertThat(objectMap).containsOnlyKeys("scenario", "feature", "share");

        objectMap = processingContext.getBingMap(List.of("scenario", "feature"));
        assertThat(objectMap).containsExactlyEntriesOf(ImmutableMap.<String,Object>builder()
            .put("object_1", object_1)
            .put("string_2", "string_2 value")
            .put("share", sharedVars)
            .put("string_1", "string_1 value")
            .build()
        );

    }

    @Test
    public void testGetBingMap() {
    }
}