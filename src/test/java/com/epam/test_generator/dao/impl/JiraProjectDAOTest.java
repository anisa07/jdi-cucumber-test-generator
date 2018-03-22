package com.epam.test_generator.dao.impl;

import com.epam.test_generator.dao.interfaces.JiraSettingsDAO;
import com.epam.test_generator.entities.JiraSettings;
import com.epam.test_generator.entities.factory.JiraClientFactory;
import com.epam.test_generator.pojo.JiraProject;
import com.epam.test_generator.services.exceptions.JiraRuntimeException;
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

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.mockito.Matchers.anyLong;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class JiraProjectDAOTest {

    @Mock
    private JiraClient client;

    @Mock
    private JiraFilterDAO jiraFilterDAO;

    @Mock
    private Project project;

    @Mock
    private JiraClientFactory jiraClientFactory;

    @Mock
    private JiraSettingsDAO jiraSettingsDAO;

    private JiraSettings jiraSettings;

    @InjectMocks
    private JiraProjectDAO jiraProjectDAO;

    private static final String NAME = "name";
    private static final String PASSWORD = "pass";
    private static final String JIRA_KEY = "key";
    private static final Long JIRA_SETTINGS_ID = 1L;

    @Before
    public void setUp() throws Exception {
        when(jiraClientFactory.getJiraClient(anyLong())).thenReturn(client);
        jiraSettings = new JiraSettings();
        jiraSettings.setLogin("login");
        jiraSettings.setPassword("password");
        jiraSettings.setUri("jira_uri");
        when(jiraSettingsDAO.findById(anyLong())).thenReturn(jiraSettings);
    }

    @Test
    public void getProjectByJiraKey_JiraProject_Success() throws Exception {
        when(client.getProject(anyString())).thenReturn(project);
        when(jiraFilterDAO.getFilters()).thenReturn(Collections.emptyList());

        JiraProject expectedProject = new JiraProject(project, Collections.emptyList());
        JiraProject resultProject = jiraProjectDAO.getProjectByJiraKey(JIRA_SETTINGS_ID, JIRA_KEY);
        Assert.assertEquals(expectedProject, resultProject);
    }

    @Test(expected = JiraRuntimeException.class)
    public void getProjectByUnvalidJiraKey_JiraProject_MalformedParametersException() throws Exception {
        when(client.getProject(anyString())).thenThrow(new JiraRuntimeException("a"));
      jiraProjectDAO.getProjectByJiraKey(JIRA_SETTINGS_ID, JIRA_KEY);
    }

    @Test(expected = JiraRuntimeException.class)
    public void getNonexistentProjectByJadaKey_JadaProject_Success() throws Exception {
      when(client.getProject(anyString())).thenThrow(new JiraException("a",new RestException("a",404,"bad")));
      JiraProject key = jiraProjectDAO.getProjectByJiraKey(JIRA_SETTINGS_ID, JIRA_KEY);
      Assert.assertNull(key);
    }

    @Test
    public void getAllProjects_JiraProjects_Success() throws Exception {
        when(client.getProjects()).thenReturn(Arrays.asList(project));
        when(jiraFilterDAO.getFilters()).thenReturn(Collections.emptyList());


        List<JiraProject> resultProjects = jiraProjectDAO.getAllProjects(JIRA_SETTINGS_ID);
        List<JiraProject> expectedProjects = Arrays
                .asList(new JiraProject(project, Collections.emptyList()));
        Assert.assertEquals(expectedProjects, resultProjects);
    }

    @Test
    public void getEmptyListOfProjects_JiraProjects_Success() throws Exception {
        when(client.getProject(anyString())).thenThrow(new JiraException("a",new RestException("a",404,"bad")));
        List<JiraProject> resultProjects = jiraProjectDAO.getAllProjects(JIRA_SETTINGS_ID);
        Assert.assertTrue(resultProjects.isEmpty());
    }


}