package com.lelangapa.app.apicalls.notification;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import com.lelangapa.app.interfaces.DataReceiver;
import com.lelangapa.app.preferences.SessionManager;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by andre on 10/04/17.
 */

public class NotificationAPI {
    public static class SendToken extends StringRequest
    {
        private HashMap<String, String> dataToken;
        private static final String POSTTOKENURL = "https://no-api.lelangapa.com/apis/v1/fcm/token/submit";
        private SendToken(HashMap<String, String> dataToken)
        {
            super(Method.POST, POSTTOKENURL, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {

                }
            }, null);
            this.dataToken = dataToken;
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
        public HashMap<String, String> getParams()
        {
            return dataToken;
        }
    }
    public static class Logout extends StringRequest
    {
        private HashMap<String, String> dataToken;
        private static final String LOGOUTTOKENURL = "https://no-api.lelangapa.com/apis/v1/fcm/token/logout";
        private Logout(HashMap<String, String> dataToken, final DataReceiver dataReceiver)
        {
            super(Method.POST, LOGOUTTOKENURL, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    dataReceiver.dataReceived(response);
                }
            }, null);
            this.dataToken = dataToken;
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
        public HashMap<String, String> getParams()
        {
            return dataToken;
        }
    }
    public static class LogoutInvalidToken extends StringRequest
    {
        private HashMap<String, String> dataToken;
        private static final String LOGOUTTOKENURL = "https://no-api.lelangapa.com/public-apis/v1/jwt/token/invalid/fcm/logout";
        private LogoutInvalidToken(HashMap<String, String> dataToken, final DataReceiver dataReceiver)
        {
            super(Method.POST, LOGOUTTOKENURL, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    dataReceiver.dataReceived(response);
                }
            }, null);
            this.dataToken = dataToken;
        }
        @Override
        public HashMap<String, String> getParams()
        {
            return dataToken;
        }
    }
    public static SendToken getSendTokenInstance(HashMap<String, String> data)
    {
        return new SendToken(data);
    }
    public static Logout getLogoutInstance(HashMap<String, String> data, DataReceiver dataReceiver)
    {
        return new Logout(data, dataReceiver);
    }
    public static LogoutInvalidToken getLogoutInvalidTokenInstance(HashMap<String, String> data, DataReceiver dataReceiver) {
        return new LogoutInvalidToken(data, dataReceiver);
    }
}
