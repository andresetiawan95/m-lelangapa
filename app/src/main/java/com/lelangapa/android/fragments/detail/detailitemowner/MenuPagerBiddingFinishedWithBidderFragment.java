package com.lelangapa.android.fragments.detail.detailitemowner;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.lelangapa.android.R;
import com.lelangapa.android.fragments.detail.detailitemowner.winnerstatus.ChosenFragment;
import com.lelangapa.android.fragments.detail.detailitemowner.winnerstatus.UnchosenFragment;
import com.lelangapa.android.resources.BiddingResources;

/**
 * Created by andre on 31/01/17.
 */

public class MenuPagerBiddingFinishedWithBidderFragment extends Fragment {
    private BiddingResources itemBiddingResources;
    private ProgressBar progressBar;

    private ChosenFragment chosenFragment;
    private UnchosenFragment unchosenFragment;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_detail_barang_bidding_finished_auctioneer_layout_new, container, false);
        initializeViews(view);
        initializeFragment();
        setupFragment();
        return view;
    }

    private void initializeViews(View view)
    {
        progressBar = (ProgressBar) view.findViewById(R.id.fragment_detail_barang_bidding_finished_auctioneer_indicator_progress_bar);
    }
    private void initializeFragment() {
        chosenFragment = new ChosenFragment();
        unchosenFragment = new UnchosenFragment();
    }
    public void setBidderInformation(BiddingResources itemBiddingResources)
    {
        this.itemBiddingResources = itemBiddingResources;
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
            getFragmentManager().beginTransaction()
                    .replace(R.id.fragment_detail_barang_bidding_finished_auctioneer_fragment, unchosenFragment)
                    .commit();
        }
    }
/*    private void setTextViews()
    {
        textView_namaBidder.setText(itemBiddingResources.getNamaBidder());
        textView_hargaBid.setText(itemBiddingResources.getHargaBid());
    }*/
}
