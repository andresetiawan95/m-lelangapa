package com.lelangapa.android.fragments.detail.detailitemowner;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.lelangapa.android.R;
import com.lelangapa.android.resources.BiddingResources;

/**
 * Created by andre on 31/01/17.
 */

public class MenuPagerBiddingFinishedWithBidderFragment extends Fragment {
    private BiddingResources itemBiddingResources;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_detail_barang_bidding_finished_auctioneer_layout, container, false);
        return view;
    }

    public void setBidderInformation(BiddingResources itemBiddingResources)
    {
        this.itemBiddingResources = itemBiddingResources;
    }
}
