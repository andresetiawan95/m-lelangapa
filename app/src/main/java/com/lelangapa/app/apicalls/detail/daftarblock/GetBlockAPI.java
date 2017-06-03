package com.lelangapa.app.apicalls.detail.daftarblock;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import com.lelangapa.app.interfaces.DataReceiver;
import com.lelangapa.app.preferences.SessionManager;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by andre on 19/04/17.
 */

public class GetBlockAPI {
    public static class GetBlockList extends StringRequest
    {
        private static final String GETBLOCKLISTURL = "https://no-api.lelangapa.com/apis/v1/bids/block";
        private GetBlockList(String urlparams, final DataReceiver dataReceiver)
        {
            super(Method.GET, GETBLOCKLISTURL + urlparams, new Response.Listener<String>() {
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
    public static GetBlockList instanceGetBlockList(String urlparams, DataReceiver dataReceiver)
    {
        return new GetBlockList(urlparams, dataReceiver);
    }
}
