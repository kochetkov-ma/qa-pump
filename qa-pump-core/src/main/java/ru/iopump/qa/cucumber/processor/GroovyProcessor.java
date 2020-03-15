package ru.iopump.qa.cucumber.processor;

import static ru.iopump.qa.component.groovy.GroovyUtil.asGString;
import static ru.iopump.qa.component.groovy.GroovyUtil.gStringContent;
import static ru.iopump.qa.component.groovy.GroovyUtil.isGString;
import static ru.iopump.qa.component.groovy.GroovyUtil.isString;
import static ru.iopump.qa.component.groovy.GroovyUtil.stringContent;
import static ru.iopump.qa.constants.PumpConfigKeys.PROCESSOR_STRICT;

import com.google.common.collect.ImmutableMap;
import groovy.lang.GString;
import groovy.lang.GroovyRuntimeException;
import javax.annotation.Nullable;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import ru.iopump.qa.component.TestContext;
import ru.iopump.qa.component.groovy.GroovyScript;
import ru.iopump.qa.exception.ProcessorException;
import ru.iopump.qa.util.Str;

@Slf4j
@Component
public class GroovyProcessor implements Processor<GroovyScript> {
    private final TestContext context;
    private final boolean strictMode;

    public GroovyProcessor(TestContext context, @Value("${" + PROCESSOR_STRICT + ":false}") boolean strictMode) {

        this.strictMode = strictMode;
        this.context = context;
    }

    public ProcessResult process(@Nullable String rawGherkinArgument, @NonNull GroovyScript evaluator) {
        if (evaluator.getBindingMap() != null) {
            // add context to script and merge with exist
            evaluator = evaluator.withBindingMap(
                ImmutableMap.<String, Object>builder().putAll(evaluator.getBindingMap()).putAll(context.snapshot()).build()
            );
        } else {
            // add context to script
            evaluator = evaluator.withBindingMap(context.snapshot());
        }

        var resultBuilder = ProcessResultImpl.builder();
        Object result = rawGherkinArgument;
        try {
            // Try evaluate as Groovy script
            result = evaluator.evaluate(rawGherkinArgument);
            if (result instanceof GString) {
                // Groovy String must be convert to Java String
                resultBuilder.result(Str.toStr(result));
            } else {
                resultBuilder.result(result);
            }
        } catch (GroovyRuntimeException scriptException) {
            if (strictMode) {
                throw ProcessorException.of(
                    "Gherkin argument interpolation error. Strict mode is enabled." +
                        "\nSource: {}." +
                        "\nYou should fix you step or try disable strictMode in configuration 'precessing.strict.mode=false'",
                    scriptException,
                    rawGherkinArgument
                );
            }
            // GroovyRuntimeException considers as expected behavior
            log.debug("Groovy processing error script string '{}'\nwith exception '{}'",
                rawGherkinArgument, scriptException.getLocalizedMessage());
            resultBuilder.processException(scriptException);

            // If it was GString -> throw exception
            if (isGString(rawGherkinArgument)) {
                throw ProcessorException.of(
                    "Gherkin argument '{}' surrounded '\"' or '\"\"\"' - it's a Groovy string.\n" +
                        "There are exception during interpolation. " +
                        "Fix it or replace quotes to single quotes without interpolation", scriptException, rawGherkinArgument
                );
            }

            // If it was Script -> try as GString
            if (!isString(rawGherkinArgument) && !isGString(rawGherkinArgument)) {
                try {
                    result = evaluator.evaluate(asGString(rawGherkinArgument));
                } catch (GroovyRuntimeException gException) {
                    resultBuilder.processException(gException);
                }
            }
        } catch (Exception throwingException) {
            // Another exceptions consider as user's mistake
            throw ProcessorException.of("Groovy processing error script string '{}' with logic exception",
                throwingException,
                rawGherkinArgument);
        }

        if (result == null) {
            if (isString(rawGherkinArgument)) {
                resultBuilder.resultAsString(stringContent(rawGherkinArgument));
            } else if (isGString(rawGherkinArgument)) {
                resultBuilder.resultAsString(gStringContent(rawGherkinArgument));
            } else {
                resultBuilder.resultAsString(rawGherkinArgument);
            }
        } else {
            resultBuilder.resultAsString(Str.toStr(result));
        }
        var res = resultBuilder.build();
        log.debug("Processed success\nProcessor:{}\nSource:{}\nResult:{}", getClass().getName(), rawGherkinArgument, res);
        return res;
    }
}