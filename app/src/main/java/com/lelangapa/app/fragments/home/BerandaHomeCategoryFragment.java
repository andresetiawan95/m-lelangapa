package com.lelangapa.app.fragments.home;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.lelangapa.app.R;
import com.lelangapa.app.activities.category.ItemCategoryActivity;
import com.lelangapa.app.adapters.category.HomeCategoryAdapter;
import com.lelangapa.app.interfaces.OnItemClickListener;
import com.lelangapa.app.listeners.RecyclerItemClickListener;

/**
 * Created by andre on 11/04/17.
 */

public class BerandaHomeCategoryFragment extends Fragment {
    private RecyclerView recyclerView_category;
    private HomeCategoryAdapter adapter;

    private String[] categoryResource;

    private Bundle categoryBundleExtras;
    private Intent categoryIntent;
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
        categoryResource = getActivity().getResources().getStringArray(R.array.categories);
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
        recyclerView_category.addOnItemTouchListener(new RecyclerItemClickListener(getActivity(), recyclerView_category, new OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                categoryBundleExtras = new Bundle();
                categoryBundleExtras.putString("id_category", Integer.toString(position+1));
                categoryBundleExtras.putString("nama_category", categoryResource[position]);
                categoryIntent = new Intent(getActivity(), ItemCategoryActivity.class);
                categoryIntent.putExtras(categoryBundleExtras);
                startActivity(categoryIntent);
            }

            @Override
            public void onLongItemClick(View view, int position) {

            }
        }));
    }
}
