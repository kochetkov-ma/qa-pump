package ru.iopump.qa.runner.vars;

import io.cucumber.junit.CucumberOptions;
import io.cucumber.junit.Pump;
import org.junit.runner.RunWith;

@RunWith(Pump.class)
@CucumberOptions(
    monochrome = true,
    plugin = {"summary"},
    glue = {"ru.iopump.qa.glue"},
    features = "classpath:features/vars"
)
public class VarsRunner {
}
