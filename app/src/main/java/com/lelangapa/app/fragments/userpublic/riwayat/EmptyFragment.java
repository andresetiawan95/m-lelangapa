package com.lelangapa.app.fragments.userpublic.riwayat;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.lelangapa.app.R;

/**
 * Created by andre on 30/03/17.
 */

public class EmptyFragment extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_detail_user_public_riwayat_empty_layout, container, false);
        return view;
    }
}
