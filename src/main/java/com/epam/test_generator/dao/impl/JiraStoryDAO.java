package com.epam.test_generator.dao.impl;

import com.epam.test_generator.entities.Suit;
import com.epam.test_generator.pojo.JiraStory;
import net.rcarz.jiraclient.*;
import org.springframework.stereotype.Component;

import java.lang.reflect.MalformedParametersException;

@Component
public class JiraStoryDAO {

    private final static String TYPE = "Story";
    private final static String uri = "https://jirapct.epam.com/jira";

    public JiraStory getStoryByJiraKey(String jiraKey, String jiraUserName, String jiraPassword) {
        BasicCredentials creds = new BasicCredentials(jiraUserName, jiraPassword);
        JiraClient client = new JiraClient(uri, creds);
        try {
            return new JiraStory(client.getIssue(jiraKey));
        } catch (JiraException e) {

            if (e.getCause() instanceof RestException) {
                RestException restException = (RestException) e.getCause();
                if (restException.getHttpStatusCode() == 404) return null;
            }
            throw new MalformedParametersException(e.getMessage());
        }
    }

    public void updateStoryByJiraKey(Suit suit, String jiraUserName, String jiraPassword) {
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

    public void createStory(Suit suit, String jiraUserName, String jiraPassword) {
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
