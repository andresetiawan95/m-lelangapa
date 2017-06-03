package com.lelangapa.app.fragments.home.category;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.lelangapa.app.R;
import com.lelangapa.app.activities.detail.DetailBarangActivity;
import com.lelangapa.app.adapters.category.ItemCategoryAdapter;
import com.lelangapa.app.apicalls.search.MainSearchAPI;
import com.lelangapa.app.apicalls.singleton.RequestController;
import com.lelangapa.app.apicalls.singleton.SearchQuery;
import com.lelangapa.app.interfaces.DataReceiver;
import com.lelangapa.app.interfaces.OnItemClickListener;
import com.lelangapa.app.listeners.RecyclerItemClickListener;
import com.lelangapa.app.modifiedviews.EndlessRecyclerViewScrollListener;
import com.lelangapa.app.resources.DetailItemResources;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by andre on 03/05/17.
 */

public class NoEmptyFragment extends Fragment {
    private ArrayList<DetailItemResources> listItem;

    private ItemCategoryAdapter adapter;
    private RecyclerView recyclerView_items;

    private static int PAGE_NOW;
    private String categoryID;
    private DataReceiver whenNewItemLoaded;
    private JSONObject newItemJSON;
    private JSONArray newItemJSONArray;
    private Bundle bundleExtras;
    private Intent intent;

    private EndlessRecyclerViewScrollListener scrollListener;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getIntentData();
        initializeDataReceiver();
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_item_category_noempty_layout, container, false);
        initializeViews(view);
        initializeAdapter();
        setupRecyclerViewProperties();
        return view;
    }
    private void getIntentData() {
        categoryID = getActivity().getIntent().getExtras().getString("id_category");
    }
    private void initializeViews(View view) {
        recyclerView_items = (RecyclerView) view.findViewById(R.id.fragment_item_category_layout_recyclerview);
    }
    private void initializeAdapter() {
        adapter = new ItemCategoryAdapter(getActivity(), listItem);
    }
    private void initializeDataReceiver() {
        whenNewItemLoaded = new DataReceiver() {
            @Override
            public void dataReceived(Object output) {
                String response = output.toString();
                try {
                    newItemJSON = new JSONObject(response);
                    newItemJSONArray = newItemJSON.getJSONArray("result");
                    for (int i=0;i<newItemJSONArray.length();i++) {
                        JSONObject newItemJSONArrayObject = newItemJSONArray.getJSONObject(i).getJSONObject("_source");
                        DetailItemResources itemResources = new DetailItemResources();
                        itemResources.setIdbarang(newItemJSONArrayObject.getString("id_item"));
                        itemResources.setIdauctioneer(newItemJSONArrayObject.getString("id_user"));
                        itemResources.setNamabarang(newItemJSONArrayObject.getString("title"));
                        itemResources.setNamaauctioneer(newItemJSONArrayObject.getString("nama_user"));
                        itemResources.setHargaawal(newItemJSONArrayObject.getString("starting_price"));
                        itemResources.setHargatarget(newItemJSONArrayObject.getString("expected_price"));
                        itemResources.setIdkategori(newItemJSONArrayObject.getString("id_category"));
                        itemResources.setNamakategori(newItemJSONArrayObject.getString("nama_category"));
                        if (newItemJSONArrayObject.has("main_image_url")) {
                            itemResources.setUrlgambarbarang("http://img-s7.lelangapa.com/" + newItemJSONArrayObject.getString("main_image_url"));
                        }
                        listItem.add(itemResources);
                    }
                    if (newItemJSONArray.length() > 0) {
                        adapter.notifyItemRangeInserted((4 * PAGE_NOW), 4);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };
    }
    private void initializeScrollListener(GridLayoutManager layoutManager) {
        scrollListener = new EndlessRecyclerViewScrollListener(layoutManager) {
            @Override
            public void onLoadMore(final int page, int totalItemsCount, RecyclerView view) {
                view.post(new Runnable() {
                    @Override
                    public void run() {
                        loadMoreItemFromServer(page);
                    }
                });
            }
        };
    }
    private void setupRecyclerViewProperties() {
        GridLayoutManager layoutManager = new GridLayoutManager(getActivity(), 2);
        initializeScrollListener(layoutManager);
        recyclerView_items.setLayoutManager(layoutManager);
        recyclerView_items.setItemAnimator(new DefaultItemAnimator());
        recyclerView_items.addOnScrollListener(scrollListener);
        recyclerView_items.setAdapter(adapter);
        recyclerView_items.addOnItemTouchListener(new RecyclerItemClickListener(getActivity(), recyclerView_items, new OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                intent = new Intent(getActivity(), DetailBarangActivity.class);
                bundleExtras = new Bundle();
                bundleExtras.putString("auctioneer_id", listItem.get(position).getIdauctioneer());
                bundleExtras.putString("items_id", listItem.get(position).getIdbarang());
                intent.putExtras(bundleExtras);
                startActivity(intent);
            }

            @Override
            public void onLongItemClick(View view, int position) {
                intent = new Intent(getActivity(), DetailBarangActivity.class);
                bundleExtras = new Bundle();
                bundleExtras.putString("auctioneer_id", listItem.get(position).getIdauctioneer());
                bundleExtras.putString("items_id", listItem.get(position).getIdbarang());
                intent.putExtras(bundleExtras);
                startActivity(intent);
            }
        }));
    }
    public void setInitListItem(ArrayList<DetailItemResources> list) {
        this.listItem = list;
    }
    private void loadMoreItemFromServer(int pg) {
        PAGE_NOW = pg;
        MainSearchAPI.QueryBulkCategory bulkAPI =
                MainSearchAPI.bulkInstance(
                        SearchQuery.getInstance()
                        .insertQuery(categoryID)
                        .insertFromAndSize(PAGE_NOW * 4, 4)
                        .buildQuery()
                , whenNewItemLoaded);
        RequestController.getInstance(getActivity()).addToRequestQueue(bulkAPI);
    }
}
