package ru.iopump.qa.cucumber.type;

import io.cucumber.java.ParameterType;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import ru.iopump.qa.cucumber.transformer.StringTransformer;

@RequiredArgsConstructor
class CoreCucumberTypes {

    public static final String PUMP_TYPE_PATTERN = "(.+?)";

    private final PumpTypeResolver resolver;

    @ParameterType(value = PUMP_TYPE_PATTERN, preferForRegexMatch = true)
    public String pString(@NonNull String rawValueFromGherkin) {
        return resolver.resolveWithGroovy(rawValueFromGherkin, StringTransformer.class);
    }

    @ParameterType(value = PUMP_TYPE_PATTERN, preferForRegexMatch = true)
    public Object pObject(@NonNull String rawValueFromGherkin) {
        return resolver.resolveWithGroovy(rawValueFromGherkin, StringTransformer.class);
    }
}
