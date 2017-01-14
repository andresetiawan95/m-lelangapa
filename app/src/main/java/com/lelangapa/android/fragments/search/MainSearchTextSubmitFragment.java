package com.lelangapa.android.fragments.search;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.lelangapa.android.R;
import com.lelangapa.android.apicalls.search.MainSearchAPI;
import com.lelangapa.android.interfaces.DataReceiver;
import com.lelangapa.android.resources.DetailItemResources;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by andre on 17/12/16.
 */

public class MainSearchTextSubmitFragment extends Fragment {
    private String query;
    private DataReceiver queryReceiver;
    private DataReceiver resultReceived;
    private ArrayList<DetailItemResources> searchResult = new ArrayList<>();
    public MainSearchTextSubmitFragment(){}
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.fragment_main_search_textsubmit_layout, container, false);
        resultReceived = new DataReceiver() {
            @Override
            public void dataReceived(Object output) {
                String res = output.toString();
                if (res.equals("done"))
                {
                    //MASIH NGEBUG DISINI. MENGHASILKAN NULL POINTER EXCEPTION WAKTU KONEKSI INTERNET TIDAK STABIL
                    if(!searchResult.isEmpty())
                    {
                        MainSearchTextSubmitNoEmptyFragment noEmptyFragment = new MainSearchTextSubmitNoEmptyFragment();
                        noEmptyFragment.setSearchResult(searchResult);
                        getActivity().getSupportFragmentManager().beginTransaction()
                                .replace(R.id.fragment_main_search_textchange, noEmptyFragment)
                                .commit();
                    }
                    else
                    {
                        //implement later
                        //jika hasil pencarian tidak ada
                    }
                }
            }
        };
        getItemSearchResultOnQuery(query);
        return view;
    }
    public void submitQuery(String query){
        this.query = query;
    }
    public void clearListBarang(){
        this.searchResult.clear();
    }
    private void getItemSearchResultOnQuery(String query){
        queryReceiver = new DataReceiver() {
            @Override
            public void dataReceived(Object output) {
                String result = output.toString();
                try {
                    JSONObject searchJSON = new JSONObject(result);
                    String resStatus = searchJSON.getString("status");
                    if (resStatus.equals("success")){
                        JSONArray searchArray = searchJSON.getJSONArray("data");
                        for (int i=0;i<searchArray.length();i++){
                            JSONObject searchResultObj = searchArray.getJSONObject(i);
                            DetailItemResources searchProperty = new DetailItemResources();
                            searchProperty.setIdbarang(searchResultObj.getString("items_id"));
                            searchProperty.setNamabarang(searchResultObj.getString("items_name"));
                            searchProperty.setNamaauctioneer(searchResultObj.getString("user_name"));
                            searchProperty.setHargaawal(searchResultObj.getString("starting_price"));
                            searchProperty.setHargatarget(searchResultObj.getString("expected_price"));
                            JSONArray searchPropertyImageArray = searchResultObj.getJSONArray("url");
                            for (int x=0;x<searchPropertyImageArray.length();x++) {
                                JSONObject searchPropertyImageObj = searchPropertyImageArray.getJSONObject(x);
                                searchProperty.setUrlgambarbarang("http://es3.lelangapa.com/" + searchPropertyImageObj.getString("url"));
                            }
                            searchResult.add(searchProperty);
                        }
                        resultReceived.dataReceived("done");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };
        MainSearchAPI mainSearchAPI = new MainSearchAPI(query, queryReceiver);
        RequestQueue queue = Volley.newRequestQueue(getActivity());
        queue.add(mainSearchAPI);
    }
}
