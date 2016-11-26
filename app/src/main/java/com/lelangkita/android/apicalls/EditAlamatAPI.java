package com.lelangkita.android.apicalls;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import com.lelangkita.android.interfaces.DataReceiver;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Andre on 11/25/2016.
 */

public class EditAlamatAPI extends StringRequest {
    private HashMap<String, String> data = new HashMap<>();
    private static final String UPDATEADDRESS_URL = "http://no-api.lelangkita.com/apis/v1/users/update/address";
    public EditAlamatAPI(String userID, String address, final DataReceiver dataReceiver){
        super(Request.Method.PUT, UPDATEADDRESS_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                dataReceiver.dataReceived(response);
            }
        }, null);
        data.put("userid", userID);
        data.put("address", address);
    }
    @Override
    public HashMap<String, String> getParams(){
        return data;
    }
}
