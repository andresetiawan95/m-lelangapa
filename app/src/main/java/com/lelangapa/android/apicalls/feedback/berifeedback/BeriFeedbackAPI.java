package com.lelangapa.android.apicalls.feedback.berifeedback;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import com.lelangapa.android.interfaces.DataReceiver;

import java.util.HashMap;

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
    public static class GetFeedbackWinnerDetail extends StringRequest
    {
        private static final String GETFEEDBACKWINNERDETAILURL = "https://no-api.lelangapa.com/apis/v1/ratings/winner/";
        public GetFeedbackWinnerDetail(String ratinglogsID, final DataReceiver dataReceiver){
            super(Request.Method.GET, GETFEEDBACKWINNERDETAILURL + ratinglogsID + "/details", new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    dataReceiver.dataReceived(response);
                }
            }, null);
        }
    }
    public static class GetFeedbackAuctioneerDetail extends StringRequest
    {
        private static final String GETFEEDBACKWINNERDETAILURL = "https://no-api.lelangapa.com/apis/v1/ratings/auctioneer/";
        public GetFeedbackAuctioneerDetail(String ratinglogsID, final DataReceiver dataReceiver){
            super(Request.Method.GET, GETFEEDBACKWINNERDETAILURL + ratinglogsID + "/details", new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    dataReceiver.dataReceived(response);
                }
            }, null);
        }
    }
    public static class InsertFeedbackForAuctioneer extends StringRequest
    {
        private HashMap<String, String> feedback;
        private static final String INSERTFEEDBACKFORAUCTIONEERURL = "https://no-api.lelangapa.com/apis/v1/ratings/auctioneer";
        public InsertFeedbackForAuctioneer(HashMap<String, String> feedback, final DataReceiver dataReceiver) {
            super(Method.POST, INSERTFEEDBACKFORAUCTIONEERURL, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    dataReceiver.dataReceived(response);
                }
            }, null);
            this.feedback = feedback;
        }
        @Override
        public HashMap<String, String> getParams()
        {
            return feedback;
        }
    }
    public static class InsertFeedbackForWinner extends StringRequest
    {
        private HashMap<String, String> feedback;
        private static final String INSERTFEEDBACKFORWINNERURL = "https://no-api.lelangapa.com/apis/v1/ratings/winner";
        public InsertFeedbackForWinner(HashMap<String, String> feedback, final DataReceiver dataReceiver) {
            super(Method.POST, INSERTFEEDBACKFORWINNERURL, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    dataReceiver.dataReceived(response);
                }
            }, null);
            this.feedback = feedback;
        }
        @Override
        public HashMap<String, String> getParams()
        {
            return feedback;
        }
    }
    public static class UpdateFeedbackForAuctioneer extends StringRequest
    {
        private HashMap<String, String> feedback;
        private static final String UPDATEFEEDBACKFORAUCTIONEERURL = "https://no-api.lelangapa.com/apis/v1/ratings/auctioneer";
        public UpdateFeedbackForAuctioneer(HashMap<String, String> feedback, final DataReceiver dataReceiver) {
            super(Method.PUT, UPDATEFEEDBACKFORAUCTIONEERURL, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    dataReceiver.dataReceived(response);
                }
            }, null);
            this.feedback = feedback;
        }
        @Override
        public HashMap<String, String> getParams()
        {
            return feedback;
        }
    }
    public static class UpdateFeedbackForWinner extends StringRequest
    {
        private HashMap<String, String> feedback;
        private static final String UPDATEFEEDBACKFORWINNERURL = "https://no-api.lelangapa.com/apis/v1/ratings/winner";
        public UpdateFeedbackForWinner(HashMap<String, String> feedback, final DataReceiver dataReceiver) {
            super(Method.PUT, UPDATEFEEDBACKFORWINNERURL, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    dataReceiver.dataReceived(response);
                }
            }, null);
            this.feedback = feedback;
        }
        @Override
        public HashMap<String, String> getParams()
        {
            return feedback;
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
    public static GetFeedbackAuctioneerDetail instanceFeedbackAuctioneerDetail(String ratinglogsID, DataReceiver dataReceiver)
    {
        return new GetFeedbackAuctioneerDetail(ratinglogsID, dataReceiver);
    }
    public static GetFeedbackWinnerDetail instanceFeedbackWinnerDetail(String ratinglogsID, DataReceiver dataReceiver)
    {
        return new GetFeedbackWinnerDetail(ratinglogsID, dataReceiver);
    }
    public static InsertFeedbackForAuctioneer instanceInsertFeedbackForAuctioneer(HashMap<String, String> favorite, DataReceiver dataReceiver)
    {
        return new InsertFeedbackForAuctioneer(favorite, dataReceiver);
    }
    public static InsertFeedbackForWinner instanceInsertFeedbackForWinner(HashMap<String, String> favorite, DataReceiver dataReceiver)
    {
        return new InsertFeedbackForWinner(favorite, dataReceiver);
    }
    public static UpdateFeedbackForAuctioneer instanceUpdateFeedbackForAuctioneer(HashMap<String, String> favorite, DataReceiver dataReceiver)
    {
        return new UpdateFeedbackForAuctioneer(favorite, dataReceiver);
    }
    public static UpdateFeedbackForWinner instanceUpdateFeedbackForWinner(HashMap<String, String> favorite, DataReceiver dataReceiver)
    {
        return new UpdateFeedbackForWinner(favorite, dataReceiver);
    }
}
