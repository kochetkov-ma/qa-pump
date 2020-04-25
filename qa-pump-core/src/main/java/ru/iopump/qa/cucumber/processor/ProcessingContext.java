package ru.iopump.qa.cucumber.processor;

import com.google.common.collect.Maps;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.ToString;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.util.CollectionUtils;
import ru.iopump.qa.component.vars.Snapshotable;
import ru.iopump.qa.exception.ProcessorException;

@ToString
@AllArgsConstructor
public class ProcessingContext {
    private static final Pattern bindPattern = Pattern.compile("\\w{3,}");
    private final ObjectFactory<Collection<? extends ProcessingBean>> processingBeansProvider;
    private final ObjectFactory<List<String>> directBindings;

    /**
     * Create binding map consider of direct bindings.
     * <p>For example:</p>
     * Without direct binds you should use scenarios vars like 'scenario.var_name'</br>
     * But with direct bind 'scenario' you should use scenarios vars like 'var_name'</br>
     *
     * @param directBindings Directs binding collection with DESC ordering. Fist bind will override following
     * @return Binding map.
     */
    public Map<String, Object> getBingMap(@NonNull List<String> directBindings) {
        // Calculate map from processing beans
        final Map<String, Object> bindMap = processingBeansProvider.getObject().stream().collect(Collectors.toMap(
            this::bind,
            bean -> bean,
            (processingBean, processingBean2) -> {
                throw ProcessorException.of("Found two ProcessingBean '{} and {}' with the same name '{}'. " +
                        "It's not allowed. Keep only one in Spring Context",
                    processingBean, processingBean2, ((ProcessingBean) processingBean).bindName());
            })
        );

        // If directBindings is not empty
        if (!CollectionUtils.isEmpty(directBindings)) {
            //noinspection UnnecessaryLocalVariable
            final List<String> tmp = directBindings; // Create tmp for reversing to avoid side effects
            Collections.reverse(tmp); // Reverse order for further foreach
            final Map<String, Object> newDirectBinds = Maps.newHashMap(); // Direct binds
            for (String directBind : tmp) {
                if (bindMap.containsKey(directBind) && bindMap.get(directBind) instanceof Snapshotable) {
                    // Put all and override previous. That's why we use reversed directBindings list
                    newDirectBinds.putAll(((Snapshotable) bindMap.remove(directBind)).snapshot());
                }
            }
            newDirectBinds.forEach((bindName, value) ->
                bindMap.merge(
                    bindName,
                    value,
                    (left, right) -> {
                        throw ProcessorException.of("Found ProcessingBean '{}' and Direct Bind '{}' with the same name '{}'. " +
                                "It's not allowed. Keep only one in Spring Context",
                            left, right, bindName);
                    }
                )
            );
        }
        return bindMap;
    }

    public Map<String, Object> getBingMap() {
        return getBingMap(directBindings.getObject());
    }

    @NonNull
    private String bind(@NonNull ProcessingBean bean) {
        if (bindPattern.matcher(bean.bindName()).matches()) {
            return bean.bindName();
        }
        throw ProcessorException.of("{} has wrong bind name '{}'. It must match the pattern '{}'",
            bean, bean.bindName(), bindPattern);
    }
}
