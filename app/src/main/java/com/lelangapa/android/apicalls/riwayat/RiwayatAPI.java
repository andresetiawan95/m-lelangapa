package com.lelangapa.android.apicalls.riwayat;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import com.lelangapa.android.interfaces.DataReceiver;

/**
 * Created by andre on 24/02/17.
 */

public class RiwayatAPI {
    public class GetRiwayat extends StringRequest {
        private static final String GETRIWAYATURL = "http://no-api.lelangapa.com/apis/v1/bids/history/";
        public GetRiwayat(String userID, final DataReceiver dataReceiver) {
            super(Method.GET, GETRIWAYATURL + userID, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    dataReceiver.dataReceived(response);
                }
            }, null);
        }
    }
}
