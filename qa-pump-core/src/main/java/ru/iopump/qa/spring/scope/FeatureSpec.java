package ru.iopump.qa.spring.scope;

import io.cucumber.java.Scenario;
import java.lang.reflect.Field;
import java.time.LocalDateTime;
import java.util.Optional;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;
import org.junit.runner.Description;
import ru.iopump.qa.exception.PumpException;
import ru.iopump.qa.util.ClassUtil;
import ru.iopump.qa.util.Str;

@ToString
@Setter
@Accessors(chain = true)
@RequiredArgsConstructor
public final class FeatureSpec {
    @Getter
    private final String title;
    @Getter
    private final String uri;
    @Getter
    private final LocalDateTime startTime;
    private LocalDateTime stopTime;

    /**
     * Can be empty for last feature if using source Cucumber runner or cucumber Main CLI.
     */
    public Optional<LocalDateTime> getStopTime() {
        return Optional.ofNullable(stopTime);
    }

    public String id() {
        return title + " " + uri;
    }

    public static FeatureSpec fromDescription(@NonNull Description description) {
        final Field field;
        try {
            field = ClassUtil.getClass(description).getDeclaredField("fUniqueId");
            field.setAccessible(true);
            return new FeatureSpec(description.getDisplayName(), Str.toStr(field.get(description)), LocalDateTime.now());
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw PumpException.of("Error when create cucumber scope " + description).withCause(e);
        }
    }

    public static FeatureSpec fromScenario(@NonNull Scenario scenario) {
        return new FeatureSpec("scenario hook", scenario.getUri().toString(), LocalDateTime.now());
    }
}
