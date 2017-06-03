package com.lelangapa.app.fragments.detail.detailitemuser;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.lelangapa.app.R;
import com.lelangapa.app.activities.LoginActivity;
import com.lelangapa.app.interfaces.BidReceiver;
import com.lelangapa.app.interfaces.DataReceiver;
import com.lelangapa.app.preferences.SessionManager;
import com.lelangapa.app.resources.DetailItemResources;
import com.lelangapa.app.resources.NumberTextWatcher;
import com.lelangapa.app.resources.PriceFormatter;

import java.text.DecimalFormat;

/**
 * Created by Andre on 12/24/2016.
 */

public class BiddingFragment extends Fragment {
    private DetailItemResources detailItem;
    private TextView textView_namaBidder;
    private TextView textView_hargaBid, textView_askLogin;
    private EditText editText_inputPriceBid;
    private Button button_submitBid;
    private LinearLayout linearLayout_biddingEnabled, linearLayout_biddingDisabled;
    private ProgressBar progressBar_indicator;
    private DataReceiver newBiddingDataReceived;
    private BidReceiver inputBidReceiver;

    private Long offerPriceGap, startAndTargetPriceGap;
    private DecimalFormat decimalFormatter;
    public BiddingFragment()
    {
        initializeConstant();
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_detail_barang_bidding_submitbid_layout, container, false);
        initializeViews(view);
        displayBidInformation();
        validateUserSession();
        return view;
    }
    private void initializeConstant()
    {
        decimalFormatter = new DecimalFormat("0.00");
    }
    private void initializeViews(View view)
    {
        textView_namaBidder = (TextView) view.findViewById(R.id.fragment_detail_barang_bid_namabidder);
        textView_hargaBid = (TextView) view.findViewById(R.id.fragment_detail_barang_bid_hargabid);
        editText_inputPriceBid = (EditText) view.findViewById(R.id.fragment_detail_barang_bid_pricetextbox);
        button_submitBid = (Button) view.findViewById(R.id.fragment_detail_barang_bid_submitbutton);
        linearLayout_biddingEnabled = (LinearLayout) view.findViewById(R.id.fragment_detail_barang_bid_enabled);
        linearLayout_biddingDisabled = (LinearLayout) view.findViewById(R.id.fragment_detail_barang_bid_disabled);
        textView_askLogin = (TextView) view.findViewById(R.id.fragment_detail_barang_bid_disabled_ask_login);
        //progressBar_indicator = (ProgressBar) view.findViewById(R.id.fragment_detail_barang_bid_indicator_progress_bar);
        //textView_indicator = (TextView) view.findViewById(R.id.fragment_detail_barang_bid_indicator);
    }
    public void setDetailItem(DetailItemResources detailItem)
    {
        this.detailItem = detailItem;
        if (textView_namaBidder != null && textView_hargaBid != null) displayBidInformation();
    }
    private void validateUserSession() {
        if (SessionManager.isLoggedInStatic()) {
            linearLayout_biddingDisabled.setVisibility(View.GONE);
            linearLayout_biddingEnabled.setVisibility(View.VISIBLE);
            setupViewPropertiesWhenLoggedIn();
        }
        else {
            linearLayout_biddingDisabled.setVisibility(View.VISIBLE);
            linearLayout_biddingEnabled.setVisibility(View.GONE);
            setupViewPropertiesWhenNotLoggedIn();
        }
    }
    private void setupViewPropertiesWhenLoggedIn() {
        editText_inputPriceBid.addTextChangedListener(new NumberTextWatcher(editText_inputPriceBid));
        button_submitBid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //belum dilakukan pengecekan inputan, langsung coba submit
                String bidPriceFromUserInput = editText_inputPriceBid.getText().toString().trim().replaceAll("[^0-9]","");
                inputBidReceiver.bidReceived(bidPriceFromUserInput);
            }
        });
    }
    private void setupViewPropertiesWhenNotLoggedIn() {
        textView_askLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), LoginActivity.class);
                startActivity(intent);
            }
        });
    }
    public void setInputReceiverToSubmitBidding(BidReceiver inputBidReceiver)
    {
        this.inputBidReceiver = inputBidReceiver;
    }
    private void displayBidInformation()
    {
        if (detailItem.getNamabidder().equals("nouser"))
        {
            textView_namaBidder.setText("Bid starter");
            textView_hargaBid.setText(PriceFormatter.formatPrice(detailItem.getHargabid()));
        }
        else
        {
            textView_namaBidder.setText(detailItem.getNamabidder());
            textView_hargaBid.setText(PriceFormatter.formatPrice(detailItem.getHargabid()));
        }
        //updateProgressBarIndicator();
    }
    public void changeNamaBidderAndHargaBid(DetailItemResources detailItem)
    {
        this.detailItem = detailItem;
        displayBidInformation();
    }
    /*private void updateProgressBarIndicator()
    {
        progressBar_indicator.setProgress(calculateProgress());
        textView_indicator.setText(decimalFormatter.format(calculateIndicatorPercentage()));
    }
    private int calculateProgress()
    {
        offerPriceGap = Long.valueOf(detailItem.getHargabid()) - Long.valueOf(detailItem.getHargaawal());
        startAndTargetPriceGap = Long.valueOf(detailItem.getHargatarget()) - Long.valueOf(detailItem.getHargaawal());
        return (int) (((double)offerPriceGap / (double)startAndTargetPriceGap) * (double)100);
    }
    private double calculateIndicatorPercentage()
    {
        offerPriceGap = Long.valueOf(detailItem.getHargabid()) - Long.valueOf(detailItem.getHargaawal());
        startAndTargetPriceGap = Long.valueOf(detailItem.getHargatarget()) - Long.valueOf(detailItem.getHargaawal());
        return (((double)offerPriceGap / (double)startAndTargetPriceGap) * (double)100);
    }*/
}
