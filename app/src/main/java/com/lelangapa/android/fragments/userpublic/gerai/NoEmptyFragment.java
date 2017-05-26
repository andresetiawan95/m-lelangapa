package com.lelangapa.android.fragments.userpublic.gerai;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.InputType;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;

import com.lelangapa.android.R;
import com.lelangapa.android.activities.detail.DetailBarangActivity;
import com.lelangapa.android.activities.userpublic.DetailUserPublicActivity;
import com.lelangapa.android.adapters.UserPublicGeraiAdapter;
import com.lelangapa.android.interfaces.OnItemClickListener;
import com.lelangapa.android.interfaces.OnLoadMore;
import com.lelangapa.android.interfaces.QueryListener;
import com.lelangapa.android.listeners.RecyclerItemClickListener;
import com.lelangapa.android.modifiedviews.EndlessRecyclerViewScrollListener;
import com.lelangapa.android.modifiedviews.SearchEditText;
import com.lelangapa.android.resources.DetailItemResources;

import java.util.ArrayList;

/**
 * Created by andre on 29/03/17.
 */

public class NoEmptyFragment extends Fragment {
    private ArrayList<DetailItemResources> listGeraiItem;
    private UserPublicGeraiAdapter userPublicGeraiAdapter;

    private RecyclerView recyclerView_listGerai;
    private SearchEditText editText_search;

    private OnLoadMore onLoadMore;
    private QueryListener queryListener;
    private EndlessRecyclerViewScrollListener scrollListener;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_detail_user_public_gerai_noempty_layout, container, false);
        initializeViews(view);
        initializeAdapter();
        setupRecyclerViewProperties();
        return view;
    }
    private void initializeViews(View view)
    {
        recyclerView_listGerai = (RecyclerView) view.findViewById(R.id.fragment_detail_user_public_gerai_items);
        editText_search = (SearchEditText) view.findViewById(R.id.fragment_detail_user_public_gerai_search);
        editText_search.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus)
                {
                    ((DetailUserPublicActivity) getActivity()).collapseToolbar();
                }
            }
        });
        editText_search.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    editText_search.setFocusable(false);
                    editText_search.setFocusable(true);
                    editText_search.setFocusableInTouchMode(true);
                    InputMethodManager in = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);

                    // NOTE: In the author's example, he uses an identifier
                    // called searchBar. If setting this code on your EditText
                    // then use v.getWindowToken() as a reference to your
                    // EditText is passed into this callback as a TextView

                    in.hideSoftInputFromWindow(v.getWindowToken(),
                            InputMethodManager.HIDE_NOT_ALWAYS);
                    queryListener.onQuerySubmit(editText_search.getText().toString().trim());
                    return true;
                }
                return false;
            }
        });
        editText_search.setInputType(InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS);
    }
    private void initializeAdapter()
    {
        userPublicGeraiAdapter = new UserPublicGeraiAdapter(getActivity(), listGeraiItem);
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
    private void setupRecyclerViewProperties()
    {
        GridLayoutManager layoutManager = new GridLayoutManager(getActivity(), 2);
        initializeScrollListener(layoutManager);
        recyclerView_listGerai.setLayoutManager(layoutManager);
        recyclerView_listGerai.setItemAnimator(new DefaultItemAnimator());
        recyclerView_listGerai.addOnScrollListener(scrollListener);
        recyclerView_listGerai.setAdapter(userPublicGeraiAdapter);
        recyclerView_listGerai.addOnItemTouchListener(new RecyclerItemClickListener(getActivity(), recyclerView_listGerai, new OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Intent intent = new Intent(getActivity(), DetailBarangActivity.class);
                Bundle bundleExtras = new Bundle();
                bundleExtras.putString("auctioneer_id", listGeraiItem.get(position).getIdauctioneer());
                bundleExtras.putString("items_id", listGeraiItem.get(position).getIdbarang());
                intent.putExtras(bundleExtras);
                //intent.putExtra("items_id", searchResult.get(position).getIdbarang());
                startActivity(intent);
            }
            @Override
            public void onLongItemClick(View view, int position) {}
        }));
    }
    public void setListGeraiItem(ArrayList<DetailItemResources> list)
    {
        this.listGeraiItem = list;
    }
    public void setOnLoadMore(OnLoadMore onLoadMore) {
        this.onLoadMore = onLoadMore;
    }
    public void setQueryListener(QueryListener queryListener) {
        this.queryListener = queryListener;
    }
    public void notifyWhenNewItemInserted(int startIndex) {
        userPublicGeraiAdapter.notifyItemRangeInserted(startIndex, 4);
    }
    public void invalidateData() {
        userPublicGeraiAdapter.notifyDataSetChanged();
        scrollListener.resetState();
    }
}
