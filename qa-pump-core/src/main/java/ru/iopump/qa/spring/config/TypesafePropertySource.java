package ru.iopump.qa.spring.config;

import com.typesafe.config.Config;
import org.springframework.core.env.PropertySource;

public class TypesafePropertySource extends PropertySource<Config> {
    public TypesafePropertySource(String name, Config source) {
        super(name, source);
    }

    @Override
    public Object getProperty(String path) {
        if (source.hasPath(path)) {
            return source.getAnyRef(path);
        }
        return null;
    }
}