package com.lelangapa.app.fragments.feedback.berifeedback.winner;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.lelangapa.app.R;
import com.lelangapa.app.apicalls.feedback.berifeedback.BeriFeedbackAPI;
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
 * Created by andre on 10/03/17.
 */

public class WinnerFragment extends Fragment {
    private ArrayList<FeedbackResources> listFeedbackWinner;

    private SwipeRefreshLayout swipeRefreshLayout;
    private SessionManager sessionManager;
    private HashMap<String, String> sessionMap;
    private String userID;
    private DataReceiver dataReceiver, dataReceived;

    private WinnerEmptyFragment emptyFragment;
    private WinnerNoEmptyFragment noEmptyFragment;

    public WinnerFragment()
    {
        this.listFeedbackWinner = new ArrayList<>();
    }
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        initializeSessions();
        initializeChildFragments();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_user_berifeedback_winner_layout, container, false);
        initializeViews(view);
        initializeDataReceivers();
        setSwipeRefreshLayoutProperties();
        return view;
    }

    private void initializeViews(View view)
    {
        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.fragment_berifeedback_winner_layout_swipeRefreshLayout);
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
                        if (statusResponse.equals("success"))
                        {
                            JSONArray jsonResponseArray = jsonResponse.getJSONArray("data");
                            listFeedbackWinner.clear();
                            for (int i=0;i<jsonResponseArray.length();i++)
                            {
                                JSONObject jsonArrayObject = jsonResponseArray.getJSONObject(i);
                                FeedbackResources feedbackResources = new FeedbackResources();
                                feedbackResources.setIdRatinglogs(jsonArrayObject.getString("id_ratinglogs_return"));
                                feedbackResources.setIdItem(jsonArrayObject.getString("id_item_return"));
                                feedbackResources.setIdUser(jsonArrayObject.getString("id_user_winner_return"));
                                feedbackResources.setNamaUser(jsonArrayObject.getString("nama_user_winner"));
                                feedbackResources.setStatusRating(jsonArrayObject.getBoolean("status_rating_from_auctioneer_return"));
                                feedbackResources.setNamaItem(jsonArrayObject.getString("nama_item_return"));
                                feedbackResources.setBidTime(jsonArrayObject.getInt("bid_time_item_return"));
                                feedbackResources.setStatusUser("winner");
                                listFeedbackWinner.add(feedbackResources);
                            }
                            Log.v("SIZE LIST WINNER", Integer.toString(listFeedbackWinner.size()));
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
                    if (listFeedbackWinner.isEmpty()) {
                        getFragmentManager().beginTransaction()
                                .replace(R.id.fragment_berifeedback_winner_layout, emptyFragment)
                                .commit();
                    }
                    else {
                        swipeRefreshLayout.setEnabled(false);
                        noEmptyFragment.setUserID(userID);
                        noEmptyFragment.setListWinnerFeedback(listFeedbackWinner);
                        getFragmentManager().beginTransaction()
                                .replace(R.id.fragment_berifeedback_winner_layout, noEmptyFragment)
                                .commit();
                    }
                }
            }
        };
    }
    private void initializeChildFragments()
    {
        emptyFragment = new WinnerEmptyFragment();
        noEmptyFragment = new WinnerNoEmptyFragment();
    }

    private void setSwipeRefreshLayoutProperties()
    {
        swipeRefreshLayout.setColorSchemeResources(android.R.color.holo_orange_dark);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                //Log.v("RefreshToggle", "Refresh Toggle on FavoriteFragment");
                getAuctioneerBeriFeedbackList();
            }
        });
        swipeRefreshLayout.post(new Runnable() {
            @Override
            public void run() {
                //Log.v("RefreshPost", "Refresh POST on FavoriteFragment");
                swipeRefreshLayout.setRefreshing(true);
                getAuctioneerBeriFeedbackList();
            }
        });
    }
    private void getAuctioneerBeriFeedbackList()
    {
        BeriFeedbackAPI.GetFeedbackWinnerList feedbackWinnerList =
                BeriFeedbackAPI.instanceFeedbackWinner(userID, dataReceiver);
        RequestController.getInstance(getActivity()).addToRequestQueue(feedbackWinnerList);
    }
}
