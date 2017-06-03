package com.lelangapa.app.apicalls;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Andre on 11/12/2016.
 */

public class LoginAPI extends StringRequest {
    private static final String LOGIN_URL = "https://no-api.lelangapa.com/public-apis/v1/users/login";
    private Map<String, String> data;
    public LoginAPI(String username, String password, Response.Listener<String> listener){
        super(Request.Method.POST, LOGIN_URL, listener, null);
        data = new HashMap<>();
        data.put("username", username);
        data.put("password", password);
    }
    @Override
    public Map<String, String> getParams(){
        return data;
    }
}
