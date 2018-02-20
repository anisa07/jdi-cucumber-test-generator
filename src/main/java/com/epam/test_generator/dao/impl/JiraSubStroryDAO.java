package com.epam.test_generator.dao.impl;

import com.epam.test_generator.entities.Case;
import net.rcarz.jiraclient.*;

import java.lang.reflect.MalformedParametersException;

public class JiraSubStroryDAO {
    private final static String TYPE = "Sub-story";

    private final static String uri = "https://jirapct.epam.com/jira";

    public Issue getSubStoryByJiraKey(String jiraKey, String jiraUserName, String jiraPassword)   {
        BasicCredentials creds = new BasicCredentials(jiraUserName, jiraPassword);
        JiraClient client = new JiraClient(uri, creds);
        try {
            return client.getIssue(jiraKey);
        } catch (JiraException e) {
            return null;
        }
    }

    public void updateSubStoryByJiraKey(Case caze, String jiraUserName, String jiraPassword)  {
        BasicCredentials creds = new BasicCredentials(jiraUserName, jiraPassword);
        JiraClient client = new JiraClient(uri, creds);
        try {
            client
                    .getIssue(caze.getJiraKey())
                    .update()
                    .field(Field.SUMMARY, caze.getName())
                    .field(Field.DESCRIPTION, caze.getDescription())
                    .execute();
        } catch (JiraException e) {
            throw new MalformedParametersException(e.getMessage());
        }

    }

    public void createSubStory(Case caze, String jiraUserName, String jiraPassword)
    {
        BasicCredentials creds = new BasicCredentials(jiraUserName, jiraPassword);
        JiraClient client = new JiraClient(uri, creds);
        try {
            client
                    .createIssue(caze.getJiraProjectKey(), TYPE)
                    .field(Field.SUMMARY, caze.getName())
                    .field(Field.DESCRIPTION, caze.getDescription())
                    .field(Field.PARENT, caze.getJiraParentKey())
                    .execute();
        } catch (JiraException e) {
            throw new MalformedParametersException(e.getMessage());
        }

    }


}
