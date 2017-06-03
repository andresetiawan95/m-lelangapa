package com.lelangapa.app.viewpagers;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Andre on 12/24/2016.
 */

public class DetailBarangViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mDetailBarangFragmentList = new ArrayList<>();
        private final List<String> mDetailBarangFragmentTitleList = new ArrayList<>();
        public DetailBarangViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }
        @Override
        public Fragment getItem(int position){
            return mDetailBarangFragmentList.get(position);
        }
        @Override
        public int getCount(){
            return mDetailBarangFragmentList.size();
        }
        @Override
        public CharSequence getPageTitle(int position){
            return mDetailBarangFragmentTitleList.get(position);
        }
        public void addFragment(Fragment fragment, String title){
            mDetailBarangFragmentList.add(fragment);
            mDetailBarangFragmentTitleList.add(title);
        }
}
