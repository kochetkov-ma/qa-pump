package ru.iopump.qa.component.vars;

import java.io.Closeable;
import javax.annotation.Nullable;
import lombok.NonNull;

public interface Vars extends Closeable, Snapshotable {

    Object put(@NonNull String varName, @Nullable Object value);

    Object remove(@NonNull String varName);

    Object get(@NonNull String varName);
}
