package com.epam.test_generator.pojo;

import java.util.Map;
import java.util.Objects;
import net.rcarz.jiraclient.Field;
import net.rcarz.jiraclient.RestClient;
import net.rcarz.jiraclient.User;
import net.sf.json.JSONObject;

public class JiraFilter {

    private static final String SELF = "self";
    private static final String FILTER_ID = "id";
    private static final String FILTER_NAME = "name";
    private static final String FILTER_JQL = "jql";
    private static final String OWNER = "owner";


    private String self;
    private String id;
    private String name;
    private String owner;
    private String jql;

    public JiraFilter(RestClient restClient, JSONObject json) {
        final JSONObject jsonObject = Objects.requireNonNull(json);
        deserialize(jsonObject, restClient);
    }

    private void deserialize(JSONObject jsonObject, RestClient restClient) {
        final Map<?, ?> map = jsonObject;

        self = Field.getString(map.get(SELF));
        id = Field.getString(map.get(FILTER_ID));
        name = Field.getString(map.get(FILTER_NAME));
        jql = Field.getString(map.get(FILTER_JQL));
        owner = Field.getResource(User.class, map.get(OWNER), restClient).getName();

    }

    public String getSelf() {
        return self;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getOwner() {
        return owner;
    }

    public String getJql() {
        return jql;
    }
}
