package com.epam.test_generator.pojo;

import net.rcarz.jiraclient.Project;


public class JiraProject {

    private String name;

    private String jiraKey;

    private String description;

    public JiraProject(Project project) {
        name = project.getName();
        jiraKey = project.getKey();
        description = project.getDescription();
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

}
