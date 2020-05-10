package ru.iopump.qa.cucumber.transformer;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.lang.reflect.Type;
import lombok.NonNull;
import ru.iopump.qa.cucumber.transformer.api.GroovyObjectMapperTransformer;

public final class ObjectTransformer extends GroovyObjectMapperTransformer<Object> {

    public ObjectTransformer(@NonNull ObjectMapper objectMapper) {
        super(objectMapper);
    }

    @Override
    public Type targetType() {
        return Object.class;
    }
}
