package ru.iopump.qa.spring;

import com.typesafe.config.Config;
import com.typesafe.config.ConfigException;
import org.springframework.core.env.PropertySource;

final class TypesafePropertySource extends PropertySource<Config> {
    public TypesafePropertySource(String name, Config source) {
        super(name, source);
    }

    @Override
    public Object getProperty(String path) {
        boolean has;
        try {
            has = source.hasPath(path);
        } catch (ConfigException ignored) {
            has = false;
        }
        if (has) {
            return source.getAnyRef(path);
        }
        return null;
    }
}