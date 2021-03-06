package ru.iopump.qa.runner.vars;

import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;
import org.junit.runner.RunWith;

@RunWith(Cucumber.class)
@CucumberOptions(
    monochrome = true,
    plugin = {"summary"},
    glue = {"ru.iopump.qa.glue"},
    features = "classpath:features/vars"
)
public class VarsRunner {
}
