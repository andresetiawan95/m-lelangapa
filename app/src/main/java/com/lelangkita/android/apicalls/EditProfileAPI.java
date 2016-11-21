package com.lelangkita.android.apicalls;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.lelangkita.android.interfaces.DataReceiver;

import org.json.JSONObject;

/**
 * Created by Andre on 11/19/2016.
 */

public class EditProfileAPI extends StringRequest {
    private DataReceiver mReceiver;
    private static final String USER_URL = "http://no-api.lelangkita.com/apis/v1/users/getusers";

    public EditProfileAPI(String userID, final DataReceiver dataReceiver) {
        super(Request.Method.GET, USER_URL + "?usrid=" + userID, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                dataReceiver.dataReceived(response);
            }
        }, null);
    }
}
