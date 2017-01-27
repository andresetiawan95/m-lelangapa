package com.lelangapa.android.fragments.detail.detailitemowner;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.lelangapa.android.R;

/**
 * Created by andre on 25/01/17.
 */

public class DetailOwnerMenuPagerFragment extends Fragment {
    private TabLayout detailOwnerTabLayout;
    private ViewPager detailOwnerViewPager;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_detail_barang_bidding_auctioneer_layout, container, false);
        return view;
    }

}
