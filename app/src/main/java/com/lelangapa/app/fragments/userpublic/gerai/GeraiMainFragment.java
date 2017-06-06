package com.lelangapa.app.fragments.userpublic.gerai;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.lelangapa.app.R;
import com.lelangapa.app.apicalls.search.MainSearchAPI;
import com.lelangapa.app.apicalls.singleton.RequestController;
import com.lelangapa.app.apicalls.singleton.SearchQuery;
import com.lelangapa.app.apicalls.userpublic.UserPublicAPI;
import com.lelangapa.app.interfaces.DataReceiver;
import com.lelangapa.app.interfaces.OnLoadMore;
import com.lelangapa.app.interfaces.QueryListener;
import com.lelangapa.app.preferences.FilterManager;
import com.lelangapa.app.resources.DetailItemResources;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by andre on 28/03/17.
 */

public class GeraiMainFragment extends Fragment {
    private ProgressBar progressBar_loadingData;

    private EmptyFragment emptyFragment;
    private NoEmptyFragment noEmptyFragment;

    private ArrayList<DetailItemResources> listGeraiItem;
    private DataReceiver loadItemWithoutKeywordQuery, loadItemWithKeywordQuery;
    private OnLoadMore onLoadMore;
    private QueryListener queryListener;
    private String userID;
    private static int PAGE_NOW;
    private static final String TAG_NO_EMPTY = "NO_EMPTY";
    private static final String TAG_EMPTY = "EMPTY";
    private static String queryString, paramsString;
    private JSONObject newSearchJSON;
    private JSONArray newSearchJSONArray;
    private String jsonResponse;

    public GeraiMainFragment()
    {
        initializeConstant();
    }
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        initializeFragments();
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_detail_user_public_gerai_layout, container, false);
        initializeViews(view);
        getIntentData();
        initializeDataReceiver();
        initializeQueryListener();
        initializeOnLoadMore();
        loadDataFromServer(PAGE_NOW, loadItemWithoutKeywordQuery);
        return view;
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        queryString = null;
        paramsString = null;
    }
    private void initializeConstant()
    {
        listGeraiItem = new ArrayList<>();
        PAGE_NOW = 0;
    }
    private void initializeViews(View view)
    {
        progressBar_loadingData = (ProgressBar) view.findViewById(R.id.fragment_detail_user_public_gerai_layout_progress_bar);
    }
    private void getIntentData()
    {
        userID = getActivity().getIntent().getExtras().getString("id_user");
    }
    private void initializeFragments()
    {
        emptyFragment = new EmptyFragment();
        noEmptyFragment = new NoEmptyFragment();
    }
    private void initializeOnLoadMore() {
        onLoadMore = new OnLoadMore() {
            @Override
            public void loadPage(int page) {
                if (TextUtils.isEmpty(queryString)) loadDataFromServer(page, loadItemWithoutKeywordQuery);
                else loadDataFromServer(page, loadItemWithKeywordQuery);
            }
        };
    }
    private void initializeDataReceiver()
    {
        loadItemWithoutKeywordQuery = new DataReceiver() {
            @Override
            public void dataReceived(Object output) {
                if (isResumed()) {
                    jsonResponse = output.toString();
                    try {
                        newSearchJSON= new JSONObject(jsonResponse);
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
                            listGeraiItem.add(searchProperty);
                        }
                        loadLogic();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        };
        loadItemWithKeywordQuery = new DataReceiver() {
            @Override
            public void dataReceived(Object output) {
                if (isResumed()) {
                    jsonResponse = output.toString();
                    try {
                        newSearchJSON= new JSONObject(jsonResponse);
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
                            listGeraiItem.add(searchProperty);
                        }
                        loadLogicOnSearch();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        };
    }
    private void initializeQueryListener() {
        queryListener = new QueryListener() {
            @Override
            public void onQuerySubmit(String query) {
                invalidateAllData();
                queryString = query;
                PAGE_NOW = 0;
                buildParamsStringOnSearch();
                loadDataFromServer(PAGE_NOW, loadItemWithKeywordQuery);
            }
        };
    }
    private void invalidateAllData() {
        listGeraiItem.clear();
        noEmptyFragment.invalidateData();
    }
    private void setupFragment(Fragment fragment, String tag) {
        getChildFragmentManager().beginTransaction()
                .replace(R.id.fragment_detail_user_public_gerai_layout, fragment, tag)
                .commit();
    }
    private void loadLogic()
    {
        /* OLD LOGIC WHEN STILL USING no-api
        progressBar_loadingData.setVisibility(View.GONE);
        if (listGeraiItem.isEmpty()) {
            getChildFragmentManager().beginTransaction()
                    .replace(R.id.fragment_detail_user_public_gerai_layout, emptyFragment)
                    .commit();
        }
        else {
            noEmptyFragment.setListGeraiItem(listGeraiItem);
            getChildFragmentManager().beginTransaction()
                    .replace(R.id.fragment_detail_user_public_gerai_layout, noEmptyFragment)
                    .commit();
        }*/
        if (newSearchJSONArray.length() > 0) {
            //kalau ada hasil search
            if (noEmptyFragment != null && !noEmptyFragment.isVisible()) {
                Log.d("SETUP FRAGMENT", "SETUP NO EMPTY FRAGMENT");
                progressBar_loadingData.setVisibility(View.GONE);
                noEmptyFragment.setListGeraiItem(listGeraiItem);
                noEmptyFragment.setOnLoadMore(onLoadMore);
                noEmptyFragment.setQueryListener(queryListener);
                setupFragment(noEmptyFragment, TAG_NO_EMPTY);
            }
            else if (noEmptyFragment != null) {
                noEmptyFragment.notifyWhenNewItemInserted(PAGE_NOW * 4);
            }
        }
        else {
            //kalau ngga ada hasil search
            if (PAGE_NOW == 0 && emptyFragment!=null && !emptyFragment.isVisible()) {
                //kalau ngga ada barang sama sekali sejak awal load
                progressBar_loadingData.setVisibility(View.GONE);
                setupFragment(emptyFragment, TAG_EMPTY);
            }
        }
    }
    private void loadLogicOnSearch() {
        if (newSearchJSONArray.length() > 0) {
            noEmptyFragment.notifyWhenNewItemInserted(PAGE_NOW * 4);
        }
        else {
            if (PAGE_NOW == 0) Toast.makeText(getActivity(), "Pencarian tidak ditemukan.", Toast.LENGTH_SHORT).show();
        }
    }
    /*private void loadDataFromServer()
    {
        UserPublicAPI.GetGeraiAPI getGeraiAPI = UserPublicAPI.instanceGeraiAPI(userID, loadItemWithoutKeywordQuery);
        RequestController.getInstance(getActivity()).addToRequestQueue(getGeraiAPI);
    }*/
    private void buildParamsStringOnSearch() {
        JSONArray paramsArray = new JSONArray();
        JSONObject paramsObject = new JSONObject();
        try {
            paramsObject.put(FilterManager.KEY_USER_ID, userID);
            paramsArray.put(paramsObject);
            paramsString = paramsArray.toString();
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }
    private void loadDataFromServer(int pg, DataReceiver dataReceiver) {
        PAGE_NOW = pg;
        Log.d("PAGE LOAD", Integer.toString(pg));

        if (!TextUtils.isEmpty(queryString)) {
            MainSearchAPI.QueryKeyWithParams searchGeraiAPI = MainSearchAPI.queryParamsInstance(
                    SearchQuery.getInstance()
                            .insertQuery(queryString)
                            .insertFilterParams(paramsString)
                            .insertFromAndSize((PAGE_NOW * 4), 4)
                            .buildQuery()
                    , dataReceiver);
            RequestController.getInstance(getActivity()).addToRequestQueue(searchGeraiAPI);
        }
        else {
            UserPublicAPI.GetGeraiAPI getGeraiAPI = UserPublicAPI.instanceGeraiAPI(
                    SearchQuery.getInstance()
                            .insertUserID(userID)
                            .insertFromAndSize((PAGE_NOW * 4), 4)
                            .buildQuery()
                    , dataReceiver);
            RequestController.getInstance(getActivity()).addToRequestQueue(getGeraiAPI);
        }

    }
    /**
     * old loadItemWithoutKeywordQuery logic (when still using no-api
     * bisa dihapus kalau sudah ga diperluka
     * datareceiver:
     * for (int i=0;i<responseArray.length();i++) {
         JSONObject respArrayObject = responseArray.getJSONObject(i);
         DetailItemResources detailItem = new DetailItemResources();
         detailItem.setIdbarang(respArrayObject.getString("items_id"));
         detailItem.setNamabarang(respArrayObject.getString("items_name"));
         detailItem.setNamaauctioneer(respArrayObject.getString("user_name"));
         detailItem.setHargaawal(respArrayObject.getString("starting_price"));
         detailItem.setHargatarget(respArrayObject.getString("expected_price"));
         JSONArray searchPropertyImageArray = respArrayObject.getJSONArray("url");
         for (int x=0;x<searchPropertyImageArray.length();x++) {
         JSONObject searchPropertyImageObj = searchPropertyImageArray.getJSONObject(x);
         detailItem.setUrlgambarbarang("http://img-s7.lelangapa.com/" + searchPropertyImageObj.getString("url"));
         }
         listGeraiItem.add(detailItem);
     }
     *
     **/
}
