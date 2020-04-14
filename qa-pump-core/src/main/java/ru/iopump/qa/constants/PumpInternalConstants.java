package ru.iopump.qa.constants;

/**
 * For Pump Developers. Not foe users.
 */
public final class PumpInternalConstants {
    public PumpInternalConstants() {
        throw new AssertionError("utility class");
    }

    /**
     * For new steps developers. Put your Spring beans here.
     */
    public static final String COMPONENT_SCAN_PACKAGE_MAIN_DEFAULT = "ru.iopump.qa.component";
    /**
     * For new steps developers. Put your Spring beans here.
     */
    public static final String COMPONENT_SCAN_PACKAGE_EXTRA_DEFAULT = "ru.iopump.qa.step.component";
    /**
     * Fake package (because null or empty string means 'everywhere').
     * In common case user will override it via system / env / typesafe configuration.
     */
    public static final String COMPONENT_SCAN_PACKAGE_USER_DEFAULT = "ru.iopump.qa.user.component";
}
