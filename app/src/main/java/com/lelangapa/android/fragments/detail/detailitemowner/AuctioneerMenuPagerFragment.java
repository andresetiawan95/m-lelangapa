package com.lelangapa.android.fragments.detail.detailitemowner;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.lelangapa.android.R;
import com.lelangapa.android.interfaces.AuctioneerResponseReceiver;
import com.lelangapa.android.interfaces.DataReceiver;
import com.lelangapa.android.resources.BiddingResources;
import com.lelangapa.android.resources.DetailItemResources;
import com.lelangapa.android.viewpagers.DetailBarangAuctioneerViewPagerAdapter;

import java.util.ArrayList;

/**
 * Created by andre on 25/01/17.
 */

public class AuctioneerMenuPagerFragment extends Fragment {
    private TabLayout detailAuctioneerTabLayout;
    private ViewPager detailAuctioneerViewPager;
    private DetailBarangAuctioneerViewPagerAdapter auctioneerViewPagerAdapter;
    private MenuPagerBiddingNotStartedFragment biddingNotStartedFragment;
    private MenuPagerBiddingStartedFragment biddingStartedFragment;
    private MenuPagerBiddingFinishedNoBidFragment biddingFinishedNoBidFragment;
    private MenuPagerBiddingFinishedWithBidderFragment biddingFinishedWithBidderFragment;
    private MenuPagerStatisticFragment statisticFragment;
    private MenuPagerRankingFragment rankingFragment;

    private DetailItemResources detailItem;
    private BiddingResources itemBiddingResources;
    private ArrayList<Fragment> fragmentViewPagerList = null;
    private ArrayList<String> titleViewPagerList = null;
    private boolean adapterAlreadyExist = false;
    private int bidStatus;

    public AuctioneerMenuPagerFragment()
    {
        biddingNotStartedFragment = new MenuPagerBiddingNotStartedFragment();
        biddingStartedFragment = new MenuPagerBiddingStartedFragment();
        biddingFinishedNoBidFragment = new MenuPagerBiddingFinishedNoBidFragment();
        biddingFinishedWithBidderFragment = new MenuPagerBiddingFinishedWithBidderFragment();
        statisticFragment = new MenuPagerStatisticFragment();
        rankingFragment = new MenuPagerRankingFragment();
        fragmentViewPagerList = new ArrayList<>();
        titleViewPagerList = new ArrayList<>();
    }
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        auctioneerViewPagerAdapter = new DetailBarangAuctioneerViewPagerAdapter(getFragmentManager());
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_detail_barang_bidding_auctioneer_layout, container, false);
        initializeViews(view);
        setViewPagerAdapter();
        detailAuctioneerTabLayout.setupWithViewPager(detailAuctioneerViewPager);
        return view;
    }

    private void initializeViews(View view)
    {
        detailAuctioneerTabLayout = (TabLayout) view.findViewById(R.id.tab_detail_barang_bidding_auctioneer);
        detailAuctioneerViewPager = (ViewPager) view.findViewById(R.id.viewpager_detail_barang_bidding_auctioneer);
    }


    public void setFragmentAndTitleListDetailItemAuctioneer(int bidStatus)
    {
        if (!fragmentViewPagerList.isEmpty()){
            fragmentViewPagerList.clear();
        }
        if (!titleViewPagerList.isEmpty())titleViewPagerList.clear();

        if (bidStatus == 0)
        {
            fragmentViewPagerList.add(biddingNotStartedFragment);
            fragmentViewPagerList.add(statisticFragment);
            titleViewPagerList.add("Tawaran");
            titleViewPagerList.add("Statistik");
            Log.v("FRAGMENT added", "FRAGMENT added " + fragmentViewPagerList.size());
        }
        else if (bidStatus == 1)
        {
            fragmentViewPagerList.add(biddingStartedFragment);
            fragmentViewPagerList.add(statisticFragment);
            titleViewPagerList.add("Tawaran");
            titleViewPagerList.add("Statistik");
        }
        else if (bidStatus == -1)
        {
            String namaBidder = itemBiddingResources.getNamaBidder();
            if (namaBidder.equals("nouser"))
            {
                fragmentViewPagerList.add(biddingFinishedNoBidFragment);
                fragmentViewPagerList.add(statisticFragment);
                titleViewPagerList.add("Tawaran");
                titleViewPagerList.add("Statistik");
            }
            else
            {
                fragmentViewPagerList.add(biddingFinishedWithBidderFragment);
                fragmentViewPagerList.add(statisticFragment);
                titleViewPagerList.add("Tawaran");
                titleViewPagerList.add("Statistik");
            }
        }
        /*
        * Untuk mengecek apakah adapter sudah di set atau belum
        * Jika sudah di set, maka tinggal meng-update fragment pada adapter saja
        * Dimana ada fungsi notifySetDataChanged() untuk merubah adapter
        * */
        if (adapterAlreadyExist)
        {
            auctioneerViewPagerAdapter.setFragmentList(fragmentViewPagerList);
            auctioneerViewPagerAdapter.setTitleList(titleViewPagerList);
        }
    }
    public void setViewPagerAdapter()
    {
        if (fragmentViewPagerList != null && titleViewPagerList != null)
        {
            auctioneerViewPagerAdapter.setFragmentList(fragmentViewPagerList);
            auctioneerViewPagerAdapter.setTitleList(titleViewPagerList);
        }
        detailAuctioneerViewPager.setAdapter(auctioneerViewPagerAdapter);
        adapterAlreadyExist = true;
    }
    public void setOrUpdateBidStatus(int bidStatus)
    {
        this.bidStatus = bidStatus;
        /*
        * Sekaligus melakukan update terhadap fragmentList dan titleList
        * */
        setFragmentAndTitleListDetailItemAuctioneer(this.bidStatus);
    }

    /*
    * Update value on child fragments methods start here
    * */
    public void setFragmentValueBidNotStart(Long startTime, Long serverTime, DataReceiver trigger)
    {
        biddingNotStartedFragment.setStartTimeAndServerTime(startTime, serverTime);
        biddingNotStartedFragment.setTriggerStarted(trigger);
    }
    public void setFragmentValueBidStart(BiddingResources itemBidResources, AuctioneerResponseReceiver receiver)
    {
        setBidderInformation(itemBidResources);
        setAuctioneerResponseReceiver(receiver);
    }
    public void setFragmentValueBidFinish()
    {

    }
    /*
    * Update value on child fragments methods end here
    * */

    /*
    * Set detail item value method start here
    * */
    public void setUpDetailItemAndBiddingResources(DetailItemResources detailItem, BiddingResources itemBiddingResources)
    {
        this.detailItem = detailItem;
        this.itemBiddingResources = itemBiddingResources;
    }
    /*
    * Set detail item value method end here
    * */

    /*
    * Set or update value on child method fragment start here
    * */
    public void setBidderInformation(BiddingResources itemBiddingResources)
    {
        biddingStartedFragment.setBidderInformation(itemBiddingResources);
    }
    public void setAuctioneerResponseReceiver(AuctioneerResponseReceiver receiver)
    {
        biddingStartedFragment.setAuctioneerResponseReceiver(receiver);
    }
    /*
    * Set or update value on child method end here
    * */
}
