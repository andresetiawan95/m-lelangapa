package com.lelangkita.android.fragments.gerai;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.lelangkita.android.R;
import com.lelangkita.android.apicalls.gerai.GetItemEditLelangBarangAPI;
import com.lelangkita.android.interfaces.DataReceiver;
import com.lelangkita.android.resources.UserGeraiResources;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by andre on 11/12/16.
 */

public class UserEditLelangBarangFragment extends Fragment {
    private ArrayList<UserGeraiResources> dataBarang = new ArrayList<>();
    private String itemID;
    private DataReceiver allDataBarangReceived;
    private DataReceiver dataBarangReceiver;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.fragment_user_gerai_layout, container, false);
        final Button btnEditBarang = (Button) getActivity().findViewById(R.id.fragment_user_edit_lelang_barang_jual_button);
        //Toast.makeText(getActivity(), getActivity().getIntent().getStringExtra("items_id"), Toast.LENGTH_SHORT).show();
        itemID = getActivity().getIntent().getStringExtra("items_id");
        btnEditBarang.setVisibility(View.INVISIBLE);
        getDataBarangFromServer(itemID);
        allDataBarangReceived = new DataReceiver() {
            @Override
            public void dataReceived(Object output) {
                String res = output.toString();
                if (res.equals("success")){
                    UserEditLelangBarangGetDataFragment userEditLelangBarangGetDataFragment = new UserEditLelangBarangGetDataFragment();
                    userEditLelangBarangGetDataFragment.getDataBarang(dataBarang);
                    getFragmentManager().beginTransaction().replace(R.id.fragment_user_edit_lelang_barang_layout, userEditLelangBarangGetDataFragment)
                            .commit();
                }
            }
        };
        return view;
    }
    private void getDataBarangFromServer(String itemID){
        dataBarangReceiver = new DataReceiver() {
            @Override
            public void dataReceived(Object output) {
                String response = output.toString();
                try {
                    JSONObject jsonResponse = new JSONObject(response);
                    JSONArray respArray = jsonResponse.getJSONArray("data");
                    JSONObject respArrayObject = respArray.getJSONObject(0);
                    UserGeraiResources geraiResources = new UserGeraiResources();
                    geraiResources.setIdbarang(respArrayObject.getString("items_id"));
                    geraiResources.setNamabarang(respArrayObject.getString("items_name"));
                    geraiResources.setDeskripsibarang(respArrayObject.getString("item_description"));
                    geraiResources.setHargaawal(respArrayObject.getString("starting_price"));
                    geraiResources.setHargatarget(respArrayObject.getString("expected_price"));
                    String startTimeBeforeSplit = respArrayObject.getString("start_time");
                    String endTimeBeforeSplit = respArrayObject.getString("end_time");
                    String[] startTimePart = startTimeBeforeSplit.split("T");
                    String[] endTimePart = endTimeBeforeSplit.split("T");
                    String startDate = startTimePart[0];
                    String endDate = endTimePart[0];
                    String [] startHourPart = startTimePart[1].split("\\.");
                    String [] endHourPart = endTimePart[1].split("\\.");
                    String startHour = startHourPart[0];
                    String endHour = endHourPart[0];
                    geraiResources.setTanggalmulai(startDate);
                    geraiResources.setJammulai(startHour);
                    geraiResources.setTanggalselesai(endDate);
                    geraiResources.setJamselesai(endHour);
                    dataBarang.add(geraiResources);
                    allDataBarangReceived.dataReceived("success");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };
        GetItemEditLelangBarangAPI getItemEditLelangBarangAPI = new GetItemEditLelangBarangAPI(itemID, dataBarangReceiver);
        RequestQueue queue = Volley.newRequestQueue(getActivity());
        queue.add(getItemEditLelangBarangAPI);
    }
}
