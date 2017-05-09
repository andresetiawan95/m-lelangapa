package com.lelangapa.android.fragments.detail.detailitemuser;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.OvershootInterpolator;
import android.widget.ImageView;

import com.lelangapa.android.R;
import com.lelangapa.android.modifiedviews.ExpandableTextViewWithLine;
import com.lelangapa.android.resources.DetailItemResources;

/**
 * Created by Andre on 12/26/2016.
 */

public class DetailDescriptionFragment extends Fragment {
    private DetailItemResources detailItem;
    private ExpandableTextViewWithLine textView_isiDeskripsi;

    private ImageView imageView_expandToggler;
    public DetailDescriptionFragment(){}
    @Override
    public View onCreateView (LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_detail_barang_deskripsi_layout, container, false);
        initializeViews(view);
        textView_isiDeskripsi.setText(detailItem.getDeskripsibarang());
        setExpandableTextViewProperties();
        setExpandOnClickListener();
        return view;
    }
    private void initializeViews(View view) {
        textView_isiDeskripsi = (ExpandableTextViewWithLine) view.findViewById(R.id.fragment_detail_barang_deskripsi_isideskripsi);
        imageView_expandToggler = (ImageView) view.findViewById(R.id.fragment_detail_barang_deskripsi_expand_button);
    }
    private void setExpandableTextViewProperties() {
        textView_isiDeskripsi.setInterpolator(new OvershootInterpolator());
    }
    private void setExpandOnClickListener() {
        imageView_expandToggler.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                textView_isiDeskripsi.toggle();
            }
        });
    }
    public void setDetailItem(DetailItemResources detailItem)
    {
        this.detailItem = detailItem;
    }
}
