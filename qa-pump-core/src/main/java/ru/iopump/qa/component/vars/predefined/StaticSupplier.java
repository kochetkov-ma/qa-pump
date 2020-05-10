package ru.iopump.qa.component.vars.predefined;

import java.util.function.Supplier;
import ru.iopump.qa.common.WithName;
import ru.iopump.qa.support.api.WithValue;

public interface StaticSupplier extends WithName, WithValue<Supplier<Object>> {
}