package com.lelangapa.android.fragments.detail.detailitemowner.pagerstarted;

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
import com.lelangapa.android.interfaces.AuctioneerResponseReceiver;

/**
 * Created by andre on 20/04/17.
 */

public class NoBidFragment extends Fragment {
    private TextView textView_openPrice;
    private ImageView icon_options;
    private PopupMenu popupMenuOptions;

    private AuctioneerResponseReceiver auctioneerResponseReceiver;

    private String openPrice;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_detail_barang_bidding_highestbid_auctioneer_layout_nobid, container, false);
        initializeViews(view);
        setIconOnClickListener();
        displayOpenPrice();
        return view;
    }
    private void initializeViews(View view)
    {
        textView_openPrice = (TextView) view.findViewById(R.id.fragment_detail_barang_bidding_highestbid_nobid_open_price);
        icon_options = (ImageView) view.findViewById(R.id.fragment_detail_barang_bidding_highestbid_nobid_option);
    }
    private void setPopupMenuOptions(View view)
    {
        if (popupMenuOptions == null) {
            popupMenuOptions = new PopupMenu(getActivity(), view);
            popupMenuOptions.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem item) {
                    if (item.getItemId() == R.id.detail_item_auctioneer_cancel_lelang) {
                        auctioneerResponseReceiver.responseCancelReceived(true);
                        return true;
                    }
                    return false;
                }
            });
            popupMenuOptions.inflate(R.menu.detail_item_auctioneer_nobid_popup_menu);
        }
    }
    private void setIconOnClickListener()
    {
        icon_options.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setPopupMenuOptions(v);
                popupMenuOptions.show();
            }
        });
    }
    public void setAuctioneerResponseReceiver(AuctioneerResponseReceiver receiver)
    {
        this.auctioneerResponseReceiver = receiver;
    }
    public void setOpenPrice(String openPrice)
    {
        this.openPrice = openPrice;
        if (textView_openPrice != null) {
            displayOpenPrice();
        }
    }
    private void displayOpenPrice()
    {
        textView_openPrice.setText(openPrice);
    }
}
