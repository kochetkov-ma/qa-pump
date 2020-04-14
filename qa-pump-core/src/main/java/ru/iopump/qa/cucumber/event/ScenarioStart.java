package ru.iopump.qa.cucumber.event;


import io.cucumber.java.Scenario;
import lombok.EqualsAndHashCode;
import lombok.Value;
import org.springframework.context.ApplicationEvent;

@EqualsAndHashCode(callSuper = true)
@Value
public class ScenarioStart extends ApplicationEvent {

    private static final long serialVersionUID = -7112969889804111905L;
    Scenario scenario;

    public ScenarioStart(Scenario scenario) {
        super(scenario);
        this.scenario = scenario;
    }
}