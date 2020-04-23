package ru.iopump.qa.cucumber.processor;

import java.util.Collection;
import java.util.Map;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.ToString;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.cache.annotation.Cacheable;
import ru.iopump.qa.exception.ProcessorException;

@ToString
@AllArgsConstructor
public class ProcessingContext {
    private static final Pattern bindPattern = Pattern.compile("\\w{3,}");
    private final ObjectFactory<Collection<? extends ProcessingBean>> processingBeansProvider;

    @Cacheable
    public Map<String, Object> getBingMap() {
        return processingBeansProvider.getObject().stream().collect(Collectors.toMap(
            this::bind,
            bean -> bean,
            (processingBean, processingBean2) -> {
                throw ProcessorException.of("Found two ProcessingBean '{} and {}' with the same name '{}'. " +
                        "It's not allowed. Keep only one in Spring Context",
                    processingBean, processingBean2, ((ProcessingBean)processingBean).bindName());
            })
        );
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
