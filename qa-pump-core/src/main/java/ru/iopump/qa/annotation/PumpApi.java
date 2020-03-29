package ru.iopump.qa.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Mark stable (API) classes for external usage.
 */
@Documented
@Inherited
@Retention(RetentionPolicy.CLASS)
@Target( {ElementType.TYPE, ElementType.METHOD, ElementType.CONSTRUCTOR})
public @interface PumpApi {
    /**
     * Briefly description.
     *
     * @return Briefly description.
     */
    String value() default "";
}
