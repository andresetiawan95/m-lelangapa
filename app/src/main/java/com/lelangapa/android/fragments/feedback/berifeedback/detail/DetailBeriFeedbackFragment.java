package com.lelangapa.android.fragments.feedback.berifeedback.detail;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.lelangapa.android.R;
import com.lelangapa.android.apicalls.feedback.berifeedback.BeriFeedbackAPI;
import com.lelangapa.android.apicalls.singleton.RequestController;
import com.lelangapa.android.interfaces.DataReceiver;
import com.lelangapa.android.resources.FeedbackResources;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by andre on 16/03/17.
 */

public class DetailBeriFeedbackFragment extends Fragment {
    private TextView textView_statusUser, textView_namaUser, textView_namaItem;
    private GiveFeedbackFragment giveFeedbackFragment;
    private LoadingFeedbackFragment loadingFeedbackFragment;

    private FeedbackResources feedbackResources;
    private DataReceiver dataFeedbackReceiver;
    public DetailBeriFeedbackFragment()
    {
        initializeFragment();
    }
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        getDataFromActivityIntent();
        initializeDataFeedbackReceiver();
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_user_berifeedback_detail_layout, container, false);
        initializeViews(view);
        setupViews();
        setupInitialFragment();
        return view;
    }
    private void getDataFromActivityIntent()
    {
        feedbackResources = new FeedbackResources();
        Bundle extrasPassed = getActivity().getIntent().getExtras();
        feedbackResources.setIdRatinglogs(extrasPassed.getString("ratinglog_id"));
        feedbackResources.setNamaItem(extrasPassed.getString("item_name"));
        feedbackResources.setNamaUser(extrasPassed.getString("user_name"));
        feedbackResources.setStatusUser(extrasPassed.getString("status_user"));
        feedbackResources.setStatusRating(extrasPassed.getBoolean("status_rating_from_user"));
    }
    private void initializeViews(View view)
    {
        textView_statusUser = (TextView) view.findViewById(R.id.fragment_user_berifeedback_detail_status_user);
        textView_namaUser = (TextView) view.findViewById(R.id.fragment_user_berifeedback_detail_nama_user);
        textView_namaItem = (TextView) view.findViewById(R.id.fragment_user_berifeedback_detail_nama_item);
    }
    private void initializeFragment()
    {
        giveFeedbackFragment = new GiveFeedbackFragment();
        loadingFeedbackFragment = new LoadingFeedbackFragment();
    }
    private void initializeDataFeedbackReceiver()
    {
        dataFeedbackReceiver = new DataReceiver() {
            @Override
            public void dataReceived(Object output) {
                String response = output.toString();
                try {
                    JSONObject jsonResponse = new JSONObject(response);
                    if (jsonResponse.getString("status").equals("success")) {
                        JSONArray responseArray = jsonResponse.getJSONArray("data");
                        JSONObject responseArrayObject = responseArray.getJSONObject(0);
                        feedbackResources.setRateGiven(responseArrayObject.getInt("rate_return"));
                        if (!responseArrayObject.getString("rate_message_return").equals("null"))
                            feedbackResources.setRateMessage(responseArrayObject.getString("rate_message_return"));
                        else
                            feedbackResources.setRateMessage(null);
                        whenDataFeedbackAlreadyReceived();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };
    }
    private void whenDataFeedbackAlreadyReceived()
    {
        giveFeedbackFragment.setGivenFeedback(feedbackResources);
        setupFragment(giveFeedbackFragment);
    }
    private void setupViews()
    {
        if (feedbackResources.getStatusUser().equals("auctioneer")) {
            textView_statusUser.setText("Auctioneer");
            textView_statusUser.setBackgroundResource(R.color.feedbackStatusPelelang);
        }
        else {
            textView_statusUser.setText("Winner");
            textView_statusUser.setBackgroundResource(R.color.feedbackStatusPemenang);
        }
        textView_namaUser.setText(feedbackResources.getNamaUser());
        textView_namaItem.setText(feedbackResources.getNamaItem());
    }
    private void setupInitialFragment()
    {
        if (feedbackResources.isStatusRating()) {
            setupFragment(loadingFeedbackFragment);
            getGivenFeedbackData();
        }
        else {
            setupFragment(giveFeedbackFragment);
        }
    }
    private void setupFragment(Fragment fragment)
    {
        getFragmentManager().beginTransaction()
                .replace(R.id.fragment_user_berifeedback_detail_layout, fragment)
                .commit();
    }

    private void getGivenFeedbackData()
    {
        if (feedbackResources.getStatusUser().equals("auctioneer")) {
            BeriFeedbackAPI.GetFeedbackAuctioneerDetail detail
                    = BeriFeedbackAPI.instanceFeedbackAuctioneerDetail(feedbackResources.getIdRatinglogs(), dataFeedbackReceiver);
            RequestController.getInstance(getActivity()).addToRequestQueue(detail);
        }
        else {
            BeriFeedbackAPI.GetFeedbackWinnerDetail detail
                    = BeriFeedbackAPI.instanceFeedbackWinnerDetail(feedbackResources.getIdRatinglogs(), dataFeedbackReceiver);
            RequestController.getInstance(getActivity()).addToRequestQueue(detail);
        }
    }
}
