package com.lelangapa.app.apicalls.riwayat;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import com.lelangapa.app.interfaces.DataReceiver;
import com.lelangapa.app.preferences.SessionManager;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by andre on 24/02/17.
 */

public class RiwayatAPI {
    public static class GetRiwayat extends StringRequest {
        private static final String GETRIWAYATURL = "https://no-api.lelangapa.com/apis/v1/users/";
        public GetRiwayat(String userID, final DataReceiver dataReceiver) {
            super(Method.GET, GETRIWAYATURL + userID + "/bids/history", new Response.Listener<String>() {
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
    public static class GetRiwayatBidList extends StringRequest {
        private static final String GETRIWAYATLISTURL = "https://no-api.lelangapa.com/apis/v1/users/";
        public GetRiwayatBidList(String urlParams, final DataReceiver dataReceiver) {
            super(Method.GET, GETRIWAYATLISTURL + urlParams, new Response.Listener<String>() {
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
    public static GetRiwayat initializeGetRiwayat(String userID, DataReceiver dataReceiver)
    {
        return new GetRiwayat(userID, dataReceiver);
    }
    public static GetRiwayatBidList initializeGetRiwayatBidList(String urlParams, DataReceiver dataReceiver)
    {
        return new GetRiwayatBidList(urlParams, dataReceiver);
    }
}
