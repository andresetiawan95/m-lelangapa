package com.lelangapa.android.apicalls.feedback.berifeedback;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import com.lelangapa.android.interfaces.DataReceiver;

/**
 * Created by andre on 13/03/17.
 */

public class BeriFeedbackAPI {
    public static class GetFeedbackWinnerList extends StringRequest
    {
        private static final String GETFEEDBACKWINNERLISTURL = "https://no-api.lelangapa.com/apis/v1/ratings/winner/";
        public GetFeedbackWinnerList(String userID, final DataReceiver dataReceiver){
            super(Method.GET, GETFEEDBACKWINNERLISTURL + userID, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    dataReceiver.dataReceived(response);
                }
            }, null);
        }
    }
    public static class GetFeedbackAuctioneerList extends StringRequest
    {
        private static final String GETFEEDBACKAUCTIONEERLISTURL = "https://no-api.lelangapa.com/apis/v1/ratings/auctioneer/";
        public GetFeedbackAuctioneerList(String userID, final DataReceiver dataReceiver){
            super(Method.GET, GETFEEDBACKAUCTIONEERLISTURL + userID, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    dataReceiver.dataReceived(response);
                }
            }, null);
        }
    }

    public static GetFeedbackWinnerList instanceFeedbackWinner(String userID, DataReceiver dataReceiver)
    {
        GetFeedbackWinnerList feedbackWinnerList = new GetFeedbackWinnerList(userID, dataReceiver);
        return feedbackWinnerList;
    }
    public static GetFeedbackAuctioneerList instanceFeedbackAuctioneer(String userID, DataReceiver dataReceiver)
    {
        GetFeedbackAuctioneerList feedbackAuctioneerList = new GetFeedbackAuctioneerList(userID, dataReceiver);
        return feedbackAuctioneerList;
    }
}
