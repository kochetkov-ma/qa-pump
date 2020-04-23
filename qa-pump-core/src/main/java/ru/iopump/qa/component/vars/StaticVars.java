package ru.iopump.qa.component.vars;

import java.util.Map;
import java.util.function.Supplier;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@Scope(value = ConfigurableBeanFactory.SCOPE_SINGLETON)
@AllArgsConstructor
public class StaticVars extends AbstractVars {

    private final Map<String, Supplier<Object>> staticSupplierMap;
    private final Map<String, Object> staticVariableMap;

    @Override
    public String bindName() {
        return "static";
    }
}