package com.lelangapa.android.fragments.detail.ownerblocklist;

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
import com.lelangapa.android.adapters.DaftarBlockAdapter;
import com.lelangapa.android.decorations.DividerItemDecoration;
import com.lelangapa.android.resources.BlockResources;

import java.util.ArrayList;

/**
 * Created by andre on 19/04/17.
 */

public class NoEmptyFragment extends Fragment {
    private ArrayList<BlockResources> listBlock;
    private DaftarBlockAdapter adapter;
    private RecyclerView recyclerView_block;

    private UnblockToggler unblockToggler;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_detail_barang_daftar_block_noempty_layout, container, false);
        initializeViews(view);
        initializeAdapter();
        setRecyclerViewProperties();
        return view;
    }
    private void initializeViews(View view)
    {
        recyclerView_block = (RecyclerView) view.findViewById(R.id.fragment_user_favorite_layout_recyclerview);
    }
    private void initializeAdapter()
    {
        adapter = new DaftarBlockAdapter(getActivity(), listBlock, unblockToggler);
    }
    private void setRecyclerViewProperties()
    {
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView_block.setLayoutManager(layoutManager);
        recyclerView_block.setItemAnimator(new DefaultItemAnimator());
        recyclerView_block.addItemDecoration(new DividerItemDecoration(getActivity(), LinearLayout.VERTICAL));
        recyclerView_block.setAdapter(adapter);
    }
    public void setUnblockToggler(UnblockToggler unblockToggler)
    {
        this.unblockToggler = unblockToggler;
    }
    public void setListBlock(ArrayList<BlockResources> list)
    {
        //if adapter masih null, maka set listBlock dengan list
        if (adapter == null) {
            this.listBlock = list;
        }
        else {
            this.listBlock = list;
            adapter.updateDataset(list);
        }
        //else set listBlock dengan list dan update adapter
    }
}
