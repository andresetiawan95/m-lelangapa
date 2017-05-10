package com.lelangapa.android.apicalls.gerai;

import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.lelangapa.android.interfaces.DataReceiver;
import com.lelangapa.android.preferences.SessionManager;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by andre on 10/05/17.
 */

public class DeleteBarangAPI extends StringRequest {
    private static HashMap<String, String> dataBarang;
    private static final String DELETE_BARANG_URL = "https://no-api.lelangapa.com/apis/v1/items/delete";
    public DeleteBarangAPI(HashMap<String, String> data, final DataReceiver dataReceiver) {
        super(Method.PUT, DELETE_BARANG_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                dataReceiver.dataReceived(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                dataReceiver.dataReceived("server-error");
            }
        });
        dataBarang = data;
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
    @Override
    public HashMap<String, String> getParams(){
        return dataBarang;
    }
}
