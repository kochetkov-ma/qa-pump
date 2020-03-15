package ru.iopump.qa.component.groovy;

import lombok.Value;

@Value
public class Import {
    boolean start;
    String importSpec;

    public boolean isNotStar() {
        return !start;
    }
}
