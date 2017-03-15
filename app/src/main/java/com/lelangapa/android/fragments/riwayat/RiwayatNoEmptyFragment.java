package com.lelangapa.android.fragments.riwayat;

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

import com.lelangapa.android.R;
import com.lelangapa.android.activities.riwayat.detailriwayat.DetailRiwayatActivity;
import com.lelangapa.android.adapters.UserRiwayatAdapter;
import com.lelangapa.android.apicalls.riwayat.RiwayatAPI;
import com.lelangapa.android.apicalls.singleton.RequestController;
import com.lelangapa.android.interfaces.DataReceiver;
import com.lelangapa.android.interfaces.OnItemClickListener;
import com.lelangapa.android.listeners.RecyclerItemClickListener;
import com.lelangapa.android.resources.RiwayatResources;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by andre on 10/03/17.
 */

public class RiwayatNoEmptyFragment extends Fragment {
    private ArrayList<RiwayatResources> listRiwayat;
    private String userID;
    private UserRiwayatAdapter riwayatAdapter;
    private DataReceiver dataRiwayatReceived;
    private SwipeRefreshLayout swipeRefreshLayout;
    private RecyclerView recyclerView_riwayat;

    private Intent intent;
    private Bundle bundleExtras;

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setDataReceiverForRiwayat();
        setIntentOnDetailRiwayat();
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_user_riwayat_noempty_layout, container, false);
        initializeViews(view);
        setSwipeRefreshLayoutProperties();
        setRecyclerViewAdapter();
        setRecyclerViewProperties();
        return view;
    }
    private void initializeViews(View view)
    {
        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.fragment_user_riwayat_noempty_swipeRefreshLayout);
        recyclerView_riwayat = (RecyclerView) view.findViewById(R.id.fragment_user_riwayat_layout_recyclerview);
    }
    private void setIntentOnDetailRiwayat()
    {
        intent = new Intent(getActivity(), DetailRiwayatActivity.class);
    }
    public void setRiwayatList(ArrayList<RiwayatResources> listRiwayat)
    {
        this.listRiwayat = listRiwayat;
    }
    public void setUserID(String userID)
    {
        this.userID = userID;
    }
    private void setRecyclerViewAdapter()
    {
        riwayatAdapter = new UserRiwayatAdapter(getActivity(), listRiwayat);
    }
    private void setRecyclerViewProperties()
    {
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity()) {
            @Override
            public boolean canScrollVertically() { return false; }
        };
        recyclerView_riwayat.setLayoutManager(layoutManager);
        recyclerView_riwayat.setItemAnimator(new DefaultItemAnimator());
        recyclerView_riwayat.setAdapter(riwayatAdapter);
        recyclerView_riwayat.addOnItemTouchListener(new RecyclerItemClickListener(getActivity(), recyclerView_riwayat, new OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                bundleExtras = new Bundle();
                bundleExtras.putString("user_id_loggedin", userID);
                bundleExtras.putString("item_id", listRiwayat.get(position).getIdItem());
                bundleExtras.putString("item_name", listRiwayat.get(position).getNamaItem());
                bundleExtras.putString("auctioneer_name", listRiwayat.get(position).getNamaAuctioneer());
                bundleExtras.putInt("bid_time", listRiwayat.get(position).getBidTime());
                intent.putExtras(bundleExtras);
                startActivity(intent);
            }

            @Override
            public void onLongItemClick(View view, int position) {

            }
        }));
    }
    private void setSwipeRefreshLayoutProperties()
    {
        swipeRefreshLayout.setColorSchemeResources(android.R.color.holo_orange_dark);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getRiwayat(userID);
            }
        });
    }

    private void setDataReceiverForRiwayat()
    {
        dataRiwayatReceived = new DataReceiver() {
            @Override
            public void dataReceived(Object output) {
                String response = output.toString();
                try {
                    JSONObject jsonResponse = new JSONObject(response);
                    JSONArray responseArray = jsonResponse.getJSONArray("data");
                    listRiwayat.clear();
                    for (int i=0;i<responseArray.length();i++)
                    {
                        JSONObject jsonArrayObject = responseArray.getJSONObject(i);
                        RiwayatResources riwayatResources = new RiwayatResources();
                        riwayatResources.setIdBid(jsonArrayObject.getString("id_bid_return"));
                        riwayatResources.setIdItem(jsonArrayObject.getString("id_item_return"));
                        riwayatResources.setIdAuctioneer(jsonArrayObject.getString("id_user_return"));
                        riwayatResources.setNamaAuctioneer(jsonArrayObject.getString("nama_user_return"));
                        riwayatResources.setNamaItem(jsonArrayObject.getString("nama_item_return"));
                        riwayatResources.setBidStatus(jsonArrayObject.getInt("bid_status_return"));
                        riwayatResources.setBidTime(jsonArrayObject.getInt("bid_time_return"));
                        riwayatResources.setWinStatus(jsonArrayObject.getBoolean("win_status_return"));
                        riwayatResources.setHargaBid(jsonArrayObject.getString("price_bid_return"));
                        riwayatResources.setBidStatus(jsonArrayObject.getInt("bid_status_return"));
                        listRiwayat.add(riwayatResources);
                    }
                    riwayatAdapter.updateDataset(listRiwayat);
                    swipeRefreshLayout.setRefreshing(false);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };
    }
    private void getRiwayat(String userID)
    {
        RiwayatAPI.GetRiwayat getRiwayatAPI = RiwayatAPI.initializeGetRiwayat(userID, dataRiwayatReceived);
        RequestController.getInstance(getActivity()).addToRequestQueue(getRiwayatAPI);
    }
}
