package ru.iopump.qa.spring;

import static ru.iopump.qa.spring.PumpConfigUtils.loadConfigFromReader;
import static ru.iopump.qa.spring.PumpConfigUtils.prettyPrint;

import com.typesafe.config.Config;
import java.io.IOException;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.core.env.PropertySource;
import org.springframework.core.io.support.EncodedResource;
import org.springframework.core.io.support.PropertySourceFactory;

@Slf4j
final class TypesafePropertySourceFactory implements PropertySourceFactory {

    @Override
    public PropertySource<Config> createPropertySource(String name, EncodedResource resource) throws IOException {
        if (StringUtils.isBlank(name)) {
            name = "typesafe";
        }
        final Config config = loadTypeSafe(resource);
        log.info("[CONFIGURATION] Loaded from {}:\n{}", resource, prettyPrint(config));

        return new TypesafePropertySource(name, config);
    }

    @NonNull
    protected Config loadTypeSafe(@NonNull EncodedResource resource) throws IOException {
        return loadConfigFromReader(resource.getReader());
    }
}