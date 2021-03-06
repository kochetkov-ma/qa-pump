package ru.iopump.qa.component.groovy;

import com.google.common.base.Preconditions;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import lombok.NonNull;
import ru.iopump.qa.annotation.PumpApi;

@PumpApi
public final class GroovyUtil {
    public static final Pattern G_STRING = Pattern.compile("^(\"|\"\"\")(.*)(\"|\"\"\")$", Pattern.MULTILINE);
    public static final Pattern STRING = Pattern.compile("^('|''')(.*)('|''')$", Pattern.MULTILINE);

    private GroovyUtil() {
        throw new AssertionError("utility class");
    }

    public static boolean isGString(@NonNull String candidate) {
        return G_STRING.matcher(candidate).matches();
    }

    public static boolean isString(@NonNull String candidate) {
        return STRING.matcher(candidate).matches();
    }

    public static String stringContent(@NonNull String string) {
        Preconditions.checkArgument(isString(string));
        final Matcher matcher = STRING.matcher(string);
        if (matcher.find()) {
            return matcher.group(1);
        }
        return string;
    }

    public static String gStringContent(@NonNull String string) {
        Preconditions.checkArgument(isGString(string));
        final Matcher matcher = G_STRING.matcher(string);
        if (matcher.find()) {
            return matcher.group(1);
        }
        return string;
    }

    public static String asGString(@NonNull String string) {
        String str = string;
        if (isGString(str)) {
            return str;
        }
        if (isString(str)) {
            str = stringContent(str);
        }

        return "\"" + str + "\"";
    }

    public static String asMultiLineGString(@NonNull String string) {
        String str = string;
        if (isGString(str)) {
            return str;
        }
        if (isString(str)) {
            str = stringContent(str);
        }

        return "\"\"\"" + str + "\"\"\"";
    }
}
