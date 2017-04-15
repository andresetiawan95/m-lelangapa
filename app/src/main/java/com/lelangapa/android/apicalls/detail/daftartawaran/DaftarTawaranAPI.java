package com.lelangapa.android.apicalls.detail.daftartawaran;

import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import com.lelangapa.android.interfaces.DataReceiver;
import com.lelangapa.android.preferences.SessionManager;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by andre on 02/04/17.
 */

public class DaftarTawaranAPI {
    public static class GetOfferListAPI extends StringRequest
    {
        private static final String GETOFFERLISTURL = "https://no-api.lelangapa.com/apis/v1/bids/item/";
        private GetOfferListAPI(String urlparams, final DataReceiver dataReceiver)
        {
            super(Method.GET, GETOFFERLISTURL + urlparams, new Response.Listener<String>() {
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
    public static GetOfferListAPI instanceGetOfferList(String urlparams, DataReceiver dataReceiver) {
        return new GetOfferListAPI(urlparams, dataReceiver);
    }
}
