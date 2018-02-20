package com.epam.test_generator.dao.impl;

import com.epam.test_generator.pojo.JiraProject;
import net.rcarz.jiraclient.BasicCredentials;
import net.rcarz.jiraclient.JiraClient;
import net.rcarz.jiraclient.JiraException;
import net.rcarz.jiraclient.RestException;
import org.springframework.stereotype.Component;

import java.lang.reflect.MalformedParametersException;

@Component
public class JiraProjectDAO {

    private final static String uri = "https://jirapct.epam.com/jira";

    public JiraProject getProjectByJiraKey(String jiraKey, String jiraUserName, String jiraPassword) throws JiraException {
        BasicCredentials creds = new BasicCredentials(jiraUserName, jiraPassword);

        JiraClient client = new JiraClient(uri, creds);
        try {
            return new JiraProject(client.getProject(jiraKey));
        } catch (JiraException e) {


            if (e.getCause() instanceof RestException) {
                RestException restException = (RestException) e.getCause();
                if (restException.getHttpStatusCode() == 404) return null;
            }
            throw new MalformedParametersException(e.getMessage());
        }
    }
}
