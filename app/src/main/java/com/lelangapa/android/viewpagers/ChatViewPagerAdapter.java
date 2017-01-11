package com.lelangapa.android.viewpagers;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by andre on 28/11/16.
 */

public class ChatViewPagerAdapter extends FragmentPagerAdapter {
    private final List<Fragment> mChatFragmentList = new ArrayList<>();
    private final List<String> mChatFragmentTitleList = new ArrayList<>();
    public ChatViewPagerAdapter(FragmentManager manager) {
        super(manager);
    }
    @Override
    public Fragment getItem(int position){
        return mChatFragmentList.get(position);
    }
    @Override
    public int getCount(){
        return mChatFragmentList.size();
    }
    @Override
    public CharSequence getPageTitle(int position){
        return mChatFragmentTitleList.get(position);
    }
    public void addFragment(Fragment fragment, String title){
        mChatFragmentList.add(fragment);
        mChatFragmentTitleList.add(title);
    }
}
