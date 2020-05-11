package ru.iopump.qa.spring.scope;

import javax.annotation.Nonnull;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.beans.factory.config.Scope;

public final class FeatureScope implements Scope {
    public final static String SCOPE_PUMP_FEATURE = "cucumber-feature";

    @Override
    @Nonnull
    public Object get(@Nonnull String name, @Nonnull ObjectFactory<?> objectFactory) {
        Execution.checkRunner();

        final FeatureCodeScope context = FeatureCodeScope.getInstance();
        Object obj = context.get(name);
        if (obj == null) {
            obj = objectFactory.getObject();
            context.put(name, obj);
        }
        return obj;
    }

    @Override
    public Object remove(@Nonnull String name) {
        final FeatureCodeScope context = FeatureCodeScope.getInstance();
        return context.remove(name);
    }

    @Override
    public void registerDestructionCallback(@Nonnull String name, @Nonnull Runnable callback) {
        final FeatureCodeScope context = FeatureCodeScope.getInstance();
        context.registerDestructionCallback(name, callback);
    }

    @Override
    public Object resolveContextualObject(@Nonnull String key) {
        return null;
    }

    @Override
    public String getConversationId() {
        final FeatureCodeScope context = FeatureCodeScope.getInstance();
        return context.getId();
    }
}
