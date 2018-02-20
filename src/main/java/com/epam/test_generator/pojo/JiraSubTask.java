package com.epam.test_generator.pojo;

import net.rcarz.jiraclient.Issue;

public class JiraSubTask {
    private String name;

    private String jiraKey;

    private String description;

    private String jiraProjectKey;

    private String jiraParentKey;

    public JiraSubTask(Issue issue) {
        name = issue.getSummary();
        description = issue.getDescription();
        jiraKey = issue.getKey();
        jiraProjectKey = issue.getProject().getKey();
        jiraParentKey = issue.getParent().getKey();

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getJiraKey() {
        return jiraKey;
    }

    public void setJiraKey(String jiraKey) {
        this.jiraKey = jiraKey;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getJiraParentKey() {
        return jiraParentKey;
    }

    public void setJiraParentKey(String jiraParentKey) {
        this.jiraParentKey = jiraParentKey;
    }

    public String getJiraProjectKey() {
        return jiraProjectKey;
    }

    public void setJiraProjectKey(String jiraProjectKey) {
        this.jiraProjectKey = jiraProjectKey;
    }
}
