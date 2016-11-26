package com.lelangkita.android.apicalls;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import com.lelangkita.android.interfaces.DataReceiver;

import java.util.HashMap;

/**
 * Created by Andre on 11/25/2016.
 */

public class EditUserProfileAPI extends StringRequest{
    private HashMap<String, String> data = new HashMap<>();
    private static final String UPDATEPROFILE_URL = "http://no-api.lelangkita.com/apis/v1/users/update/profile";
    public EditUserProfileAPI(String userID, String name, String phone, String email, final DataReceiver dataReceiver){
        super(Request.Method.PUT, UPDATEPROFILE_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                dataReceiver.dataReceived(response);
            }
        }, null);
        data.put("userid", userID);
        data.put("name", name);
        data.put("phone", phone);
        data.put("email", email);

    }
    @Override
    public HashMap<String, String> getParams(){
        return data;
    }
}
