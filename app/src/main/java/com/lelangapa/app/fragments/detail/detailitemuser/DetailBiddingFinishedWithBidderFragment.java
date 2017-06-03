package com.lelangapa.app.fragments.detail.detailitemuser;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lelangapa.app.R;
import com.lelangapa.app.resources.BiddingResources;
import com.lelangapa.app.resources.PriceFormatter;

/**
 * Created by Andre on 1/8/2017.
 */

public class DetailBiddingFinishedWithBidderFragment extends Fragment {
    private BiddingResources biddingInformation;
    private TextView textView_namaBidderWinner, textView_hargaBidderWinner, textView_status;
    private LinearLayout linearLayout_notChosen, linearLayout_chosen;
    public DetailBiddingFinishedWithBidderFragment(){}
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_detail_barang_bidding_finished_layout, container, false);
        initializeViews(view);
        showInformation();
        return view;
    }
    private void initializeViews(View view) {
        textView_status = (TextView) view.findViewById(R.id.fragment_detail_barang_bidding_finished_status);
        textView_namaBidderWinner = (TextView) view.findViewById(R.id.fragment_detail_barang_bidding_finished_name_winner);
        textView_hargaBidderWinner = (TextView) view.findViewById(R.id.fragment_detail_barang_bidding_finished_price_winner);
        linearLayout_notChosen = (LinearLayout) view.findViewById(R.id.fragment_detail_barang_bidding_finished_not_yet_chosen);
        linearLayout_chosen = (LinearLayout) view.findViewById(R.id.fragment_detail_barang_bidding_finished_chosen);
    }
    private void showInformation() {
        textView_namaBidderWinner.setText(biddingInformation.getNamaBidder());
        textView_hargaBidderWinner.setText(PriceFormatter.formatPrice(biddingInformation.getHargaBid()));
        if (biddingInformation.isWinnerStatus()) {
            textView_status.setText("Pemenang");
            linearLayout_chosen.setVisibility(View.VISIBLE);
            linearLayout_notChosen.setVisibility(View.GONE);
        }
        else {
            textView_status.setText("Tawaran Tertinggi");
            linearLayout_notChosen.setVisibility(View.VISIBLE);
            linearLayout_chosen.setVisibility(View.GONE);
        }
    }
    public void setBiddingInformation(BiddingResources info)
    {
        this.biddingInformation = info;
    }
}
