package com.lelangapa.android.fragments.detail.detailitemowner;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.lelangapa.android.R;
import com.lelangapa.android.resources.DetailItemResources;

/**
 * Created by andre on 04/02/17.
 */

public class DescriptionFragment extends Fragment {
    private DetailItemResources detailItem;

    private TextView textView_isiDeskripsi;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_detail_barang_deskripsi_layout, container, false);
        initializeViews(view);
        setDescriptionText();
        return view;
    }

    /*
    * Initialization method start here
    * */
    public void initializeViews(View view)
    {
        textView_isiDeskripsi = (TextView) view.findViewById(R.id.fragment_detail_barang_deskripsi_isideskripsi);
    }
    /*
    * Initialization method end here
    * */

    /*
    * Setter method start here
    * */
    public void setDetailItem(DetailItemResources detailItem)
    {
        this.detailItem = detailItem;
    }
    private void setDescriptionText()
    {
        if (textView_isiDeskripsi != null)
        {
            textView_isiDeskripsi.setText(detailItem.getDeskripsibarang());
        }
    }
    /*
    * Setter method end here
    * */
}
