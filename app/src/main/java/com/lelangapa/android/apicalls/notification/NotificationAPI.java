package com.lelangapa.android.apicalls.notification;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import com.lelangapa.android.interfaces.DataReceiver;

import java.util.HashMap;

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
}
