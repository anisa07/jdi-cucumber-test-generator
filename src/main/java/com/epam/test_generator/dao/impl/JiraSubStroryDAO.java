package com.epam.test_generator.dao.impl;

import com.epam.test_generator.dao.interfaces.CaseDAO;
import com.epam.test_generator.entities.Case;
import com.epam.test_generator.pojo.JiraSubTask;
import com.epam.test_generator.services.exceptions.JiraRuntimeException;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import net.rcarz.jiraclient.Field;
import net.rcarz.jiraclient.Issue;
import net.rcarz.jiraclient.JiraClient;
import net.rcarz.jiraclient.JiraException;
import net.rcarz.jiraclient.RestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

@Component
public class JiraSubStroryDAO {

    @Autowired
    private CaseDAO caseDAO;

    @Autowired
    private JiraClient client;

    private final static String TYPE = "Sub-story";
    private final static Integer MAX_NUMBER_OF_ISSUES = 10000;

    public JiraSubTask getSubStoryByJiraKey(String jiraKey) throws JiraException {

        return new JiraSubTask(client.getIssue(jiraKey));
    }

    public List<JiraSubTask> getJiraSubtoriesByFilter(String search) {
        try {
            List<Issue> issues = client.searchIssues(search, MAX_NUMBER_OF_ISSUES).issues;
            return issues.stream().map(JiraSubTask::new).collect(Collectors.toList());

        } catch (JiraException e) {
            if (e.getCause() instanceof RestException) {
                RestException restException = (RestException) e.getCause();
                if (restException.getHttpStatusCode() == HttpStatus.NOT_FOUND.value()) {
                    return Collections.emptyList();
                }
            }
            throw new JiraRuntimeException(e.getMessage(), e);
        }
    }

    public void updateSubStoryByJiraKey(Case caze) throws JiraException {
        client
                .getIssue(caze.getJiraKey())
                .update()
                .field(Field.SUMMARY, caze.getName())
                .field(Field.DESCRIPTION, caze.getDescription())
                .execute();
        caze.setLastJiraSyncDate(LocalDateTime.now());
        caseDAO.save(caze);

    }


    public void createSubStory(Case caze) throws JiraException {

        Issue execute = client
                .createIssue(caze.getJiraProjectKey(), TYPE)
                .field(Field.SUMMARY, caze.getName())
                .field(Field.DESCRIPTION, caze.getDescription())
                .field(Field.PARENT, caze.getJiraParentKey())
                .execute();
        caze.setJiraKey(execute.getKey());
        caze.setJiraProjectKey(execute.getProject().getKey());
        caze.setJiraParentKey(execute.getParent().getKey());
        caze.setLastJiraSyncDate(LocalDateTime.now());
        caseDAO.save(caze);

    }


}
