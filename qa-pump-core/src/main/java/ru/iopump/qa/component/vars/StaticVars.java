package ru.iopump.qa.component.vars;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import ru.iopump.qa.component.vars.predefined.StaticSupplier;
import ru.iopump.qa.component.vars.predefined.StaticValue;
import ru.iopump.qa.util.StreamUtil;

@Slf4j
@Component
@Scope(value = ConfigurableBeanFactory.SCOPE_SINGLETON)
public class StaticVars extends AbstractVars {
    private final LazyMap map = new LazyMap();
    @Value("${pump.bind.static:#{null}}")
    private String overriddenBind;

    public StaticVars(final Collection<StaticValue> staticValues,
                      final Collection<StaticSupplier> staticSuppliers,
                      @Qualifier("builtInStaticValues") final Collection<StaticValue> builtInStaticValues,
                      @Qualifier("builtInStaticSuppliers") final Collection<StaticSupplier> builtInStaticSuppliers
    ) {
        StreamUtil.stream(builtInStaticSuppliers).forEach(i -> map.put(i.getName(), i.getValue()));
        StreamUtil.stream(staticSuppliers).forEach(i -> map.put(i.getName(), i.getValue()));

        // Will override 'StaticSupplier'
        StreamUtil.stream(builtInStaticValues).forEach(i -> map.put(i.getName(), i.getValue()));
        StreamUtil.stream(staticValues).forEach(i -> map.put(i.getName(), i.getValue()));
    }

    @Override
    public Object remove(@NonNull String varName) {
        throw new UnsupportedOperationException("Remove is unsupported for 'StaticVars'. Consider using 'SharedVars'");
    }

    @Override
    public Object put(@NonNull String varName, Object value) {
        throw new UnsupportedOperationException("Put is unsupported for 'StaticVars'. Consider using 'SharedVars'");
    }

    @Override
    public String bindName() {
        return overriddenBind == null ? "static" : overriddenBind;
    }

    @Override
    protected Map<String, Object> map() {
        return map;
    }

    private static class LazyMap extends HashMap<String, Object> {
        private static final long serialVersionUID = -6029938255154367275L;

        @Override
        public Object get(Object key) {
            return eval(super.get(key));
        }

        @Override
        public Set<Entry<String, Object>> entrySet() {
            return StreamUtil.stream(super.entrySet())
                .map(e -> new SimpleImmutableEntry<>(e.getKey(), eval(e.getValue()))).collect(Collectors.toSet());
        }

        @Override
        public Collection<Object> values() {
            return StreamUtil.stream(super.values()).map(this::eval).collect(Collectors.toSet());
        }

        //region Private methods
        private Object eval(Object res) {
            if (res instanceof Supplier) {
                //noinspection rawtypes
                return ((Supplier) res).get(); // evaluate value at runtime
            } else {
                return res;
            }
        }
        //endregion

    }
}