package ru.iopump.qa.component.context;

import com.google.common.collect.Maps;
import java.util.Collections;
import java.util.Map;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@Scope(value = ConfigurableBeanFactory.SCOPE_SINGLETON, proxyMode = ScopedProxyMode.TARGET_CLASS)
public class FeatureContext implements Context {

    private final Map<String, Object> map = Maps.newConcurrentMap();

    @Override
    public Object put(@NonNull String varName, Object value) {
        return map.put(varName, value);
    }

    @Override
    public Object remove(@NonNull String varName) {
        return map.remove(varName);
    }

    @Override
    public Object get(@NonNull String varName) {
        return map.get(varName);
    }

    @Override
    public Map<String, Object> getAll() {
        return Collections.unmodifiableMap(map);
    }

    @Override
    public void close() {
        map.values().forEach(value -> {
            if (value instanceof AutoCloseable) {
                try {
                    ((AutoCloseable) value).close();
                } catch (Exception e) {
                    log.error("FeatureContext value closing error: " + value, e);
                }
            }
        });
        map.clear();
    }
}
