package ru.iopump.qa.cucumber.transformer;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.lang.reflect.Type;
import javax.annotation.Nullable;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import ru.iopump.qa.cucumber.processor.ProcessResult;
import ru.iopump.qa.cucumber.transformer.api.GroovyObjectMapperTransformer;

public final class LastResortTransformer extends GroovyObjectMapperTransformer<Object> {

    public LastResortTransformer(@NonNull ObjectMapper objectMapper) {
        super(objectMapper);
    }

    @Override
    public int priority() {
        return Integer.MIN_VALUE;
    }

    @Override
    public Type targetType() {
        return Object.class;
    }

    @Nullable
    @Override
    public Object transform(@NonNull ProcessResult gherkinArgumentAfterProcessing, @NonNull Type toValueType) {
        var fromObject = gherkinArgumentAfterProcessing.getResult().orElse(gherkinArgumentAfterProcessing.getResultAsString());
        return convertWithObjectMapper(fromObject, toValueType, gherkinArgumentAfterProcessing.getSourceString());
    }
}
