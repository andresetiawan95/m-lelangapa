package com.lelangapa.android.fragments.detail.detailitemowner;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.lelangapa.android.R;
import com.lelangapa.android.fragments.detail.detailitemowner.pagerstarted.ExistBidFragment;
import com.lelangapa.android.fragments.detail.detailitemowner.pagerstarted.NoBidFragment;
import com.lelangapa.android.interfaces.AuctioneerResponseReceiver;
import com.lelangapa.android.resources.BiddingResources;
import com.lelangapa.android.resources.DetailItemResources;

import java.text.DecimalFormat;

/**
 * Created by andre on 25/01/17.
 */

public class MenuPagerBiddingStartedFragmentNew extends Fragment {
    private TextView textView_indicator;
    private ProgressBar progressBar_indicator;

    private AuctioneerResponseReceiver auctioneerResponseReceiver;
    private BiddingResources itemBidResources;
    private DetailItemResources detailItem;

    private NoBidFragment noBidFragment;
    private ExistBidFragment existBidFragment;

    private int FRAGMENT_SWITCH_INDICATOR;
    private Long offerPriceGap, startAndTargetPriceGap;
    private DecimalFormat decimalFormatter;
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        initializeConstants();
        setFragmentSwitchIndicator();
        initializeFragments();
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_detail_barang_bidding_highestbid_auctioneer_layout_new, container, false);
        initializeViews(view);
        setAuctioneerResponseReceiverForChildFragments();
        setBidderInformation(itemBidResources);
        return view;
    }

    @Override
    public void onDestroy()
    {
        super.onDestroy();
        setFragmentSwitchIndicator();
    }
    /*
    * Initialization method start here
    * */
    private void initializeConstants()
    {
        decimalFormatter = new DecimalFormat("#.00");
    }
    private void setFragmentSwitchIndicator()
    {
        FRAGMENT_SWITCH_INDICATOR = -1;
    }
    private void initializeViews(View view)
    {
        textView_indicator = (TextView) view.findViewById(R.id.fragment_detail_barang_bidding_highestbid_indicator_percent);
        progressBar_indicator = (ProgressBar) view.findViewById(R.id.fragment_detail_barang_bidding_highestbid_indicator_progress_bar);
    }
    private void initializeFragments()
    {
        noBidFragment = new NoBidFragment();
        existBidFragment = new ExistBidFragment();
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
        if (noBidFragment!= null && existBidFragment != null) {
            whenBidInformationReceived();
            updateProgressBarIndicator();
        }
    }
    public void setDetailItem(DetailItemResources detailItem)
    {
        this.detailItem = detailItem;
    }
    private void whenBidInformationReceived()
    {
        if (itemBidResources.getNamaBidder().equals("nouser")) {
            noBidFragment.setOpenPrice(itemBidResources.getHargaBid());
            if (FRAGMENT_SWITCH_INDICATOR != 0) {
                setupFragment(noBidFragment);
                FRAGMENT_SWITCH_INDICATOR = 0;
            }
        }
        else {
            existBidFragment.setBidderInformation(itemBidResources);
            if (FRAGMENT_SWITCH_INDICATOR != 1) {
                setupFragment(existBidFragment);
                FRAGMENT_SWITCH_INDICATOR = 1;
            }
        }
    }
    private void updateProgressBarIndicator()
    {
        progressBar_indicator.setProgress(calculateProgress());
        textView_indicator.setText(decimalFormatter.format(calculateIndicatorPercentage()));
    }
    private int calculateProgress()
    {
        offerPriceGap = Long.valueOf(itemBidResources.getHargaBid()) - Long.valueOf(detailItem.getHargaawal());
        startAndTargetPriceGap = Long.valueOf(detailItem.getHargatarget()) - Long.valueOf(detailItem.getHargaawal());
        return (int) (((double)offerPriceGap / (double)startAndTargetPriceGap) * (double)100);
    }
    private double calculateIndicatorPercentage()
    {
        offerPriceGap = Long.valueOf(itemBidResources.getHargaBid()) - Long.valueOf(detailItem.getHargaawal());
        startAndTargetPriceGap = Long.valueOf(detailItem.getHargatarget()) - Long.valueOf(detailItem.getHargaawal());
        return (((double)offerPriceGap / (double)startAndTargetPriceGap) * (double)100);
    }
    private void setupFragment(Fragment fragment)
    {
        getFragmentManager().beginTransaction()
                .replace(R.id.fragment_detail_barang_bidding_highestbid_auctioneer_fragment, fragment)
                .commit();
    }
    public void setAuctioneerResponseReceiver(AuctioneerResponseReceiver receiver)
    {
        this.auctioneerResponseReceiver = receiver;
    }
    private void setAuctioneerResponseReceiverForChildFragments()
    {
        noBidFragment.setAuctioneerResponseReceiver(auctioneerResponseReceiver);
        existBidFragment.setAuctioneerResponseReceiver(auctioneerResponseReceiver);
    }
    /*private void setButtonAmbilTawaranResponse()
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
    }*/
    /*
    * Setter method end here
    * */
}
