package com.lelangapa.app.fragments.gerai;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.lelangapa.app.R;

/**
 * Created by andre on 06/12/16.
 */

public class UserGeraiEmptyFragment extends Fragment {
    public UserGeraiEmptyFragment(){}
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.fragment_user_gerai_layout_empty, container, false);
        return view;
    }
}
