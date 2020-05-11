package ru.iopump.qa.cucumber.transformer.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import java.lang.reflect.Type;
import java.util.Optional;
import javax.annotation.Nullable;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NonNull;
import ru.iopump.qa.annotation.PumpApi;
import ru.iopump.qa.cucumber.processor.ProcessResult;
import ru.iopump.qa.exception.PumpException;

@PumpApi
public abstract class GroovyObjectMapperTransformer<TARGET> extends GroovyTransformer<TARGET> {

    @Getter(AccessLevel.PROTECTED)
    public ObjectMapper objectMapper;

    public GroovyObjectMapperTransformer(@NonNull ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
        objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
    }

    @Nullable
    @Override
    public TARGET transform(@NonNull ProcessResult gherkinArgumentAfterProcessing, @NonNull Type toValueType) {
        final Optional<? extends RuntimeException> exception = gherkinArgumentAfterProcessing.getProcessException();
        final Optional<Object> fromObjectO = gherkinArgumentAfterProcessing.getResult();
        if (exception.isEmpty() && fromObjectO.isEmpty()) {
            return null;
        }

        final Object fromObject = fromObjectO.orElseThrow(() ->
            PumpException.of("Transforming error '{}'. Object result is empty '{}'",
                this,
                gherkinArgumentAfterProcessing)
                .withCause(gherkinArgumentAfterProcessing.getProcessException().orElse(null)));


        return convertWithObjectMapper(fromObject, toValueType, gherkinArgumentAfterProcessing.getSourceString());
    }

    /**
     * Helper method.
     *
     * @param fromObject               Object after processing.
     * @param toValueType              Target type from cucumber.
     * @param rawStringForErrorMessage Raw string from gherkin.
     * @return Final value for method argument with expected type.
     */
    protected TARGET convertWithObjectMapper(
        @Nullable Object fromObject,
        @NonNull Type toValueType,
        @Nullable String rawStringForErrorMessage
    ) {
        try {
            //noinspection unchecked
            return (TARGET) getObjectMapper().convertValue(fromObject, getObjectMapper().constructType(toValueType));
        } catch (Exception e) { //NOPMD
            throw PumpException.of(
                "Transformation error. Check your transformer.\nSource: {}\nTarget type: {}\nTransformer: {}",
                rawStringForErrorMessage, toValueType, this
            ).withCause(e);
        }
    }
}
