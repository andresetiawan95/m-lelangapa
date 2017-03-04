package com.lelangapa.android.fragments.favorite;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.lelangapa.android.R;
import com.lelangapa.android.adapters.UserFavoriteAdapter;
import com.lelangapa.android.interfaces.OnItemClickListener;
import com.lelangapa.android.listeners.RecyclerItemClickListener;
import com.lelangapa.android.resources.FavoriteResources;

import java.util.ArrayList;

/**
 * Created by andre on 22/02/17.
 */

public class FavoriteNoEmptyFragment extends Fragment {
    private ArrayList<FavoriteResources> listBarangFavorit;
    private UserFavoriteAdapter favoriteAdapter;
    private RecyclerView recyclerView_favorite;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_user_favorite_noempty_layout, container, false);
        initializeViews(view);
        setRecyclerViewAdapter();
        setRecyclerViewProperties();
        return view;
    }

    /*
    * Initialization method start here
    * */
    private void initializeViews(View view)
    {
        recyclerView_favorite = (RecyclerView) view.findViewById(R.id.fragment_user_favorite_layout_recyclerview);
    }
    /*
    * Initialization method end here
    * */
    public void setItemFavoriteList(ArrayList<FavoriteResources> listBarangFavorit)
    {
        this.listBarangFavorit = listBarangFavorit;
    }
    private void setRecyclerViewAdapter()
    {
        favoriteAdapter = new UserFavoriteAdapter(getActivity(), listBarangFavorit);
    }
    private void setRecyclerViewProperties()
    {
        RecyclerView.LayoutManager upLayoutManager = new GridLayoutManager(getActivity(), 2) {
            @Override
            public boolean canScrollVertically() { return false; }
        };
        recyclerView_favorite.setLayoutManager(upLayoutManager);
        recyclerView_favorite.setAdapter(favoriteAdapter);
        recyclerView_favorite.setItemAnimator(new DefaultItemAnimator());
        recyclerView_favorite.addOnItemTouchListener(new RecyclerItemClickListener(getActivity(), recyclerView_favorite, new OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {

            }

            @Override
            public void onLongItemClick(View view, int position) {

            }
        }));
    }
}
