package com.lelangapa.android.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.lelangapa.android.R;
import com.lelangapa.android.adapters.imageslider.ImageSliderAdapter;
import com.lelangapa.android.interfaces.pageindicator.PageIndicator;
import com.lelangapa.android.modifiedviews.CirclePageIndicator;

import java.util.ArrayList;

/**
 * Created by andre on 24/10/16.
 */

public class BerandaHomeFragment extends Fragment {
    private ArrayList<Integer> listImages;
    private ViewPager viewPager;
    private PageIndicator pageIndicator;
    public BerandaHomeFragment()
    {
        listImages = new ArrayList<>();
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.fragment_beranda_home_layout, container, false);
        initializeViews(view);
        initializeImages();
        setupViewPagerAndIndicator();
        return view;
    }

    private void initializeViews(View view)
    {
        viewPager = (ViewPager) view.findViewById(R.id.viewpager_beranda);
        pageIndicator = (CirclePageIndicator) view.findViewById(R.id.indicator);
    }
    private void initializeImages()
    {
        listImages.add(R.drawable.vector_marketplace_nyolong_gambar);
        listImages.add(R.drawable.ic_insert_photo_grey_128dp);
        listImages.add(R.drawable.ic_insert_photo_grey_128dp);
        listImages.add(R.drawable.ic_insert_photo_grey_128dp);
        listImages.add(R.drawable.ic_insert_photo_grey_128dp);
    }
    private void setupViewPagerAndIndicator()
    {
        ImageSliderAdapter adapter = new ImageSliderAdapter(getActivity(), listImages);

        viewPager.setAdapter(adapter);
        pageIndicator.setViewPager(viewPager);
    }
}
