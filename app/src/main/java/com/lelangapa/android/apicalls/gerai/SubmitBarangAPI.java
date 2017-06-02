package com.lelangapa.android.apicalls.gerai;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import com.lelangapa.android.interfaces.DataReceiver;
import com.lelangapa.android.preferences.SessionManager;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by andre on 03/12/16.
 */

public class SubmitBarangAPI extends StringRequest {
    private HashMap<String, String> dataBarang;
    private static final String SUBMIT_DATA_BARANG_URL = "https://no-api.lelangapa.com/apis/v1/items";
    public SubmitBarangAPI(HashMap<String, String> dataInput, final DataReceiver dataReceiver){
        super(Request.Method.POST, SUBMIT_DATA_BARANG_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                dataReceiver.dataReceived(response);
            }
        }, null);
        dataBarang = dataInput;
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
        return dataBarang;
    }
}
