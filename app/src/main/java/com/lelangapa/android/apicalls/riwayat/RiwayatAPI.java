package com.lelangapa.android.apicalls.riwayat;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import com.lelangapa.android.interfaces.DataReceiver;

/**
 * Created by andre on 24/02/17.
 */

public class RiwayatAPI {
    public static class GetRiwayat extends StringRequest {
        private static final String GETRIWAYATURL = "https://no-api.lelangapa.com/apis/v1/bids/history/";
        public GetRiwayat(String userID, final DataReceiver dataReceiver) {
            super(Method.GET, GETRIWAYATURL + userID, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    dataReceiver.dataReceived(response);
                }
            }, null);
        }
    }
    public static class GetRiwayatBidList extends StringRequest {
        private static final String GETRIWAYATLISTURL = "https://no-api.lelangapa.com/apis/v1/bids/history/list/";
        public GetRiwayatBidList(String urlParams, final DataReceiver dataReceiver) {
            super(Method.GET, GETRIWAYATLISTURL + urlParams, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    dataReceiver.dataReceived(response);
                }
            }, null);
        }
    }
    public static GetRiwayat initializeGetRiwayat(String userID, DataReceiver dataReceiver)
    {
        return new GetRiwayat(userID, dataReceiver);
    }
    public static GetRiwayatBidList initializeGetRiwayatBidList(String urlParams, DataReceiver dataReceiver)
    {
        return new GetRiwayatBidList(urlParams, dataReceiver);
    }
}
