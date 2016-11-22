package com.lelangkita.android.fragments.profile;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.lelangkita.android.R;

/**
 * Created by andre on 22/11/16.
 */

public class EditAlamatFragment extends Fragment {
    public EditAlamatFragment(){};
    @Override
    public View onCreateView (LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.fragment_userprofile_editalamat_layout, container, false);
        return view;
    }

}
