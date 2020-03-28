package ru.iopump.qa.step;

import io.cucumber.java.en.Given;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
public class CucumberStepDef {

    private final TestBean testBean;

    @Given("spring context is loaded")
    public void contextLoad() {
        log.info("[STEP] spring context is loaded - Started");
    }

    @Given("pump configuration is printed")
    public void configurationPrint() {
        log.info("[STEP] pump configuration is printed - Started");
    }
}