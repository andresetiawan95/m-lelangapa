package com.lelangapa.app.fragments;

import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarDrawerToggle;

/**
 * Created by andre on 22/10/16.
 */

public class HomeFragment extends Fragment {
    public ActionBarDrawerToggle mDrawerToggle;
    public HomeFragment (){

    }

/*    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
//        mDrawerToggle.setDrawerIndicatorEnabled(true);
        View view = inflater.inflate(R.layout.fragment_main_layout, container, false);
        ((MainActivity)getActivity()).getSupportActionBar().setTitle("Lelangkita");
//        ((MainActivity)getActivity()).getSupportActionBar().setHomeButtonEnabled(true);
//        ((MainActivity)getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(false);
//        MainActivity.drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
//        MainActivity.toggle.setDrawerIndicatorEnabled(true);
//        MainActivity.toggle.syncState();
        return view;
    }*/
}
