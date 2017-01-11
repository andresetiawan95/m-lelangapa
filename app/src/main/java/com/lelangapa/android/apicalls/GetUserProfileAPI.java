package com.lelangapa.android.apicalls;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import com.lelangapa.android.interfaces.DataReceiver;

/**
 * Created by Andre on 11/19/2016.
 */

public class GetUserProfileAPI extends StringRequest {
    private DataReceiver mReceiver;
    private static final String USER_URL = "http://no-api.lelangapa.com/apis/v1/users/getusers";

    public GetUserProfileAPI(String userID, final DataReceiver dataReceiver) {
        super(Request.Method.GET, USER_URL + "?usrid=" + userID, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                dataReceiver.dataReceived(response);
            }
        }, null);
    }
}
