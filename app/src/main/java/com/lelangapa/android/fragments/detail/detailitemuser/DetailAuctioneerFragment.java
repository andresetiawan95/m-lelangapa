package com.lelangapa.android.fragments.detail.detailitemuser;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.lelangapa.android.R;

/**
 * Created by Andre on 12/26/2016.
 */

public class DetailAuctioneerFragment extends Fragment {
    private String auctioneerID, auctioneerName;
    private TextView textView_namaAuctioneer;
    public DetailAuctioneerFragment(){}
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_detail_barang_auctioneer_layout, container, false);
        textView_namaAuctioneer = (TextView) view.findViewById(R.id.fragment_detail_barang_auctioneer_layout_nama);
        setTextViewAuctioneerInformation();
        return view;
    }
    public void setAuctioneerInfo(String auctioneerID, String auctioneerName)
    {
        this.auctioneerID = auctioneerID;
        this.auctioneerName = auctioneerName;
    }
    private void setTextViewAuctioneerInformation()
    {
        textView_namaAuctioneer.setText(auctioneerName);
    }
}
