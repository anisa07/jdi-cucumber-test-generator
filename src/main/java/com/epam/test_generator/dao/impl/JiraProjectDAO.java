package com.epam.test_generator.dao.impl;

import net.rcarz.jiraclient.BasicCredentials;
import net.rcarz.jiraclient.JiraClient;
import net.rcarz.jiraclient.JiraException;
import net.rcarz.jiraclient.Project;

public class JiraProjectDAO {

    private final static String uri = "https://jirapct.epam.com/jira";

    public Project getProjectByJiraKey(String jiraKey, String jiraUserName,String jiraPassword) throws JiraException {
        BasicCredentials creds = new BasicCredentials(jiraUserName, jiraPassword);
        JiraClient client = new JiraClient(uri,creds);
        return client.getProject(jiraKey);
    }
}
