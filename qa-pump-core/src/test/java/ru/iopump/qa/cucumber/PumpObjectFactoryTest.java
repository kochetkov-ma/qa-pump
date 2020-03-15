package ru.iopump.qa.cucumber;

import org.joor.Reflect;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import ru.iopump.qa.exception.PumpException;

public class PumpObjectFactoryTest {

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

    @Test
    public void start() {
    }

    @Test
    public void stop() {
    }

    @Test
    public void addClass() {
    }

    @Test
    public void getInstance() {
    }

    @Test
    public void findImplementations() {
    }

    @Test(expected = PumpException.class)
    public void checkObjectFactoryLoaded() {
        try {
            Reflect.onClass(PumpObjectFactory.class).set("loaded", false);
            PumpObjectFactory.checkObjectFactoryLoaded();
        } catch (PumpException e) {
            System.err.println(e.getLocalizedMessage());
            throw e;
        }
    }
}