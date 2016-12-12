package com.lelangkita.android.apicalls.gerai;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import com.lelangkita.android.interfaces.DataReceiver;

/**
 * Created by andre on 12/12/16.
 */

public class GetItemEditLelangBarangAPI extends StringRequest {
    private static final String GETITEMINFO_URL = "http://no-api.lelangkita.com/apis/v1/items/getitem";
    public GetItemEditLelangBarangAPI(String itemID, final DataReceiver dataReceiver){
        super(Request.Method.GET, GETITEMINFO_URL + "?item_id=" + itemID, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                dataReceiver.dataReceived(response);
            }
        }, null);
    }
}
