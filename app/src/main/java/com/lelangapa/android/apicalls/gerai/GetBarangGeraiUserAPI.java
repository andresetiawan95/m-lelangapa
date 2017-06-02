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
 * Created by andre on 06/12/16.
 */

public class GetBarangGeraiUserAPI extends StringRequest {
    private static final String GETBARANGGERAIUSER_URL = "https://no-api.lelangapa.com/apis/v1/users/items";
    public GetBarangGeraiUserAPI(String userID, final DataReceiver dataReceiver){
        super(Request.Method.GET, GETBARANGGERAIUSER_URL, new Response.Listener<String>() {
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
            //Log.v("HEADER", params.toString());
        }
        return params;
    }
}
