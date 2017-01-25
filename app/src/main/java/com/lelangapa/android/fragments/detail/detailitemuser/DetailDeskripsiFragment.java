package com.lelangapa.android.fragments.detail.detailitemuser;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.lelangapa.android.R;
import com.lelangapa.android.resources.DetailItemResources;

/**
 * Created by Andre on 12/26/2016.
 */

public class DetailDeskripsiFragment extends Fragment {
    private DetailItemResources detailItem;
    private TextView textView_isiDeskripsi;
    public DetailDeskripsiFragment(){}
    @Override
    public View onCreateView (LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_detail_barang_deskripsi_layout, container, false);
        textView_isiDeskripsi = (TextView) view.findViewById(R.id.fragment_detail_barang_deskripsi_isideskripsi);
        textView_isiDeskripsi.setText(detailItem.getDeskripsibarang());
        return view;
    }
    public void setDetailItem(DetailItemResources detailItem)
    {
        this.detailItem = detailItem;
    }
}
