package com.lelangapa.android.fragments.detail.detailitemowner;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.lelangapa.android.R;
import com.lelangapa.android.resources.BiddingResources;

/**
 * Created by andre on 31/01/17.
 */

public class MenuPagerBiddingFinishedWithBidderFragment extends Fragment {
    private BiddingResources itemBiddingResources;
    private TextView textView_namaBidder, textView_hargaBid;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_detail_barang_bidding_finished_auctioneer_layout, container, false);
        initializeViews(view);
        setTextViews();
        return view;
    }

    private void initializeViews(View view)
    {
        textView_namaBidder = (TextView) view.findViewById(R.id.fragment_detail_barang_bidding_finished_auctioneer_nama);
        textView_hargaBid = (TextView) view.findViewById(R.id.fragment_detail_barang_bidding_finished_auctioneer_hargabid);
    }
    public void setBidderInformation(BiddingResources itemBiddingResources)
    {
        this.itemBiddingResources = itemBiddingResources;
        if (textView_namaBidder != null && textView_hargaBid != null)
        {
            setTextViews();
        }
    }
    private void setTextViews()
    {
        textView_namaBidder.setText(itemBiddingResources.getNamaBidder());
        textView_hargaBid.setText(itemBiddingResources.getHargaBid());
    }
}
