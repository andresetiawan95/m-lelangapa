package com.lelangapa.android.fragments.userpublic.gerai;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.lelangapa.android.R;
import com.lelangapa.android.apicalls.singleton.RequestController;
import com.lelangapa.android.apicalls.userpublic.UserPublicAPI;
import com.lelangapa.android.interfaces.DataReceiver;
import com.lelangapa.android.resources.DetailItemResources;

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
    private DataReceiver dataReceiver;
    private String userID;
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
        getGeraiItems();
        return view;
    }
    private void initializeConstant()
    {
        listGeraiItem = new ArrayList<>();
    }
    private void initializeViews(View view)
    {
        progressBar_loadingData = (ProgressBar) view.findViewById(R.id.fragment_detail_user_public_gerai_layout_progress_bar);
    }
    private void getIntentData()
    {
        userID = getActivity().getIntent().getStringExtra("id_user");
    }
    private void initializeFragments()
    {
        emptyFragment = new EmptyFragment();
        noEmptyFragment = new NoEmptyFragment();
    }
    private void initializeDataReceiver()
    {
        dataReceiver = new DataReceiver() {
            @Override
            public void dataReceived(Object output) {
                String response = output.toString();
                listGeraiItem.clear();
                try {
                    JSONObject jsonResponse = new JSONObject(response);
                    JSONArray responseArray = jsonResponse.getJSONArray("data");
                    for (int i=0;i<responseArray.length();i++) {
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
                    setupFragment();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };
    }
    private void setupFragment()
    {
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
        }
    }
    private void getGeraiItems()
    {
        UserPublicAPI.GetGeraiAPI getGeraiAPI = UserPublicAPI.instanceGeraiAPI(userID, dataReceiver);
        RequestController.getInstance(getActivity()).addToRequestQueue(getGeraiAPI);
    }
}
