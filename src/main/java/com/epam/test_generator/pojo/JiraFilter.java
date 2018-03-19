package com.epam.test_generator.pojo;

import net.rcarz.jiraclient.*;
import net.sf.json.JSONObject;

import java.util.Map;
import java.util.Objects;

public class JiraFilter extends Resource {
    private String self;
    private String id;
    private String name;
    private String owner;
    private String jql;

    public JiraFilter(RestClient restClient,JSONObject json) {
        super(restClient);
        final JSONObject jsonObject = Objects.requireNonNull(json);
        deserialize(jsonObject);
    }

    private void deserialize(JSONObject jsonObject) {
        final Map<?,?> map = jsonObject;

        self = Field.getString(map.get("self"));
        id = Field.getString(map.get("id"));
        name = Field.getString(map.get("name"));
        jql = Field.getString(map.get("jql"));
        owner = Field.getResource(User.class,map.get("owner"), restclient).getName();

    }

    @Override
    public String getSelf() {
        return self;
    }

    @Override
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
