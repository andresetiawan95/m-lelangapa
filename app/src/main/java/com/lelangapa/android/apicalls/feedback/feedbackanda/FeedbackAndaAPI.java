package com.lelangapa.android.apicalls.feedback.feedbackanda;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import com.lelangapa.android.interfaces.DataReceiver;

/**
 * Created by andre on 13/03/17.
 */

public class FeedbackAndaAPI {
    public static class GetFeedbackAndaAsAuctioneer extends StringRequest
    {
        private static final String GETFEEDBACKASAUCTIONEERURL = "https://no-api.lelangapa.com/apis/v1/users/";
        public GetFeedbackAndaAsAuctioneer(String urlparams, final DataReceiver dataReceiver){
            super(Method.GET, GETFEEDBACKASAUCTIONEERURL + urlparams, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    dataReceiver.dataReceived(response);
                }
            }, null);
        }
    }
    public static class GetFeedbackAndaAsWinner extends StringRequest
    {
        private static final String GETFEEDBACKASWINNERURL = "https://no-api.lelangapa.com/apis/v1/users/";
        public GetFeedbackAndaAsWinner(String urlparams, final DataReceiver dataReceiver){
            super(Method.GET, GETFEEDBACKASWINNERURL + urlparams, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    dataReceiver.dataReceived(response);
                }
            }, null);
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
