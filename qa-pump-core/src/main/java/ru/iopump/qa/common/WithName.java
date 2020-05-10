package ru.iopump.qa.common;

import javax.annotation.Nullable;
import lombok.NonNull;
import org.apache.commons.lang3.ObjectUtils;

public interface WithName {
    @NonNull
    String getName();

    default boolean hasName(@Nullable String expectedEqualsNameIgnoreCase) {
        return getName().equalsIgnoreCase(ObjectUtils.defaultIfNull(expectedEqualsNameIgnoreCase, ""));
    }
}
