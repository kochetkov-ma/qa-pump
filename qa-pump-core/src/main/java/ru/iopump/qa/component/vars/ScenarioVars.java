package ru.iopump.qa.component.vars;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import ru.iopump.qa.constants.PumpConstants;

@Slf4j
@Component
@Scope(value = PumpConstants.SCENARIO_SCOPE)
public class ScenarioVars extends AbstractVars {
    @Override
    public String bindName() {
        return "feature";
    }
}