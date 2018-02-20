package com.epam.test_generator.dao.impl;

import com.epam.test_generator.entities.Suit;
import net.rcarz.jiraclient.*;

import java.lang.reflect.MalformedParametersException;

public class JiraStoryDAO {

    private final static String TYPE = "Story";
    private final static String uri = "https://jirapct.epam.com/jira";

    public Issue getStoryByJiraKey(String jiraKey, String jiraUserName, String jiraPassword)   {
        BasicCredentials creds = new BasicCredentials(jiraUserName, jiraPassword);
        JiraClient client = new JiraClient(uri, creds);
        try {
            return client.getIssue(jiraKey);
        } catch (JiraException e) {
            return null;
        }
    }

    public void updateStoryByJiraKey(Suit suit, String jiraUserName, String jiraPassword)  {
        BasicCredentials creds = new BasicCredentials(jiraUserName, jiraPassword);
        JiraClient client = new JiraClient(uri, creds);
        try {
            client
                    .getIssue(suit.getJiraKey())
                    .update()
                    .field(Field.SUMMARY, suit.getName())
                    .field(Field.DESCRIPTION, suit.getDescription())
                    .execute();
        } catch (JiraException e) {
            throw new MalformedParametersException(e.getMessage());
        }

    }

    public void createStory(Suit suit, String jiraUserName, String jiraPassword)
    {
        BasicCredentials creds = new BasicCredentials(jiraUserName, jiraPassword);
        JiraClient client = new JiraClient(uri, creds);
        try {
            client
                    .createIssue(suit.getJiraProjectKey(), TYPE)
                    .field(Field.SUMMARY, suit.getName())
                    .field(Field.DESCRIPTION, suit.getDescription())
                    .execute();
        } catch (JiraException e) {
            throw new MalformedParametersException(e.getMessage());
                    }

    }


}
