package ru.iopump.qa.spring;

import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import org.apache.commons.lang3.StringUtils;
import org.springframework.core.env.PropertySource;
import org.springframework.core.io.support.EncodedResource;
import org.springframework.core.io.support.PropertySourceFactory;

public class TypesafePropertySourceFactory implements PropertySourceFactory {

    @Override
    public PropertySource<?> createPropertySource(String name, EncodedResource resource) {
        if (StringUtils.isBlank(name)) name = "typesafe";
        Config config = loadTypeSafe(resource);
        return new TypesafeConfigPropertySource(name, config);
    }

    protected Config loadTypeSafe(EncodedResource resource) {
        return ConfigFactory.load(resource.getResource().getFilename()).resolve();
    }
}