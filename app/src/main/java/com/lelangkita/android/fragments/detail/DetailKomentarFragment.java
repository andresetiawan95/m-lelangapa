package com.lelangkita.android.fragments.detail;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.lelangkita.android.R;
import com.lelangkita.android.resources.DetailItemResources;

/**
 * Created by Andre on 12/26/2016.
 */

public class DetailKomentarFragment extends Fragment {
    public DetailKomentarFragment(){}
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_detail_barang_komentar_layout, container, false);
        return view;
    }
}
