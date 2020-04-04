package ru.iopump.qa.step;

import io.cucumber.java.en.Given;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ru.iopump.qa.step.type.StepEnum;
import ru.iopump.qa.user.component.TestBean;

@Slf4j
@RequiredArgsConstructor
public class PreprocessorStepDef {

    @Given("^preprocessor string (.+) object (.+)$")
    public void contextLoad(String string, Object object) {
        log.info("[STEP] string={}", string);
        log.info("[STEP] object={}", object);
    }
}