package com.lelangapa.android.apicalls;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Andre on 11/12/2016.
 */

public class RegisterAPI extends StringRequest {
    private static final String REGISTER_URL = "https://no-api.lelangapa.com/public-apis/v1/users/register";
    private Map<String, String> data;
    public RegisterAPI(String username, String domain, String name, String password, String address, String email, String city, String province, String telepon, Response.Listener<String> listener){
        super(Request.Method.POST, REGISTER_URL, listener, null);
        data = new HashMap<>();
        data.put("name", name);
        data.put("email", email);
        data.put("password", password);
        data.put("address", address);
        data.put("phone", telepon);
        data.put("id_city", city);
        data.put("id_province", province);
        data.put("username", username);
        data.put("domain", domain);
    }
    @Override
    public Map<String, String> getParams(){
        return data;
    }
}
