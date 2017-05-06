package com.lelangapa.android.fragments.search.result;

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
import com.lelangapa.android.interfaces.OnLoadMore;
import com.lelangapa.android.listeners.RecyclerItemClickListener;
import com.lelangapa.android.modifiedviews.EndlessRecyclerViewScrollListener;
import com.lelangapa.android.resources.DetailItemResources;

import java.util.ArrayList;

/**
 * Created by andre on 06/05/17.
 */

public class ResultNoEmpty extends Fragment {
    private ArrayList<DetailItemResources> searchResult;

    private RecyclerView searchResultRecycleView;
    private MainSearchAdapter searchAdapter;
    private EndlessRecyclerViewScrollListener scrollListener;

    private OnLoadMore onLoadMore;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main_search_with_result_layout, container, false);
        initializeViews(view);
        initializeAdapter();
        setRecyclerViewProperties();
        return view;
    }
    private void initializeViews(View view) {
        searchResultRecycleView = (RecyclerView) view.findViewById(R.id.fragment_main_search_layout_result_recyclerview);
    }
    private void initializeAdapter() {
        searchAdapter = new MainSearchAdapter(getActivity(), searchResult);
    }
    private void initializeScrollListener(GridLayoutManager layoutManager) {
        scrollListener = new EndlessRecyclerViewScrollListener(layoutManager) {
            @Override
            public void onLoadMore(final int page, int totalItemsCount, RecyclerView view) {
                view.post(new Runnable() {
                    @Override
                    public void run() {
                        onLoadMore.loadPage(page);
                    }
                });
            }
        };
    }
    private void setRecyclerViewProperties() {
        GridLayoutManager searchLayoutManager = new GridLayoutManager(getActivity(), 2);
        initializeScrollListener(searchLayoutManager);
        searchResultRecycleView.setLayoutManager(searchLayoutManager);
        searchResultRecycleView.setAdapter(searchAdapter);
        searchResultRecycleView.setItemAnimator(new DefaultItemAnimator());
        searchResultRecycleView.addOnScrollListener(scrollListener);
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
            public void onLongItemClick(View view, int position) {}
        }));
    }
    public void setSearchResult(ArrayList<DetailItemResources> searchResult) {
        this.searchResult = searchResult;
    }
    public void setLoadMoreInterface(OnLoadMore onLoadMore) {
        this.onLoadMore = onLoadMore;
    }
    public void notifyWhenNewItemInserted(int startIndex) {
        searchAdapter.notifyItemRangeInserted(startIndex, 4);
    }
    public void invalidateData() {
        searchAdapter.notifyDataSetChanged();
        scrollListener.resetState();
    }
}
