package com.lelangkita.android.apicalls.gerai;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import com.lelangkita.android.interfaces.DataReceiver;

/**
 * Created by andre on 06/12/16.
 */

public class GetBarangGeraiUserAPI extends StringRequest {
    private static final String GETBARANGGERAIUSER_URL = "http://no-api.lelangkita.com/apis/v1/items/getitemonuser";
    public GetBarangGeraiUserAPI(String userID, final DataReceiver dataReceiver){
        super(Request.Method.GET, GETBARANGGERAIUSER_URL + "?userid=" + userID, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                dataReceiver.dataReceived(response);
            }
        }, null);
    }
}
