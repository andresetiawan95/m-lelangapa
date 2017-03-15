package com.lelangapa.android.fragments.riwayat.detailriwayat;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.lelangapa.android.R;
import com.lelangapa.android.apicalls.riwayat.RiwayatAPI;
import com.lelangapa.android.apicalls.singleton.RequestController;
import com.lelangapa.android.interfaces.DataReceiver;
import com.lelangapa.android.resources.RiwayatResources;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by andre on 15/03/17.
 */

public class DetailRiwayatFragment extends Fragment {
    private ArrayList<RiwayatResources> biddingList;
    private String userID;
    private DataReceiver listReceiver;

    private TextView textView_namaItem, textView_namaAuctioneer, textView_kaliBid;
    private RiwayatResources riwayatResources;
    private LoadingListFragment loadingListFragment;
    private ListNoEmptyFragment listNoEmptyFragment;

    public DetailRiwayatFragment()
    {
        initializeConstants();
        initializeFragment();
        initializeListDataReceiver();
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_user_riwayat_detail_layout, container, false);
        initializeViews(view);
        initializeEssentialInformation();
        setupFragment(loadingListFragment);
        getBiddingList();
        return view;
    }
    private void initializeConstants()
    {
        biddingList = new ArrayList<>();
    }
    private void initializeViews(View view)
    {
        textView_namaItem = (TextView) view.findViewById(R.id.fragment_user_riwayat_detail_judulitem);
        textView_namaAuctioneer = (TextView) view.findViewById(R.id.fragment_user_riwayat_detail_namaauctioneer);
        textView_kaliBid = (TextView) view.findViewById(R.id.fragment_user_riwayat_detail_bidkali);
    }
    private void initializeEssentialInformation()
    {
        riwayatResources = new RiwayatResources();
        Bundle extrasPassed = getActivity().getIntent().getExtras();
        userID = extrasPassed.getString("user_id_loggedin");
        riwayatResources.setIdItem(extrasPassed.getString("item_id"));
        riwayatResources.setNamaItem(extrasPassed.getString("item_name"));
        riwayatResources.setNamaAuctioneer(extrasPassed.getString("auctioneer_name"));
        riwayatResources.setBidTime(extrasPassed.getInt("bid_time"));

        String bidTime = Integer.toString(riwayatResources.getBidTime());
        textView_namaItem.setText(riwayatResources.getNamaItem());
        textView_namaAuctioneer.setText(riwayatResources.getNamaAuctioneer());
        textView_kaliBid.setText(bidTime);
    }
    private void initializeFragment()
    {
        loadingListFragment = new LoadingListFragment();
        listNoEmptyFragment = new ListNoEmptyFragment();
    }
    private void setupFragment(Fragment fragment)
    {
        getFragmentManager().beginTransaction()
                .replace(R.id.fragment_user_riwayat_detail_list_tawaran_layout, fragment)
                .commit();
    }

    /*
    * Retrieving list methods
    * */
    private void setAndShowTheList()
    {
        listNoEmptyFragment.setBiddingList(biddingList);
        setupFragment(listNoEmptyFragment);
    }
    private void initializeListDataReceiver()
    {
        listReceiver = new DataReceiver() {
            @Override
            public void dataReceived(Object output) {
                String response = output.toString();
                try {
                    JSONObject jsonResponse = new JSONObject(response);
                    //Log.v("RESPONSE", jsonResponse.toString());
                    biddingList.clear();
                    if (jsonResponse.getString("status").equals("success"))
                    {
                        JSONArray responseArray = jsonResponse.getJSONArray("data");
                        for (int i=0;i<responseArray.length();i++)
                        {
                            JSONObject responseArrayObject = responseArray.getJSONObject(i);
                            RiwayatResources riwayatResources = new RiwayatResources();
                            riwayatResources.setBidTimestamp(responseArrayObject.getString("bid_timestamp_return"));
                            riwayatResources.setHargaBid(responseArrayObject.getString("price_bid_return"));
                            biddingList.add(riwayatResources);
                        }
                        setAndShowTheList();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };
    }
    private void getBiddingList()
    {
        String urlParam = userID + '-' + riwayatResources.getIdItem() + '-' + Integer.toString(riwayatResources.getBidTime());
        //Log.v("URLPARAM", urlParam);
        RiwayatAPI.GetRiwayatBidList getRiwayatBidList = RiwayatAPI.initializeGetRiwayatBidList(urlParam, listReceiver);
        RequestController.getInstance(getActivity()).addToRequestQueue(getRiwayatBidList);
    }
}
