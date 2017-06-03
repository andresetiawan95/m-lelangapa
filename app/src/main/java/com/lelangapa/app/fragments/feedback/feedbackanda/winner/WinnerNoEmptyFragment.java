package com.lelangapa.app.fragments.feedback.feedbackanda.winner;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.lelangapa.app.R;
import com.lelangapa.app.adapters.UserFeedbackAndaAdapter;
import com.lelangapa.app.apicalls.feedback.feedbackanda.FeedbackAndaAPI;
import com.lelangapa.app.apicalls.singleton.RequestController;
import com.lelangapa.app.decorations.DividerItemDecoration;
import com.lelangapa.app.interfaces.DataReceiver;
import com.lelangapa.app.interfaces.OnItemClickListener;
import com.lelangapa.app.listeners.RecyclerItemClickListener;
import com.lelangapa.app.resources.FeedbackResources;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by andre on 18/03/17.
 */

public class WinnerNoEmptyFragment extends Fragment {
    private ArrayList<FeedbackResources> listFeedback;
    private String userID, urlparams;
    private UserFeedbackAndaAdapter userFeedbackAndaAdapter;
    private DataReceiver dataFeedbackReceived;
    private SwipeRefreshLayout swipeRefreshLayout;
    private RecyclerView recyclerView_feedback;

    private Intent intent;
    private Bundle bundleExtras;

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        initializeConstants();
        setDataFeedbackReceived();
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_user_feedbackanda_winner_noempty_layout, container, false);
        initializeViews(view);
        setSwipeRefreshLayoutProperties();
        setRecyclerViewAdapter();
        setRecyclerViewProperties();
        return view;
    }
    private void initializeConstants()
    {
        //intent = new Intent(getActivity(), DetailBeriFeedbackActivity.class);
        urlparams = userID + "/ratings/winner";
    }
    private void initializeViews(View view)
    {
        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.fragment_feedbackanda_winner_noempty_swipeRefreshLayout);
        recyclerView_feedback = (RecyclerView) view.findViewById(R.id.fragment_feedbackanda_winner_noempty_recyclerview);
    }
    private void setSwipeRefreshLayoutProperties()
    {
        swipeRefreshLayout.setColorSchemeResources(android.R.color.holo_orange_dark);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getFeedbackAsWinnerList();
            }
        });
    }
    public void setUserID(String userID)
    {
        this.userID = userID;
    }
    public void setListFeedback(ArrayList<FeedbackResources> list)
    {
        this.listFeedback = list;
    }
    private void setRecyclerViewAdapter()
    {
        userFeedbackAndaAdapter = new UserFeedbackAndaAdapter(getActivity(), listFeedback);
    }
    private void setRecyclerViewProperties()
    {
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        /*{
            @Override
            public boolean canScrollVertically() { return false; }
        };*/
        recyclerView_feedback.setLayoutManager(layoutManager);
        recyclerView_feedback.setItemAnimator(new DefaultItemAnimator());
        recyclerView_feedback.addItemDecoration(new DividerItemDecoration(getActivity(), LinearLayout.VERTICAL));
        recyclerView_feedback.setAdapter(userFeedbackAndaAdapter);
        recyclerView_feedback.addOnItemTouchListener(new RecyclerItemClickListener(getActivity(), recyclerView_feedback, new OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                /*bundleExtras = new Bundle();
                bundleExtras.putString("ratinglog_id", listFeedback.get(position).getIdRatinglogs());
                bundleExtras.putString("user_name", listFeedback.get(position).getNamaUser());
                bundleExtras.putString("user_id", listFeedback.get(position).getIdUser());
                bundleExtras.putString("rater_id", userID);
                bundleExtras.putString("item_name", listFeedback.get(position).getNamaItem());
                bundleExtras.putString("item_id", listFeedback.get(position).getIdItem());
                bundleExtras.putInt("item_bid_time", listFeedback.get(position).getBidTime());
                bundleExtras.putString("status_user", listFeedback.get(position).getStatusUser());
                bundleExtras.putBoolean("status_rating_from_user", listFeedback.get(position).isStatusRating());
                intent.putExtras(bundleExtras);
                startActivity(intent);*/
            }

            @Override
            public void onLongItemClick(View view, int position) {

            }
        }));

    }
    private void setDataFeedbackReceived()
    {
        dataFeedbackReceived = new DataReceiver() {
            @Override
            public void dataReceived(Object output) {
                String response = output.toString();
                try {
                    JSONObject jsonResponse = new JSONObject(response);
                    String statusResponse = jsonResponse.getString("status");
                    if (statusResponse.equals("success"))
                    {
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
                            feedbackResources.setStatusUser("auctioneer");
                            listFeedback.add(feedbackResources);
                        }
                        userFeedbackAndaAdapter.updateDataset(listFeedback);
                        swipeRefreshLayout.setRefreshing(false);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };
    }
    private void getFeedbackAsWinnerList()
    {
        FeedbackAndaAPI.GetFeedbackAndaAsWinner feedback =
                FeedbackAndaAPI.instanceFeedbackAndaAsWinner(urlparams, dataFeedbackReceived);
        RequestController.getInstance(getActivity()).addToRequestQueue(feedback);
    }
}
