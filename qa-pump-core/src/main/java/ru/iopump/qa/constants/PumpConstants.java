package ru.iopump.qa.constants;

import lombok.experimental.UtilityClass;
import org.apache.commons.lang3.StringUtils;

@UtilityClass
public class PumpConstants {
    /**
     * Default configuration folder in classpath resources.
     */
    public final static String CONF_DIR_DEFAULT = "/configuration";

    /**
     * Default configuration file name.
     * Prefix = 'pump-'.
     * Main name = {@link PumpVariables#SPRING_ACTIVE_PROFILE_KEY}.
     * Extension = '.conf'.
     * Full name = Prefix + Main name + Extension -> 'pump-default.conf' when Spring Profile = 'default'.
     * Full default path will be '/configuration/pump-default.conf' in classpath resources.
     */
    public static String getDefaultConfigName(String activeProfile) {
        activeProfile = StringUtils.isBlank(activeProfile) ? "default" : activeProfile;
        return "pump-" + activeProfile + ".conf";
    }
}
