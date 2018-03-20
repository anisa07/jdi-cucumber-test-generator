package com.epam.test_generator.dao.impl;

import com.epam.test_generator.pojo.JiraFilter;
import com.epam.test_generator.services.exceptions.JiraRuntimeException;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import net.rcarz.jiraclient.JiraClient;
import net.rcarz.jiraclient.RestClient;
import net.sf.json.JSON;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class JiraFilterDAO {

    @Autowired
    private JiraClient client;


    public List<JiraFilter> getFilters() {
        final RestClient restclient = client.getRestClient();
        try {
            final URI uri = restclient.buildURI("/rest/api/2/filter/favourite");
            final JSON response = restclient.get(uri);
            final JSONArray filters = JSONArray.fromObject(response);

            final List<JiraFilter> ret = new ArrayList<>();
            for (Object filter : filters) {
                final JSONObject jsonFilter = (JSONObject) filter;
                ret.add(new JiraFilter(restclient, jsonFilter));
            }
            return ret;
        } catch (Exception e) {
            throw new JiraRuntimeException(e.getMessage(), e);
        }
    }


}
