package com.lelangapa.android.fragments.favorite;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.lelangapa.android.R;
import com.lelangapa.android.apicalls.favorite.FavoriteAPI;
import com.lelangapa.android.apicalls.singleton.RequestController;
import com.lelangapa.android.interfaces.DataReceiver;
import com.lelangapa.android.preferences.SessionManager;
import com.lelangapa.android.resources.FavoriteResources;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by andre on 22/02/17.
 */

public class FavoriteFragment extends Fragment {
    private ArrayList<FavoriteResources> listItemFavorites;
    private HashMap<String, String> userSession;

    private SessionManager sessionManager;
    private DataReceiver received, whenListFavoriteIsEmptyReceiver;
    private String userID;
    private SwipeRefreshLayout swipeRefreshLayout;

    private FavoriteEmptyFragment emptyFragment;
    private FavoriteNoEmptyFragment noEmptyFragment;

    public FavoriteFragment() {
        listItemFavorites = new ArrayList<>();
    }
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        initializeSession();
        initializeChildFragment();
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_user_favorite_layout, container, false);
        initializeViews(view);
        initializeWhenDataAlreadyReceived();
        initializeWhenListFavoriteIsEmptyReceiver();
        setSwipeRefreshLayoutProperties();
        return view;
    }

    /*
    * Initialization method start here
    * */
    private void initializeSession()
    {
        sessionManager = new SessionManager(getActivity());
        if (sessionManager.isLoggedIn())
        {
            userSession = sessionManager.getSession();
            userID = userSession.get(sessionManager.getKEY_ID());
        }
    }
    private void initializeViews(View view)
    {
        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.fragment_user_favorite_swipe_refreshLayout);
    }
    private void initializeChildFragment()
    {
        emptyFragment = new FavoriteEmptyFragment();
        noEmptyFragment = new FavoriteNoEmptyFragment();
    }
    private void setSwipeRefreshLayoutProperties()
    {
        swipeRefreshLayout.setColorSchemeResources(android.R.color.holo_orange_dark);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                Log.v("RefreshToggle", "Refresh Toggle on FavoriteFragment");
                getDetailItemFavorites(userID);
            }
        });
        swipeRefreshLayout.post(new Runnable() {
            @Override
            public void run() {
                Log.v("RefreshPost", "Refresh POST on FavoriteFragment");
                swipeRefreshLayout.setRefreshing(true);
                getDetailItemFavorites(userID);
            }
        });
    }
    private void initializeWhenDataAlreadyReceived()
    {
        received = new DataReceiver() {
            @Override
            public void dataReceived(Object output) {
                String response = output.toString();
                if (response.equals("done")) {
                    swipeRefreshLayout.setRefreshing(false);
                    if (listItemFavorites.isEmpty()) {
                        //FavoriteEmptyFragment emptyFragment = new FavoriteEmptyFragment();
                        getFragmentManager().beginTransaction()
                                .replace(R.id.fragment_user_favorite_layout, emptyFragment)
                                .commit();
                    }
                    else {
                        //attach fragment recyclerview
                        //FavoriteNoEmptyFragment noEmptyFragment = new FavoriteNoEmptyFragment();
                        swipeRefreshLayout.setEnabled(false);
                        noEmptyFragment.setItemFavoriteList(listItemFavorites);
                        noEmptyFragment.setUserID(userID);
                        noEmptyFragment.setWhenListFavoriteIsEmptyReceiver(whenListFavoriteIsEmptyReceiver);
                        getFragmentManager().beginTransaction()
                                .replace(R.id.fragment_user_favorite_layout, noEmptyFragment)
                                .commit();
                    }
                }
            }
        };
    }
    private void initializeWhenListFavoriteIsEmptyReceiver()
    {
        this.whenListFavoriteIsEmptyReceiver = new DataReceiver() {
            @Override
            public void dataReceived(Object output) {
                //akan dieksekusi ketika listFavorite di FavoriteNoEmptyFragment sudah habis
                swipeRefreshLayout.setEnabled(true);
                getFragmentManager().beginTransaction()
                        .replace(R.id.fragment_user_favorite_layout, emptyFragment)
                        .commit();
            }
        };
    }
    /*
    * Initialization method end here
    * */

    /*
    * Content Provider method start here
    * */
    private void getDetailItemFavorites(String userID)
    {
        DataReceiver dataReceiver = setDataReceiverForItemFavorites();
        //FavoriteAPI favoriteAPI = new FavoriteAPI();
        FavoriteAPI.GetFavorite getFavoriteAPI = FavoriteAPI.initializeGetFavorite(userID, dataReceiver);
        RequestController.getInstance(getActivity()).addToRequestQueue(getFavoriteAPI);
    }
    private DataReceiver setDataReceiverForItemFavorites()
    {
        DataReceiver dataReceiver = new DataReceiver() {
            @Override
            public void dataReceived(Object output) {
                String response = output.toString();
                try {
                    JSONObject jsonResponse = new JSONObject(response);
                    JSONArray jsonResponseArray = jsonResponse.getJSONArray("data");
                    listItemFavorites.clear();
                    for (int i=0;i<jsonResponseArray.length(); i++)
                    {
                        JSONObject jsonArrayObject = jsonResponseArray.getJSONObject(i);
                        FavoriteResources favoriteResources = new FavoriteResources();
                        favoriteResources.setIdFavorite(jsonArrayObject.getString("id_favorite_return"));
                        favoriteResources.setIdItemFavorite(jsonArrayObject.getString("id_item_favorite_return"));
                        favoriteResources.setNamaItemFavorite(jsonArrayObject.getString("nama_item_return"));
                        favoriteResources.setIdUserAuctioneer(jsonArrayObject.getString("id_user_auctioneer_return"));
                        favoriteResources.setNamaUserAuctioneerItemFavorite(jsonArrayObject.getString("nama_user_auctioneer_return"));
                        favoriteResources.setTimeListedItemFavorite(jsonArrayObject.getString("time_listed_return"));
                        JSONArray favoriteJSONArray = jsonArrayObject.getJSONArray("imageurl");
                        for (int j=0;j<favoriteJSONArray.length();j++)
                        {
                            JSONObject imageJSONObject = favoriteJSONArray.getJSONObject(j);
                            favoriteResources.setImageURLItem("http://img-s7.lelangapa.com/" + imageJSONObject.getString("url"));
                            //Log.v("IMAGE GET", favoriteResources.getImageURLItem());
                        }
                        listItemFavorites.add(favoriteResources);
                    }
                    received.dataReceived("done");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };
        return dataReceiver;
    }

    /*
    * Content Provider method end here
    * */
}
