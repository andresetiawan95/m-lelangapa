package com.lelangkita.android.fragments.detail;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.lelangkita.android.R;
import com.lelangkita.android.interfaces.BidReceiver;
import com.lelangkita.android.interfaces.DataReceiver;
import com.lelangkita.android.interfaces.InputReceiver;
import com.lelangkita.android.resources.DetailItemResources;

import io.socket.emitter.Emitter;

/**
 * Created by Andre on 12/24/2016.
 */

public class BiddingFragment extends Fragment {
    private DetailItemResources detailItem;
    private TextView textView_namaBidder;
    private TextView textView_hargaBid;
    private EditText editText_inputPriceBid;
    private Button button_submitBid;
    private DataReceiver newBiddingDataReceived;
    private BidReceiver inputBidReceiver;
    public BiddingFragment(){}
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_detail_barang_bidding_submitbid_layout, container, false);
        textView_namaBidder = (TextView) view.findViewById(R.id.fragment_detail_barang_bid_namabidder);
        textView_hargaBid = (TextView) view.findViewById(R.id.fragment_detail_barang_bid_hargabid);
        editText_inputPriceBid = (EditText) view.findViewById(R.id.fragment_detail_barang_bid_pricetextbox);
        button_submitBid = (Button) view.findViewById(R.id.fragment_detail_barang_bid_submitbutton);
        setInitialNamaBidderAndHargaBid();
        button_submitBid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //belum dilakukan pengecekan inputan, langsung coba submit
                inputBidReceiver.bidReceived(editText_inputPriceBid.getText().toString().trim());
            }
        });
        return view;
    }
    public void setDetailItem(DetailItemResources detailItem)
    {
        this.detailItem = detailItem;
    }
    public void setInputReceiverToSubmitBidding(BidReceiver inputBidReceiver)
    {
        this.inputBidReceiver = inputBidReceiver;
    }
    private void setInitialNamaBidderAndHargaBid()
    {
        if (detailItem.getNamabidder().equals("nouser"))
        {
            textView_namaBidder.setText("Bid starter");
            textView_hargaBid.setText(detailItem.getHargabid());
        }
        else
        {
            textView_namaBidder.setText(detailItem.getNamabidder());
            textView_hargaBid.setText(detailItem.getHargabid());
        }
    }
    public void changeNamaBidderAndHargaBid(DetailItemResources detailItem)
    {
        this.detailItem = detailItem;
        if (detailItem.getNamabidder().equals("nouser"))
        {
            textView_namaBidder.setText("Bid starter");
            textView_hargaBid.setText(detailItem.getHargabid());
        }
        else
        {
            textView_namaBidder.setText(detailItem.getNamabidder());
            textView_hargaBid.setText(detailItem.getHargabid());
        }
    }
}
