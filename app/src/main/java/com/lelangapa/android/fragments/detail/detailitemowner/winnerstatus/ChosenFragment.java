package com.lelangapa.android.fragments.detail.detailitemowner.winnerstatus;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.PopupMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.lelangapa.android.R;
import com.lelangapa.android.activities.detail.DaftarTawaranFinalActivity;
import com.lelangapa.android.resources.BiddingResources;

/**
 * Created by andre on 10/05/17.
 */

public class ChosenFragment extends Fragment {
    private TextView textView_namaBidder, textView_hargaBid;
    private ImageView icon_options;

    private PopupMenu popupMenuOptions;

    private BiddingResources biddingResources;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_detail_barang_bidding_finished_auctioneer_layout_chosen, container, false);
        initializeViews(view);
        initializeOnClickListener();
        showInformation();
        return view;
    }
    private void initializeViews(View view) {
        textView_namaBidder = (TextView) view.findViewById(R.id.fragment_detail_barang_bidding_finished_auctioneer_chosen_namabidder);
        textView_hargaBid = (TextView) view.findViewById(R.id.fragment_detail_barang_bidding_finished_auctioneer_chosen_bid_price);
        icon_options = (ImageView) view.findViewById(R.id.fragment_detail_barang_bidding_finished_auctioneer_chosen_option);
    }
    private void initializeOnClickListener() {
        icon_options.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setPopupMenuOptions(v);
                popupMenuOptions.show();
            }
        });
    }
    private void setPopupMenuOptions(View view) {
        popupMenuOptions = new PopupMenu(getActivity(), view);
        popupMenuOptions.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.detail_item_auctioneer_chosen_offer_list:
                        intentToDaftarTawaran();
                        return true;
                    default:
                        return false;
                }
            }
        });
        popupMenuOptions.inflate(R.menu.detail_item_auctioneer_chosen_popup_menu);
    }
    public void setBidderInformation(BiddingResources resources) {
        this.biddingResources = resources;
    }
    private void showInformation() {
        if (textView_namaBidder!=null && textView_hargaBid!=null) {
            textView_namaBidder.setText(biddingResources.getNamaBidder());
            textView_hargaBid.setText(biddingResources.getHargaBid());
        }
    }
    private void intentToDaftarTawaran() {
        Intent intent = new Intent(getActivity(), DaftarTawaranFinalActivity.class);
        startActivity(intent);
    }
}
