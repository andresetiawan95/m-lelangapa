package com.lelangapa.android.apicalls;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import com.lelangapa.android.interfaces.DataReceiver;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Andre on 11/12/2016.
 */

public class RegisterAPI {
    public static class Register extends StringRequest {
        private static final String REGISTER_URL = "https://no-api.lelangapa.com/public-apis/v1/users/register";
        private Map<String, String> data;

        private Register(HashMap<String, String> data, Response.Listener<String> listener) {
            super(Request.Method.POST, REGISTER_URL, listener, null);
            this.data = data;
        }

        @Override
        public Map<String, String> getParams() {
            return data;
        }
    }
    public static class CheckUsername extends StringRequest {
        private static final String CHECK_USERNAME_URL = "https://no-api.lelangapa.com/public-apis/v1/checker/username/";
        private CheckUsername(String username, final DataReceiver dataReceiver) {
            super(Method.GET, CHECK_USERNAME_URL + username, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    dataReceiver.dataReceived(response);
                }
            }, null);
        }
    }
    public static class CheckDomain extends StringRequest {
        private static final String CHECK_DOMAIN_URL = "https://no-api.lelangapa.com/public-apis/v1/checker/domain/";
        private CheckDomain(String domain, final DataReceiver dataReceiver) {
            super(Method.GET, CHECK_DOMAIN_URL + domain, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    dataReceiver.dataReceived(response);
                }
            }, null);
        }
    }
    public static Register instanceRegister(HashMap<String, String> dataRegister, Response.Listener<String> listener) {
        return new Register(dataRegister, listener);
    }
    public static CheckUsername instanceCheckUsername(String username, DataReceiver dataReceiver) {
        return new CheckUsername(username, dataReceiver);
    }
    public static CheckDomain instanceCheckDomain(String domain, DataReceiver dataReceiver) {
        return new CheckDomain(domain, dataReceiver);
    }
}
