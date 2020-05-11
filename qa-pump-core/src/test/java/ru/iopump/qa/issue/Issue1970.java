package ru.iopump.qa.issue;

import io.cucumber.java.en.Given;
import io.cucumber.spring.CucumberContextConfiguration;
import io.cucumber.spring.SpringFactory;
import org.junit.Ignore;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Scope;
import org.springframework.test.context.ContextConfiguration;
@Ignore
public class Issue1970 {

    @Test
    public void issue1970() {
        var factory = new SpringFactory();
        factory.addClass(GlueClass.class); // Add glue with Spring configuration
        factory.start();
    }

    @CucumberContextConfiguration
    @ContextConfiguration(classes = MySpringConfiguration.class)
    public static class GlueClass {

        @Given("step")
        public void step() {
            /* do nothing */
        }
    }

    public static class MySpringConfiguration {

        /**
         * It need {@link #simpleGlueScopeBean}.
         * By default Spring try to create singleton bean in an eager manner on start up.
         * There are no "cucumber-glue" scope at this moment.
         */
        @Bean
        // @Lazy // Lazy is workaround. But every bean in init chain must be lazy.
        public String singletonBean(@Autowired String simpleGlueScopeBean) {
            return simpleGlueScopeBean + " singleton";
        }

        /**
         * Bean with 'cucumber-glue'. Not glue.
         * I want use it in multi-thread scenario execution and Spring will manage it via scope.
         */
        @Bean
        @Scope("cucumber-glue") // This is Cucumber Glue scope
        public String simpleGlueScopeBean() {
            return "cucumber-glue";
        }
    }
}
