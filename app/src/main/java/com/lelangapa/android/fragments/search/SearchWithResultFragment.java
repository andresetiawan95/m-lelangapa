package com.lelangapa.android.fragments.search;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.lelangapa.android.R;
import com.lelangapa.android.activities.search.MainSearchActivity;
import com.lelangapa.android.activities.search.filter.FilterSearchActivity;
import com.lelangapa.android.apicalls.search.MainSearchAPI;
import com.lelangapa.android.apicalls.singleton.RequestController;
import com.lelangapa.android.apicalls.singleton.SearchQuery;
import com.lelangapa.android.fragments.search.result.ResultEmpty;
import com.lelangapa.android.fragments.search.result.ResultNoEmpty;
import com.lelangapa.android.interfaces.DataReceiver;
import com.lelangapa.android.interfaces.OnLoadMore;
import com.lelangapa.android.preferences.FilterManager;
import com.lelangapa.android.resources.DetailItemResources;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Andre on 12/17/2016.
 */

public class SearchWithResultFragment extends Fragment {
    private ArrayList<DetailItemResources> searchResult;
    private FrameLayout frameLayout_Filter;
    private ResultEmpty resultEmpty;
    private ResultNoEmpty resultNoEmpty;

    private DataReceiver whenNewSearchDataIsLoaded, whenFilterChoosen;
    private OnLoadMore onLoadMore;
    private JSONObject newSearchJSON;
    private JSONArray newSearchJSONArray;
    private static int PAGE_NOW;
    private static String queryString, paramsString;
    private String jsonResponse;
    private static final int REQUEST_FILTER = 1;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_main_search_textsubmit_layout_noempty, container, false);
        initializeViews(view);
        initializeChildFragments();
        initializeClickListener();
        initializeDataReceivers();
        initializeOnLoadMore();
        setupInitialFragment();
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
            paramsString = data.getStringExtra("params");
            //Toast.makeText(getActivity(), paramsString + " " + FilterManager.IS_FILTER_SPECIFIED, Toast.LENGTH_SHORT).show();
            if (FilterManager.IS_FILTER_SPECIFIED) {
                invalidateAllData();
                PAGE_NOW = 0; //load data dari mulai awal
                loadNewFilteredDataFromServer(PAGE_NOW);
            }
            else {
                invalidateAllData();
                PAGE_NOW = 0; //load data dari mulai awal
                loadNewDataFromServer(PAGE_NOW);
            }
        }
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
    }
    private void initializeViews(View view) {
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
                    loadLogic();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };
    }
    private void initializeChildFragments() {
        resultEmpty = new ResultEmpty();
        resultNoEmpty = new ResultNoEmpty();
    }
    private void initializeOnLoadMore() {
        onLoadMore = new OnLoadMore() {
            @Override
            public void loadPage(int page) {
                if (FilterManager.IS_FILTER_SPECIFIED) loadNewFilteredDataFromServer(page);
                else loadNewDataFromServer(page);
            }
        };
    }
    private void setupInitialFragment() {
        resultNoEmpty.setSearchResult(searchResult);
        resultNoEmpty.setLoadMoreInterface(onLoadMore);
        setupFragment(resultNoEmpty, "RESULT_NO_EMPTY");
    }
    private void setupFragment(Fragment fragment, String tag) {
        getChildFragmentManager().beginTransaction()
                .replace(R.id.fragment_main_search_layout_search_result, fragment, tag)
                .commit();
    }
    private void invalidateAllData() {
        searchResult.clear();
        resultNoEmpty.invalidateData();
    }
    private void loadLogic() {
        if (newSearchJSONArray.length() > 0) {
            if (PAGE_NOW == 0) {
                setupFragment(resultNoEmpty, "RESULT_NO_EMPTY");
            }
            resultNoEmpty.notifyWhenNewItemInserted(PAGE_NOW * 4);
        }
        else {
            if (PAGE_NOW == 0) {
                setupFragment(resultEmpty, "RESULT_EMPTY");
            }
        }
    }
    public void setQueryParams(String query) {
        queryString = query;
    }
    public void setSearchResult(ArrayList<DetailItemResources> searchResult)
    {
        this.searchResult = searchResult;
    }
    private void loadNewDataFromServer(int pg) {
        Log.v("LOAD PAGE ", "PAGE KE " + pg);
        PAGE_NOW = pg;
        MainSearchAPI.QueryKey queryKeyAPI =
                MainSearchAPI.queryKeyInstance(SearchQuery.getInstance()
                        .insertQuery(queryString).insertFromAndSize((PAGE_NOW * 4), 4).buildQuery(), whenNewSearchDataIsLoaded);
        RequestController.getInstance(getActivity()).addToRequestQueue(queryKeyAPI);
    }
    private void loadNewFilteredDataFromServer(int pg) {
        Log.v("LOAD PAGE ", "PAGE KE " + pg);
        PAGE_NOW = pg;
        MainSearchAPI.QueryKeyWithParams paramsAPI =
                MainSearchAPI.queryParamsInstance(
                        SearchQuery.getInstance()
                                .insertQuery(queryString)
                                .insertFromAndSize(PAGE_NOW * 4, 4)
                                .insertFilterParams(paramsString)
                                .buildQuery()
                        , whenNewSearchDataIsLoaded);
        RequestController.getInstance(getActivity()).addToRequestQueue(paramsAPI);
    }
}
