package ru.iopump.qa.constants;

import lombok.experimental.UtilityClass;

/**
 * For Pump Developers. Not foe users.
 */
@UtilityClass
public class PumpInternalConstants {
    /**
     * For new steps developers. Put your Spring beans here.
     */
    public final static String COMPONENT_SCAN_PACKAGE_MAIN_DEFAULT = "ru.iopump.qa.component";
    /**
     * For new steps developers. Put your Spring beans here.
     */
    public final static String COMPONENT_SCAN_PACKAGE_EXTRA_DEFAULT = "ru.iopump.qa.step.component";
    /**
     * Fake package (because null or empty string means 'everywhere').
     * In common case user will override it via system / env / typesafe configuration.
     */
    public final static String COMPONENT_SCAN_PACKAGE_USER_DEFAULT = "ru.iopump.qa.user.component";
}
