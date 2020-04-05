package ru.iopump.qa.step;

import io.cucumber.java.en.Given;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ru.iopump.qa.step.type.StepEnum;
import ru.iopump.qa.user.component.TestBean;

@Slf4j
@RequiredArgsConstructor
public class ProcessorStepDef {

    @Given("^preprocessor string (.+?) object (.+?)$")
    public void contextLoad(List<? extends String> string, Object object) {
        log.info("[STEP] string={}", string);
        log.info("[STEP] object={} {}", object, object.getClass());
    }
}