package com.epam.test_generator.config.jira;


import com.atlassian.jira.rest.client.api.IssueRestClient;
import com.atlassian.jira.rest.client.api.JiraRestClient;
import com.atlassian.jira.rest.client.api.domain.*;
import com.atlassian.jira.rest.client.api.domain.input.IssueInput;
import com.atlassian.jira.rest.client.api.domain.input.IssueInputBuilder;
import com.atlassian.jira.rest.client.internal.async.AsynchronousJiraRestClientFactory;
import com.atlassian.util.concurrent.Promise;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class JiraClient {

    private static final String URL = "https://jirapct.epam.com/jira";

    private String username;
    private String password;
    private String jiraUrl;
    private JiraRestClient restClient;

    public JiraClient(String username, String password) {
        this.username = username;
        this.password = password;
        this.jiraUrl = URL;
        this.restClient = getJiraRestClient();
    }

    public List<Project> getAllProjects() {

        List<Promise<Project>> list = new ArrayList<>();
        Promise<Iterable<BasicProject>> allProjects = restClient.getProjectClient().getAllProjects();
        allProjects.claim().forEach((BasicProject s) -> list.add(restClient.getProjectClient().getProject(s.getKey())));
        return list.stream().map(Promise::claim).collect(Collectors.toList());

    }


    public String createIssue(String projectKey, Long issueType, String issueSummary) {
        IssueRestClient issueClient = restClient.getIssueClient();
        IssueInput newIssue = new IssueInputBuilder(projectKey, issueType, issueSummary).build();
        return issueClient.createIssue(newIssue).claim().getKey();
    }

    public Issue getIssue(String issueKey) {
        return restClient.getIssueClient().getIssue(issueKey).claim();
    }

    public void voteForAnIssue(Issue issue) {
        restClient.getIssueClient().vote(issue.getVotesUri()).claim();
    }

    public int getTotalVotesCount(String issueKey) {
        BasicVotes votes = getIssue(issueKey).getVotes();
        return votes == null ? 0 : votes.getVotes();
    }

    public void addComment(Issue issue, String commentBody) {
        restClient.getIssueClient().addComment(issue.getCommentsUri(), Comment.valueOf(commentBody));
    }

    public List<Comment> getAllComments(String issueKey) {
        return StreamSupport.stream(getIssue(issueKey).getComments().spliterator(), false)
                .collect(Collectors.toList());
    }

    public void updateIssueDescription(String issueKey, String newDescription) {
        IssueInput input = new IssueInputBuilder().setDescription(newDescription).build();
        restClient.getIssueClient().updateIssue(issueKey, input).claim();
    }

    public void deleteIssue(String issueKey, boolean deleteSubtasks) {
        restClient.getIssueClient().deleteIssue(issueKey, deleteSubtasks).claim();
    }

    public JiraRestClient getJiraRestClient() {
        return new AsynchronousJiraRestClientFactory()
                .createWithBasicHttpAuthentication(getJiraUri(), this.username, this.password);
    }

    private URI getJiraUri() {
        return URI.create(this.jiraUrl);
    }
}