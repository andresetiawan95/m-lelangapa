package com.lelangapa.app.fragments.detail.detailitemowner;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.OvershootInterpolator;
import android.widget.ImageView;

import com.lelangapa.app.R;
import com.lelangapa.app.modifiedviews.ExpandableTextViewWithLine;
import com.lelangapa.app.resources.DetailItemResources;

/**
 * Created by andre on 04/02/17.
 */

public class DescriptionFragment extends Fragment {
    private DetailItemResources detailItem;

    private ExpandableTextViewWithLine textView_isiDeskripsi;
    private ImageView imageView_expandToggler;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_detail_barang_deskripsi_layout, container, false);
        initializeViews(view);
        setExpandableTextViewProperties();
        setExpandOnClickListener();
        setDescriptionText();
        return view;
    }

    /*
    * Initialization method start here
    * */
    public void initializeViews(View view)
    {
        textView_isiDeskripsi = (ExpandableTextViewWithLine) view.findViewById(R.id.fragment_detail_barang_deskripsi_isideskripsi);
        imageView_expandToggler = (ImageView) view.findViewById(R.id.fragment_detail_barang_deskripsi_expand_button);
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
        if (textView_isiDeskripsi != null)
        {
            setDescriptionText();
        }
    }
    private void setExpandableTextViewProperties() {
        textView_isiDeskripsi.setInterpolator(new OvershootInterpolator());
    }
    private void setExpandOnClickListener() {
        imageView_expandToggler.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                textView_isiDeskripsi.toggle();
                imageView_expandToggler.setImageResource(textView_isiDeskripsi.isExpanded() ? R.drawable.ic_expand_more_grey_128dp : R.drawable.ic_expand_less_grey_128dp);
            }
        });
    }
    private void setDescriptionText()
    {
        textView_isiDeskripsi.setText(detailItem.getDeskripsibarang());
    }
    /*
    * Setter method end here
    * */
}
