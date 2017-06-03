package com.lelangapa.app.fragments.detail.ownerblocklist;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.lelangapa.app.R;
import com.lelangapa.app.apicalls.detail.daftarblock.GetBlockAPI;
import com.lelangapa.app.apicalls.singleton.RequestController;
import com.lelangapa.app.interfaces.DataReceiver;
import com.lelangapa.app.resources.BlockResources;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by andre on 19/04/17.
 */

public class DaftarBlockFragment extends Fragment {
    private ArrayList<BlockResources> listBlock;

    private SwipeRefreshLayout swipeRefreshLayout;
    private EmptyFragment emptyFragment;
    private NoEmptyFragment noEmptyFragment;

    private DataReceiver whenBlockListLoaded, whenUserUnblocked;
    private UnblockToggler unblockToggler;

    private String itemID, auctioneerID;
    private Integer bidTime;

    public DaftarBlockFragment()
    {
        initializeConstants();
    }
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        initializeBundle();
        initializeDataReceivers();
        initializeChildFragments();
        initializeToggler();
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_detail_barang_daftar_block_layout, container, false);
        initializeViews(view);
        setSwipeRefreshLayoutProperties();
        return view;
    }
    private void initializeConstants()
    {
        listBlock = new ArrayList<>();
    }
    private void initializeBundle()
    {
        Bundle bundleExtras = getActivity().getIntent().getExtras();
        itemID = bundleExtras.getString("id_item");
        auctioneerID = bundleExtras.getString("id_auctioneer");
        bidTime = bundleExtras.getInt("bid_time");
    }
    private void initializeViews(View view)
    {
        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.fragment_detail_barang_daftar_block_swipe_refreshLayout);
    }
    private void initializeChildFragments()
    {
        emptyFragment = new EmptyFragment();
        noEmptyFragment = new NoEmptyFragment();
    }
    private void initializeToggler()
    {
        unblockToggler = new UnblockToggler(getActivity(), whenUserUnblocked, itemID, auctioneerID, bidTime);
        noEmptyFragment.setUnblockToggler(unblockToggler);
    }
    private void initializeDataReceivers()
    {
        whenBlockListLoaded = new DataReceiver() {
            @Override
            public void dataReceived(Object output) {
                //ketika daftar block sudah loaded dari server
                String response = output.toString();
                try {
                    JSONObject jsonResponse = new JSONObject(response);
                    if (jsonResponse.getString("status").equals("success")) {
                        JSONArray responseArray = jsonResponse.getJSONArray("data");
                        listBlock.clear();
                        for (int i=0;i<responseArray.length();i++) {
                            JSONObject responseArrayObject = responseArray.getJSONObject(i);
                            BlockResources blockResources = new BlockResources();
                            blockResources.setIdBlock(responseArrayObject.getString("id_block_return"));
                            blockResources.setIdUserBlocked(responseArrayObject.getString("id_user_blocked_return"));
                            blockResources.setNamaUserBlocked(responseArrayObject.getString("nama_user_blocked_return"));
                            listBlock.add(blockResources);
                        }
                        whenBlockListLoaded();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };
        whenUserUnblocked = new DataReceiver() {
            @Override
            public void dataReceived(Object output) {
                //ketika user telah di unblock oleh auctioneer
                String response = output.toString();
                try {
                    JSONObject jsonResponse = new JSONObject(response);
                    if (jsonResponse.getString("status").equals("success")) {
                        setWhenUserUnblockedReceiverOnNoEmptyFragment();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };
    }
    private void setSwipeRefreshLayoutProperties()
    {
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getBlockListFromServer();
            }
        });
        swipeRefreshLayout.post(new Runnable() {
            @Override
            public void run() {
                swipeRefreshLayout.setRefreshing(true);
                getBlockListFromServer();
            }
        });
    }
    private void setWhenUserUnblockedReceiverOnNoEmptyFragment()
    {
        //unshow progress dialog
        unblockToggler.unshowProgressDialog();
        //enable swipe refresh, set refreshing true
        swipeRefreshLayout.setEnabled(true);
        swipeRefreshLayout.setRefreshing(true);
        //load block offer
        getBlockListFromServer();
    }
    private void whenBlockListLoaded()
    {
        swipeRefreshLayout.setRefreshing(false);
        swipeRefreshLayout.setEnabled(false);
        if (listBlock.isEmpty()) {
            setupFragment(emptyFragment);
        }
        else {
            noEmptyFragment.setListBlock(listBlock);
            setupFragment(noEmptyFragment);
        }
    }
    private void setupFragment(Fragment fragment)
    {
        getChildFragmentManager().beginTransaction()
                .replace(R.id.fragment_detail_barang_daftar_block_layout, fragment)
                .commit();
    }
    private void getBlockListFromServer()
    {
        String urlparams = "/item/" + itemID + "/bidtime/" + bidTime + "/list";
        GetBlockAPI.GetBlockList getBlockListAPI = GetBlockAPI.instanceGetBlockList(urlparams, whenBlockListLoaded);
        RequestController.getInstance(getActivity()).addToRequestQueue(getBlockListAPI);
    }
}
