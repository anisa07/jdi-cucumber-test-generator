package com.epam.test_generator.transformers.parsers;

public interface JqlParser {
    boolean queryBelongsToProject(String jql, String jiraKey);
}
