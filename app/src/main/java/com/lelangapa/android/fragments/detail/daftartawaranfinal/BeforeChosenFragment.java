package com.lelangapa.android.fragments.detail.daftartawaranfinal;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lelangapa.android.R;
import com.lelangapa.android.adapters.daftartawaranfinal.UnchosenAdapter;
import com.lelangapa.android.decorations.DividerItemDecoration;
import com.lelangapa.android.interfaces.DataReceiver;
import com.lelangapa.android.interfaces.OnItemClickListener;
import com.lelangapa.android.listeners.RecyclerItemClickListener;
import com.lelangapa.android.resources.BiddingResources;

import java.util.ArrayList;

/**
 * Created by andre on 11/05/17.
 */

public class BeforeChosenFragment extends Fragment {
    private ArrayList<BiddingResources> listOffer;
    private static String ID_BID_CHOSEN;

    private BiddingResources leadBidder;
    private DataReceiver afterSelectionDone;
    private UnchosenAdapter adapter;
    private ChooseWinnerToggler chooseWinnerToggler;

    private LinearLayout linearLayout_alert;
    private TextView textView_nama, textView_offer, textView_alert;
    private ImageView imageView_avatar;
    private RecyclerView recyclerView_listOffer;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_daftar_tawaran_final_loaded_layout, container, false);
        initializeViews(view);
        initializeDataReceiver();
        initializeToggler();
        initializeAdapter();
        setRecyclerViewProperties();
        setupViews();
        return view;
    }
    private void initializeViews(View view) {
        linearLayout_alert = (LinearLayout) view.findViewById(R.id.fragment_daftar_tawaran_final_alert);
        textView_nama = (TextView) view.findViewById(R.id.fragment_daftar_tawaran_final_name);
        textView_offer = (TextView) view.findViewById(R.id.fragment_daftar_tawaran_final_bidprice);
        textView_alert = (TextView) view.findViewById(R.id.fragment_daftar_tawaran_final_alert_message);
        imageView_avatar = (ImageView) view.findViewById(R.id.fragment_daftar_tawaran_final_avatar);
        recyclerView_listOffer = (RecyclerView) view.findViewById(R.id.fragment_daftar_tawaran_final_recyclerview);
    }
    private void initializeToggler() {
        chooseWinnerToggler = new ChooseWinnerToggler(getActivity(), afterSelectionDone);
    }
    private void initializeDataReceiver() {
        afterSelectionDone = new DataReceiver() {
            @Override
            public void dataReceived(Object output) {
                String result = output.toString();
                if (result.equals("done")) {
                    whenWinnerSelectionDoneSuccess();
                }
            }
        };
    }
    private void initializeAdapter() {
        adapter = new UnchosenAdapter(getActivity(), listOffer);
    }
    private void setRecyclerViewProperties() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView_listOffer.setLayoutManager(layoutManager);
        recyclerView_listOffer.addItemDecoration(new DividerItemDecoration(getActivity(), LinearLayout.VERTICAL));
        recyclerView_listOffer.setItemAnimator(new DefaultItemAnimator());
        recyclerView_listOffer.setAdapter(adapter);
        recyclerView_listOffer.addOnItemTouchListener(new RecyclerItemClickListener(getActivity(), recyclerView_listOffer, new OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                BiddingResources chosen = listOffer.get(position);
                ID_BID_CHOSEN = chosen.getIdBid();
                chooseWinnerToggler.setInformation(chosen.getIdBid(), chosen.getNamaBidder(), chosen.getHargaBid());
                chooseWinnerToggler.showAlertDialog();
            }

            @Override
            public void onLongItemClick(View view, int position) {
                BiddingResources chosen = listOffer.get(position);
                ID_BID_CHOSEN = chosen.getIdBid();
                chooseWinnerToggler.setInformation(chosen.getIdBid(), chosen.getNamaBidder(), chosen.getHargaBid());
                chooseWinnerToggler.showAlertDialog();
            }
        }));
    }
    private void setupViews() {
        textView_nama.setText(leadBidder.getNamaBidder());
        textView_offer.setText(leadBidder.getHargaBid());
    }
    private void whenWinnerSelectionDoneSuccess() {
        BiddingResources selectedBidder = null;
        for (int x=0;x<listOffer.size();x++) {
            if (listOffer.get(x).getIdBid().equals(ID_BID_CHOSEN)) {
                selectedBidder = listOffer.get(x);
                selectedBidder.setWinnerStatus(true);
                break;
            }
        }

        if (selectedBidder != null) {
            Intent resultIntent = new Intent();
            Bundle resultExtras = new Bundle();
            resultExtras.putString("id_bid", selectedBidder.getIdBid());
            resultExtras.putString("id_bidder", selectedBidder.getIdBidder());
            resultExtras.putString("harga_bid", selectedBidder.getHargaBid());
            resultExtras.putString("nama_bidder", selectedBidder.getNamaBidder());
            resultExtras.putBoolean("winner_status", selectedBidder.isWinnerStatus());
            resultIntent.putExtras(resultExtras);

            getActivity().setResult(Activity.RESULT_OK, resultIntent);
            getActivity().finish();
        }
    }
    public void setListOffer(ArrayList<BiddingResources> list) {
        this.listOffer = list;
    }
    public void setLeadBidder(BiddingResources resources) {
        this.leadBidder = resources;
    }
}
