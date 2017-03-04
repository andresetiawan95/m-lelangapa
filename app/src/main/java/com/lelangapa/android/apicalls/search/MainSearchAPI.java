package com.lelangapa.android.apicalls.search;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import com.lelangapa.android.interfaces.DataReceiver;

/**
 * Created by Andre on 12/17/2016.
 */

public class MainSearchAPI extends StringRequest {
    private static final String MAINSEARCHBARANG_URL = "https://no-api.lelangapa.com/apis/v1/search";
    public MainSearchAPI(String queryText, final DataReceiver dataReceiver){
        super(Request.Method.GET, MAINSEARCHBARANG_URL + "?q=" + queryText, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
               dataReceiver.dataReceived(response);
            }
        }, null);
    }
}
