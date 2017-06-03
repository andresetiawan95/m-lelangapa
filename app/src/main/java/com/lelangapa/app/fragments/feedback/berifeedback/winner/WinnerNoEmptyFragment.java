package com.lelangapa.app.fragments.feedback.berifeedback.winner;

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
import com.lelangapa.app.activities.feedback.detailfeedback.DetailBeriFeedbackActivity;
import com.lelangapa.app.adapters.UserBeriFeedbackAdapter;
import com.lelangapa.app.apicalls.feedback.berifeedback.BeriFeedbackAPI;
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
 * Created by andre on 14/03/17.
 */

public class WinnerNoEmptyFragment extends Fragment {
    private ArrayList<FeedbackResources> listWinnerFeedback;
    private String userID;
    private UserBeriFeedbackAdapter userBeriFeedbackAdapter;
    private DataReceiver dataFeedbackReceived;
    private SwipeRefreshLayout swipeRefreshLayout;
    private RecyclerView recyclerView_winnerFeedback;

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
        View view = inflater.inflate(R.layout.fragment_user_berifeedback_winner_noempty_layout, container, false);
        initializeViews(view);
        setSwipeRefreshLayoutProperties();
        setRecyclerViewAdapter();
        setRecyclerViewProperties();
        return view;
    }
    private void initializeConstants()
    {
        intent = new Intent(getActivity(), DetailBeriFeedbackActivity.class);
    }
    private void initializeViews(View view)
    {
        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.fragment_berifeedback_winner_noempty_swipeRefreshLayout);
        recyclerView_winnerFeedback = (RecyclerView) view.findViewById(R.id.fragment_berifeedback_winner_noempty_recyclerview);
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
    public void setListWinnerFeedback(ArrayList<FeedbackResources> list)
    {
        this.listWinnerFeedback = list;
    }
    private void setRecyclerViewAdapter()
    {
        userBeriFeedbackAdapter = new UserBeriFeedbackAdapter(getActivity(), listWinnerFeedback);
    }
    private void setRecyclerViewProperties()
    {
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        /*{
            @Override
            public boolean canScrollVertically() { return false; }
        };*/
        recyclerView_winnerFeedback.setLayoutManager(layoutManager);
        recyclerView_winnerFeedback.setItemAnimator(new DefaultItemAnimator());
        recyclerView_winnerFeedback.addItemDecoration(new DividerItemDecoration(getActivity(), LinearLayout.VERTICAL));
        recyclerView_winnerFeedback.setAdapter(userBeriFeedbackAdapter);
        recyclerView_winnerFeedback.addOnItemTouchListener(new RecyclerItemClickListener(getActivity(), recyclerView_winnerFeedback, new OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                bundleExtras = new Bundle();
                bundleExtras.putString("ratinglog_id", listWinnerFeedback.get(position).getIdRatinglogs());
                bundleExtras.putString("user_name", listWinnerFeedback.get(position).getNamaUser());
                bundleExtras.putString("user_id", listWinnerFeedback.get(position).getIdUser());
                bundleExtras.putString("rater_id", userID);
                bundleExtras.putString("item_name", listWinnerFeedback.get(position).getNamaItem());
                bundleExtras.putString("item_id", listWinnerFeedback.get(position).getIdItem());
                bundleExtras.putInt("item_bid_time", listWinnerFeedback.get(position).getBidTime());
                bundleExtras.putString("status_user", listWinnerFeedback.get(position).getStatusUser());
                bundleExtras.putBoolean("status_rating_from_user", listWinnerFeedback.get(position).isStatusRating());
                intent.putExtras(bundleExtras);
                startActivity(intent);
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
                        listWinnerFeedback.clear();
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
                            listWinnerFeedback.add(feedbackResources);
                        }
                        userBeriFeedbackAdapter.updateDataset(listWinnerFeedback);
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
        BeriFeedbackAPI.GetFeedbackWinnerList feedbackWinnerList =
                BeriFeedbackAPI.instanceFeedbackWinner(userID, dataFeedbackReceived);
        RequestController.getInstance(getActivity()).addToRequestQueue(feedbackWinnerList);
    }
}
