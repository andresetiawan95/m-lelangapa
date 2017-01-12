package com.lelangapa.android.fragments.detail;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.lelangapa.android.R;
import com.lelangapa.android.adapters.BiddingPeringkatAdapter;
import com.lelangapa.android.resources.BiddingPeringkatResources;

import java.util.ArrayList;

/**
 * Created by Andre on 12/24/2016.
 */

public class BiddingPeringkatFragment extends Fragment {
    private ArrayList<BiddingPeringkatResources> biddingPeringkatList;
    private RecyclerView biddingPeringkatRecyclerView;
    private BiddingPeringkatAdapter biddingPeringkatAdapter = null;
    public BiddingPeringkatFragment(){}
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_detail_barang_peringkat_top_three_layout, container, false);
        biddingPeringkatRecyclerView = (RecyclerView) view.findViewById(R.id.fragment_detail_barang_peringkat_top_three_recyclerview);
        biddingPeringkatAdapter = new BiddingPeringkatAdapter(getActivity(), biddingPeringkatList);
        RecyclerView.LayoutManager linearLayout = new LinearLayoutManager(getActivity());
        biddingPeringkatRecyclerView.setLayoutManager(linearLayout);
        biddingPeringkatRecyclerView.setAdapter(biddingPeringkatAdapter);

        return view;
    }
    public void setBiddingPeringkatList(ArrayList<BiddingPeringkatResources> biddingPeringkatList)
    {
        this.biddingPeringkatList = biddingPeringkatList;
        if (biddingPeringkatAdapter != null)
        {
            biddingPeringkatAdapter.updateDataSet(this.biddingPeringkatList);
        }
    }
    public void updateBiddingPeringkatList(ArrayList<BiddingPeringkatResources> biddingPeringkatList)
    {
        //untuk mengganti list rank dengan yang terbaru;
        this.biddingPeringkatList = biddingPeringkatList;
        biddingPeringkatAdapter.updateDataSet(this.biddingPeringkatList);
    }
}
