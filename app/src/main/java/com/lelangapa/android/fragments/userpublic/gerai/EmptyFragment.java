package com.lelangapa.android.fragments.userpublic.gerai;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.lelangapa.android.R;

/**
 * Created by andre on 29/03/17.
 */

public class EmptyFragment extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        return inflater.inflate(R.layout.fragment_detail_user_public_gerai_empty_layout, container, false);
    }
}
