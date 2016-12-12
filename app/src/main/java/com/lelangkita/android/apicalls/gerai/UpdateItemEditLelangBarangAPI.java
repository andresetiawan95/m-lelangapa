package com.lelangkita.android.apicalls.gerai;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import com.lelangkita.android.interfaces.DataReceiver;

import java.util.HashMap;

/**
 * Created by andre on 12/12/16.
 */

public class UpdateItemEditLelangBarangAPI extends StringRequest {
    private HashMap<String, String> dataBarang;
    private static final String UPDATE_DATA_BARANG_URL = "http://no-api.lelangkita.com/apis/v1/items/updateitem";
    public UpdateItemEditLelangBarangAPI(HashMap<String, String> dataBarang, final DataReceiver dataReceiver){
        super(Request.Method.PUT, UPDATE_DATA_BARANG_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                dataReceiver.dataReceived(response);
            }
        }, null);
        this.dataBarang = dataBarang;
    }
    @Override
    public HashMap<String, String> getParams(){
        return dataBarang;
    }
}
