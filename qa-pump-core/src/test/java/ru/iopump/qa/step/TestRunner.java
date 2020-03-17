package ru.iopump.qa.step;

import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;
import org.junit.runner.RunWith;

@RunWith(Cucumber.class)
@CucumberOptions(
    monochrome = true,
    plugin = {"pretty", "summary"},
    glue = "ru.iopump.qa.step",
    features = "classpath:features/"
)
public class TestRunner {
}
