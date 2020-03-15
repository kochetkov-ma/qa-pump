package ru.iopump.qa.component.context;

import com.google.common.collect.ImmutableMap;
import java.util.Map;
import org.springframework.beans.factory.annotation.Lookup;
import org.springframework.stereotype.Component;
import ru.iopump.qa.component.TestContext;

@Component
public class TestContextImpl implements TestContext {

    @Override
    public Context scenarioContext() {
        throw new UnsupportedOperationException();
    }

    @Override
    @Lookup("featureContext")
    public Context featureContext() {
        return null;
    }

    @Override
    public Context staticContext() {
        throw new UnsupportedOperationException();
    }

    @Override
    public Context sharedContext() {
        throw new UnsupportedOperationException();
    }

    @Override
    public Map<String, Object> snapshot() {
        return ImmutableMap.<String, Object>builder()
            .putAll(featureContext().getAll())
            .build();
    }
}
