package com.epam.test_generator.dao.impl;

import com.epam.test_generator.pojo.JiraFilter;
import com.epam.test_generator.pojo.JiraProject;
import net.rcarz.jiraclient.JiraClient;
import net.rcarz.jiraclient.JiraException;
import net.rcarz.jiraclient.RestClient;
import net.sf.json.JSON;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.net.URI;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class JiraProjectDAO {

    @Autowired
    private JiraClient client;

    public JiraProject getProjectByJiraKey(String jiraKey) throws JiraException {

        return new JiraProject(client.getProject(jiraKey));
    }

    public List<JiraProject> getAllProjects() throws JiraException {

        return client.getProjects().stream().map(JiraProject::new).collect(Collectors.toList());
    }

    public List<JiraFilter> getFilters() throws JiraException {
        final RestClient restclient = client.getRestClient();
        try {
            final URI uri = restclient.buildURI("/rest/api/2/filter/favourite");
            final JSON response = restclient.get(uri);
            final JSONArray filters = JSONArray.fromObject(response);

            final List<JiraFilter> ret = new ArrayList<>();
            for(Object filter : filters) {
                final JSONObject jsonFilter = (JSONObject) filter;
                ret.add(new JiraFilter(restclient, jsonFilter));
            }
            return ret;
        } catch (Exception e) {
            throw new JiraException(e.getMessage(), e);
        }
    }
}
