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
 * Created by Andre on 12/24/2016.
 */

public class DetailFragment extends Fragment {
    public DetailFragment(){}
    @Override
    public View onCreateView (LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_detail_barang_layout, container, false);
        //untuk menampilkan fragment submit bid
        getFragmentManager().beginTransaction()
                .replace(R.id.fragment_detail_barang_bidding_fragment, new DetailBiddingFragment())
                .replace(R.id.fragment_detail_barang_waktubid_fragment, new DetailWaktuBidFragment())
                .replace(R.id.fragment_detail_barang_deskripsi_fragment, new DetailDeskripsiFragment())
                .commit();
        return view;
    }
}
