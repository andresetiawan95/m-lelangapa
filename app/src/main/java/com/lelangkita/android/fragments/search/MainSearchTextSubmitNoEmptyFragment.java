package com.lelangkita.android.fragments.search;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.lelangkita.android.R;
import com.lelangkita.android.activities.detail.DetailBarangActivity;
import com.lelangkita.android.adapters.MainSearchAdapter;
import com.lelangkita.android.interfaces.OnItemClickListener;
import com.lelangkita.android.listeners.RecyclerItemClickListener;
import com.lelangkita.android.resources.DetailItemResources;

import java.util.ArrayList;

/**
 * Created by Andre on 12/17/2016.
 */

public class MainSearchTextSubmitNoEmptyFragment extends Fragment {
    private ArrayList<DetailItemResources> searchResult;
    private RecyclerView searchResultRecycleView;
    private MainSearchAdapter searchAdapter;
    public MainSearchTextSubmitNoEmptyFragment(){}
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_main_search_textsubmit_layout_noempty, container, false);
        searchResultRecycleView = (RecyclerView) view.findViewById(R.id.fragment_main_search_layout_recyclerview);
        searchAdapter = new MainSearchAdapter(getActivity(), searchResult);
        RecyclerView.LayoutManager searchLayoutManager = new GridLayoutManager(getActivity(), 2) {
            @Override
            public boolean canScrollVertically(){
                return false;
            }
        };
        searchResultRecycleView.setLayoutManager(searchLayoutManager);
        searchResultRecycleView.setAdapter(searchAdapter);
        searchResultRecycleView.setItemAnimator(new DefaultItemAnimator());
        searchResultRecycleView.addOnItemTouchListener(new RecyclerItemClickListener(getActivity(), searchResultRecycleView, new OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Intent intent = new Intent(getActivity(), DetailBarangActivity.class);
                intent.putExtra("items_id", searchResult.get(position).getIdbarang());
                Log.v("id item", searchResult.get(position).getIdbarang());
                startActivity(intent);
            }
            @Override
            public void onLongItemClick(View view, int position) {

            }
        }));
        return view;
    }
    public void setSearchResult(ArrayList<DetailItemResources> searchResult)
    {
        this.searchResult = searchResult;
    }
}
