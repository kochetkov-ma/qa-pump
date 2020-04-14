package ru.iopump.qa.glue;

import io.cucumber.java.en.Given;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
public class ProcessorStepDef {

    @Given("^preprocessor list of string (.+?) object (.+?)$")
    public void preprocessor(List<? extends String> listOfString, Object object) {
        log.info("[STEP][preprocessor] Current thread={}", Thread.currentThread().toString());
        log.info("[STEP][preprocessor] listOfString={} {}", listOfString, listOfString.getClass());
        log.info("[STEP][preprocessor] object={} {}", object, object.getClass());
    }
}