package com.lelangkita.android.fragments.gerai;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.lelangkita.android.R;
import com.lelangkita.android.activities.gerai.UserEditLelangBarangActivity;
import com.lelangkita.android.adapters.UserGeraiBarangAdapter;
import com.lelangkita.android.interfaces.OnItemClickListener;
import com.lelangkita.android.listeners.RecyclerItemClickListener;
import com.lelangkita.android.resources.UserGeraiResources;

import java.util.ArrayList;

/**
 * Created by andre on 06/12/16.
 */

public class UserGeraiNoEmptyFragment extends Fragment {
    private ArrayList<UserGeraiResources> dataBarang;
    private UserGeraiBarangAdapter barangAdapter;
    public UserGeraiNoEmptyFragment(){}
    public void setDataBarang(ArrayList<UserGeraiResources> barang){
        this.dataBarang = barang;
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.fragment_user_gerai_layout_notempty, container, false);
        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.fragment_user_gerai_layout_recyclerview);
        barangAdapter = new UserGeraiBarangAdapter(getActivity(), dataBarang);
        RecyclerView.LayoutManager upLayoutManager = new LinearLayoutManager(getActivity()) {
            @Override
            public boolean canScrollVertically(){
                return false;
            }
        };
        recyclerView.setLayoutManager(upLayoutManager);
        recyclerView.setAdapter(barangAdapter);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addOnItemTouchListener(new RecyclerItemClickListener(getActivity(), recyclerView, new OnItemClickListener() {
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
        }));
        return view;
    }
}
