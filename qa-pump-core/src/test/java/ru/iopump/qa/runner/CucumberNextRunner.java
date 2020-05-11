package ru.iopump.qa.runner;

import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;
import org.junit.runner.RunWith;

@RunWith(Cucumber.class)
@CucumberOptions(
    monochrome = true,
    plugin = {"summary"},
    glue = {"ru.iopump.qa.glue"},
    features = "classpath:features/parallel/feature-parallel-1.feature"
)
public class CucumberNextRunner {
}
