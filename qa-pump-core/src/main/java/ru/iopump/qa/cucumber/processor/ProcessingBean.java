package ru.iopump.qa.cucumber.processor;

import lombok.NonNull;

public interface ProcessingBean {
    /**
     * The name of the variable in processor script (or other template engine).
     * User should get access to the content of this bean thought this name.
     *
     * @return Binding (variable) name in processor engine context.
     */
    @NonNull
    String bindName();
}
