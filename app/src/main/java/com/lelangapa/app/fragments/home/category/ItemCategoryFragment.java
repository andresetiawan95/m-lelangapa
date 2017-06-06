package com.lelangapa.app.fragments.home.category;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.lelangapa.app.R;
import com.lelangapa.app.apicalls.search.MainSearchAPI;
import com.lelangapa.app.apicalls.singleton.RequestController;
import com.lelangapa.app.apicalls.singleton.SearchQuery;
import com.lelangapa.app.interfaces.DataReceiver;
import com.lelangapa.app.resources.DetailItemResources;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by andre on 03/05/17.
 */

public class ItemCategoryFragment extends Fragment {
    private SwipeRefreshLayout swipeRefreshLayout;
    private EmptyFragment emptyFragment;
    private NoEmptyFragment noEmptyFragment;

    private ArrayList<DetailItemResources> listItems;
    private JSONArray paramsArray;
    private DataReceiver whenItemListLoaded;
    private String categoryID;

    public ItemCategoryFragment() {
        initializeConstants();
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getIntentData();
        initializeFragments();
        initializeDataReceiver();
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_item_category_layout, container, false);
        initializeViews(view);
        setSwipeRefreshLayoutProperties();
        return view;
    }
    private void initializeConstants() {
        listItems = new ArrayList<>();
        paramsArray = new JSONArray();
    }
    private void initializeViews(View view) {
        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.fragment_item_category_layout_swipeRefreshLayout);
    }
    private void initializeFragments() {
        emptyFragment = new EmptyFragment();
        noEmptyFragment = new NoEmptyFragment();
    }
    private void initializeDataReceiver() {
        whenItemListLoaded = new DataReceiver() {
            @Override
            public void dataReceived(Object output) {
                if (isResumed()) {
                    String response = output.toString();
                    try {
                        JSONObject jsonResponse = new JSONObject(response);
                        JSONArray jsonResponseArray = jsonResponse.getJSONArray("result");
                        listItems.clear();
                        for (int i=0;i<jsonResponseArray.length();i++) {
                            JSONObject responseObject = jsonResponseArray.getJSONObject(i).getJSONObject("_source");
                            DetailItemResources itemProperty = new DetailItemResources();
                            itemProperty.setIdbarang(responseObject.getString("id_item"));
                            itemProperty.setIdauctioneer(responseObject.getString("id_user"));
                            itemProperty.setNamabarang(responseObject.getString("title"));
                            itemProperty.setNamaauctioneer(responseObject.getString("nama_user"));
                            itemProperty.setHargaawal(responseObject.getString("starting_price"));
                            itemProperty.setHargatarget(responseObject.getString("expected_price"));
                            itemProperty.setIdkategori(responseObject.getString("id_category"));
                            itemProperty.setNamakategori(responseObject.getString("nama_category"));
                            if (responseObject.has("main_image_url")) {
                                itemProperty.setUrlgambarbarang("http://img-s7.lelangapa.com/" + responseObject.getString("main_image_url"));
                            }
                            listItems.add(itemProperty);
                        }
                        whenAllDataLoaded();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        };
    }
    private void getIntentData() {
        categoryID = getActivity().getIntent().getExtras().getString("id_category");
    }
    private void setSwipeRefreshLayoutProperties() {
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                loadDataFromServer();
            }
        });
        swipeRefreshLayout.post(new Runnable() {
            @Override
            public void run() {
                swipeRefreshLayout.setRefreshing(true);
                loadDataFromServer();
            }
        });
    }
    private void whenAllDataLoaded() {
        swipeRefreshLayout.setRefreshing(false);
        swipeRefreshLayout.setEnabled(false);
        if (listItems.isEmpty()) {
            setupFragment(emptyFragment);
        }
        else {
            noEmptyFragment.setInitListItem(listItems);
            setupFragment(noEmptyFragment);
        }
    }
    private void setupFragment(Fragment fragment) {
        getChildFragmentManager().beginTransaction()
                .replace(R.id.fragment_item_category_layout, fragment)
                .commit();
    }
    private void buildPostRequestData(String key, String value) {
        JSONObject reqBodyObject = new JSONObject();
        try {
            reqBodyObject.put(key, value);
            paramsArray.put(reqBodyObject);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    private void loadDataFromServer() {
        MainSearchAPI.QueryBulkCategory queryBulkAPI =
                MainSearchAPI.bulkInstance(
                        SearchQuery.getInstance()
                        .insertQuery(categoryID)
                        .insertFromAndSize(0, 4)
                        .buildQuery(), whenItemListLoaded);
        RequestController.getInstance(getActivity()).addToRequestQueue(queryBulkAPI);
    }
}
