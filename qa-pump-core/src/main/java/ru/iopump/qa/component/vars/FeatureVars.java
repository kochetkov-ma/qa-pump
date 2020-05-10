package ru.iopump.qa.component.vars;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import ru.iopump.qa.constants.PumpConstants;

@Slf4j
@Component
@Scope(value = PumpConstants.FEATURE_SCOPE)
public class FeatureVars extends AbstractVars {
    @Value("${pump.bind.feature:#{null}}")
    private String overriddenBind;

    @Override
    public String bindName() {
        return overriddenBind == null ? "feature" : overriddenBind;
    }
}