package com.lelangapa.android.apicalls.gerai;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import com.lelangapa.android.interfaces.DataReceiver;

import java.util.HashMap;

/**
 * Created by andre on 26/04/17.
 */

public class UpdateGambarBarangAPI {
    public static class UpdateGambar extends StringRequest {
        private HashMap<String, String> dataImage;
        private static final String GAMBAR_BARANG_URL = "http://es3.lelangapa.com/update-aws.php";
        private UpdateGambar(HashMap<String, String> dataInput, final DataReceiver dataReceiver){
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
    public static class UpdateMain extends StringRequest {
        private HashMap<String, String> dataUpdateMainImage;
        private static final String UPDATE_MAIN_URL = "http://es3.lelangapa.com/update-main-image.php";
        private UpdateMain(HashMap<String, String> data, final DataReceiver dataReceiver) {
            super(Method.POST, UPDATE_MAIN_URL, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    dataReceiver.dataReceived(response);
                }
            }, null);
            dataUpdateMainImage = data;
        }
        @Override
        public HashMap<String, String> getParams() {
            return dataUpdateMainImage;
        }
    }
    public static UpdateGambar instanceUpdateGambar(HashMap<String, String> dataImage, DataReceiver dataReceiver)
    {
        return new UpdateGambar(dataImage, dataReceiver);
    }
    public static UpdateMain instanceUpdateMain(HashMap<String, String> data, DataReceiver dataReceiver) {
        return new UpdateMain(data, dataReceiver);
    }
}
