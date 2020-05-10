package ru.iopump.qa.component.vars;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@Scope(value = ConfigurableBeanFactory.SCOPE_SINGLETON)
public class SharedVars extends AbstractVars {
    @Value("${pump.bind.shared:#{null}}")
    private String overriddenBind;

    @Override
    public String bindName() {
        return overriddenBind == null ? "shared" : overriddenBind;
    }
}