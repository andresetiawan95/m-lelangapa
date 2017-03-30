package com.lelangapa.android.apicalls.userpublic;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import com.lelangapa.android.interfaces.DataReceiver;

/**
 * Created by andre on 29/03/17.
 */

public class UserPublicAPI {
    public static class GetGeraiAPI extends StringRequest {
        private static final String GETGERAIAPIURL = "https://no-api.lelangapa.com/apis/v1/items/getitemonuser";
        private GetGeraiAPI(String userID, final DataReceiver dataReceiver)
        {
            super(Method.GET, GETGERAIAPIURL + "?userid=" + userID, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    dataReceiver.dataReceived(response);
                }
            }, null);
        }
    }
    public static class GetRiwayatAPI extends StringRequest {
        private static final String GETRIWAYATURL = "https://no-api.lelangapa.com/apis/v1/bids/history/";
        private GetRiwayatAPI(String userID, final DataReceiver dataReceiver) {
            super(Method.GET, GETRIWAYATURL + userID, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    dataReceiver.dataReceived(response);
                }
            }, null);
        }
    }
    public static GetGeraiAPI instanceGeraiAPI(String userID, DataReceiver dataReceiver)
    {
        return new GetGeraiAPI(userID, dataReceiver);
    }
    public static GetRiwayatAPI instanceRiwayatAPI (String userID, DataReceiver dataReceiver)
    {
        return new GetRiwayatAPI(userID, dataReceiver);
    }
}
