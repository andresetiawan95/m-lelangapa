package com.lelangapa.android.fragments.home;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.lelangapa.android.R;

/**
 * Created by andre on 11/04/17.
 */

public class BerandaHomeCategoryFragment extends Fragment {
    private RecyclerView recyclerView_category;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_beranda_home_category_layout, container, false);
        initializeViews(view);
        return view;
    }

    private void initializeViews(View view) {
        recyclerView_category = (RecyclerView) view.findViewById(R.id.fragment_beranda_home_category_recyclerview);
    }
}
