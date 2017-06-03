package com.lelangapa.app.fragments.detail.detailitemowner.pagerstarted;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.PopupMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.lelangapa.app.R;
import com.lelangapa.app.interfaces.AuctioneerResponseReceiver;
import com.lelangapa.app.resources.BiddingResources;

/**
 * Created by andre on 20/04/17.
 */

public class ExistBidFragment extends Fragment {
    private TextView textView_priceNow, textView_namaBidder;
    private ImageView icon_options;
    private ImageView imageView_avatar;
    private PopupMenu popupMenuOptions;

    private BiddingResources itemBidResources;
    private AuctioneerResponseReceiver auctioneerResponseReceiver;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_detail_barang_bidding_highestbid_auctioneer_layout_existbid, container, false);
        initializeViews(view);
        setIconOptionsOnClickListener();
        displayBidderInformation();
        return view;
    }
    private void initializeViews(View view)
    {
        textView_namaBidder = (TextView) view.findViewById(R.id.fragment_detail_barang_bidding_highestbid_existbid_namabidder);
        textView_priceNow = (TextView) view.findViewById(R.id.fragment_detail_barang_bidding_highestbid_existbid_bid_price);
        imageView_avatar = (ImageView) view.findViewById(R.id.fragment_detail_barang_bidding_highestbid_existbid_bidderavatar);
        icon_options = (ImageView) view.findViewById(R.id.fragment_detail_barang_bidding_highestbid_existbid_option);
    }
    private void setPopupMenuOptions(View view)
    {
        /* SINGLE OBJECT STILL ERROR WHEN FRAGMENT IN ONPAUSE
        if (popupMenuOptions == null) {
            popupMenuOptions = new PopupMenu(getActivity(), view);
            popupMenuOptions.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem item) {
                    switch (item.getItemId()) {
                        case R.id.detail_item_auctioneer_choose_offer :
                            auctioneerResponseReceiver.responseWinnerChosenReceived(true, itemBidResources.getIdBid());
                            return true;
                        case R.id.detail_item_auctioneer_offer_list :
                            auctioneerResponseReceiver.responseDaftarTawaranReceived();
                            return true;
                        case R.id.detail_item_auctioneer_block_list :
                            return true;
                        case R.id.detail_item_auctioneer_cancel_lelang :
                            auctioneerResponseReceiver.responseCancelReceived(true);
                            return true;
                    }
                    return false;
                }
            });
            popupMenuOptions.inflate(R.menu.detail_item_auctioneer_existbid_popup_menu);
        }*/
        popupMenuOptions = new PopupMenu(getActivity(), view);
        popupMenuOptions.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.detail_item_auctioneer_choose_offer :
                        auctioneerResponseReceiver.responseWinnerChosenReceived(true, itemBidResources.getIdBid());
                        return true;
                    case R.id.detail_item_auctioneer_offer_list :
                        auctioneerResponseReceiver.responseDaftarTawaranReceived();
                        return true;
                    case R.id.detail_item_auctioneer_block_list :
                        auctioneerResponseReceiver.responseBlockListReceived();
                        return true;
                    case R.id.detail_item_auctioneer_cancel_lelang :
                        auctioneerResponseReceiver.responseCancelReceived(true);
                        return true;
                    default:
                        return false;
                }
            }
        });
        popupMenuOptions.inflate(R.menu.detail_item_auctioneer_existbid_popup_menu);
    }
    private void setIconOptionsOnClickListener()
    {
        icon_options.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setPopupMenuOptions(v);
                popupMenuOptions.show();
            }
        });
    }
    public void setBidderInformation(BiddingResources itemBidResources)
    {
        this.itemBidResources = itemBidResources;
        if (textView_namaBidder != null && textView_priceNow != null) {
            displayBidderInformation();
        }
    }
    public void setAuctioneerResponseReceiver(AuctioneerResponseReceiver receiver)
    {
        this.auctioneerResponseReceiver = receiver;
    }
    private void displayBidderInformation()
    {
        textView_namaBidder.setText(itemBidResources.getNamaBidder());
        textView_priceNow.setText(itemBidResources.getHargaBid());
    }
}
