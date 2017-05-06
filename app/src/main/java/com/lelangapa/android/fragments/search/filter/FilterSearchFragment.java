package com.lelangapa.android.fragments.search.filter;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

import com.lelangapa.android.R;
import com.lelangapa.android.activities.search.filter.FilterSearchActivity;
import com.lelangapa.android.adapters.FilterSearchAdapter;
import com.lelangapa.android.decorations.DividerItemDecoration;
import com.lelangapa.android.interfaces.OnItemClickListener;
import com.lelangapa.android.listeners.RecyclerItemClickListener;
import com.lelangapa.android.preferences.FilterManager;
import com.lelangapa.android.resources.FilterSearchResources;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by andre on 05/05/17.
 */

public class FilterSearchFragment extends Fragment {
    private RecyclerView recyclerView_filter;
    private Button button_reset, button_applyfilter;
    private AlertDialog categoryAlertDialog;
    private AlertDialog.Builder categoryAlertDialogBuilder;

    private ArrayList<FilterSearchResources> filter;
    private String[] listAllCategory;

    private FilterSearchAdapter adapter;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_filter_layout, container, false);
        initializeConstants();
        initializeViews(view);
        initializeAdapters();
        setRecyclerViewProperties();
        buildCategoryDialog();
        initializeClickListeners();
        return view;
    }
    private void initializeConstants() {
        filter = new ArrayList<>();
        listAllCategory = getActivity().getResources().getStringArray(R.array.categories_filter);
    }
    private void initializeViews(View view) {
        recyclerView_filter = (RecyclerView) view.findViewById(R.id.fragment_filter_layout_recyclerview);
        button_applyfilter = (Button) view.findViewById(R.id.fragment_filter_layout_button_filter);
        button_reset = (Button) view.findViewById(R.id.fragment_filter_layout_button_reset);
    }
    private void initializeAdapters() {
        loadAllFilterToArrayList();
        adapter = new FilterSearchAdapter(filter);
    }
    private void initializeClickListeners() {
        button_reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        button_applyfilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //susun jsonarray berdasarkan filter yang sudah dipilih
                if (!FilterManager.getFilter().get(FilterManager.KEY_ID_CATEGORY).equals("0")) FilterManager.IS_FILTER_SPECIFIED = true;
                else FilterManager.IS_FILTER_SPECIFIED = false;

                buildParamsJSONObjectAndSendResult();
            }
        });
    }
    private void setRecyclerViewProperties() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView_filter.setLayoutManager(layoutManager);
        recyclerView_filter.addItemDecoration(new DividerItemDecoration(getActivity(), LinearLayout.VERTICAL));
        recyclerView_filter.setAdapter(adapter);
        recyclerView_filter.addOnItemTouchListener
                (new RecyclerItemClickListener(getActivity(), recyclerView_filter, new OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        showCategoryDialog();
                    }

                    @Override
                    public void onLongItemClick(View view, int position) {
                        showCategoryDialog();
                    }
                }));
    }
    private void buildCategoryDialog() {
        categoryAlertDialogBuilder = new AlertDialog.Builder(getActivity());
        categoryAlertDialogBuilder.setTitle("Kategori")
                .setItems(listAllCategory, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        whenCategoryChosen(which);
                    }
                });
    }
    private void showCategoryDialog() {
        categoryAlertDialog = categoryAlertDialogBuilder.create();
        categoryAlertDialog.show();
    }
    private void whenCategoryChosen(int idx) {
        FilterManager.insertFilter(Integer.toString(idx));
        filter.get(0).setValueFilter(listAllCategory[idx]);
        adapter.notifyDataSetChanged();
    }
    private void loadAllFilterToArrayList() {
        HashMap<String, String> prefs = FilterManager.getFilter();
        for (String key : prefs.keySet()) {
            switch (key) {
                case FilterManager.KEY_ID_CATEGORY :
                    FilterSearchResources newRes = new FilterSearchResources();
                    newRes.setJudulFilter("Kategori");
                    newRes.setKeyFilter(FilterManager.KEY_ID_CATEGORY);
                    int index = Integer.parseInt(prefs.get(FilterManager.KEY_ID_CATEGORY));
                    newRes.setValueFilter(listAllCategory[index]);
                    filter.add(newRes);
            }
        }
    }
    private void buildParamsJSONObjectAndSendResult() {
        JSONArray params = new JSONArray();
        JSONObject paramsObject = new JSONObject();
        try {
            if (FilterManager.IS_FILTER_SPECIFIED) {
                paramsObject.put(FilterManager.KEY_ID_CATEGORY, FilterManager.getFilter().get(FilterManager.KEY_ID_CATEGORY));
                params.put(paramsObject);
            }
            ((FilterSearchActivity) getActivity()).whenFilterSubmitted(params.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
