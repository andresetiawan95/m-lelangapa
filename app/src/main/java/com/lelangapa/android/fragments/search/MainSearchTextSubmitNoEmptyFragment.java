package com.lelangapa.android.fragments.search;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.lelangapa.android.R;
import com.lelangapa.android.activities.detail.DetailBarangActivity;
import com.lelangapa.android.activities.search.MainSearchActivity;
import com.lelangapa.android.activities.search.filter.FilterSearchActivity;
import com.lelangapa.android.adapters.MainSearchAdapter;
import com.lelangapa.android.apicalls.search.MainSearchAPI;
import com.lelangapa.android.apicalls.singleton.RequestController;
import com.lelangapa.android.apicalls.singleton.SearchQuery;
import com.lelangapa.android.interfaces.DataReceiver;
import com.lelangapa.android.interfaces.OnItemClickListener;
import com.lelangapa.android.listeners.RecyclerItemClickListener;
import com.lelangapa.android.modifiedviews.EndlessRecyclerViewScrollListener;
import com.lelangapa.android.resources.DetailItemResources;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Andre on 12/17/2016.
 */

public class MainSearchTextSubmitNoEmptyFragment extends Fragment {
    private ArrayList<DetailItemResources> searchResult;
    private RecyclerView searchResultRecycleView;
    private MainSearchAdapter searchAdapter;
    private FrameLayout frameLayout_Filter;

    private DataReceiver whenNewSearchDataIsLoaded, whenFilterChoosen;
    private JSONObject newSearchJSON;
    private JSONArray newSearchJSONArray;
    private static int PAGE_NOW;
    private static String queryString, paramsString;
    private String jsonResponse;
    private static final int REQUEST_FILTER = 1;
    private static boolean IS_FILTER_APPLIED;

    private EndlessRecyclerViewScrollListener scrollListener;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_main_search_textsubmit_layout_noempty, container, false);
        noAppliedFilter();
        initializeViews(view);
        initializeClickListener();
        initializeDataReceivers();
        initializeAdapters();
        setRecyclerViewProperties();
        return view;
    }
    @Override
    public void onResume() {
        super.onResume();
        ((MainSearchActivity) getActivity()).clearSearchViewFocus();
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_FILTER && resultCode == Activity.RESULT_OK && data!= null) {
            paramsString = data.getExtras().getString("params");
            boolean is_specified = data.getExtras().getBoolean("is_filter_specified");
            Toast.makeText(getActivity(), paramsString, Toast.LENGTH_SHORT).show();
            /*if (is_specified) {
                filterApplied();
                loadNewFilteredDataFromServer(0);
            }
            else {
                noAppliedFilter();
                loadNewDataFromServer(0);
            }*/
        }
    }
    @Override
    public void onDestroy() {
        scrollListener.resetState();
        noAppliedFilter();
        super.onDestroy();
    }
    private void noAppliedFilter() {
        IS_FILTER_APPLIED = false;
    }
    private void filterApplied() {
        IS_FILTER_APPLIED = true;
    }
    private void initializeViews(View view) {
        searchResultRecycleView = (RecyclerView) view.findViewById(R.id.fragment_main_search_layout_recyclerview);
        frameLayout_Filter = (FrameLayout) view.findViewById(R.id.fragment_main_search_layout_filter);
    }
    private void initializeClickListener() {
        frameLayout_Filter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent filterIntent = new Intent(getActivity(), FilterSearchActivity.class);
                startActivityForResult(filterIntent, REQUEST_FILTER);
            }
        });
    }
    private void initializeDataReceivers() {
        whenNewSearchDataIsLoaded = new DataReceiver() {
            @Override
            public void dataReceived(Object output) {
                jsonResponse = output.toString();
                try {
                    newSearchJSON = new JSONObject(jsonResponse);
                    newSearchJSONArray = newSearchJSON.getJSONArray("result");
                    for (int i=0;i<newSearchJSONArray.length();i++){
                        JSONObject searchResultObj = newSearchJSONArray.getJSONObject(i).getJSONObject("_source");
                        DetailItemResources searchProperty = new DetailItemResources();
                        searchProperty.setIdbarang(searchResultObj.getString("id_item"));
                        searchProperty.setIdauctioneer(searchResultObj.getString("id_user"));
                        searchProperty.setNamabarang(searchResultObj.getString("title"));
                        searchProperty.setNamaauctioneer(searchResultObj.getString("nama_user"));
                        searchProperty.setHargaawal(searchResultObj.getString("starting_price"));
                        searchProperty.setHargatarget(searchResultObj.getString("expected_price"));
                        searchProperty.setIdkategori(searchResultObj.getString("id_category"));
                        searchProperty.setNamakategori(searchResultObj.getString("nama_category"));
                        if (searchResultObj.has("main_image_url")) {
                            searchProperty.setUrlgambarbarang("http://img-s7.lelangapa.com/" + searchResultObj.getString("main_image_url"));
                        }
                        searchResult.add(searchProperty);
                    }
                    if (newSearchJSONArray.length() > 0) {
                        searchAdapter.notifyItemRangeInserted((4 * PAGE_NOW), 4);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };
        whenFilterChoosen = new DataReceiver() {
            @Override
            public void dataReceived(Object output) {

            }
        };
    }
    private void initializeAdapters() {
        searchAdapter = new MainSearchAdapter(getActivity(), searchResult);
    }
    private void setRecyclerViewProperties() {
        GridLayoutManager searchLayoutManager = new GridLayoutManager(getActivity(), 2);
        scrollListener = new EndlessRecyclerViewScrollListener(searchLayoutManager) {
            @Override
            public void onLoadMore(final int page, int totalItemsCount, RecyclerView view) {
                view.post(new Runnable() {
                    @Override
                    public void run() {
                        loadNewDataFromServer(page);
                    }
                });
            }
        };
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
    public void setQueryParams(String query) {
        queryString = query;
    }
    public void setSearchResult(ArrayList<DetailItemResources> searchResult)
    {
        this.searchResult = searchResult;
    }
    private void loadNewDataFromServer(int pg) {
        PAGE_NOW = pg;
        MainSearchAPI.QueryKey queryKeyAPI =
                MainSearchAPI.queryKeyInstance(SearchQuery.getInstance()
                        .insertQuery(queryString).insertFromAndSize((PAGE_NOW * 4), 4).buildQuery(), whenNewSearchDataIsLoaded);
        RequestController.getInstance(getActivity()).addToRequestQueue(queryKeyAPI);
    }
    private void loadNewFilteredDataFromServer(int pg) {
        PAGE_NOW = pg;
        MainSearchAPI.QueryKeyWithParams paramsAPI =
                MainSearchAPI.queryParamsInstance(
                        SearchQuery.getInstance()
                                .insertQuery(queryString)
                                .insertFromAndSize(pg * 4, 4)
                                .insertFilterParams(paramsString)
                                .buildQuery()
                        , whenFilterChoosen);
        RequestController.getInstance(getActivity()).addToRequestQueue(paramsAPI);
    }
}
