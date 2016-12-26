package com.lelangkita.android.fragments.detail;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.lelangkita.android.R;

/**
 * Created by Andre on 12/25/2016.
 */

public class DetailWaktuBidFragment extends Fragment {
    public DetailWaktuBidFragment(){}
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_detail_barang_waktubid_layout, container, false);
        return view;
    }
}
