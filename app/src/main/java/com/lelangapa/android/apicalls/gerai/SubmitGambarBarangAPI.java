package com.lelangapa.android.apicalls.gerai;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import com.lelangapa.android.interfaces.DataReceiver;

import java.util.HashMap;

/**
 * Created by andre on 03/12/16.
 */

public class SubmitGambarBarangAPI extends StringRequest {
    private HashMap<String, String> dataImage;
    private static final String GAMBAR_BARANG_URL = "http://es3.lelangapa.com/upload-aws.php";
    public SubmitGambarBarangAPI(HashMap<String, String> dataInput, final DataReceiver dataReceiver){
        super(Request.Method.POST, GAMBAR_BARANG_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                dataReceiver.dataReceived(response);
            }
        }, null);
        dataImage = dataInput;
    }
    @Override
    public HashMap<String, String> getParams(){
        return dataImage;
    }
}
