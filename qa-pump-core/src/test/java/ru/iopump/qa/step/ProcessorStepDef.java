package ru.iopump.qa.step;

import io.cucumber.java.en.Given;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
public class ProcessorStepDef {

    @Given("^preprocessor string (.+?) object (.+?)$")
    public void contextLoad(List<? extends String> listOfString, Object object) {
        log.info("[STEP] listOfString={} {}", listOfString, listOfString.getClass());
        log.info("[STEP] object={} {}", object, object.getClass());
    }
}