package ru.iopump.qa.constants;

import lombok.experimental.UtilityClass;

import static ru.iopump.qa.constants.PumpVariables.SPRING_ACTIVE_PROFILE_KEY;

@UtilityClass
public class PumpConstants {
    public final static String CONF_DIR_DEFAULT = "/configuration";
    public final static String CONF_FILE_DEFAULT = "${" + SPRING_ACTIVE_PROFILE_KEY + "}.conf";
}
