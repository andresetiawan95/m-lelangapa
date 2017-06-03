package com.lelangapa.app.fragments.search;

import android.os.Bundle;
import android.support.v4.app.Fragment;
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
 * Created by andre on 17/12/16.
 */

public class SearchTextSubmitFragment extends Fragment {
    private String query;
    private DataReceiver queryReceiver;
    private DataReceiver resultReceived;
    private ArrayList<DetailItemResources> searchResult = new ArrayList<>();
    public SearchTextSubmitFragment(){}
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.fragment_main_search_textsubmit_layout, container, false);
        initializeDataReceivers();
        getItemSearchResultOnQuery(query);
        return view;
    }
    public void submitQuery(String query){
        this.query = query;
    }
    public void clearListBarang(){
        this.searchResult.clear();
    }
    private void initializeDataReceivers()
    {
        resultReceived = new DataReceiver() {
            @Override
            public void dataReceived(Object output) {
                String res = output.toString();
                if (res.equals("done"))
                {
                    if(!searchResult.isEmpty())
                    {
                        SearchWithResultFragment noEmptyFragment = new SearchWithResultFragment();
                        noEmptyFragment.setQueryParams(query);
                        noEmptyFragment.setSearchResult(searchResult);
                        getActivity().getSupportFragmentManager().beginTransaction()
                                .replace(R.id.fragment_main_search_textchange, noEmptyFragment)
                                .commit();
                    }
                    else
                    {
                        //jika hasil pencarian tidak ada
                        SearchNoResultFragment emptyFragment = new SearchNoResultFragment();
                        getActivity().getSupportFragmentManager().beginTransaction()
                                .replace(R.id.fragment_main_search_textchange, emptyFragment)
                                .commit();
                    }
                }
            }
        };
        queryReceiver = new DataReceiver() {
            @Override
            public void dataReceived(Object output) {
                String result = output.toString();
                try {
                    JSONObject searchJSON = new JSONObject(result);
                    JSONArray searchArray = searchJSON.getJSONArray("result");
                    for (int i=0;i<searchArray.length();i++){
                        JSONObject searchResultObj = searchArray.getJSONObject(i).getJSONObject("_source");
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
                    resultReceived.dataReceived("done");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };
    }
    private void getItemSearchResultOnQuery(String query){
        MainSearchAPI.QueryKey queryKeyAPI =
                MainSearchAPI.queryKeyInstance(SearchQuery.getInstance().insertQuery(query).insertFromAndSize(0, 4).buildQuery(), queryReceiver);
        RequestController.getInstance(getActivity()).addToRequestQueue(queryKeyAPI);
        //RequestQueue queue = Volley.newRequestQueue(getActivity());
        //queue.add(mainSearchAPI);
    }
}
