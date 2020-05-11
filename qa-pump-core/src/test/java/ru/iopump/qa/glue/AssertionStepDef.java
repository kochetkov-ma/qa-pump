package ru.iopump.qa.glue;

import io.cucumber.java.en.Given;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ru.iopump.qa.component.groovy.GroovyScript;

@Slf4j
@RequiredArgsConstructor
public class AssertionStepDef {

    @Given("^assert that (.*)$")
    public void contextLoad(String assertion) {
        GroovyScript.create().evaluate("assert " + assertion);
    }
}