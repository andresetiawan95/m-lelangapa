package com.lelangapa.android.apicalls.feedback.feedback_average;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import com.lelangapa.android.interfaces.DataReceiver;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by andre on 17/04/17.
 */

public class FeedbackAverage {
    public static class FeedbackAsAuctioneer extends StringRequest
    {
        private static final String GETFEEDBACKASAUCTIONEERAVERAGE = "https://no-api.lelangapa.com/public-apis/v1/users/";
        private FeedbackAsAuctioneer(String urlparams, final DataReceiver dataReceiver)
        {
            super(Method.GET, GETFEEDBACKASAUCTIONEERAVERAGE + urlparams, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    dataReceiver.dataReceived(response);
                }
            }, null);
        }
        @Override
        public Map<String, String> getHeaders() throws AuthFailureError {
            Map<String, String>  params = new HashMap<String, String>();
            params.put("Cache-Control", "max-age=0");
            //Log.v("REQUEST HEADER", params.toString());
            return params;
        }
    }
    public static class FeedbackAsWinner extends StringRequest
    {
        private static final String GETFEEDBACKASWINNERAVERAGE = "https://no-api.lelangapa.com/public-apis/v1/users/";
        private FeedbackAsWinner(String urlparams, final DataReceiver dataReceiver)
        {
            super(Method.GET, GETFEEDBACKASWINNERAVERAGE + urlparams, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    dataReceiver.dataReceived(response);
                }
            }, null);
        }
        @Override
        public Map<String, String> getHeaders() throws AuthFailureError {
            Map<String, String>  params = new HashMap<String, String>();
            params.put("Cache-Control", "max-age=0");
            //Log.v("REQUEST HEADER", params.toString());
            return params;
        }
    }

    public static FeedbackAsAuctioneer feedbackAsAuctioneerInstance(String urlparams, DataReceiver dataReceiver)
    {
        return new FeedbackAsAuctioneer(urlparams, dataReceiver);
    }
    public static FeedbackAsWinner feedbackAsWinnerInstance(String urlparams, DataReceiver dataReceiver)
    {
        return new FeedbackAsWinner(urlparams, dataReceiver);
    }
}
