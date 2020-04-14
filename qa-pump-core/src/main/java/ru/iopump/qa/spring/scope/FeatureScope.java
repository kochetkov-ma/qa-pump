package ru.iopump.qa.spring.scope;

import static ru.iopump.qa.spring.scope.RunnerType.CUCUMBER_SINGLE_THREAD;
import static ru.iopump.qa.spring.scope.RunnerType.PUMP_JUNIT;
import static ru.iopump.qa.util.Str.frm;

import io.cucumber.junit.Pump;
import io.cucumber.junit.PumpFeatureParallel;
import io.cucumber.junit.PumpScenarioParallel;
import javax.annotation.Nonnull;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.beans.factory.config.Scope;

public final class FeatureScope implements Scope {
    public final static String SCOPE_PUMP_FEATURE = "cucumber-feature";

    static RunnerType RUNNER_TYPE;

    @Override
    @Nonnull
    public Object get(@Nonnull String name, @Nonnull ObjectFactory<?> objectFactory) {
        checkScope();

        final FeatureCodeScope context = FeatureCodeScope.getInstance();
        Object obj = context.get(name);
        if (obj == null) {
            obj = objectFactory.getObject();
            context.put(name, obj);
        }
        return obj;
    }

    @Override
    public Object remove(@Nonnull String name) {
        final FeatureCodeScope context = FeatureCodeScope.getInstance();
        return context.remove(name);
    }

    @Override
    public void registerDestructionCallback(@Nonnull String name, @Nonnull Runnable callback) {
        final FeatureCodeScope context = FeatureCodeScope.getInstance();
        context.registerDestructionCallback(name, callback);
    }

    @Override
    public Object resolveContextualObject(@Nonnull String key) {
        return null;
    }

    @Override
    public String getConversationId() {
        final FeatureCodeScope context = FeatureCodeScope.getInstance();
        return context.getId();
    }

    private void checkScope() {
        final RunnerType runner = RUNNER_TYPE;
        final boolean viaSupportedRunners = (runner == CUCUMBER_SINGLE_THREAD || runner == PUMP_JUNIT);

        if (!viaSupportedRunners) {
            throw new IllegalStateException(frm(
                "You use unsupported Runner.\n" +
                    "Pump Framework support only two Cucumber Runners: {} and {}\n" +
                    "You should run your tests via @RunWith({}) or @RunWith({}} or @RunWith({}) with as JUnit tests.\n" +
                    "Also you can use Cucumber Main CLI with Pump Hook CoreCucumberHook.class " +
                    "- just add pkg 'ru.iopump.qa.glue' to your glue",
                CUCUMBER_SINGLE_THREAD,
                PUMP_JUNIT,
                Pump.class.getName(),
                PumpFeatureParallel.class.getName(),
                PumpScenarioParallel.class.getName()
            ));
        }
    }
}
