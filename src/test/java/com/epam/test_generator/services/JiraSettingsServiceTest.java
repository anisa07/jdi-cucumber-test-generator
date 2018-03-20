package com.epam.test_generator.services;

import com.epam.test_generator.dao.interfaces.JiraSettingsDAO;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class JiraSettingsServiceTest {
    @Mock
    private JiraSettingsDAO jiraSettingsDAO;

    @InjectMocks
    private JiraSettingsService jiraSettingsService;

    @Before
    public void setUp() throws Exception {
    }

    @Test
    public void createJiraSettings_CorrectData_Success() {
    }

    @Test
    public void updateJiraSettings() {
    }

    @Test
    public void getJiraSettings() {
    }
}