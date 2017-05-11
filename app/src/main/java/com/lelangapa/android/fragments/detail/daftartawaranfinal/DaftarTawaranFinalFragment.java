package com.lelangapa.android.fragments.detail.daftartawaranfinal;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.lelangapa.android.R;
import com.lelangapa.android.apicalls.detail.daftartawaran.DaftarTawaranAPI;
import com.lelangapa.android.apicalls.singleton.RequestController;
import com.lelangapa.android.interfaces.DataReceiver;
import com.lelangapa.android.resources.BiddingResources;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by andre on 11/05/17.
 */

public class DaftarTawaranFinalFragment extends Fragment {
    private ArrayList<BiddingResources> listOffer;
    private String itemID;

    private SwipeRefreshLayout swipeRefreshLayout;

    private BiddingResources leadBidder;
    private DataReceiver whenOfferListLoaded;

    private AfterChosenFragment afterChosenFragment;
    private BeforeChosenFragment beforeChosenFragment;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_daftar_tawaran_final_layout, container, false);
        initializeConstants();
        initializeFragments();
        getIntentData();
        initializeViews(view);
        initializeDataReceivers();
        setSwipeRefreshLayoutProperties();
        return view;
    }
    private void initializeConstants() {
        listOffer = new ArrayList<>();
        leadBidder = new BiddingResources();
    }
    private void getIntentData() {
        Bundle bundleExtras = getActivity().getIntent().getExtras();
        itemID = bundleExtras.getString("id_item");
        leadBidder.setIdBid(bundleExtras.getString("id_bid"));
        leadBidder.setIdBidder(bundleExtras.getString("id_bidder"));
        leadBidder.setHargaBid(bundleExtras.getString("harga_bid"));
        leadBidder.setNamaBidder(bundleExtras.getString("nama_bidder"));
        leadBidder.setWinnerStatus(bundleExtras.getBoolean("winner_status"));
    }
    private void initializeViews(View view) {
        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.fragment_daftar_tawaran_layout_swipeRefreshLayout);
    }
    private void initializeFragments() {
        afterChosenFragment = new AfterChosenFragment();
        beforeChosenFragment = new BeforeChosenFragment();
    }
    private void initializeDataReceivers() {
        whenOfferListLoaded = new DataReceiver() {
            @Override
            public void dataReceived(Object output) {
                String response = output.toString();
                try {
                    JSONObject jsonResponse = new JSONObject(response);
                    JSONArray responseArray = jsonResponse.getJSONArray("data");
                    listOffer.clear();
                    for (int i=0;i<responseArray.length();i++) {
                        JSONObject arrayObject = responseArray.getJSONObject(i);
                        BiddingResources biddingResources = new BiddingResources();
                        biddingResources.setIdBid(arrayObject.getString("bid_id"));
                        biddingResources.setNamaBidder(arrayObject.getString("user_name"));
                        biddingResources.setHargaBid(arrayObject.getString("bid_price"));
                        biddingResources.setIdBidder(arrayObject.getString("user_id"));
                        listOffer.add(biddingResources);
                    }
                    whenOfferListLoaded();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };
    }
    private void setSwipeRefreshLayoutProperties() {
        swipeRefreshLayout.post(new Runnable() {
            @Override
            public void run() {
                swipeRefreshLayout.setRefreshing(true);
                getOfferListFromServer();
            }
        });
    }
    private void whenOfferListLoaded() {
        swipeRefreshLayout.setRefreshing(false);
        swipeRefreshLayout.setEnabled(false);
        if (leadBidder.isWinnerStatus()) {
            //ke after chosen fragment
            setWinnerOnOfferList();
            afterChosenFragment.setListOffer(listOffer);
            afterChosenFragment.setLeadBidder(leadBidder);
            getFragmentManager().beginTransaction()
                    .replace(R.id.fragment_daftar_tawaran_final_layout, afterChosenFragment)
                    .commit();
        }
        else {
            //ke before chosen fragment
            beforeChosenFragment.setListOffer(listOffer);
            beforeChosenFragment.setLeadBidder(leadBidder);
            getFragmentManager().beginTransaction()
                    .replace(R.id.fragment_daftar_tawaran_final_layout, beforeChosenFragment)
                    .commit();
        }
    }
    private void setWinnerOnOfferList() {
        for (int x=0;x<listOffer.size();x++) {
            if (listOffer.get(x).getIdBid().equals(leadBidder.getIdBid())) {
                listOffer.get(x).setWinnerStatus(true);
                break;
            }
        }
    }
    private void getOfferListFromServer() {
        String urlparams = itemID + "/offers?limit=50";
        DaftarTawaranAPI.GetOfferListAPI getOfferListAPI
                = DaftarTawaranAPI.instanceGetOfferList(urlparams, whenOfferListLoaded);
        RequestController.getInstance(getActivity()).addToRequestQueue(getOfferListAPI);
    }
}
