package com.lelangapa.app.fragments.search;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.lelangapa.app.R;

import java.util.ArrayList;

/**
 * Created by andre on 01/05/17.
 */

public class SearchTextEmptyFragment extends Fragment {
    private ArrayList<String> listKeyword;
    private ListView listView_keywordHistory;
    private ArrayAdapter adapter;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main_search_textempty_layout, container, false);
        initializeViews(view);
        initializeAdapter();
        showAllKeywordList();
        return view;
    }
    private void initializeViews(View view) {
        listView_keywordHistory = (ListView) view.findViewById(R.id.fragment_main_search_keyword_history_listview);
    }
    private void initializeAdapter()
    {
        adapter = new ArrayAdapter<>(getActivity(), R.layout.fragment_main_search_textempty_keyword_history_items, R.id.keyword_history_label, listKeyword);
    }
    private void showAllKeywordList()
    {
        listView_keywordHistory.setAdapter(adapter);
    }

    public void setListKeyword(ArrayList<String> list) {
        this.listKeyword = list;
        if (adapter != null) {
            adapter.clear();
            adapter.addAll(listKeyword);
            adapter.notifyDataSetChanged();
        }
    }
}
