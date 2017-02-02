package com.lelangapa.android.viewpagers;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;

/**
 * Created by andre on 26/01/17.
 */

public class DetailBarangAuctioneerViewPagerAdapter extends FragmentPagerAdapter {
    private ArrayList<Fragment> mFragmentList = new ArrayList<>();
    private ArrayList<String> mTitleList = new ArrayList<>();

    public DetailBarangAuctioneerViewPagerAdapter(FragmentManager fragmentmanager)
    {
        super(fragmentmanager);
    }
    @Override
    public Fragment getItem(int position)
    {
        return mFragmentList.get(position);
    }
    @Override
    public int getCount()
    {
        return mFragmentList.size();
    }
    @Override
    public CharSequence getPageTitle(int position)
    {
        return mTitleList.get(position);
    }

    public void setFragmentList(ArrayList<Fragment> newFragmentList)
    {
        this.mFragmentList = newFragmentList;
        notifyDataSetChanged();
    }
    public void setTitleList(ArrayList<String> newTitleList)
    {
        this.mTitleList = newTitleList;
        notifyDataSetChanged();
    }
}
