package ru.iopump.qa.cucumber.tag;

import static java.lang.String.format;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Iterables;
import io.cucumber.java.Scenario;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.Optional;
import java.util.function.BiPredicate;
import java.util.function.Predicate;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import javax.annotation.Nullable;
import lombok.NonNull;
import lombok.Setter;
import ru.iopump.qa.annotation.PumpApi;
import ru.iopump.qa.util.StreamUtil;

@PumpApi
public class TagParser {

    private static final String DEFAULT_SEPARATOR = "=";

    @NonNull
    private final Collection<String> tags;
    @Setter
    @NonNull
    private String complexTagSeparator = DEFAULT_SEPARATOR;

    private TagParser(@NonNull Collection<String> tags) {
        this.tags = tags;
    }

    public static TagParser of(@Nullable Collection<String> rawTags) {
        return new TagParser(StreamUtil.stream(rawTags)
            .map(tag -> tag.startsWith("@") ? tag.substring(1) : tag)
            .collect(Collectors.toSet()));
    }

    public static TagParser of(Scenario scenario) {
        return of(scenario.getSourceTagNames());
    }


    @NonNull
    public Map<String, Collection<String>> findComplexTags(@Nullable Predicate<String> keyPredicate,
                                                           @Nullable Predicate<String> valuePredicate) {

        return StreamUtil.stream(getAllComplexTags())
            .filter(entry -> Optional.ofNullable(keyPredicate)
                .map(p -> p.test(entry.getKey())).orElse(true))
            .filter(entry -> {
                    final Collection<String> values = Optional.ofNullable(valuePredicate)
                        .map(p -> (Collection<String>) entry.getValue().stream().filter(p).collect(Collectors.toSet()))
                        .orElse(entry.getValue());
                    entry.setValue(values);
                    return !values.isEmpty();
                }
            ).collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }

    @NonNull
    public Optional<Collection<String>> findComplexTagValues(@NonNull String key) {
        return Optional.ofNullable(findComplexTags(key::equalsIgnoreCase, null).get(key));
    }

    @NonNull
    public Optional<String> findComplexTagOneValue(@NonNull String key) {
        return findComplexTagValues(key)
            .map(result -> {
                if (result.size() > 1) {
                    throw new IllegalArgumentException(format("We find several values %s for complex tag '%s'. Must be the only",
                        result, key));
                }
                return Iterables.getFirst(result, null);
            });
    }

    @NonNull
    public Collection<String> findTags(@NonNull String[] candidateTags,
                                       @NonNull BiPredicate<String, String> matcherCandidateVsActual) {
        return findTags(Arrays.asList(candidateTags), matcherCandidateVsActual);
    }

    @NonNull
    public Collection<String> findTags(@NonNull Collection<String> candidateTags,
                                       @NonNull BiPredicate<String, String> matcherCandidateVsActual) {
        return candidateTags.stream()
            .flatMap(candidate -> findTags(tag -> matcherCandidateVsActual.test(candidate.toLowerCase(),
                tag.toLowerCase())).stream())
            .collect(Collectors.toList());
    }

    @NonNull
    public Collection<String> findTags(@NonNull String tagKeyContains) {
        return findTags(tag -> tag.toLowerCase().contains(tagKeyContains.toLowerCase()));
    }

    @NonNull
    public Collection<String> findTags(@Nullable Predicate<String> tagPredicate) {
        return Optional.ofNullable(tagPredicate)
            .map(p -> (Collection<String>) tags.stream()
                .filter(tagPredicate)
                .collect(Collectors.toSet())
            ).orElse(tags);
    }

    private Map<String, Collection<String>> getAllComplexTags() {
        final Pattern pattern = complexTagPattern();
        return tags.stream()
            .map(pattern::matcher)
            .filter(Matcher::matches)
            .collect(Collectors
                .toMap(matcher -> matcher.group(1).toUpperCase(),
                    matcher -> Collections.singleton(matcher.group(2)),
                    (c1, c2) -> ImmutableSet.<String>builder().addAll(c1).addAll(c2).build()
                ));
    }

    private Pattern complexTagPattern() {
        return Pattern.compile("(.+?)" + complexTagSeparator + "(.+?)", Pattern.CASE_INSENSITIVE);
    }
}