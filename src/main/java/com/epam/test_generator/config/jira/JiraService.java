package com.epam.test_generator.config.jira;

import com.atlassian.jira.rest.client.api.domain.Issue;
import com.atlassian.jira.rest.client.api.domain.Project;

import java.io.IOException;
import java.util.List;


public class JiraService {


    public JiraClient createJiraClient(String user, String password) {
        return new JiraClient(user, password);
    }

    public void getAllIssues(JiraClient jiraClient, String issueKey) {
        Issue issue = jiraClient.getIssue(issueKey);
        System.out.println(issue.getDescription());
    }

    public static void main(String[] args) throws IOException {
        JiraService jiraService = new JiraService();
        JiraClient client = jiraService.createJiraClient("NAME", "PASS");
        List<Project> allProjects = client.getAllProjects();
        String key = allProjects.get(31).getKey();
        System.out.println(key);
        // client.createIssue(key,12L,"kontantin");


    }
}
