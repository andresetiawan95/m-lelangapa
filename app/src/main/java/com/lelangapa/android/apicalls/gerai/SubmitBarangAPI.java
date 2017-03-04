package com.lelangapa.android.apicalls.gerai;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import com.lelangapa.android.interfaces.DataReceiver;

import java.util.HashMap;

/**
 * Created by andre on 03/12/16.
 */

public class SubmitBarangAPI extends StringRequest {
    private HashMap<String, String> dataBarang;
    private static final String SUBMIT_DATA_BARANG_URL = "https://no-api.lelangapa.com/apis/v1/items/additem";
    public SubmitBarangAPI(HashMap<String, String> dataInput, final DataReceiver dataReceiver){
        super(Request.Method.POST, SUBMIT_DATA_BARANG_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                dataReceiver.dataReceived(response);
            }
        }, null);
        dataBarang = dataInput;
    }
    @Override
    public HashMap<String, String> getParams(){
        return dataBarang;
    }
}
