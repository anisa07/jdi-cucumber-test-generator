package com.epam.test_generator.dao.impl;

import com.epam.test_generator.services.exceptions.JiraRuntimeException;
import net.rcarz.jiraclient.JiraClient;
import net.rcarz.jiraclient.RestClient;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class JiraFilterDAOTest {

    @Mock
    private RestClient restClient;

    @Mock
    private JiraClient client;

    @InjectMocks
    private JiraFilterDAO jiraFilterDAO;



    @Test
    public void getFilters_Success() {
    }

    @Test(expected = JiraRuntimeException.class)
    public void getFilters_RuntimeException_unableToConnect() {
    }
}