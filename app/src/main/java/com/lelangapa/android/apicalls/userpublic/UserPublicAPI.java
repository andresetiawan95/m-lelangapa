package com.lelangapa.android.apicalls.userpublic;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import com.lelangapa.android.interfaces.DataReceiver;

/**
 * Created by andre on 29/03/17.
 */

public class UserPublicAPI {
    public static class GetGeraiAPI extends StringRequest {
        private static final String GETGERAIAPIURL = "https://no-api.lelangapa.com/public-apis/v1/users/";
        private GetGeraiAPI(String userID, final DataReceiver dataReceiver)
        {
            super(Method.GET, GETGERAIAPIURL + userID + "/items", new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    dataReceiver.dataReceived(response);
                }
            }, null);
        }
    }
    public static class GetRiwayatAPI extends StringRequest {
        private static final String GETRIWAYATURL = "https://no-api.lelangapa.com/public-apis/v1/users/";
        private GetRiwayatAPI(String userID, final DataReceiver dataReceiver) {
            super(Method.GET, GETRIWAYATURL + userID + "/bids/history", new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    dataReceiver.dataReceived(response);
                }
            }, null);
        }
    }
    public static class GetFeedbackAsAuctioneerAPI extends StringRequest {
        private static final String GETFEEDBACKASAUCTIONEERURL = "https://no-api.lelangapa.com/public-apis/v1/users/";
        private GetFeedbackAsAuctioneerAPI(String urlparams, final DataReceiver dataReceiver){
            super(Method.GET, GETFEEDBACKASAUCTIONEERURL + urlparams, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    dataReceiver.dataReceived(response);
                }
            }, null);
        }
    }
    public static class GetFeedbackAsWinnerAPI extends StringRequest {
        private static final String GETFEEDBACKASWINNERURL = "https://no-api.lelangapa.com/public-apis/v1/users/";
        private GetFeedbackAsWinnerAPI(String urlparams, final DataReceiver dataReceiver){
            super(Method.GET, GETFEEDBACKASWINNERURL + urlparams, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    dataReceiver.dataReceived(response);
                }
            }, null);
        }
    }
    public static GetGeraiAPI instanceGeraiAPI(String userID, DataReceiver dataReceiver)
    {
        return new GetGeraiAPI(userID, dataReceiver);
    }
    public static GetRiwayatAPI instanceRiwayatAPI (String userID, DataReceiver dataReceiver)
    {
        return new GetRiwayatAPI(userID, dataReceiver);
    }
    public static GetFeedbackAsAuctioneerAPI instanceFeedbackAuctioneer (String urlparams, DataReceiver dataReceiver)
    {
        return new GetFeedbackAsAuctioneerAPI(urlparams, dataReceiver);
    }
    public static GetFeedbackAsWinnerAPI instanceFeedbackWinner (String urlparams, DataReceiver dataReceiver)
    {
        return new GetFeedbackAsWinnerAPI(urlparams, dataReceiver);
    }
}
