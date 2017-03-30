package com.lelangapa.android.fragments.userpublic.riwayat;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.lelangapa.android.R;
import com.lelangapa.android.adapters.UserPublicRiwayatAdapter;
import com.lelangapa.android.resources.RiwayatResources;

import java.util.ArrayList;

/**
 * Created by andre on 30/03/17.
 */

public class NoEmptyFragment extends Fragment {
    private ArrayList<RiwayatResources> listRiwayat;
    private UserPublicRiwayatAdapter userPublicRiwayatAdapter;

    private RecyclerView recyclerView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_detail_user_public_riwayat_noempty_layout, container, false);
        initializeViews(view);
        initializeAdapter();
        setRecyclerViewProperties();
        return view;
    }
    private void initializeViews(View view)
    {
        recyclerView = (RecyclerView) view.findViewById(R.id.fragment_detail_user_public_riwayat_layout_recyclerview);
    }
    private void initializeAdapter()
    {
        userPublicRiwayatAdapter = new UserPublicRiwayatAdapter(getActivity(), listRiwayat);
    }
    private void setRecyclerViewProperties()
    {
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getActivity(), 2);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(userPublicRiwayatAdapter);
    }
    public void setListRiwayat(ArrayList<RiwayatResources> list)
    {
        this.listRiwayat = list;
    }
}
