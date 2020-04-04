package ru.iopump.qa.cucumber.type;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.BeanFactory;
import ru.iopump.qa.annotation.PumpApi;
import ru.iopump.qa.cucumber.processor.ProcessResult;
import ru.iopump.qa.cucumber.processor.Processor;
import ru.iopump.qa.cucumber.transformer.AbstractTransformer;
import ru.iopump.qa.cucumber.transformer.Transformer;

/**
 * Use this resolver for define new parameter type {@link io.cucumber.java.ParameterType}.
 */
@PumpApi
@RequiredArgsConstructor
public class PumpTypeResolver {

    public final BeanFactory beanFactory;

    /**
     * More simple for external using with standard groovy processor.
     *
     * @param rawValueFromGherkin Argument string from feature file.
     * @param transformerClass    {@link Transformer} class for converting.
     * @param <TARGET>            Expected type form cucumber step-def method.
     * @return Converted and processed argument with expected type to step-def method.
     */
    public <TARGET> TARGET resolveWithGroovy(@NonNull String rawValueFromGherkin,
                                             @NonNull Class<? extends AbstractTransformer<TARGET>> transformerClass) {

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

        // Get Processor from context
        final Processor<P_TYPE, P_EXCEPTION> processor = beanFactory.getBean(transformer.preProcessorClass());

        // Evaluate Processor against gherkin argument string
        final ProcessResult<P_TYPE, P_EXCEPTION> processResult = processor.process(rawValueFromGherkin);

        // Call final conversation form processing value to cucumber step-def method type.
        return transformer.transform(processResult);
    }
}
