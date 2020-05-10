package ru.iopump.qa.cucumber.transformer;

import java.lang.reflect.Type;
import javax.annotation.Nullable;
import lombok.NonNull;
import ru.iopump.qa.cucumber.processor.ProcessResult;
import ru.iopump.qa.cucumber.transformer.api.GroovyTransformer;
import ru.iopump.qa.exception.PumpException;
import ru.iopump.qa.util.EnumUtil;

public class EnumTransformer extends GroovyTransformer<Enum<?>> {
    @Override
    public @NonNull Type targetType() {
        return Enum.class;
    }

    @Override
    public RelativeType relativeType() {
        return RelativeType.PARENT;
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    @Nullable
    @Override
    public Enum<?> transform(ProcessResult gherkinArgumentAfterPostProcessing, @NonNull Type toValueType) {
        try {
            final Class<Enum> enumClass = (Class<Enum>) toValueType;
            return EnumUtil.getByName(enumClass, gherkinArgumentAfterPostProcessing.getResultAsString());
        } catch (ClassCastException ex) {
            throw PumpException.of(
                "Enum Transformation error. Check your transformer.\nSource: {}\nTarget type: {}\nTransformer: {}",
                gherkinArgumentAfterPostProcessing, toValueType, this
            ).withCause(ex);
        }
    }
}
