package ru.iopump.qa.cucumber.tag;

import ru.iopump.qa.annotation.PumpApi;

/**
 * Use this tags in scenarios.
 */
@PumpApi
public final class TagConstants {
    /**
     * Skip scenario or feature. Example: @SKIP
     */
    public final static String SKIP = "SKIP";
    /**
     * Complex tag.
     * Change environment. But spring active profile will be kept. Example: @ENV=docker
     */
    public final static String ENV = "ENV";

    /**
     * Complex tag.
     * Fail Scenario if active environment doesn't equals the value after '='. Example: @REQ-ENV=docker
     */
    public final static String REQ_ENV = "REQ_ENV";

    /**
     * Complex tag.
     * Skip Scenario if active environment doesn't equals the value after '='. Example: @RUN-ON-ENV=docker
     */
    public final static String RUN_ON_ENV = "RUN_ON_ENV";
}