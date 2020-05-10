package ru.iopump.qa.glue;

import io.cucumber.java.en.Given;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ru.iopump.qa.component.vars.FeatureVars;
import ru.iopump.qa.component.vars.ScenarioVars;
import ru.iopump.qa.component.vars.SharedVars;

@Slf4j
@RequiredArgsConstructor
public class VarsStepDef {

    private final FeatureVars featureVars;
    private final ScenarioVars scenarioVars;
    private final SharedVars sharedVars;

    @Given("^for (scenario|feature|shared) variables errorIfNotExists set to (true|false)$")
    public void errorIfNotExists(VarType varType, boolean errorIfNotExists) {
        switch (varType) {
            case SHARED:
                sharedVars.setErrorIfNotExists(errorIfNotExists);
                break;
            case FEATURE:
                featureVars.setErrorIfNotExists(errorIfNotExists);
                break;
            case SCENARIO:
                scenarioVars.setErrorIfNotExists(errorIfNotExists);
                break;
            default:
                throw new UnsupportedOperationException("VarType " + varType);
        }
    }

    @Given("^define (scenario|feature|shared) variable (.+?) = (.+?)$")
    public void contextLoad(VarType varType, String name, Object object) {
        switch (varType) {
            case SHARED:
                sharedVars.put(name, object);
                break;
            case FEATURE:
                featureVars.put(name, object);
                break;
            case SCENARIO:
                scenarioVars.put(name, object);
                break;
            default:
                throw new UnsupportedOperationException("VarType " + varType);
        }
    }

    @Given("^evaluate and print (.+)$")
    public void contextLoad(Object argumentAfterTransforming) {
        log.info("\nRESULT: {}\n", argumentAfterTransforming);
    }

    public enum VarType {
        SCENARIO, FEATURE, SHARED
    }
}