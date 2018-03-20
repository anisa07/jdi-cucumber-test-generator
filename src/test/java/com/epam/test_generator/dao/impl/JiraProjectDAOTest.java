package com.epam.test_generator.dao.impl;

import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.when;

import com.epam.test_generator.pojo.JiraProject;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import net.rcarz.jiraclient.JiraClient;
import net.rcarz.jiraclient.JiraException;
import net.rcarz.jiraclient.Project;
import net.rcarz.jiraclient.RestException;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class JiraProjectDAOTest {

    @Mock
    private JiraClient client;

    @Mock
    private JiraFilterDAO jiraFilterDAO;

    @Mock
    private Project project;

    @InjectMocks
    private JiraProjectDAO jiraProjectDAO;

    private static final String NAME = "name";
    private static final String PASSWORD = "pass";
    private static final String JIRA_KEY = "key";

    @Before
    public void setUp() throws Exception {

    }

    @Test
    public void getProjectByJiraKey_JiraProject_Success() throws Exception {
        when(client.getProject(anyString())).thenReturn(project);
        when(jiraFilterDAO.getFilters()).thenReturn(Collections.emptyList());

        JiraProject expectedProject = new JiraProject(project, Collections.emptyList());
        JiraProject resultProject = jiraProjectDAO.getProjectByJiraKey(JIRA_KEY);
        Assert.assertEquals(expectedProject, resultProject);
    }

    @Test(expected = JiraException.class)
    public void getProjectByUnvalidJiraKey_JiraProject_MalformedParametersException() throws Exception {
      when(client.getProject(anyString())).thenThrow(new JiraException("a"));
      jiraProjectDAO.getProjectByJiraKey(JIRA_KEY);
    }

    @Test(expected = JiraException.class)
    public void getNonexistentProjectByJadaKey_JadaProject_Success() throws JiraException {
      when(client.getProject(anyString())).thenThrow(new JiraException("a",new RestException("a",404,"bad")));
      JiraProject key = jiraProjectDAO.getProjectByJiraKey(JIRA_KEY);
      Assert.assertNull(key);
    }

    @Test
    public void getAllProjects_JiraProjects_Success() throws Exception {
        when(client.getProjects()).thenReturn(Arrays.asList(project));
        when(jiraFilterDAO.getFilters()).thenReturn(Collections.emptyList());


        List<JiraProject> resultProjects = jiraProjectDAO.getAllProjects();
        List<JiraProject> expectedProjects = Arrays
            .asList(new JiraProject(project, Collections.emptyList()));
        Assert.assertEquals(expectedProjects, resultProjects);
    }

    @Test
    public void getEmptyListOfProjects_JiraProjects_Success() throws Exception {
        when(client.getProject(anyString())).thenThrow(new JiraException("a",new RestException("a",404,"bad")));
        List<JiraProject> resultProjects = jiraProjectDAO.getAllProjects();
        Assert.assertTrue(resultProjects.isEmpty());
    }


}