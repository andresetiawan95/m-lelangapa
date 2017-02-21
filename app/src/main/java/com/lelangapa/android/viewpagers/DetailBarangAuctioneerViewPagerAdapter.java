package com.lelangapa.android.viewpagers;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import java.util.ArrayList;

/**
 * Created by andre on 26/01/17.
 */

public class DetailBarangAuctioneerViewPagerAdapter extends FragmentStatePagerAdapter {
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

    public void addFragment (Fragment fragment, String title)
    {
        mFragmentList.add(fragment);
        mTitleList.add(title);
    }
    @Override
    public int getItemPosition(Object object) {
    // POSITION_NONE makes it possible to reload the PagerAdapter
        return POSITION_NONE;
    }
}
