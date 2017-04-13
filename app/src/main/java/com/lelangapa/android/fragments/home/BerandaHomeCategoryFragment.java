package com.lelangapa.android.fragments.home;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.lelangapa.android.R;
import com.lelangapa.android.adapters.category.HomeCategoryAdapter;

/**
 * Created by andre on 11/04/17.
 */

public class BerandaHomeCategoryFragment extends Fragment {
    private RecyclerView recyclerView_category;
    private HomeCategoryAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_beranda_home_category_layout, container, false);
        initializeViews(view);
        setAdapter();
        setRecyclerViewProperties();
        return view;
    }

    private void initializeViews(View view) {
        recyclerView_category = (RecyclerView) view.findViewById(R.id.fragment_beranda_home_category_recyclerview);
    }
    private void setAdapter()
    {
        String[] categoryResource = getActivity().getResources().getStringArray(R.array.categories);
        adapter = new HomeCategoryAdapter(getActivity(), categoryResource);
    }
    private void setRecyclerViewProperties()
    {
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getActivity(),2) {
            @Override
            public boolean canScrollVertically(){
                return false;
            }
        };
        recyclerView_category.setLayoutManager(layoutManager);
        recyclerView_category.setItemAnimator(new DefaultItemAnimator());
        recyclerView_category.setFocusable(false);
        recyclerView_category.setAdapter(adapter);
    }
}
