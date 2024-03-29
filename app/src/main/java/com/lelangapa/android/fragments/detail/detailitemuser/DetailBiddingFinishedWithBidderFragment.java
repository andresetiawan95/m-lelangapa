package com.lelangapa.android.fragments.detail.detailitemuser;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.lelangapa.android.R;
import com.lelangapa.android.resources.DetailItemResources;

/**
 * Created by Andre on 1/8/2017.
 */

public class DetailBiddingFinishedWithBidderFragment extends Fragment {
    private DetailItemResources detailItem;
    private TextView textView_namaBidderWinner;
    private TextView textView_hargaBidderWinner;
    public DetailBiddingFinishedWithBidderFragment(){}
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_detail_barang_bidding_finished_layout, container, false);
        textView_namaBidderWinner = (TextView) view.findViewById(R.id.fragment_detail_barang_bidding_finished_name_winner);
        textView_hargaBidderWinner = (TextView) view.findViewById(R.id.fragment_detail_barang_bidding_finished_price_winner);
        textView_namaBidderWinner.setText(detailItem.getNamabidder());
        textView_hargaBidderWinner.setText(detailItem.getHargabid());
        return view;
    }
    public void setDetailItem(DetailItemResources detailItem)
    {
        this.detailItem = detailItem;
    }
}
