package com.epam.test_generator.dao.impl;

import com.epam.test_generator.pojo.JiraProject;
import java.util.List;
import java.util.stream.Collectors;
import net.rcarz.jiraclient.JiraClient;
import net.rcarz.jiraclient.JiraException;
import org.springframework.stereotype.Component;

@Component
public class JiraProjectDAO {

    public JiraProject getProjectByJiraKey(JiraClient client, String jiraKey) throws JiraException {

        return new JiraProject(client.getProject(jiraKey));
    }

    public List<JiraProject> getAllProjects(JiraClient client) throws JiraException {
        return client.getProjects().stream().map(JiraProject::new).collect(Collectors.toList());
    }
}
