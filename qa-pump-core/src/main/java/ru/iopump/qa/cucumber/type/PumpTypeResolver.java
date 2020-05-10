package ru.iopump.qa.cucumber.type;

import static ru.iopump.qa.constants.PumpConfigKeys.TRANSFORMER_LAST_RESORT;

import java.lang.reflect.Type;
import java.util.Collection;
import java.util.Optional;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import javax.annotation.Nullable;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.annotation.Value;
import ru.iopump.qa.annotation.PumpApi;
import ru.iopump.qa.cucumber.processor.ProcessResult;
import ru.iopump.qa.cucumber.processor.Processor;
import ru.iopump.qa.cucumber.transformer.api.Transformer;
import ru.iopump.qa.exception.PumpException;
import ru.iopump.qa.util.Str;

/**
 * Use this resolver for define new parameter type {@link io.cucumber.java.ParameterType}.
 */
@PumpApi
@Slf4j
public class PumpTypeResolver {

    public final BeanFactory beanFactory;
    public final TransformerProvider transformerProvider;
    public final boolean lastResort;

    public PumpTypeResolver(BeanFactory beanFactory,
                            TransformerProvider transformerProvider,
                            @Value("${" + TRANSFORMER_LAST_RESORT + ":true}") boolean lastResort) {
        this.beanFactory = beanFactory;
        this.transformerProvider = transformerProvider;
        this.lastResort = lastResort;
    }

    public <TARGET> TARGET resolve(
        @NonNull Object rawValueFromGherkin,
        @NonNull Type toValueType) {

        //noinspection rawtypes
        final Collection<Transformer> transformers = transformerProvider.findByTypeSorted(toValueType);
        log.debug("Related transformers.\nSource: {}\nTarget type: {}\nTransformers: {}",
            rawValueFromGherkin,
            toValueType,
            Str.toPrettyString(transformers.stream().map(t -> t.getClass().getName()).collect(Collectors.toList()))
        );

        //noinspection rawtypes
        final Transformer transformer = transformers.stream().findFirst()
            .or(() -> Optional.ofNullable(lastResort ? transformerProvider.getLastResortTransformer() : null))
            .orElseThrow(() -> PumpException.of("Cannot find any transformer.\nSource: {}\nTarget type: {}\nAll transformers: {}",
                rawValueFromGherkin,
                toValueType,
                Str.toPrettyString(transformerProvider.getAll())
            ));

        log.debug("Found transformer.\nSource: {}\nTarget type: {}\nTransformer: {}", rawValueFromGherkin, toValueType, transformer);

        //noinspection unchecked
        return (TARGET) resolve((String) rawValueFromGherkin, transformer, toValueType);
    }

    /**
     * Complex type-safe resolving.
     *
     * @param rawValueFromGherkin Argument string from feature file.
     * @param transformerClass    {@link Transformer} class for converting.
     * @param <TARGET>            Expected type form cucumber step-def method.
     * @param <HELPER>            Type of processing helper.
     * @param <PROCESSOR>         Processor base type.
     * @return Converted and processed argument with expected type to step-def method.
     */
    @SuppressWarnings("unused")
    public <TARGET, HELPER, PROCESSOR extends Processor<HELPER>> TARGET resolve(
        @NonNull String rawValueFromGherkin,
        @NonNull Class<? extends Transformer<TARGET, HELPER, PROCESSOR>> transformerClass) {

        // Get transformer from context
        final Transformer<TARGET, HELPER, PROCESSOR> transformer = beanFactory.getBean(transformerClass);
        return resolve(rawValueFromGherkin, transformer, transformer.targetType());
    }

    //// PRIVATE

    //region Private methods
    private <TARGET, HELPER, PROCESSOR extends Processor<HELPER>> TARGET resolve(
        @NonNull String rawValueFromGherkin,
        @NonNull Transformer<TARGET, HELPER, PROCESSOR> transformer,
        @Nullable Type toValueType) {

        log.debug("Start transforming.\nSource:{}\nTransformer:{}", rawValueFromGherkin, transformer);

        // Get Processor from context
        final Processor<HELPER> processor = beanFactory.getBean(transformer.processorClass());

        // Create helper or null
        final HELPER helper = Optional.ofNullable(transformer.helperSupplier()).map(Supplier::get).orElse(null);

        // Evaluate Processor against gherkin argument string
        final ProcessResult processResult = processor.process(rawValueFromGherkin, helper);

        // Call final conversation form processing value to cucumber step-def method type.
        TARGET result = transformer.transform(processResult, toValueType);

        log.debug("Finish transforming." +
                "\nSource:{}" +
                "\nTransformer:{}" +
                "\nHelper:{}" +
                "\nWith result:{}" +
                "\nArgument type:{}",
            rawValueFromGherkin, transformer, Str.toStr(helper), Str.toStr(result), toValueType);
        return result;
    }
//endregion
}
