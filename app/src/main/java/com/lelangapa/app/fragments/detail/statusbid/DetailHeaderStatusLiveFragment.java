package com.lelangapa.app.fragments.detail.statusbid;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.lelangapa.app.R;

/**
 * Created by andre on 09/01/17.
 */

public class DetailHeaderStatusLiveFragment extends Fragment {
    public DetailHeaderStatusLiveFragment(){}
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_detail_barang_header_statusbid_live_layout, container, false);
        return view;
    }
}
