package ru.iopump.qa.glue;

import io.cucumber.java.DefaultDataTableCellTransformer;
import io.cucumber.java.DefaultDataTableEntryTransformer;
import io.cucumber.java.DefaultParameterTransformer;
import java.lang.reflect.Type;
import lombok.RequiredArgsConstructor;
import ru.iopump.qa.cucumber.type.PumpTypeResolver;

@RequiredArgsConstructor
public class CoreCucumberTypes {

    private final PumpTypeResolver resolver;

    @DefaultParameterTransformer
    @DefaultDataTableEntryTransformer
    @DefaultDataTableCellTransformer
    public Object transformer(Object fromValue, Type toValueType) {
        return resolver.resolve(fromValue, toValueType);
    }
}
