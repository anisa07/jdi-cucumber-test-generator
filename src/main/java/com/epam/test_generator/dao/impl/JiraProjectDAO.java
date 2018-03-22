package com.epam.test_generator.dao.impl;

import com.epam.test_generator.entities.factory.JiraClientFactory;
import com.epam.test_generator.pojo.JiraFilter;
import com.epam.test_generator.pojo.JiraProject;
import com.epam.test_generator.services.exceptions.JiraRuntimeException;
import com.epam.test_generator.transformers.parsers.JqlParser;
import net.rcarz.jiraclient.JiraException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class JiraProjectDAO {

    @Autowired
    private JiraClientFactory jiraClientFactory;

    @Autowired
    private JiraFilterDAO jiraFilterDAO;

    @Autowired
    private JqlParser jqlParser;

    public JiraProject getProjectByJiraKey(Long clientId, String jiraKey) {
        final List<JiraFilter> filters = jiraFilterDAO.getFilters(clientId);

        try {
            return new JiraProject(jiraClientFactory.getJiraClient(clientId).getProject(jiraKey),
                    getProjectFilters(filters, jiraKey));
        } catch (JiraException e) {
            throw new JiraRuntimeException(e.getMessage(), e);
        }
    }

    public List<JiraProject> getAllProjects(Long clientId) {
        final List<JiraFilter> allFilters = jiraFilterDAO.getFilters(clientId);
        try {
            return jiraClientFactory.getJiraClient(clientId).getProjects()
                    .stream()
                    .map(p -> new JiraProject(p, getProjectFilters(allFilters, p.getKey())))
                    .collect(Collectors.toList());
        } catch (JiraException e) {
            throw new JiraRuntimeException(e.getMessage(), e);
        }
    }

    private boolean filterIsAssignedToProject(JiraFilter filter, String jiraKey) {
        return jqlParser.queryBelongsToProject(filter.getJql(), jiraKey);
    }

    private List<JiraFilter> getProjectFilters(List<JiraFilter> filters, String jiraKey) {
        return filters.stream()
                .filter(f -> filterIsAssignedToProject(f, jiraKey)).collect(Collectors.toList());
    }
}

