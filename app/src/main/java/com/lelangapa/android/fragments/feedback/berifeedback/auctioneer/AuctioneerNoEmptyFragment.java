package com.lelangapa.android.fragments.feedback.berifeedback.auctioneer;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.lelangapa.android.R;
import com.lelangapa.android.adapters.UserFeedbackAdapter;
import com.lelangapa.android.apicalls.feedback.berifeedback.BeriFeedbackAPI;
import com.lelangapa.android.apicalls.singleton.RequestController;
import com.lelangapa.android.interfaces.DataReceiver;
import com.lelangapa.android.interfaces.OnItemClickListener;
import com.lelangapa.android.listeners.RecyclerItemClickListener;
import com.lelangapa.android.resources.FeedbackResources;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by andre on 14/03/17.
 */

public class AuctioneerNoEmptyFragment extends Fragment {
    private ArrayList<FeedbackResources> listAuctioneerFeedback;
    private String userID;
    private UserFeedbackAdapter userFeedbackAdapter;
    private DataReceiver dataFeedbackReceived;
    private SwipeRefreshLayout swipeRefreshLayout;
    private RecyclerView recyclerView_auctioneerFeedback;

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setDataFeedbackReceived();
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_user_berifeedback_auctioneer_noempty_layout, container, false);
        initializeViews(view);
        setSwipeRefreshLayoutProperties();
        setRecyclerViewAdapter();
        setRecyclerViewProperties();
        return view;
    }

    private void initializeViews(View view)
    {
        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.fragment_berifeedback_auctioneer_noempty_swipeRefreshLayout);
        recyclerView_auctioneerFeedback = (RecyclerView) view.findViewById(R.id.fragment_berifeedback_auctioneer_recyclerview);
    }
    private void setSwipeRefreshLayoutProperties()
    {
        swipeRefreshLayout.setColorSchemeResources(android.R.color.holo_orange_dark);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getAuctioneerBeriFeedbackList();
            }
        });
    }
    public void setUserID(String userID)
    {
        this.userID = userID;
    }
    public void setListAuctioneerFeedback(ArrayList<FeedbackResources> list)
    {
        this.listAuctioneerFeedback = list;
    }
    private void setRecyclerViewAdapter()
    {
        userFeedbackAdapter = new UserFeedbackAdapter(getActivity(), listAuctioneerFeedback);
    }
    private void setRecyclerViewProperties()
    {
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity()) {
            @Override
            public boolean canScrollVertically() { return false; }
        };
        recyclerView_auctioneerFeedback.setLayoutManager(layoutManager);
        recyclerView_auctioneerFeedback.setItemAnimator(new DefaultItemAnimator());
        //recyclerView_auctioneerFeedback.addItemDecoration(new DividerItemDecoration(getActivity(), LinearLayout.VERTICAL));
        recyclerView_auctioneerFeedback.setAdapter(userFeedbackAdapter);
        recyclerView_auctioneerFeedback.addOnItemTouchListener(new RecyclerItemClickListener(getActivity(), recyclerView_auctioneerFeedback, new OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {

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
                        listAuctioneerFeedback.clear();
                        for (int i=0;i<jsonResponseArray.length();i++)
                        {
                            JSONObject jsonArrayObject = jsonResponseArray.getJSONObject(i);
                            FeedbackResources feedbackResources = new FeedbackResources();
                            feedbackResources.setIdRatinglogs(jsonArrayObject.getString("id_ratinglogs_return"));
                            feedbackResources.setIdItem(jsonArrayObject.getString("id_item_return"));
                            feedbackResources.setIdUser(jsonArrayObject.getString("id_user_auctioneer_return"));
                            feedbackResources.setNamaUser(jsonArrayObject.getString("nama_user_auctioneer"));
                            feedbackResources.setStatusRating(jsonArrayObject.getBoolean("status_rating_from_winner_return"));
                            feedbackResources.setNamaItem(jsonArrayObject.getString("nama_item_return"));
                            feedbackResources.setBidTime(jsonArrayObject.getInt("bid_time_item_return"));
                            feedbackResources.setStatusUser("auctioneer");
                            listAuctioneerFeedback.add(feedbackResources);
                        }
                        userFeedbackAdapter.updateDataset(listAuctioneerFeedback);
                        swipeRefreshLayout.setRefreshing(false);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };
    }
    private void getAuctioneerBeriFeedbackList()
    {
        BeriFeedbackAPI.GetFeedbackAuctioneerList feedbackAuctioneerList =
                BeriFeedbackAPI.instanceFeedbackAuctioneer(userID, dataFeedbackReceived);
        RequestController.getInstance(getActivity()).addToRequestQueue(feedbackAuctioneerList);
    }
}
