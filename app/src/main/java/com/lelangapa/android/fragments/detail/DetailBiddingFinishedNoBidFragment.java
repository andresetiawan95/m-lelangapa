package com.lelangapa.android.fragments.detail;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.lelangapa.android.R;

/**
 * Created by Andre on 1/8/2017.
 */

public class DetailBiddingFinishedNoBidFragment extends Fragment {
    public DetailBiddingFinishedNoBidFragment(){}
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_detail_barang_bidding_finished_nobidder_layout, container, false);
        return view;
    }
}
