package com.epam.test_generator.pojo;

import java.util.List;
import java.util.Objects;
import net.rcarz.jiraclient.Project;
import org.apache.commons.lang3.StringUtils;


/**
 * Represents project from Jira. It is used to map projects from Jira to BDD {@link Project}.
 */
public class JiraProject {

    private String name;

    private String jiraKey;

    private String description;

    private List<JiraFilter> jiraFilters;

    public JiraProject(Project project, List<JiraFilter> jiraFilters) {
        this.name = project.getName();
        this.jiraKey = project.getKey();
        this.jiraFilters = jiraFilters;
        this.description = StringUtils
            .substring(StringUtils.defaultIfEmpty(project.getDescription(),
                "No description"), 0, 250);
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

    public List<JiraFilter> getJiraFilters() {
        return jiraFilters;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final JiraProject that = (JiraProject) o;
        return Objects.equals(name, that.name) &&
                Objects.equals(jiraKey, that.jiraKey) &&
            Objects.equals(jiraFilters, that.jiraFilters) &&
                Objects.equals(description, that.description);
    }

    @Override
    public int hashCode() {

        return Objects.hash(name, jiraKey, description, jiraFilters);
    }
}
