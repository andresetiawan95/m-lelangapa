package com.lelangkita.android.apicalls.detail;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import com.lelangkita.android.interfaces.DataReceiver;

/**
 * Created by Andre on 12/29/2016.
 */

public class DetailItemAPI extends StringRequest {
    private static final String GETDETAILITEMURL = "http://no-api.lelangkita.com/apis/v1/details/getdetailitem";
    public DetailItemAPI(String itemID, final DataReceiver dataReceiver) {
        super(Method.GET, GETDETAILITEMURL + "?item_id=" + itemID, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                dataReceiver.dataReceived(response);
            }
        }, null);
    }
}
