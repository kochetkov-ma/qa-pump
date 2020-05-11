package ru.iopump.qa.component.vars.predefined;

import com.google.common.collect.ImmutableSet;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.function.Supplier;
import javax.annotation.Nullable;
import lombok.NonNull;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.RandomUtils;
import org.apache.commons.lang3.SystemUtils;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.iopump.qa.spring.scope.Execution;
import ru.iopump.qa.spring.scope.FeatureSpec;

@Configuration
public class BuiltInStaticVars {

    public static StaticValue newValue(@NonNull String name, @Nullable Object value) {
        return new StaticValue() {

            @Override
            public Object getValue() {
                return value;
            }

            @Override
            public @NonNull String getName() {
                return name;
            }
        };
    }

    public static StaticSupplier newSupplier(@NonNull String name, @Nullable Supplier<Object> value) {
        return new StaticSupplier() {

            @Override
            public Supplier<Object> getValue() {
                return value;
            }

            @Override
            public @NonNull String getName() {
                return name;
            }
        };
    }

    @Bean
    StaticValue nullVar() {
        return newValue("NULL", null);
    }

    @Bean
    StaticSupplier startTime() {
        return newSupplier("started_time",
            () -> Execution.getFirstFeature().map(FeatureSpec::getStartTime).orElse(LocalDateTime.MIN));
    }

    @Bean
    @Qualifier("builtInStaticValues")
    Collection<StaticValue> builtInStaticValues() {
        return ImmutableSet.<StaticValue>builder()
            .add(newValue("hostname", SystemUtils.getHostName()))
            .build();
    }

    @Bean
    @Qualifier("builtInStaticSuppliers")
    Collection<StaticSupplier> builtInStaticSuppliers() {
        return ImmutableSet.<StaticSupplier>builder()
            .add(newSupplier("now", LocalDateTime::now))
            .add(newSupplier("timestamp", () -> LocalDateTime.now().getNano()))
            .add(newSupplier("rnd_int", RandomUtils::nextInt))
            .add(newSupplier("rnd_long", RandomUtils::nextLong))
            .add(newSupplier("rnd_char", () -> RandomStringUtils.random(1)))
            .build();
    }
}
