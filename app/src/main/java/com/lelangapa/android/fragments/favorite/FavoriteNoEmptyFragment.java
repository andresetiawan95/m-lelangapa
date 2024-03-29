package com.lelangapa.android.fragments.favorite;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.lelangapa.android.R;
import com.lelangapa.android.activities.detail.DetailBarangActivity;
import com.lelangapa.android.adapters.UserFavoriteAdapter;
import com.lelangapa.android.apicalls.favorite.FavoriteAPI;
import com.lelangapa.android.apicalls.singleton.RequestController;
import com.lelangapa.android.interfaces.DataReceiver;
import com.lelangapa.android.interfaces.OnItemClickListener;
import com.lelangapa.android.resources.FavoriteResources;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by andre on 22/02/17.
 */

public class FavoriteNoEmptyFragment extends Fragment {
    private ArrayList<FavoriteResources> listBarangFavorit;
    private UserFavoriteAdapter favoriteAdapter;
    private RecyclerView recyclerView_favorite;
    private SwipeRefreshLayout swipeRefreshLayout;
    private DataReceiver unfavoriteReceived, favoriteDataReceiver, whenListFavoriteIsEmptyReceiver;
    private OnItemClickListener onItemClickListener;

    private String userID;
    private Bundle bundleExtras;
    private Intent intent;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_user_favorite_noempty_layout, container, false);
        initializeViews(view);
        setDataReceiverForItemFavorites();
        setOnItemClickListener();
        setUnfavoriteReceived();
        setSwipeRefreshLayoutProperties();
        setRecyclerViewAdapter();
        setRecyclerViewProperties();
        return view;
    }
    /*
    * Initialization method start here
    * */
    private void initializeViews(View view)
    {
        recyclerView_favorite = (RecyclerView) view.findViewById(R.id.fragment_user_favorite_layout_recyclerview);
        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.fragment_user_favorite_noempty_swipeRefreshLayout);
    }
    private void setSwipeRefreshLayoutProperties()
    {
        swipeRefreshLayout.setColorSchemeResources(android.R.color.holo_red_light);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                Log.v("RefreshToggle", "Refresh Toggle on noemptyFragment");
                getDetailItemFavorites(userID);
            }
        });
    }
    /*
    * Initialization method end here
    * */
    private void setOnItemClickListener() {
        onItemClickListener = new OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                intent = new Intent(getActivity(), DetailBarangActivity.class);
                bundleExtras = new Bundle();
                bundleExtras.putString("auctioneer_id", listBarangFavorit.get(position).getIdUserAuctioneer());
                bundleExtras.putString("items_id", listBarangFavorit.get(position).getIdItemFavorite());
                intent.putExtras(bundleExtras);

                startActivity(intent);
            }

            @Override
            public void onLongItemClick(View view, int position) {

            }
        };
    }
    public void setUserID(String userID)
    {
        this.userID = userID;
    }
    public void setWhenListFavoriteIsEmptyReceiver(DataReceiver dataReceiver)
    {
        this.whenListFavoriteIsEmptyReceiver = dataReceiver;
    }
    public void setItemFavoriteList(ArrayList<FavoriteResources> listBarangFavorit)
    {
        this.listBarangFavorit = listBarangFavorit;
    }
    private void setRecyclerViewAdapter()
    {
        favoriteAdapter = new UserFavoriteAdapter(getActivity(), listBarangFavorit, userID, unfavoriteReceived, onItemClickListener);
    }
    private void setRecyclerViewProperties()
    {
        RecyclerView.LayoutManager upLayoutManager = new GridLayoutManager(getActivity(), 2);
        recyclerView_favorite.setLayoutManager(upLayoutManager);
        recyclerView_favorite.setAdapter(favoriteAdapter);
        recyclerView_favorite.setItemAnimator(new DefaultItemAnimator());
        /*recyclerView_favorite.addOnItemTouchListener(new RecyclerItemClickListener(getActivity(), recyclerView_favorite, new OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                intent = new Intent(getActivity(), DetailBarangActivity.class);
                bundleExtras = new Bundle();
                bundleExtras.putString("auctioneer_id", listBarangFavorit.get(position).getIdUserAuctioneer());
                bundleExtras.putString("items_id", listBarangFavorit.get(position).getIdItemFavorite());
                intent.putExtras(bundleExtras);

                startActivity(intent);
            }

            @Override
            public void onLongItemClick(View view, int position) {

            }
        }));*/
    }
    /*
    * Content Provider method start here
    * */
    private void getDetailItemFavorites(String userID)
    {
        //DataReceiver dataReceiver = setDataReceiverForItemFavorites();
        //FavoriteAPI favoriteAPI = new FavoriteAPI();
        FavoriteAPI.GetFavorite getFavoriteAPI = FavoriteAPI.initializeGetFavorite(userID, favoriteDataReceiver);
        RequestController.getInstance(getActivity()).addToRequestQueue(getFavoriteAPI);
    }
    private void setDataReceiverForItemFavorites()
    {
        favoriteDataReceiver = new DataReceiver() {
            @Override
            public void dataReceived(Object output) {
                String response = output.toString();
                try {
                    JSONObject jsonResponse = new JSONObject(response);
                    JSONArray jsonResponseArray = jsonResponse.getJSONArray("data");
                    listBarangFavorit.clear();
                    for (int i=0;i<jsonResponseArray.length(); i++)
                    {
                        JSONObject jsonArrayObject = jsonResponseArray.getJSONObject(i);
                        FavoriteResources favoriteResources = new FavoriteResources();
                        favoriteResources.setIdFavorite(jsonArrayObject.getString("id_favorite_return"));
                        favoriteResources.setIdItemFavorite(jsonArrayObject.getString("id_item_favorite_return"));
                        favoriteResources.setNamaItemFavorite(jsonArrayObject.getString("nama_item_return"));
                        favoriteResources.setNamaUserAuctioneerItemFavorite(jsonArrayObject.getString("nama_user_auctioneer_return"));
                        favoriteResources.setTimeListedItemFavorite(jsonArrayObject.getString("time_listed_return"));

                        JSONArray favoriteJSONArray = jsonArrayObject.getJSONArray("imageurl");
                        for (int j=0;j<favoriteJSONArray.length();j++)
                        {
                            JSONObject imageJSONObject = favoriteJSONArray.getJSONObject(j);
                            favoriteResources.setImageURLItem("http://img-s7.lelangapa.com/" + imageJSONObject.getString("url"));
                            //Log.v("IMAGE GET", favoriteResources.getImageURLItem());
                        }
                        listBarangFavorit.add(favoriteResources);
                    }
                    if (listBarangFavorit.isEmpty())
                    {
                        favoriteAdapter.updateDataSet(listBarangFavorit);
                        swipeRefreshLayout.setRefreshing(false);
                        whenListFavoriteIsEmptyReceiver.dataReceived("done");
                    }
                    else {
                        favoriteAdapter.updateDataSet(listBarangFavorit);
                        swipeRefreshLayout.setRefreshing(false);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };
    }
    private void setUnfavoriteReceived()
    {
        unfavoriteReceived = new DataReceiver() {
            @Override
            public void dataReceived(Object output) {
                swipeRefreshLayout.setRefreshing(true);
                getDetailItemFavorites(userID);
            }
        };
    }
    /*
    * Content Provider method end here
    * */
}
