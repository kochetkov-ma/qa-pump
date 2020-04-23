package ru.iopump.qa.component.vars;

import com.google.common.collect.ImmutableMap;
import java.util.Map;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.stereotype.Component;
import ru.iopump.qa.component.TestVars;

@Component
@AllArgsConstructor
class TestVarsImpl implements TestVars {

    private final BeanFactory beanFactory;

    @Override
    public Vars scenarioVars() {
        return beanFactory.getBean(ScenarioVars.class);
    }

    @Override
    public Vars featureVars() {
        return beanFactory.getBean(FeatureVars.class);
    }

    @Override
    public Vars staticVars() {
        return beanFactory.getBean(StaticVars.class);
    }

    @Override
    public Vars sharedVars() {
        return beanFactory.getBean(SharedVars.class);
    }

    @Override
    public Map<String, Object> snapshot() {
        return ImmutableMap.<String, Object>builder()
            .putAll(featureVars().getAll())
            .build();
    }
}
