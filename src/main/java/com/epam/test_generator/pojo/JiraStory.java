package com.epam.test_generator.pojo;

import net.rcarz.jiraclient.Issue;

public class JiraStory {

    private String name;

    private String jiraKey;

    private String description;

    private String jiraProjectKey;

    public JiraStory(Issue issue) {
        name = issue.getSummary();
        jiraKey = issue.getKey();
        description = issue.getDescription();
        jiraProjectKey = issue.getProject().getKey();
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

    public String getJiraProjectKey() {
        return jiraProjectKey;
    }

    public void setJiraProjectKey(String jiraProjectKey) {
        this.jiraProjectKey = jiraProjectKey;
    }
}
