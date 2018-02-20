package com.epam.test_generator.dao.impl;

import com.epam.test_generator.entities.Case;
import com.epam.test_generator.pojo.JiraSubTask;
import net.rcarz.jiraclient.*;
import org.springframework.stereotype.Component;

import java.lang.reflect.MalformedParametersException;

@Component
public class JiraSubStroryDAO {
    private final static String TYPE = "Sub-story";

    private final static String uri = "https://jirapct.epam.com/jira";

    public JiraSubTask getSubStoryByJiraKey(String jiraKey, String jiraUserName, String jiraPassword)   {
        BasicCredentials creds = new BasicCredentials(jiraUserName, jiraPassword);
        JiraClient client = new JiraClient(uri, creds);
        try {
            return new JiraSubTask(client.getIssue(jiraKey));
        } catch (JiraException e) {

            if (e.getCause() instanceof RestException) {
                RestException restException = (RestException) e.getCause();
                if (restException.getHttpStatusCode() == 404) return null;
            }
            throw new MalformedParametersException(e.getMessage());
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
