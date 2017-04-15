package com.lelangapa.android.apicalls.gerai;

import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import com.lelangapa.android.interfaces.DataReceiver;
import com.lelangapa.android.preferences.SessionManager;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by andre on 12/12/16.
 */

public class GetItemEditLelangBarangAPI extends StringRequest {
    private static final String GETITEMINFO_URL = "https://no-api.lelangapa.com/apis/v1/items/";
    public GetItemEditLelangBarangAPI(String itemID, final DataReceiver dataReceiver){
        super(Request.Method.GET, GETITEMINFO_URL + itemID + "/info", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                dataReceiver.dataReceived(response);
            }
        }, null);
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
}
