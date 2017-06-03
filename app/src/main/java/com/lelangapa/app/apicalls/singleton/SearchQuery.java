package com.lelangapa.app.apicalls.singleton;

import java.util.HashMap;

/**
 * Created by andre on 01/05/17.
 */

public class SearchQuery {
    private static SearchQuery searchQuery;
    public HashMap<String, String> query;
    private SearchQuery() {
        query = new HashMap<>();
    }
    public static synchronized SearchQuery getInstance() {
        if (searchQuery == null) searchQuery = new SearchQuery();
        return searchQuery;
    }
    public SearchQuery insertQuery(String q) {
        searchQuery.query.put("query", q);
        return searchQuery;
    }
    public SearchQuery insertUserID(String q) {
        searchQuery.query.put("user", q);
        return searchQuery;
    }
    public SearchQuery insertFromAndSize(int from, int size) {
        searchQuery.query.put("from", Integer.toString(from));
        searchQuery.query.put("size", Integer.toString(size));
        return searchQuery;
    }
    public SearchQuery insertFilterParams(String params) {
        searchQuery.query.put("params", params);
        return searchQuery;
    }
    public HashMap<String, String> buildQuery() {
        return searchQuery.query;
    }
}
