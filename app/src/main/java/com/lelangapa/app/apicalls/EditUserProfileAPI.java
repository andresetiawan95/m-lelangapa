package com.lelangapa.app.apicalls;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import com.lelangapa.app.interfaces.DataReceiver;
import com.lelangapa.app.preferences.SessionManager;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Andre on 11/25/2016.
 */

public class EditUserProfileAPI extends StringRequest{
    private HashMap<String, String> data = new HashMap<>();
    private static final String UPDATEPROFILE_URL = "https://no-api.lelangapa.com/apis/v1/users/update/profile";
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
    public Map<String, String> getHeaders() throws AuthFailureError
    {
        Map<String, String> params = new HashMap<>();
        if (SessionManager.isLoggedInStatic()) {
            params.put("token", SessionManager.getUserTokenStatic());
            //Log.v("HEADER", params.toString());
        }
        return params;
    }
    @Override
    public HashMap<String, String> getParams(){
        return data;
    }
}
