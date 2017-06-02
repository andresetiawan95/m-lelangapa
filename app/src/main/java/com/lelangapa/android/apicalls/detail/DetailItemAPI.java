package com.lelangapa.android.apicalls.detail;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import com.lelangapa.android.interfaces.DataReceiver;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Andre on 12/29/2016.
 */

public class DetailItemAPI extends StringRequest {
    private static final String GETDETAILITEMURL = "https://no-api.lelangapa.com/public-apis/v1/items/";
    public DetailItemAPI(String itemID, final DataReceiver dataReceiver) {
        super(Method.GET, GETDETAILITEMURL + itemID + "/details", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                dataReceiver.dataReceived(response);
            }
        }, null);
    }
    @Override
    protected Response<String> parseNetworkResponse(NetworkResponse networkResponse)
    {
        //Log.v("Header LALALALLAA", networkResponse.headers.toString());
        return super.parseNetworkResponse(networkResponse);
    }
    @Override
    public Map<String, String> getHeaders() throws AuthFailureError {
        Map<String, String>  params = new HashMap<String, String>();
        params.put("Cache-Control", "max-age=0");
        //Log.v("REQUEST HEADER", params.toString());
        return params;
    }
}
