package ru.iopump.qa.component.context;

import java.io.Closeable;
import java.util.Collection;
import java.util.Map;
import javax.annotation.Nullable;
import lombok.NonNull;

public interface Context extends Closeable {

    Object put(@NonNull String varName, @Nullable Object value);

    Object remove(@NonNull String varName);

    Object get(@NonNull String varName);

    Map<String, Object> getAll();
}
