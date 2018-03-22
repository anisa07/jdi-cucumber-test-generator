package com.epam.test_generator.transformers.parsers;

import org.springframework.stereotype.Service;

@Service
public class DefaultJqlParser implements JqlParser {
    @Override
    public boolean queryBelongsToProject(String jql, String jiraKey) {
        return jql.matches("^project\\s*=\\s*" + jiraKey + " .*") ||
                jql.matches("^project\\s+in\\s*\\(\\s*" + jiraKey + "\\s*\\).*");
    }
}
