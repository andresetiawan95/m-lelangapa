package com.lelangapa.app.apicalls.home;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import com.lelangapa.app.interfaces.DataReceiver;
import com.lelangapa.app.preferences.SessionManager;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by andre on 15/06/17.
 */

public class HomeAPI {
    public static class VerifyToken extends StringRequest {
        private static final String VERIFYTOKENURL = "https://no-api.lelangapa.com/apis/v1/validation/token";
        private VerifyToken(final DataReceiver dataReceiver) {
            super(Method.GET, VERIFYTOKENURL, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                   dataReceiver.dataReceived(response);
                }
            }, null);
        }
        @Override
        protected Response<String> parseNetworkResponse(NetworkResponse networkResponse)
        {
            return super.parseNetworkResponse(networkResponse);
        }
        @Override
        public Map<String, String> getHeaders() throws AuthFailureError {
            Map<String, String>  params = new HashMap<String, String>();
            params.put("Cache-Control", "max-age=0");
            if (SessionManager.isLoggedInStatic()) {
                params.put("token", SessionManager.getUserTokenStatic());
            }
            return params;
        }
    }
    public static VerifyToken verifyInstance(DataReceiver dataReceiver) {
        return new VerifyToken(dataReceiver);
    }
}
