package ru.iopump.qa.cucumber.event;

import lombok.EqualsAndHashCode;
import lombok.Value;
import org.springframework.context.ApplicationEvent;
import ru.iopump.qa.spring.scope.FeatureSpec;

@EqualsAndHashCode(callSuper = true)
@Value
public class FeatureFinish extends ApplicationEvent {

    private static final long serialVersionUID = -8620921053369242257L;
    FeatureSpec feature;

    public FeatureFinish(FeatureSpec feature) {
        super(feature);
        this.feature = feature;
    }
}