package com.lelangapa.android.fragments.detail.detailitemuser;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.lelangapa.android.R;
import com.lelangapa.android.adapters.imageslider.ItemImageSliderAdapter;
import com.lelangapa.android.interfaces.pageindicator.PageIndicator;
import com.lelangapa.android.modifiedviews.CirclePageIndicator;
import com.lelangapa.android.resources.DetailItemResources;

/**
 * Created by Andre on 12/29/2016.
 */

public class DetailImageFragment extends Fragment {
    private DetailItemResources detailItem;
    private ImageView imageView_gambaritem;

    private ViewPager viewPager;
    private PageIndicator indicator;

    public DetailImageFragment(){}
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_detail_barang_gambar_layout, container, false);
        initializeViews(view);
        setupViewPagerAndIndicator();
        return view;
    }
    private void initializeViews(View view)
    {
        viewPager = (ViewPager) view.findViewById(R.id.fragment_detail_barang_gambar_viewpager);
        indicator = (CirclePageIndicator) view.findViewById(R.id.fragment_detail_barang_gambar_indicator);
    }
    private void setupViewPagerAndIndicator()
    {
        ItemImageSliderAdapter adapter = new ItemImageSliderAdapter(getActivity(), detailItem.getListImageURL());
        int limit = adapter.getCount() > 1 ? adapter.getCount() -1 : 1;
        viewPager.setAdapter(adapter);
        viewPager.setOffscreenPageLimit(limit);
        indicator.setViewPager(viewPager);
    }
    public void setDetailItem(DetailItemResources detailItem)
    {
        this.detailItem = detailItem;
    }
}
