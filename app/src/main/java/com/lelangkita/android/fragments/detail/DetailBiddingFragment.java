package com.lelangkita.android.fragments.detail;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.lelangkita.android.R;
import com.lelangkita.android.viewpagers.DetailBarangViewPagerAdapter;

/**
 * Created by Andre on 12/25/2016.
 */

public class DetailBiddingFragment extends Fragment {
    private TabLayout detailTabLayout;
    private ViewPager detailViewPager;
    public DetailBiddingFragment(){}
    @Override
    public View onCreateView (LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_detail_barang_bidding_layout, container, false);
        detailTabLayout = (TabLayout) view.findViewById(R.id.tab_detail_barang);
        detailViewPager = (ViewPager) view.findViewById(R.id.viewpager_detail_barang);
        setUpDetailViewPager(detailViewPager);
        detailTabLayout.setupWithViewPager(detailViewPager);
        return view;
    }
    private void setUpDetailViewPager(ViewPager viewPager)
    {
        DetailBarangViewPagerAdapter detailBarangViewPagerAdapter = new DetailBarangViewPagerAdapter(getFragmentManager());
        detailBarangViewPagerAdapter.addFragment(new BiddingFragment(), "Tawar");
        detailBarangViewPagerAdapter.addFragment(new BiddingPeringkatFragment(), "Peringkat");
        viewPager.setAdapter(detailBarangViewPagerAdapter);
    }
}
