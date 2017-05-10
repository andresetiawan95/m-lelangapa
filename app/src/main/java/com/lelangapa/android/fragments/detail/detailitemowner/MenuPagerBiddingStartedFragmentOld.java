package com.lelangapa.android.fragments.detail.detailitemowner;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.lelangapa.android.R;
import com.lelangapa.android.interfaces.AuctioneerResponseReceiver;
import com.lelangapa.android.resources.BiddingResources;

/**
 * Created by andre on 25/01/17.
 */

public class MenuPagerBiddingStartedFragmentOld extends Fragment {
    private TextView textView_namaBidder, textView_hargaBid;
    private Button button_ambilTawaran, button_daftarTawaran, button_hentikanLelang;

    private AuctioneerResponseReceiver auctioneerResponseReceiver;

    private BiddingResources itemBidResources;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_detail_barang_bidding_highestbid_auctioneer_layout, container, false);
        initializeViews(view);
        setBidderInformation(itemBidResources);
        setButtonResponses();
        return view;
    }

    /*
    * Initialization method start here
    * */
    private void initializeViews(View view)
    {
        textView_namaBidder = (TextView) view.findViewById(R.id.fragment_detail_barang_bidding_highestbid_auctioneer_namabidder);
        textView_hargaBid = (TextView) view.findViewById(R.id.fragment_detail_barang_bidding_highestbid_auctioneer_hargabid);
        button_ambilTawaran = (Button) view.findViewById(R.id.fragment_detail_barang_bidding_highestbid_auctioneer_ambiltawaran);
        button_daftarTawaran = (Button) view.findViewById(R.id.fragment_detail_barang_bidding_highestbid_auctioneer_daftartawaran);
        button_hentikanLelang = (Button) view.findViewById(R.id.fragment_detail_barang_bidding_highestbid_auctioneer_hentikanlelang);
    }
    /*
    * Initialization method end here
    * */

    /*
    * Setter method start here
    * */
    public void setBidderInformation(BiddingResources itemBidResources)
    {
        this.itemBidResources = itemBidResources;
        if (textView_namaBidder != null && textView_hargaBid != null)
        {
            textView_namaBidder.setText(this.itemBidResources.getNamaBidder());
            textView_hargaBid.setText(this.itemBidResources.getHargaBid());
        }
    }
    public void setAuctioneerResponseReceiver(AuctioneerResponseReceiver receiver)
    {
        this.auctioneerResponseReceiver = receiver;
    }
    private void setButtonResponses()
    {
        setButtonAmbilTawaranResponse();
        setButtonDaftarTawaranResponse();
        setButtonHentikanLelangResponse();
    }
    private void setButtonAmbilTawaranResponse()
    {
        button_ambilTawaran.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                auctioneerResponseReceiver.responseWinnerChosenReceived(true, itemBidResources.getIdBid());
            }
        });
    }
    private void setButtonDaftarTawaranResponse()
    {
        button_daftarTawaran.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                auctioneerResponseReceiver.responseDaftarTawaranReceived();
                //implemented later
            }
        });
    }
    private void setButtonHentikanLelangResponse()
    {
        button_hentikanLelang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                auctioneerResponseReceiver.responseCancelReceived(true);
            }
        });
    }
    /*
    * Setter method end here
    * */
}
