package com.lelangapa.android.apicalls;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import com.lelangapa.android.interfaces.DataReceiver;

import java.util.HashMap;

/**
 * Created by Andre on 11/25/2016.
 */

public class EditPasswordAPI extends StringRequest{
    private HashMap<String, String> data = new HashMap<>();
    private static final String UPDATEPASSWORD_URL = "https://no-api.lelangapa.com/apis/v1/users/update/password";
    public EditPasswordAPI(HashMap<String, String> dataInput, final DataReceiver dataReceiver){
        super(Request.Method.PUT, UPDATEPASSWORD_URL, new Response.Listener<String>() {
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
