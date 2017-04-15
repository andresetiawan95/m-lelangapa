package com.lelangapa.android.apicalls.feedback.feedbackanda;

import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import com.lelangapa.android.interfaces.DataReceiver;
import com.lelangapa.android.preferences.SessionManager;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by andre on 13/03/17.
 */

public class FeedbackAndaAPI {
    public static class GetFeedbackAndaAsAuctioneer extends StringRequest
    {
        private static final String GETFEEDBACKASAUCTIONEERURL = "https://no-api.lelangapa.com/apis/v1/users/ratings/as/auctioneer";
        public GetFeedbackAndaAsAuctioneer(String urlparams, final DataReceiver dataReceiver){
            super(Method.GET, GETFEEDBACKASAUCTIONEERURL, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    dataReceiver.dataReceived(response);
                }
            }, null);
        }
        @Override
        public Map<String, String> getHeaders() throws AuthFailureError
        {
            Map<String, String> params = new HashMap<>();
            if (SessionManager.isLoggedInStatic()) {
                params.put("token", SessionManager.getUserTokenStatic());
                Log.v("HEADER", params.toString());
            }
            return params;
        }
    }
    public static class GetFeedbackAndaAsWinner extends StringRequest
    {
        private static final String GETFEEDBACKASWINNERURL = "https://no-api.lelangapa.com/apis/v1/users/ratings/as/winner";
        public GetFeedbackAndaAsWinner(String urlparams, final DataReceiver dataReceiver){
            super(Method.GET, GETFEEDBACKASWINNERURL, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    dataReceiver.dataReceived(response);
                }
            }, null);
        }
        @Override
        public Map<String, String> getHeaders() throws AuthFailureError
        {
            Map<String, String> params = new HashMap<>();
            if (SessionManager.isLoggedInStatic()) {
                params.put("token", SessionManager.getUserTokenStatic());
                Log.v("HEADER", params.toString());
            }
            return params;
        }
    }
    public static GetFeedbackAndaAsAuctioneer instanceFeedbackAndaAsAuctioneer
            (String urlparams, DataReceiver dataReceiver)
    {
        return new GetFeedbackAndaAsAuctioneer(urlparams, dataReceiver);
    }
    public static GetFeedbackAndaAsWinner instanceFeedbackAndaAsWinner
            (String urlparams, DataReceiver dataReceiver)
    {
        return new GetFeedbackAndaAsWinner(urlparams, dataReceiver);
    }
}
