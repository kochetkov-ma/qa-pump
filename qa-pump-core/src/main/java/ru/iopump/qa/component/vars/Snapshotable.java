package ru.iopump.qa.component.vars;

import java.util.Map;
import lombok.NonNull;

public interface Snapshotable {

    /**
     * Get copy of content as immutable map.
     *
     * @return Copy of content as immutable map.
     */
    @NonNull
    Map<String, Object> snapshot();
}
