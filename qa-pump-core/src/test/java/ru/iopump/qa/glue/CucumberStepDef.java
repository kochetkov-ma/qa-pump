package ru.iopump.qa.glue;

import io.cucumber.java.en.Given;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ru.iopump.qa.glue.type.StepEnum;
import ru.iopump.qa.user.component.TestBean;

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

    @Given("^pass step type (.+?) and more (.+?) then enum (.+?)$")
    public void arguments(StepEnum stepEnum1, StepEnum stepEnum2, StepEnum stepEnum) {
        log.info("[STEP] stepEnum1='{}'\nstepEnum2='{}'\nstepEnum='{}'", stepEnum1, stepEnum2, stepEnum);
    }
}