package ru.iopump.qa.component.vars;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import lombok.NonNull;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import ru.iopump.qa.cucumber.processor.ProcessingBean;
import ru.iopump.qa.exception.TestVarException;
import ru.iopump.qa.util.Str;

@Slf4j
abstract class AbstractVars implements Vars, ProcessingBean {

    private final Map<String, Object> map = Collections.synchronizedMap(new HashMap<>());
    @Setter
    private boolean errorIfNotExists = true;

    @Override
    public Object put(@NonNull String varName, Object value) {
        log.info("[VARS][{}] Put {}:{}", getClass().getSimpleName(), varName, Str.toStr(value));
        return map().put(varName, value);
    }

    @Override
    public Object remove(@NonNull String varName) {
        log.debug("[VARS][{}] Remove var '{}'", getClass().getSimpleName(), varName);
        return map().remove(varName);
    }

    @Override
    public Object get(@NonNull String varName) {
        log.debug("[VARS][{}] Get var '{}'", getClass().getSimpleName(), varName);
        if (map().containsKey(varName)) {
            return map().get(varName);
        }
        if (errorIfNotExists) {
            throw TestVarException.of("[{}] Var '{}' doesn't exist. All vars: {}",
                getClass().getSimpleName(), varName, map().keySet()
            );
        } else {
            return null;
        }
    }

    @Override
    public void close() {
        map().forEach((key, value) -> {
            if (value instanceof AutoCloseable) {
                try {
                    log.debug("[VARS][{}] Var '{}' is going to close", getClass().getSimpleName(), key);
                    ((AutoCloseable) value).close();
                } catch (Exception e) {
                    log.error(Str.frm("[VARS][{}] Error closing '{}'", getClass().getSimpleName(), key), e);
                }
            }
        });
        map().clear();
    }

    @Override
    public Map<String, Object> snapshot() {
        return Collections.unmodifiableMap(map());
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + " " + bindName() + " " + Str.toPrettyString(map());
    }

    /**
     * Internal map (must be mutable).
     */
    protected Map<String, Object> map() {
        return map;
    }
}