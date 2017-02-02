package com.lelangapa.android.fragments.search;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.lelangapa.android.R;
import com.lelangapa.android.activities.detail.DetailBarangActivity;
import com.lelangapa.android.adapters.MainSearchAdapter;
import com.lelangapa.android.interfaces.OnItemClickListener;
import com.lelangapa.android.listeners.RecyclerItemClickListener;
import com.lelangapa.android.resources.DetailItemResources;

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
                Bundle bundleExtras = new Bundle();
                bundleExtras.putString("auctioneer_id", searchResult.get(position).getIdauctioneer());
                bundleExtras.putString("items_id", searchResult.get(position).getIdbarang());
                intent.putExtras(bundleExtras);
                //intent.putExtra("items_id", searchResult.get(position).getIdbarang());
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
