package com.lelangapa.app.fragments.detail.detailitemowner;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.lelangapa.app.R;
import com.lelangapa.app.adapters.BiddingPeringkatAdapter;
import com.lelangapa.app.resources.BiddingResources;

import java.util.ArrayList;

/**
 * Created by andre on 25/01/17.
 */

public class MenuPagerRankingFragment extends Fragment {
    private ArrayList<BiddingResources> biddingRankingList;
    private RecyclerView biddingRankingRecyclerView;
    private BiddingPeringkatAdapter biddingRankingAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_detail_barang_peringkat_top_three_layout, container, false);
        /*biddingRankingRecyclerView = (RecyclerView) view.findViewById(R.id.fragment_detail_barang_peringkat_top_three_recyclerview);
        biddingRankingAdapter = new BiddingPeringkatAdapter(getActivity(), biddingRankingList);
        RecyclerView.LayoutManager linearLayout = new LinearLayoutManager(getActivity());
        biddingRankingRecyclerView.setLayoutManager(linearLayout);
        biddingRankingRecyclerView.setAdapter(biddingRankingAdapter);
*/
        return view;
    }
    public void setBiddingRankingList(ArrayList<BiddingResources> biddingRankingList)
    {
        this.biddingRankingList = biddingRankingList;
        if (biddingRankingAdapter != null)
        {
            biddingRankingAdapter.updateDataSet(this.biddingRankingList);
        }
    }
    public void updateBiddingPeringkatList(ArrayList<BiddingResources> biddingPeringkatList)
    {
        //untuk mengganti list rank dengan yang terbaru;
        this.biddingRankingList = biddingPeringkatList;
        biddingRankingAdapter.updateDataSet(this.biddingRankingList);
    }
}
