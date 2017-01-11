package com.lelangapa.android.fragments.detail;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.lelangapa.android.R;

/**
 * Created by Andre on 12/26/2016.
 */

public class DetailAuctioneerFragment extends Fragment {
    public DetailAuctioneerFragment(){}
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_detail_barang_auctioneer_layout, container, false);
        return view;
    }
}
