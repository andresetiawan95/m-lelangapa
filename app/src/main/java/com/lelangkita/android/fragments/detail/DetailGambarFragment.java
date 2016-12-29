package com.lelangkita.android.fragments.detail;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.lelangkita.android.R;
import com.lelangkita.android.resources.DetailItemResources;

/**
 * Created by Andre on 12/29/2016.
 */

public class DetailGambarFragment extends Fragment {
    private DetailItemResources detailItem;
    public DetailGambarFragment(){}
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_detail_barang_gambar_layout, container, false);
        return view;
    }
    public void setDetailItem(DetailItemResources detailItem)
    {
        this.detailItem = detailItem;
    }
}
