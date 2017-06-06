package com.lelangapa.app.fragments.detail.detailitemuser;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.lelangapa.app.R;
import com.lelangapa.app.activities.userpublic.DetailUserPublicActivity;
import com.lelangapa.app.apicalls.feedback.feedback_average.FeedbackAverage;
import com.lelangapa.app.apicalls.singleton.RequestController;
import com.lelangapa.app.interfaces.DataReceiver;
import com.lelangapa.app.resources.DetailItemResources;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Andre on 12/26/2016.
 */

public class DetailAuctioneerFragment extends Fragment {
    private DetailItemResources detailItemResources;
    private String auctioneerID, auctioneerName, auctioneerAvatarURL;
    private TextView textView_namaAuctioneer, textView_ratingAuctioneer_rate, textView_ratingAuctioneer_total;
    private TextView textView_ratingWinner_rate, textView_ratingWinner_total;
    private ImageView imageView_auctioneerAvatar;
    //private Button button_checkProfile;

    private DataReceiver dataReceiverFeedbackAsAuctioneer, dataReceiverFeedbackAsWinner;

    private View.OnClickListener onClickListener;
    public DetailAuctioneerFragment(){}
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_detail_barang_auctioneer_layout, container, false);
        initializeViews(view);
        initializeDataReceivers();
        setOnClickListener();
        setTextViewAuctioneerInformation();
        loadAuctioneerAvatar();
        getAverageFeedbackData();
        return view;
    }
    private void initializeViews(View view)
    {
        textView_namaAuctioneer = (TextView) view.findViewById(R.id.fragment_detail_barang_auctioneer_layout_nama);
        textView_ratingAuctioneer_rate = (TextView) view.findViewById(R.id.fragment_detail_barang_auctioneer_layout_rating_auctioneer_rate);
        textView_ratingAuctioneer_total = (TextView) view.findViewById(R.id.fragment_detail_barang_auctioneer_layout_rating_auctioneer_total);

        textView_ratingWinner_rate = (TextView) view.findViewById(R.id.fragment_detail_barang_auctioneer_layout_rating_winner_rate);
        textView_ratingWinner_total = (TextView) view.findViewById(R.id.fragment_detail_barang_auctioneer_layout_rating_winner_total);
        imageView_auctioneerAvatar = (ImageView) view.findViewById(R.id.fragment_detail_barang_auctioneer_layout_profilepicture);
        //button_checkProfile = (Button) view.findViewById(R.id.fragment_detail_barang_auctioneer_layout_button);
    }
    private void initializeDataReceivers()
    {
        dataReceiverFeedbackAsWinner = new DataReceiver() {
            @Override
            public void dataReceived(Object output) {
                if (isVisible()) {
                    String response = output.toString();
                    try {
                        JSONObject jsonResponse = new JSONObject(response);
                        if (jsonResponse.getString("status").equals("success")) {
                            if (jsonResponse.getInt("total_data") > 0) {
                                String totalData = Integer.toString(jsonResponse.getInt("total_data"));
                                textView_ratingWinner_rate.setText(jsonResponse.getString("rate_average"));
                                textView_ratingWinner_total.setText(totalData);
                            }
                            else {
                                textView_ratingWinner_rate.setText("0.0");
                                textView_ratingWinner_total.setText("0");
                            }
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        };
        dataReceiverFeedbackAsAuctioneer = new DataReceiver() {
            @Override
            public void dataReceived(Object output) {
                if (isResumed()) {
                    String response = output.toString();
                    try {
                        JSONObject jsonResponse = new JSONObject(response);
                        if (jsonResponse.getString("status").equals("success")) {
                            if (jsonResponse.getInt("total_data") > 0) {
                                String totalData = Integer.toString(jsonResponse.getInt("total_data"));
                                textView_ratingAuctioneer_rate.setText(jsonResponse.getString("rate_average"));
                                textView_ratingAuctioneer_total.setText(totalData);
                            }
                            else {
                                textView_ratingAuctioneer_rate.setText("0.0");
                                textView_ratingAuctioneer_total.setText("0");
                            }
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        };
    }
    private void setOnClickListener()
    {
        onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), DetailUserPublicActivity.class);
                intent.putExtra("id_user", auctioneerID);
                intent.putExtra("nama_user", auctioneerName);
                startActivity(intent);
            }
        };

        textView_namaAuctioneer.setOnClickListener(onClickListener);
    }
    public void setAuctioneerInfo(String auctioneerID, String auctioneerName, String auctioneerAvatarURL)
    {
        this.auctioneerID = auctioneerID;
        this.auctioneerName = auctioneerName;
        if (!auctioneerAvatarURL.equals("null"))
            this.auctioneerAvatarURL = "http://img-s7.lelangapa.com/"+auctioneerAvatarURL;
    }
    public void setAuctioneerInfo(DetailItemResources detailItemResources)
    {
        this.detailItemResources = detailItemResources;
    }
    private void setTextViewAuctioneerInformation()
    {
        textView_namaAuctioneer.setText(auctioneerName);
    }
    private void loadAuctioneerAvatar() {
        if (auctioneerAvatarURL != null) Picasso.with(getActivity()).load(auctioneerAvatarURL).into(imageView_auctioneerAvatar);
    }
    private void getAverageFeedbackData()
    {
        String auctioneerUrlParams = auctioneerID + "/ratings/as/auctioneer/average";
        String winnerUrlParams = auctioneerID + "/ratings/as/winner/average";

        FeedbackAverage.FeedbackAsAuctioneer feedbackAsAuctioneerAPI
                = FeedbackAverage.feedbackAsAuctioneerInstance(auctioneerUrlParams, dataReceiverFeedbackAsAuctioneer);
        FeedbackAverage.FeedbackAsWinner feedbackAsWinnerAPI
                = FeedbackAverage.feedbackAsWinnerInstance(winnerUrlParams, dataReceiverFeedbackAsWinner);

        RequestController.getInstance(getActivity()).addToRequestQueue(feedbackAsAuctioneerAPI);
        RequestController.getInstance(getActivity()).addToRequestQueue(feedbackAsWinnerAPI);
    }
}
