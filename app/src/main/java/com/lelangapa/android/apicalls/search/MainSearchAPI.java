package com.lelangapa.android.apicalls.search;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import com.lelangapa.android.interfaces.DataReceiver;

import java.util.HashMap;

/**
 * Created by Andre on 12/17/2016.
 */

public class MainSearchAPI {
    public static class SearchByGET extends StringRequest {
        private static final String MAINSEARCHBARANG_URL = "https://no-api.lelangapa.com/apis/v1/search";
        public SearchByGET(String queryText, final DataReceiver dataReceiver){
            super(Request.Method.GET, MAINSEARCHBARANG_URL + "?q=" + queryText, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    dataReceiver.dataReceived(response);
                }
            }, null);
        }
    }
    public static class QueryKey extends StringRequest {
        private static final String QUERY_URL = "https://src-api.lelangapa.com/apis/search";
        private static HashMap<String, String> data;
        private QueryKey(HashMap<String, String> data, final DataReceiver dataReceiver) {
            super(Method.POST, QUERY_URL, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    dataReceiver.dataReceived(response);
                }
            }, null);
            QueryKey.data = data;
        }
        @Override
        public HashMap<String, String> getParams(){
            return data;
        }
    }
    public static class QueryBulkCategory extends StringRequest {
        private static final String QUERY_URL = "https://src-api.lelangapa.com/apis/search/category";
        private static HashMap<String, String> data;
        private QueryBulkCategory(HashMap<String, String> data, final DataReceiver dataReceiver) {
            super(Method.POST, QUERY_URL, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    dataReceiver.dataReceived(response);
                }
            }, null);
            QueryBulkCategory.data = data;
        }
        @Override
        public HashMap<String, String> getParams(){
            return data;
        }
    }
    public static class QueryKeyWithParams extends StringRequest {
        private static final String QUERY_URL = "https://src-api.lelangapa.com/apis/search/filter";
        private static HashMap<String, String> data;
        private QueryKeyWithParams(HashMap<String, String> data, final DataReceiver dataReceiver) {
            super(Method.POST, QUERY_URL, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    dataReceiver.dataReceived(response);
                }
            }, null);
            QueryKeyWithParams.data = data;
        }
        @Override
        public HashMap<String, String> getParams(){
            return data;
        }
    }
    public static QueryKey queryKeyInstance(HashMap<String, String> data, DataReceiver dataReceiver) {
        return new QueryKey(data, dataReceiver);
    }
    public static QueryBulkCategory bulkInstance(HashMap<String, String> data, DataReceiver dataReceiver) {
        return new QueryBulkCategory(data, dataReceiver);
    }
    public static QueryKeyWithParams queryParamsInstance(HashMap<String, String> data, DataReceiver dataReceiver) {
        return new QueryKeyWithParams(data, dataReceiver);
    }
}
