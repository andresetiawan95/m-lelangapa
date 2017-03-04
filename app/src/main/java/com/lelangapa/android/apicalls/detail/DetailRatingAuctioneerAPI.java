package com.lelangapa.android.apicalls.detail;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import com.lelangapa.android.interfaces.DataReceiver;

/**
 * Created by andre on 14/01/17.
 */

public class DetailRatingAuctioneerAPI {
    public class RatingAuctioneer extends StringRequest {
        private static final String GETDETAILRATINGAUCTIONEER = "https://no-api.lelangapa.com/apis/v1/details/getrating";
        public RatingAuctioneer(String auctioneerID, final DataReceiver dataReceiver)
        {
            super(Method.GET, GETDETAILRATINGAUCTIONEER + "?type=auctioneer&auctioneer_id=" + auctioneerID, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    dataReceiver.dataReceived(response);
                }
            }, null);
        }
    }

    public class RatingBidder extends StringRequest {
        private static final String GETDETAILRATINGBIDDER = "http://no-api.lelangapa.com/apis/v1/details/getrating";
        public RatingBidder(String auctioneerID, final DataReceiver dataReceiver)
        {
            super(Method.GET, GETDETAILRATINGBIDDER + "?type=bidder&auctioneer_id=" + auctioneerID, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    dataReceiver.dataReceived(response);
                }
            }, null);
        }
    }
}
