package com.lelangapa.android.fragments.userpublic;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.lelangapa.android.R;

/**
 * Created by andre on 28/03/17.
 */

public class UserPublicGeraiFragment extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_detail_user_public_gerai_layout, container, false);
        setupFragment();
        return view;
    }
    private void setupFragment()
    {
        getFragmentManager().beginTransaction()
                .replace(R.id.test_fragment, new UserPublicGeraiEmptyFragment())
                .commit();
    }
}
