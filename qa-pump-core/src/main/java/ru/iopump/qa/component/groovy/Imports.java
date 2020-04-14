package ru.iopump.qa.component.groovy;

import static java.util.stream.Collectors.toSet;

import java.util.Collection;
import java.util.Map;
import lombok.Builder;
import lombok.Singular;
import lombok.Value;
import org.apache.commons.lang3.tuple.Pair;
import ru.iopump.qa.annotation.PumpApi;

@Value
@Builder
@PumpApi
public class Imports {
    /**
     * For example: import java.util.Collection;<br>
     * java.util.Collection - Class
     */
    @Singular
    Collection<Class<?>> simpleImports;
    /**
     * For example: import java.util.*;<br>
     * java.util - package name
     */
    @Singular
    Collection<String> starImports;
    /**
     * For example: import static java.util.stream.Collectors.toSet<br>
     * java.util.stream.Collectors - Class<br>
     * toSet - method or field name
     */
    @Singular
    Map<Class<?>, String> staticImports;
    /**
     * For example: import static java.util.stream.Collectors.*<br>
     * java.util.stream.Collectors - Class
     */
    @Singular
    Collection<Class<?>> staticStarImports;

    public String[] simpleImports() {
        return simpleImports.stream().map(Class::getName).toArray(String[]::new);
    }

    public String[] starImports() {
        return starImports.toArray(new String[0]);
    }

    public Collection<Pair<String, String>> staticImports() {
        return staticImports.entrySet().stream().map(i -> Pair.of(i.getKey().getName(), i.getValue())).collect(toSet());
    }

    public String[] staticStarImports() {
        return staticStarImports.stream().map(Class::getName).toArray(String[]::new);
    }
}
