package ru.iopump.qa.cucumber.type;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.lang.reflect.Type;
import java.util.Collection;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.BeanFactory;
import ru.iopump.qa.annotation.PumpApi;
import ru.iopump.qa.cucumber.processor.ProcessResult;
import ru.iopump.qa.cucumber.processor.Processor;
import ru.iopump.qa.cucumber.transformer.AbstractGroovyTransformer;
import ru.iopump.qa.cucumber.transformer.Transformer;
import ru.iopump.qa.exception.PumpException;
import ru.iopump.qa.util.Str;

/**
 * Use this resolver for define new parameter type {@link io.cucumber.java.ParameterType}.
 */
@PumpApi
@RequiredArgsConstructor
@Slf4j
public class PumpTypeResolver {

    public final BeanFactory beanFactory;
    public final TransformerProvider transformerProvider;

    public <TARGET> TARGET resolve(
        @NonNull Object rawValueFromGherkin,
        @NonNull Type toValueType) {

        Collection<Transformer> transformers = transformerProvider.findByTypeSorted(toValueType);

        //noinspection rawtypes
        final Transformer transformer = transformers.stream().findFirst().orElseThrow(() ->
            PumpException.of("Cannot find any transformers.\nSource: {}\nTarget type: {}\nAll transformers: {}",
                rawValueFromGherkin,
                toValueType,
                Str.toPrettyString(transformerProvider.getAll())
            ));

        log.debug("Found transfer.\nSource: {}\nTarget type: {}\nTransformer: {}", rawValueFromGherkin, toValueType, transformer);

        TARGET result;
        if (rawValueFromGherkin instanceof String) {
            //noinspection unchecked
            Object object = resolve((String) rawValueFromGherkin, transformer);
            try {
                //noinspection unchecked
                result = (TARGET) object;
            } catch (ClassCastException e) {
                throw PumpException.of(
                    "Transformation error. Check your transformer.\nSource: {}\nTarget type: {}\nTransformer: {}",
                    rawValueFromGherkin, toValueType, transformer
                ).withCause(e);
            }
        } else {
            var om = beanFactory.getBean(ObjectMapper.class);
            result = om.convertValue(rawValueFromGherkin, om.constructType(toValueType));
        }
        return result;
    }

    /**
     * More simple for external using with standard groovy processor.
     *
     * @param rawValueFromGherkin Argument string from feature file.
     * @param transformerClass    {@link Transformer} class for converting.
     * @param <TARGET>            Expected type form cucumber step-def method.
     * @return Converted and processed argument with expected type to step-def method.
     */
    public <TARGET> TARGET resolveWithGroovy(@NonNull String rawValueFromGherkin,
                                             @NonNull Class<? extends AbstractGroovyTransformer<TARGET>> transformerClass) {

        return resolve(rawValueFromGherkin, transformerClass);
    }

    /**
     * Complex type-safe resolving.
     *
     * @param rawValueFromGherkin Argument string from feature file.
     * @param transformerClass    {@link Transformer} class for converting.
     * @param <TARGET>            Expected type form cucumber step-def method.
     * @param <P_TYPE>            Type after processing full argument string.
     * @param <P_EXCEPTION>       Suppressed processing exception type.
     * @return Converted and processed argument with expected type to step-def method.
     */
    public <TARGET, P_TYPE, P_EXCEPTION extends RuntimeException> TARGET resolve(
        @NonNull String rawValueFromGherkin,
        @NonNull Class<? extends Transformer<TARGET, P_TYPE, P_EXCEPTION, ? extends Processor<P_TYPE, P_EXCEPTION>>> transformerClass) {

        // Get transformer from context
        final Transformer<TARGET, P_TYPE, P_EXCEPTION, ? extends Processor<P_TYPE, P_EXCEPTION>> transformer
            = beanFactory.getBean(transformerClass);
        return resolve(rawValueFromGherkin, transformer);
    }

    //// PRIVATE

    private <TARGET, P_TYPE, P_EXCEPTION extends RuntimeException> TARGET resolve(
        @NonNull String rawValueFromGherkin,
        @NonNull Transformer<TARGET, P_TYPE, P_EXCEPTION, ? extends Processor<P_TYPE, P_EXCEPTION>> transformer) {

        log.debug("Start transforming.\nSource:{}\nTransformer:{}", rawValueFromGherkin, transformer);

        // Get Processor from context
        final Processor<P_TYPE, P_EXCEPTION> processor = beanFactory.getBean(transformer.preProcessorClass());

        // Evaluate Processor against gherkin argument string
        final ProcessResult<P_TYPE, P_EXCEPTION> processResult = processor.process(rawValueFromGherkin);

        // Call final conversation form processing value to cucumber step-def method type.
        TARGET result = transformer.transform(processResult);

        log.debug("Finish transforming.\nSource:{}\nTransformer:{}\nWith result:{}",
            rawValueFromGherkin, transformer, Str.toStr(result));
        return result;
    }
}
