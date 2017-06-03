package com.lelangapa.app.fragments.userpublic.riwayat;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.lelangapa.app.R;
import com.lelangapa.app.apicalls.singleton.RequestController;
import com.lelangapa.app.apicalls.userpublic.UserPublicAPI;
import com.lelangapa.app.interfaces.DataReceiver;
import com.lelangapa.app.resources.RiwayatResources;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by andre on 29/03/17.
 */

public class RiwayatMainFragment extends Fragment {
    private ArrayList<RiwayatResources> listRiwayat;
    private String userID;
    private DataReceiver dataReceiver;

    private ProgressBar progressBar_loadingData;

    private EmptyFragment emptyFragment;
    private NoEmptyFragment noEmptyFragment;
    public RiwayatMainFragment()
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
        View view = inflater.inflate(R.layout.fragment_detail_user_public_riwayat_layout, container, false);
        initializeViews(view);
        getIntentData();
        getRiwayatItems();
        return view;
    }
    private void initializeConstants()
    {
        listRiwayat = new ArrayList<>();
    }
    private void getIntentData()
    {
        userID = getActivity().getIntent().getStringExtra("id_user");
    }
    private void initializeViews(View view)
    {
        progressBar_loadingData = (ProgressBar) view.findViewById(R.id.fragment_detail_user_public_riwayat_layout_progress_bar);
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
                    setupFragment();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };
    }
    private void getAndRemoveFragment()
    {
        Fragment currentFragment = getChildFragmentManager().findFragmentById(R.id.fragment_detail_user_public_riwayat_layout);
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
        if (listRiwayat.isEmpty())
        {
            getChildFragmentManager().beginTransaction()
                    .replace(R.id.fragment_detail_user_public_riwayat_layout, emptyFragment)
                    .commit();
        }
        else
        {
            noEmptyFragment.setListRiwayat(listRiwayat);
            getChildFragmentManager().beginTransaction()
                    .replace(R.id.fragment_detail_user_public_riwayat_layout, noEmptyFragment)
                    .commit();
        }
    }
    private void getRiwayatItems()
    {
        getAndRemoveFragment();
        enableProgressBar();
        UserPublicAPI.GetRiwayatAPI getRiwayatAPI = UserPublicAPI.instanceRiwayatAPI(userID, dataReceiver);
        RequestController.getInstance(getActivity()).addToRequestQueue(getRiwayatAPI);
    }
}
