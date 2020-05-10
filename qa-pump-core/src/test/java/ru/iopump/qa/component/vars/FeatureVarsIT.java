package ru.iopump.qa.component.vars;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.google.common.collect.ImmutableMap;
import org.junit.Test;
import ru.iopump.qa.component.groovy.GroovyScript;
import ru.iopump.qa.exception.TestVarException;

public class FeatureVarsIT {

    @Test
    public void groovyTest() {
        FeatureVars featureVars = new FeatureVars();
        featureVars.put("key1", 1);
        featureVars.put("null", null);

        GroovyScript script = GroovyScript.create()
            .withBindingMap(ImmutableMap.of(featureVars.bindName(), featureVars));

        assertThat(script.evaluate("$feature.key1")).isEqualTo(1);
        assertThat(script.evaluate("$feature.null")).isEqualTo(null);
        assertThatThrownBy(() -> script.evaluate("$feature.not_exists")).isInstanceOf(TestVarException.class);

    }
}