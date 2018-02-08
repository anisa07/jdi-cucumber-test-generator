package com.epam.test_generator.entities;


/**
 * This enum represents status essence. Status in this case means result of verification (execution of test case's steps)
 */
public enum Status {

    NOT_DONE("Not done"),
    NOT_RUN("Not run"),
    PASSED("Passed"),
    FAILED("Failed"),
    SKIPPED("Skipped");

    private String statusName;

    Status(String statusName) {
        this.statusName = statusName;
    }
}