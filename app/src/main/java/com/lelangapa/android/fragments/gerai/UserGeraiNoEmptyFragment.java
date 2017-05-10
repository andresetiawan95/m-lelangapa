package com.lelangapa.android.fragments.gerai;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.lelangapa.android.R;
import com.lelangapa.android.activities.UserGeraiActivity;
import com.lelangapa.android.activities.detail.DetailBarangActivity;
import com.lelangapa.android.adapters.UserGeraiBarangAdapter;
import com.lelangapa.android.apicalls.gerai.GetBarangGeraiUserAPI;
import com.lelangapa.android.apicalls.singleton.RequestController;
import com.lelangapa.android.fragments.gerai.toggler.DeleteToggler;
import com.lelangapa.android.interfaces.DataReceiver;
import com.lelangapa.android.interfaces.OnItemClickListener;
import com.lelangapa.android.preferences.SessionManager;
import com.lelangapa.android.resources.UserGeraiResources;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by andre on 06/12/16.
 */

public class UserGeraiNoEmptyFragment extends Fragment {
    private ArrayList<UserGeraiResources> dataBarang;
    private UserGeraiBarangAdapter barangAdapter;
    private OnItemClickListener onItemClickListener;
    private DeleteToggler deleteToggler;
    private DataReceiver whenItemDeleted, whenItemsFromServerLoaded;
    private AlertDialog.Builder alertDialogBuilderDoneDeleted;
    private AlertDialog alertDialog;

    private RecyclerView recyclerView;
    private SwipeRefreshLayout swipeRefreshLayout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.fragment_user_gerai_layout_notempty, container, false);
        initializeViews(view);
        initializeDataReceiver();
        initializeAlertDialogDoneDeleted();
        initializeToggler();
        disableSwipeRefreshLayout();
        setOnItemClickListener();
        initializeAdapter();
        setRecyclerViewProperties();
        return view;
    }
    private void initializeViews(View view) {
        recyclerView = (RecyclerView) view.findViewById(R.id.fragment_user_gerai_layout_recyclerview);
        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.fragment_user_gerai_layout_noempty_swipeRefreshLayout);
    }
    private void initializeAdapter() {
        barangAdapter = new UserGeraiBarangAdapter(getActivity(), dataBarang, onItemClickListener, deleteToggler);
    }
    private void initializeToggler() {
        deleteToggler = new DeleteToggler(getActivity(), whenItemDeleted);
    }
    private void initializeAlertDialogDoneDeleted() {
        alertDialogBuilderDoneDeleted = new AlertDialog.Builder(getActivity());
        alertDialogBuilderDoneDeleted.setTitle(R.string.DELETE_ITEM_GERAI_DONE_ALERTDIALOGTITLE)
                .setMessage(R.string.DELETE_ITEM_GERAI_DONE_ALERTDIALOGMSG)
                .setPositiveButton(R.string.DELETE_ITEM_GERAI_DONE_ALERTDIALOGOKBUTTON, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        whenItemDeletedReceived();
                    }
                });
    }
    public void showAlertDialogDoneDeleted() {
        alertDialog = alertDialogBuilderDoneDeleted.create();
        alertDialog.show();
    }
    private void initializeDataReceiver() {
        whenItemDeleted = new DataReceiver() {
            @Override
            public void dataReceived(Object output) {
                String response = output.toString();
                if (!response.equals("server-error")) {
                    showAlertDialogDoneDeleted();
                }
                else {

                }
            }
        };
        whenItemsFromServerLoaded = new DataReceiver() {
            @Override
            public void dataReceived(Object output) {
                String response = output.toString();
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String status = jsonObject.getString("status");
                    if (status.equals("success")){
                        JSONArray jsonArray = jsonObject.getJSONArray("data");
                        for (int i = 0; i<jsonArray.length(); i++){
                            JSONObject resObject = jsonArray.getJSONObject(i);
                            UserGeraiResources geraiResources = new UserGeraiResources();
                            geraiResources.setIdbarang(resObject.getString("items_id"));
                            geraiResources.setNamabarang(resObject.getString("items_name"));
                            geraiResources.setNamapengguna(resObject.getString("user_name"));
                            geraiResources.setHargaawal(resObject.getString("starting_price"));
                            geraiResources.setStatuslelang("active");
                            JSONArray resArrayGambar = resObject.getJSONArray("url");
                            for (int j=0;j<resArrayGambar.length();j++){
                                JSONObject resGambarObject = resArrayGambar.getJSONObject(j);
                                geraiResources.setUrlgambarbarang("http://img-s7.lelangapa.com/" + resGambarObject.getString("url"));
                            }
                            dataBarang.add(geraiResources);
                        }
                        whenDataAlreadyLoaded();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };
    }
    private void disableSwipeRefreshLayout() {
        swipeRefreshLayout.setEnabled(false);
    }
    private void enableSwipeRefreshLayout() {
        swipeRefreshLayout.setEnabled(true);
    }
    private void setRecyclerViewProperties() {
        RecyclerView.LayoutManager upLayoutManager = new GridLayoutManager(getActivity(), 2);
        /*
        * {
            @Override
            public boolean canScrollVertically(){
                return false;
            }
        }
        */
        recyclerView.setLayoutManager(upLayoutManager);
        recyclerView.setAdapter(barangAdapter);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setNestedScrollingEnabled(false);
        /*recyclerView.addOnItemTouchListener(new RecyclerItemClickListener(getActivity(), recyclerView, new OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                //Toast.makeText(getActivity(), dataBarang.get(position).getNamabarang(), Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getActivity(), UserEditLelangBarangActivity.class);
                //Bundle idBarang = new Bundle();
                //sending just one value
                intent.putExtra("items_id", dataBarang.get(position).getIdbarang());
                getActivity().startActivity(intent);
            }

            @Override
            public void onLongItemClick(View view, int position) {

            }
        }));*/
    }
    private void setOnItemClickListener() {
        onItemClickListener = new OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Intent intent = new Intent(getActivity(), DetailBarangActivity.class);
                Bundle bundleExtras = new Bundle();
                bundleExtras.putString("items_id", dataBarang.get(position).getIdbarang());
                bundleExtras.putString("auctioneer_id", SessionManager.getSessionStatic().get(SessionManager.KEY_ID));
                intent.putExtras(bundleExtras);
                getActivity().startActivity(intent);
            }

            @Override
            public void onLongItemClick(View view, int position) {

            }
        };
    }
    private void whenItemDeletedReceived() {
        deleteToggler.unshowProgressDialog();
        enableSwipeRefreshLayout();
        invalidateAllData();
        loadAllNewDataFromServer();
    }
    private void whenDataAlreadyLoaded() {
        if (dataBarang.isEmpty()) {
            swipeRefreshLayout.setRefreshing(false);
            disableSwipeRefreshLayout();
            barangAdapter.notifyDataSetChanged();
            ((UserGeraiActivity) getActivity()).whenItemsNowEmpty();
        }
        else {
            swipeRefreshLayout.setRefreshing(false);
            disableSwipeRefreshLayout();
            barangAdapter.notifyDataSetChanged();
        }
    }
    private void invalidateAllData() {
        dataBarang.clear();
        barangAdapter.notifyDataSetChanged();
    }
    public void setDataBarang(ArrayList<UserGeraiResources> barang){
        this.dataBarang = barang;
    }
    private void loadAllNewDataFromServer() {
        swipeRefreshLayout.setRefreshing(true);
        GetBarangGeraiUserAPI getBarangGeraiUserAPI = new GetBarangGeraiUserAPI
                (SessionManager.getSessionStatic().get(SessionManager.KEY_ID), whenItemsFromServerLoaded);
        RequestController.getInstance(getActivity()).addToRequestQueue(getBarangGeraiUserAPI);
    }
}
