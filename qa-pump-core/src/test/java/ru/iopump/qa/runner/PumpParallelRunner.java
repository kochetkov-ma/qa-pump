package ru.iopump.qa.runner;

import io.cucumber.junit.CucumberOptions;
import io.cucumber.junit.PumpFeatureParallel;
import org.junit.runner.RunWith;

@RunWith(PumpFeatureParallel.class)
@CucumberOptions(
    monochrome = true,
    plugin = {"summary"},
    glue = {"ru.iopump.qa.glue"},
    features = "classpath:features/parallel"
)
public class PumpParallelRunner {
}
