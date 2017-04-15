package com.lelangapa.android.apicalls;

import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import com.lelangapa.android.interfaces.DataReceiver;
import com.lelangapa.android.preferences.SessionManager;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Andre on 11/19/2016.
 */

public class GetUserProfileAPI extends StringRequest {
    private DataReceiver mReceiver;
    private static final String USER_URL = "https://no-api.lelangapa.com/apis/v1/users/profile/info";

    public GetUserProfileAPI(String userID, final DataReceiver dataReceiver) {
        super(Request.Method.GET, USER_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                dataReceiver.dataReceived(response);
            }
        }, null);
    }

    @Override
    public Map<String, String> getHeaders() throws AuthFailureError
    {
        Map<String, String> params = new HashMap<>();
        if (SessionManager.isLoggedInStatic()) {
            params.put("token", SessionManager.getUserTokenStatic());
            Log.v("HEADER", params.toString());
        }
        return params;
    }
}
