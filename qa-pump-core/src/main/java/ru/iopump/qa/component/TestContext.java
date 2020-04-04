package ru.iopump.qa.component;

import java.io.Closeable;
import java.io.IOException;
import java.util.Map;
import ru.iopump.qa.component.context.Context;

public interface TestContext extends Closeable {

    /**
     * Single scenario vars in single thread.
     *
     * @return Scenario context.
     */
    Context scenarioContext();

    /**
     * Feature vars in single thread.
     *
     * @return Feature context.
     */
    Context featureContext();

    /**
     * Static global vars and {@link java.util.function.Supplier}.
     *
     * @return Static global context.
     */
    Context staticContext();

    /**
     * Global vars between features
     *
     * @return Context on all feature.
     */
    Context sharedContext();

    /**
     * Aggregate all variables and copy to immutable map.
     *
     * @return Snapshot of all current variables.
     */
    Map<String, Object> snapshot();

    @Override
    default void close() throws IOException {
        scenarioContext().close();
        featureContext().close();
        staticContext().close();
        sharedContext().close();
    }
}
