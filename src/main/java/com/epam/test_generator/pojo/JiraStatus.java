package com.epam.test_generator.pojo;

public enum JiraStatus {
    CLOSED(31),
    RESOLVED(21),
    REOPENED(71);

    private Integer actionId;

    JiraStatus(Integer actionId) {
        this.actionId = actionId;
    }

    public Integer getActionId() {
        return actionId;
    }
}
