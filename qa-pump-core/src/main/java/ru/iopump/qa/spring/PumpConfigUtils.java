package ru.iopump.qa.spring;

import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import com.typesafe.config.ConfigRenderOptions;
import com.typesafe.config.ConfigResolveOptions;
import java.io.IOException;
import java.io.Reader;
import javax.annotation.Nullable;
import lombok.NonNull;
import ru.iopump.qa.util.Str;


final class PumpConfigUtils {
    public PumpConfigUtils() {
        throw new AssertionError("utility class");
    }

    private final static TypesafePropertySourceFactory DEFAULT_CONFIG_FACTORY = new TypesafePropertySourceFactory();
    private final static TypesafePropertySourceFactory TYPESAFE_CONFIG_FACTORY = DEFAULT_CONFIG_FACTORY;

    public static TypesafePropertySourceFactory getConfigFactory() {
        return TYPESAFE_CONFIG_FACTORY;
    }

    public static Config loadConfigFromReader(@NonNull Reader reader) throws IOException {
        final Config result;
        try (reader) {
            result = ConfigFactory.parseReader(reader).resolve();
        }
        return result;
    }

    public static Config loadSystemConfig() {
        return ConfigFactory.systemProperties()
            .withoutPath("awt")
            .withoutPath("jdk")
            .withoutPath("file")
            .withoutPath("line")
            .withoutPath("org")
            .withoutPath("path")
            .withoutPath("java")
            .withoutPath("sun")
            .withoutPath("swing");
    }

    public static Config loadEnvConfig() {
        return ConfigFactory.systemEnvironment();
    }

    public static Config resolveConfigWithSystem(@NonNull Config loadedConfig) {
        return loadSystemConfig().withFallback(loadedConfig).resolve(ConfigResolveOptions.defaults());
    }

    public static String prettyPrint(@Nullable Config config) {
        if (config == null) {
            return Str.nullStr();
        }
        return config.root().render(ConfigRenderOptions.concise().setFormatted(true));
    }
}
