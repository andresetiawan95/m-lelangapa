package com.lelangapa.app.fragments.home;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.lelangapa.app.R;

/**
 * Created by andre on 24/10/16.
 */

public class TrendingHomeFragment extends Fragment {
    public TrendingHomeFragment(){
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        return inflater.inflate(R.layout.fragment_trending_home_layout, container, false);
    }
}
