package com.lelangapa.android.fragments.userpublic.feedback;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ProgressBar;
import android.widget.Spinner;

import com.lelangapa.android.R;
import com.lelangapa.android.apicalls.singleton.RequestController;
import com.lelangapa.android.apicalls.userpublic.UserPublicAPI;
import com.lelangapa.android.interfaces.DataReceiver;
import com.lelangapa.android.resources.FeedbackResources;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by andre on 28/03/17.
 */

public class FeedbackMainFragment extends Fragment {
    private Spinner spinner_feedback;
    private ProgressBar progressBar_loadingData;

    private EmptyFragment emptyFragment;
    private NoEmptyFragment noEmptyFragment;

    private String userID, urlParams;
    private ArrayList<FeedbackResources> listFeedback;
    private DataReceiver dataReceiver;
    private AdapterView.OnItemSelectedListener onItemSelectedListener;
    public FeedbackMainFragment()
    {
        initializeConstants();
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        initializeFragments();
        initializeDataReceiver();
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_detail_user_public_feedback_layout, container, false);
        initializeViews(view);
        getIntentData();
        setSpinnerItemSelectedListener();
        setSpinnerProperties();
        return view;
    }
    private void initializeConstants()
    {
        listFeedback = new ArrayList<>();
    }
    private void initializeViews(View view)
    {
        spinner_feedback = (Spinner) view.findViewById(R.id.fragment_detail_user_public_feedback_spinner);
        progressBar_loadingData = (ProgressBar) view.findViewById(R.id.fragment_detail_user_public_feedback_layout_progress_bar);
    }
    private void getIntentData()
    {
        userID = getActivity().getIntent().getStringExtra("id_user");
    }
    private void setSpinnerProperties()
    {
        spinner_feedback.setOnItemSelectedListener(onItemSelectedListener);
    }
    private void setSpinnerItemSelectedListener()
    {
        onItemSelectedListener = new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                getFeedback(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        };
    }
    private void initializeFragments()
    {
        emptyFragment = new EmptyFragment();
        noEmptyFragment = new NoEmptyFragment();
    }
    private void initializeDataReceiver()
    {
        dataReceiver = new DataReceiver() {
            @Override
            public void dataReceived(Object output) {
                String response = output.toString();
                try {
                    JSONObject jsonResponse = new JSONObject(response);
                    JSONArray jsonResponseArray = jsonResponse.getJSONArray("data");
                    listFeedback.clear();
                    for (int i=0;i<jsonResponseArray.length();i++)
                    {
                        JSONObject jsonArrayObject = jsonResponseArray.getJSONObject(i);
                        FeedbackResources feedbackResources = new FeedbackResources();
                        feedbackResources.setIdRating(jsonArrayObject.getString("id_rating_return"));
                        feedbackResources.setIdItem(jsonArrayObject.getString("id_item_return"));
                        feedbackResources.setIdUser(jsonArrayObject.getString("id_rater_return"));
                        feedbackResources.setNamaUser(jsonArrayObject.getString("nama_rater_return"));
                        feedbackResources.setNamaItem(jsonArrayObject.getString("nama_item_return"));
                        feedbackResources.setBidTime(jsonArrayObject.getInt("bid_time_return"));
                        feedbackResources.setRateGiven(jsonArrayObject.getInt("rate"));
                        feedbackResources.setRateMessage(jsonArrayObject.getString("rate_message"));
                        listFeedback.add(feedbackResources);
                    }
                    setupFragment();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };
    }
    private void getFeedback(int position)
    {
        getAndRemoveFragment();
        enableProgressBar();
        if (position == 0) {
            urlParams = userID + "/ratings/as/winner";
            UserPublicAPI.GetFeedbackAsWinnerAPI winnerAPI = UserPublicAPI.instanceFeedbackWinner(urlParams, dataReceiver);
            RequestController.getInstance(getActivity()).addToRequestQueue(winnerAPI);
        }
        else {
            urlParams = userID + "/ratings/as/auctioneer";
            UserPublicAPI.GetFeedbackAsAuctioneerAPI auctioneerAPI =
                    UserPublicAPI.instanceFeedbackAuctioneer(urlParams, dataReceiver);
            RequestController.getInstance(getActivity()).addToRequestQueue(auctioneerAPI);
        }
    }
    private void getAndRemoveFragment()
    {
        Fragment currentFragment = getChildFragmentManager().findFragmentById(R.id.fragment_detail_user_public_feedback_layout);
        if (currentFragment != null) {
            getChildFragmentManager().beginTransaction()
                    .remove(currentFragment)
                    .commit();
        }
    }
    private void enableProgressBar()
    {
        this.progressBar_loadingData.setVisibility(View.VISIBLE);
    }
    private void disableProgressBar()
    {
        this.progressBar_loadingData.setVisibility(View.GONE);
    }
    private void setupFragment()
    {
        disableProgressBar();
        if (listFeedback.isEmpty()) {
            getChildFragmentManager().beginTransaction()
                    .replace(R.id.fragment_detail_user_public_feedback_layout, emptyFragment)
                    .commit();
        }
        else {
            noEmptyFragment.setListFeedback(listFeedback);
            getChildFragmentManager().beginTransaction()
                    .replace(R.id.fragment_detail_user_public_feedback_layout, noEmptyFragment)
                    .commit();
        }
    }
}
