package ru.iopump.qa.cucumber.event;

import lombok.EqualsAndHashCode;
import lombok.Value;
import org.springframework.context.ApplicationEvent;
import ru.iopump.qa.spring.scope.FeatureSpec;

@EqualsAndHashCode(callSuper = true)
@Value
public class FeatureStart extends ApplicationEvent {

    private static final long serialVersionUID = -8620921053366242257L;
    FeatureSpec feature;

    public FeatureStart(FeatureSpec feature) {
        super(feature);
        this.feature = feature;
    }
}