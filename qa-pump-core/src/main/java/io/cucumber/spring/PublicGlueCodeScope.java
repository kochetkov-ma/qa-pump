package io.cucumber.spring;

/**
 * Workaround.
 * {@link GlueCodeScope} has package-default visibility.
 * In my Spring Configuration I use Scope 'cucumber-glue'
 * before its registration by {@link TestContextAdaptor#registerGlueCodeScope(org.springframework.context.ConfigurableApplicationContext)}.
 * This scope registers after context loading on glue loading stage.
 * But I want use this Scope for my Spring-beans because this scope is allowed for me in {@link CucumberTestContext}.
 * https://github.com/cucumber/cucumber-jvm/issues/1970
 */
public class PublicGlueCodeScope extends GlueCodeScope {

}
