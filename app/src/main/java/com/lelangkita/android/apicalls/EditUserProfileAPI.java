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
    public EditUserProfileAPI(HashMap<String, String> dataInput, final DataReceiver dataReceiver){
        super(Request.Method.PUT, UPDATEPROFILE_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                dataReceiver.dataReceived(response);
            }
        }, null);
        data = dataInput;
    }
    @Override
    public HashMap<String, String> getParams(){
        return data;
    }
}
