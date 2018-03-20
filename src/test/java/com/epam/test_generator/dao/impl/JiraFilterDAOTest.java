package com.epam.test_generator.dao.impl;

import com.epam.test_generator.services.exceptions.JiraRuntimeException;
import net.rcarz.jiraclient.JiraClient;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import static org.junit.Assert.*;

public class JiraFilterDAOTest {
    @Mock
    JiraClient client;

    @InjectMocks
    JiraFilterDAO jiraFilterDAO;

    @Test
    public void getFilters_Success() throws Exception {
        fail("Not implemented");
    }

    @Test(expected = JiraRuntimeException.class)
    public void getFilters_RuntimeException() throws Exception {
        fail("Not implemented");
    }
}