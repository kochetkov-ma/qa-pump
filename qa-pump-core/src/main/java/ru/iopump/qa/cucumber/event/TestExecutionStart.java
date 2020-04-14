package ru.iopump.qa.cucumber.event;

import lombok.EqualsAndHashCode;
import lombok.Value;
import org.springframework.context.ApplicationEvent;
import ru.iopump.qa.spring.scope.FeatureSpec;

@EqualsAndHashCode(callSuper = true)
@Value
public class TestExecutionStart extends ApplicationEvent {
    private static final long serialVersionUID = -7548058109427064395L;
    FeatureSpec firstFeature;

    public TestExecutionStart(FeatureSpec firstFeature) {
        super(firstFeature);
        this.firstFeature = firstFeature;
    }
}