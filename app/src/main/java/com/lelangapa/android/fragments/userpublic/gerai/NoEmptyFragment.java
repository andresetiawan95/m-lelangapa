package com.lelangapa.android.fragments.userpublic.gerai;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;

import com.lelangapa.android.R;
import com.lelangapa.android.activities.userpublic.DetailUserPublicActivity;
import com.lelangapa.android.adapters.UserPublicGeraiAdapter;
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

                    return true;
                }
                else {
                    return false;
                }
            }
        });
    }
    private void initializeAdapter()
    {
        userPublicGeraiAdapter = new UserPublicGeraiAdapter(getActivity(), listGeraiItem);
    }
    private void setupRecyclerViewProperties()
    {
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getActivity(), 2);
        recyclerView_listGerai.setLayoutManager(layoutManager);
        recyclerView_listGerai.setItemAnimator(new DefaultItemAnimator());
        recyclerView_listGerai.setAdapter(userPublicGeraiAdapter);
    }
    public void setListGeraiItem(ArrayList<DetailItemResources> list)
    {
        this.listGeraiItem = list;
    }

}
