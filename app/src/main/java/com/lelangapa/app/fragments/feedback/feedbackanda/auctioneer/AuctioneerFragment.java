package com.lelangapa.app.fragments.feedback.feedbackanda.auctioneer;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.lelangapa.app.R;
import com.lelangapa.app.apicalls.feedback.feedbackanda.FeedbackAndaAPI;
import com.lelangapa.app.apicalls.singleton.RequestController;
import com.lelangapa.app.interfaces.DataReceiver;
import com.lelangapa.app.preferences.SessionManager;
import com.lelangapa.app.resources.FeedbackResources;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by andre on 18/03/17.
 */

public class AuctioneerFragment extends Fragment {
    private ArrayList<FeedbackResources> listFeedbackAuctioneer;

    private SwipeRefreshLayout swipeRefreshLayout;
    private SessionManager sessionManager;
    private HashMap<String, String> sessionMap;
    private String userID, urlparams;
    private DataReceiver dataReceiver, dataReceived;

    private AuctioneerEmptyFragment emptyFragment;
    private AuctioneerNoEmptyFragment noEmptyFragment;

    public AuctioneerFragment()
    {
        listFeedbackAuctioneer = new ArrayList<>();
    }
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        initializeSessions();
        initializeUrlParams();
        initializeChildFragments();
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_user_feedbackanda_auctioneer_layout, container, false);
        initializeViews(view);
        initializeDataReceivers();
        setSwipeRefreshLayoutProperties();
        return view;
    }
    private void initializeUrlParams()
    {
        urlparams = userID + "/ratings/auctioneer";
    }
    private void initializeViews(View view)
    {
        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.fragment_feedbackanda_auctioneer_layout_swipeRefreshLayout);
    }
    private void initializeSessions()
    {
        sessionManager = new SessionManager(getActivity());
        if (sessionManager.isLoggedIn())
        {
            sessionMap = sessionManager.getSession();
            userID = sessionMap.get(sessionManager.getKEY_ID());
        }
    }
    private void initializeDataReceivers()
    {
        dataReceiver = new DataReceiver() {
            @Override
            public void dataReceived(Object output) {
                if (isResumed()) {
                    String response = output.toString();
                    try {
                        JSONObject jsonResponse = new JSONObject(response);
                        String statusResponse = jsonResponse.getString("status");
                        //Log.v("RESPONSE", jsonResponse.toString());
                        if (statusResponse.equals("success"))
                        {
                            JSONArray jsonResponseArray = jsonResponse.getJSONArray("data");
                            listFeedbackAuctioneer.clear();
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
                                feedbackResources.setStatusUser("winner");
                                listFeedbackAuctioneer.add(feedbackResources);
                            }
                            dataReceived.dataReceived("done");
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        };
        dataReceived = new DataReceiver() {
            @Override
            public void dataReceived(Object output) {
                String response = output.toString();
                if (response.equals("done"))
                {
                    swipeRefreshLayout.setRefreshing(false);
                    if (listFeedbackAuctioneer.isEmpty()) {
                        getFragmentManager().beginTransaction()
                                .replace(R.id.fragment_feedbackanda_auctioneer_layout, emptyFragment)
                                .commit();
                    }
                    else {
                        swipeRefreshLayout.setEnabled(false);
                        noEmptyFragment.setUserID(userID);
                        noEmptyFragment.setListFeedback(listFeedbackAuctioneer);
                        getFragmentManager().beginTransaction()
                                .replace(R.id.fragment_feedbackanda_auctioneer_layout, noEmptyFragment)
                                .commit();
                    }
                }
            }
        };
    }
    private void initializeChildFragments()
    {
        emptyFragment = new AuctioneerEmptyFragment();
        noEmptyFragment = new AuctioneerNoEmptyFragment();
    }

    private void setSwipeRefreshLayoutProperties()
    {
        swipeRefreshLayout.setColorSchemeResources(android.R.color.holo_orange_dark);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                //Log.v("RefreshToggle", "Refresh Toggle on FavoriteFragment");
                getFeedbackAndaAsAuctioneerList();
            }
        });
        swipeRefreshLayout.post(new Runnable() {
            @Override
            public void run() {
                //Log.v("RefreshPost", "Refresh POST on FavoriteFragment");
                swipeRefreshLayout.setRefreshing(true);
                getFeedbackAndaAsAuctioneerList();
            }
        });
    }
    private void getFeedbackAndaAsAuctioneerList()
    {
        FeedbackAndaAPI.GetFeedbackAndaAsAuctioneer feedback =
                FeedbackAndaAPI.instanceFeedbackAndaAsAuctioneer(urlparams, dataReceiver);
        RequestController.getInstance(getActivity()).addToRequestQueue(feedback);
    }
}
