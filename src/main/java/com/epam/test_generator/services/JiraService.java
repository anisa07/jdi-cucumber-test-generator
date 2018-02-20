package com.epam.test_generator.services;

import com.epam.test_generator.dao.impl.JiraStoryDAO;
import com.epam.test_generator.dao.impl.JiraSubStroryDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class JiraService {
    @Autowired
    private SuitService suitService;

    @Autowired
    private CaseService caseService;

    @Autowired
    private JiraStoryDAO jiraStoryDAO;

    @Autowired
    private JiraSubStroryDAO jiraSubStroryDAO;

    public void syncToJira(String userName, String password) {
        suitService.getSuits().forEach(s -> {
            if (jiraStoryDAO.getStoryByJiraKey(s.getJiraKey(), userName, password) == null) {
                jiraStoryDAO.createStory(s, userName, password);
            } else jiraStoryDAO.updateStoryByJiraKey(s, userName, password);
        });


        caseService.getCases().forEach(s -> {
            if (jiraSubStroryDAO.getSubStoryByJiraKey(s.getJiraKey(), userName, password) == null) {
                jiraSubStroryDAO.createSubStory(s, userName, password);
            } else jiraSubStroryDAO.updateSubStoryByJiraKey(s, userName, password);

        });


    }
}
