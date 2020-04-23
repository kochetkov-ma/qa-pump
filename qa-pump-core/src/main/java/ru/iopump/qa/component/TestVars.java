package ru.iopump.qa.component;

import java.io.Closeable;
import java.io.IOException;
import java.util.Map;
import ru.iopump.qa.component.vars.Vars;

public interface TestVars extends Closeable {

    /**
     * Single scenario vars in single thread.
     *
     * @return Scenario context.
     */
    Vars scenarioVars();

    /**
     * Feature vars in single thread.
     *
     * @return Feature context.
     */
    Vars featureVars();

    /**
     * Static global vars and {@link java.util.function.Supplier}.
     *
     * @return Static global context.
     */
    Vars staticVars();

    /**
     * Global vars between features
     *
     * @return Context on all feature.
     */
    Vars sharedVars();

    /**
     * Aggregate all variables and copy to immutable map.
     *
     * @return Snapshot of all current variables.
     */
    Map<String, Object> snapshot();

    @Override
    default void close() throws IOException {
        scenarioVars().close();
        featureVars().close();
        staticVars().close();
        sharedVars().close();
    }
}
