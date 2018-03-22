package com.epam.test_generator.dao.impl;

import com.epam.test_generator.pojo.JiraFilter;
import com.epam.test_generator.pojo.JiraProject;
import com.epam.test_generator.services.exceptions.JiraRuntimeException;
import java.util.List;
import java.util.stream.Collectors;
import net.rcarz.jiraclient.JiraClient;
import net.rcarz.jiraclient.JiraException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class JiraProjectDAO {

    @Autowired
    private JiraClient client;

    @Autowired
    private JiraFilterDAO jiraFilterDAO;

    public JiraProject getProjectByJiraKey(String jiraKey) {
        final List<JiraFilter> filters = jiraFilterDAO.getFilters();

        try {
            return new JiraProject(client.getProject(jiraKey), getProjectFilters(filters, jiraKey));
        } catch (JiraException e) {
            throw new JiraRuntimeException(e.getMessage(), e);
        }
    }

    public List<JiraProject> getAllProjects() throws JiraException {
        final List<JiraFilter> allFilters = jiraFilterDAO.getFilters();
        return client.getProjects().stream()
            .map(p -> new JiraProject(p, getProjectFilters(allFilters, p.getKey())))
            .collect(Collectors.toList());
    }

    private List<JiraFilter> getProjectFilters(List<JiraFilter> filters, String jiraKey) {
        return filters.stream()
            .filter(f -> f.getJql().contains(jiraKey))
            .collect(Collectors.toList());
    }

}
