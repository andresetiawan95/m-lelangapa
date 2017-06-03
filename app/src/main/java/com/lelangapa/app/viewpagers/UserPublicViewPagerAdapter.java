package com.lelangapa.app.viewpagers;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by andre on 28/03/17.
 */

public class UserPublicViewPagerAdapter extends FragmentPagerAdapter {
    private List<Fragment> listFragment;
    private List<String> listTitle;
    public UserPublicViewPagerAdapter(FragmentManager manager)
    {
        super(manager);
        listFragment = new ArrayList<>();
        listTitle = new ArrayList<>();
    }

    @Override
    public Fragment getItem(int position) {
        return listFragment.get(position);
    }

    @Override
    public int getCount() {
        return listFragment.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return listTitle.get(position);
    }

    public void addFragment(Fragment fragment, String title) {
        listFragment.add(fragment);
        listTitle.add(title);
    }
}
