package com.lelangkita.android.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.lelangkita.android.R;

/**
 * Created by andre on 24/10/16.
 */

public class BerandaHomeFragment extends Fragment {
    public BerandaHomeFragment(){}
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        return inflater.inflate(R.layout.fragment_beranda_home_layout, container, false);
    }
}
