package com.lelangapa.app.fragments.riwayat;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.lelangapa.app.R;
import com.lelangapa.app.apicalls.riwayat.RiwayatAPI;
import com.lelangapa.app.apicalls.singleton.RequestController;
import com.lelangapa.app.interfaces.DataReceiver;
import com.lelangapa.app.preferences.SessionManager;
import com.lelangapa.app.resources.RiwayatResources;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by andre on 28/02/17.
 */

public class RiwayatFragment extends Fragment {
    private ArrayList<RiwayatResources> listRiwayat;
    private String userID;
    private HashMap<String, String> userSession;
    private DataReceiver receiver, dataRiwayatReceived;

    private SwipeRefreshLayout swipeRefreshLayout;
    private SessionManager sessionManager;
    private RiwayatEmptyFragment emptyFragment;
    private RiwayatNoEmptyFragment noEmptyFragment;

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        initializeConstant();
        initializeSession();
        initializeChildFragments();
        setDataReceiverForRiwayat();
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_user_riwayat_layout, container, false);
        initializeViews(view);
        setSwipeRefreshLayoutProperties();
        initializeWhenDataAlreadyReceived();
        return view;
    }

    private void initializeConstant()
    {
        listRiwayat = new ArrayList<>();
    }
    private void initializeSession()
    {
        sessionManager = new SessionManager(getActivity());
        if (sessionManager.isLoggedIn())
        {
            userSession = sessionManager.getSession();
            userID = userSession.get(sessionManager.getKEY_ID());
        }
    }
    private void initializeViews(View view)
    {
        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.fragment_user_riwayat_swipeRefreshLayout);
    }
    private void initializeChildFragments()
    {
        emptyFragment = new RiwayatEmptyFragment();
        noEmptyFragment = new RiwayatNoEmptyFragment();
    }

    private void initializeWhenDataAlreadyReceived()
    {
        receiver = new DataReceiver() {
            @Override
            public void dataReceived(Object output) {
                String response = output.toString();
                if (response.equals("done"))
                {
                    swipeRefreshLayout.setRefreshing(false);
                    if (listRiwayat.isEmpty()){
                        getFragmentManager().beginTransaction()
                                .replace(R.id.fragment_activity_riwayat_layout, emptyFragment)
                                .commit();
                    }
                    else {
                        swipeRefreshLayout.setEnabled(false);
                        noEmptyFragment.setUserID(userID);
                        noEmptyFragment.setRiwayatList(listRiwayat);
                        getFragmentManager().beginTransaction()
                                .replace(R.id.fragment_user_riwayat_layout, noEmptyFragment)
                                .commit();
                    }
                }
            }
        };
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
        swipeRefreshLayout.post(new Runnable() {
            @Override
            public void run() {
                swipeRefreshLayout.setRefreshing(true);
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
                    receiver.dataReceived("done");
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
