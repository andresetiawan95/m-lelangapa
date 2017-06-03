package com.lelangapa.app.fragments.detail.daftartawaranfinal;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lelangapa.app.R;
import com.lelangapa.app.adapters.daftartawaranfinal.ChosenAdapter;
import com.lelangapa.app.decorations.DividerItemDecoration;
import com.lelangapa.app.resources.BiddingResources;

import java.util.ArrayList;

/**
 * Created by andre on 11/05/17.
 */

public class AfterChosenFragment extends Fragment {
    private ArrayList<BiddingResources> listOffer;
    private BiddingResources leadBidder;
    private ChosenAdapter adapter;

    private LinearLayout linearLayout_alert;
    private TextView textView_nama, textView_offer, textView_alert, textView_header;
    private ImageView imageView_avatar;
    private RecyclerView recyclerView_listOffer;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_daftar_tawaran_final_loaded_layout, container, false);
        initializeViews(view);
        initializeAdapter();
        setupViews();
        setRecyclerViewProperties();
        return view;
    }
    private void initializeViews(View view) {
        linearLayout_alert = (LinearLayout) view.findViewById(R.id.fragment_daftar_tawaran_final_alert);
        textView_header = (TextView) view.findViewById(R.id.fragment_daftar_tawaran_final_sign);
        textView_nama = (TextView) view.findViewById(R.id.fragment_daftar_tawaran_final_name);
        textView_offer = (TextView) view.findViewById(R.id.fragment_daftar_tawaran_final_bidprice);
        textView_alert = (TextView) view.findViewById(R.id.fragment_daftar_tawaran_final_alert_message);
        imageView_avatar = (ImageView) view.findViewById(R.id.fragment_daftar_tawaran_final_avatar);
        recyclerView_listOffer = (RecyclerView) view.findViewById(R.id.fragment_daftar_tawaran_final_recyclerview);
    }
    private void initializeAdapter() {
        adapter = new ChosenAdapter(getActivity(), listOffer);
    }
    private void setRecyclerViewProperties() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView_listOffer.setLayoutManager(layoutManager);
        recyclerView_listOffer.setItemAnimator(new DefaultItemAnimator());
        recyclerView_listOffer.addItemDecoration(new DividerItemDecoration(getActivity(), LinearLayout.VERTICAL));
        recyclerView_listOffer.setAdapter(adapter);
    }
    private void setupViews() {
        linearLayout_alert.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.colorOKAlertBackground));
        textView_alert.setText("Pemenang telah terpilih");
        textView_alert.setTextColor(Color.parseColor("#ffffff"));
        textView_header.setText("Pemenang Terpilih");
        textView_nama.setText(leadBidder.getNamaBidder());
        textView_offer.setText(leadBidder.getHargaBid());
    }
    public void setListOffer(ArrayList<BiddingResources> list) {
        this.listOffer = list;
    }
    public void setLeadBidder(BiddingResources resources) {
        this.leadBidder = resources;
    }
}
