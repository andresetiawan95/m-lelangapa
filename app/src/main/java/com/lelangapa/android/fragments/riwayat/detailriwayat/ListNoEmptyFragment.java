package com.lelangapa.android.fragments.riwayat.detailriwayat;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.lelangapa.android.R;
import com.lelangapa.android.adapters.UserRiwayatDetailAdapter;
import com.lelangapa.android.decorations.DividerItemDecoration;
import com.lelangapa.android.resources.RiwayatResources;

import java.util.ArrayList;

/**
 * Created by andre on 15/03/17.
 */

public class ListNoEmptyFragment extends Fragment {
    private ArrayList<RiwayatResources> biddingList;

    private RecyclerView recyclerView_biddingList;
    private UserRiwayatDetailAdapter riwayatDetailAdapter;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_user_riwayat_detail_noempty_layout, container, false);
        initializeViews(view);
        setRecyclerViewAdapter();
        setRecyclerViewProperties();
        return view;
    }

    private void initializeViews(View view)
    {
        recyclerView_biddingList = (RecyclerView) view.findViewById(R.id.fragment_user_riwayat_layout_recyclerview);
    }
    private void setRecyclerViewAdapter()
    {
        riwayatDetailAdapter = new UserRiwayatDetailAdapter(getActivity(), biddingList);
    }
    private void setRecyclerViewProperties()
    {
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView_biddingList.setLayoutManager(layoutManager);
        recyclerView_biddingList.setItemAnimator(new DefaultItemAnimator());
        recyclerView_biddingList.addItemDecoration(new DividerItemDecoration(getActivity(), LinearLayout.VERTICAL));
        recyclerView_biddingList.setAdapter(riwayatDetailAdapter);
    }
    public void setBiddingList(ArrayList<RiwayatResources> list)
    {
        this.biddingList = list;
    }
}
