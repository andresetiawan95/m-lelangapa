package com.lelangapa.android.fragments.detail.detailitemowner;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.lelangapa.android.R;
import com.lelangapa.android.activities.detail.DaftarTawaranFinalActivity;
import com.lelangapa.android.fragments.detail.detailitemowner.winnerstatus.ChosenFragment;
import com.lelangapa.android.fragments.detail.detailitemowner.winnerstatus.UnchosenFragment;
import com.lelangapa.android.interfaces.AuctioneerResponseReceiver;
import com.lelangapa.android.resources.BiddingResources;

/**
 * Created by andre on 31/01/17.
 */

public class MenuPagerBiddingFinishedWithBidderFragment extends Fragment {
    private BiddingResources itemBiddingResources;
    private ProgressBar progressBar;
    private AuctioneerResponseReceiver auctioneerResponseReceiver;

    private static final int REQUEST_DAFTAR_TAWARAN = 1;
    private String itemID;

    private ChosenFragment chosenFragment;
    private UnchosenFragment unchosenFragment;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_detail_barang_bidding_finished_auctioneer_layout_new, container, false);
        initializeViews(view);
        initializeFragment();
        setAuctioneerResponseReceiver();
        setupFragment();
        return view;
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_DAFTAR_TAWARAN && resultCode == Activity.RESULT_OK && data!=null) {
            //ubah data2 di itemBiddingResource dengan bundle yang sudah dikirim
            //setupFragment lagi
        }
    }

    private void initializeViews(View view)
    {
        progressBar = (ProgressBar) view.findViewById(R.id.fragment_detail_barang_bidding_finished_auctioneer_indicator_progress_bar);
    }
    private void initializeFragment() {
        chosenFragment = new ChosenFragment();
        unchosenFragment = new UnchosenFragment();
    }
    private void setAuctioneerResponseReceiver() {
        auctioneerResponseReceiver = new AuctioneerResponseReceiver() {
            @Override
            public void responseCancelReceived(boolean status) {/*not implemented*/}

            @Override
            public void responseWinnerChosenReceived(boolean status, String idBid) {
                //implemented later
            }

            @Override
            public void responseDaftarTawaranReceived() {
                intentToDaftarTawaran();
            }

            @Override
            public void responseBlockListReceived() {/*not implemented*/}
        };
    }
    public void setBidderInformation(BiddingResources itemBiddingResources)
    {
        this.itemBiddingResources = itemBiddingResources;
    }
    public void setItemID(String itemID) {
        this.itemID = itemID;
    }
    private void setupFragment() {
        if (itemBiddingResources.isWinnerStatus()) {
            chosenFragment.setBidderInformation(itemBiddingResources);
            getFragmentManager().beginTransaction()
                    .replace(R.id.fragment_detail_barang_bidding_finished_auctioneer_fragment, chosenFragment)
                    .commit();
        }
        else {
            unchosenFragment.setBidderInformation(itemBiddingResources);
            unchosenFragment.setAuctioneerResponseReceiver(auctioneerResponseReceiver);
            getFragmentManager().beginTransaction()
                    .replace(R.id.fragment_detail_barang_bidding_finished_auctioneer_fragment, unchosenFragment)
                    .commit();
        }
    }
    private void intentToDaftarTawaran() {
        Intent intent = new Intent(getActivity(), DaftarTawaranFinalActivity.class);
        Bundle bundleExtras = new Bundle();
        bundleExtras.putString("id_item", itemID);
        bundleExtras.putString("id_bid", itemBiddingResources.getIdBid());
        bundleExtras.putString("id_bidder", itemBiddingResources.getIdBidder());
        bundleExtras.putString("harga_bid", itemBiddingResources.getHargaBid());
        bundleExtras.putString("nama_bidder", itemBiddingResources.getNamaBidder());
        bundleExtras.putBoolean("winner_status", itemBiddingResources.isWinnerStatus());
        intent.putExtras(bundleExtras);
        startActivityForResult(intent, REQUEST_DAFTAR_TAWARAN);
    }
}
