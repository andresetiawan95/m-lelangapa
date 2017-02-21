package com.lelangapa.android.fragments.detail.detailitemowner;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.lelangapa.android.R;
import com.lelangapa.android.resources.BiddingResources;
import com.lelangapa.android.resources.DetailItemResources;
import com.lelangapa.android.viewpagers.DetailBarangAuctioneerViewPagerAdapter;

import java.util.ArrayList;

/**
 * Created by andre on 13/02/17.
 */

public class AuctioneerMenuPagerFinishedFragment extends Fragment {
    private TabLayout detailAuctioneerTabLayout;
    private ViewPager detailAuctioneerViewPager;
    private DetailBarangAuctioneerViewPagerAdapter auctioneerViewPagerAdapter;
    private MenuPagerBiddingFinishedNoBidFragment biddingFinishedNoBidFragment;
    private MenuPagerBiddingFinishedWithBidderFragment biddingFinishedWithBidderFragment;
    private MenuPagerStatisticFragment statisticFragment;
    private MenuPagerRankingFragment rankingFragment;

    private DetailItemResources detailItem;
    private BiddingResources itemBiddingResources;
    private ArrayList<Fragment> fragmentViewPagerList = null;
    private ArrayList<String> titleViewPagerList = null;

    private FragmentManager manager;
    private int bidStatus;

    public AuctioneerMenuPagerFinishedFragment()
    {
        biddingFinishedNoBidFragment = new MenuPagerBiddingFinishedNoBidFragment();
        biddingFinishedWithBidderFragment = new MenuPagerBiddingFinishedWithBidderFragment();
        statisticFragment = new MenuPagerStatisticFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_detail_barang_bidding_auctioneer_layout_finished, container, false);
        initializeViews(view);
        setViewPagerAdapter();
        detailAuctioneerTabLayout.setupWithViewPager(detailAuctioneerViewPager);
        return view;
    }

    /*
    * Initialization method start here
    * */
    private void initializeAdapter()
    {
        auctioneerViewPagerAdapter = new DetailBarangAuctioneerViewPagerAdapter(getChildFragmentManager());
    }
    private void initializeViews(View view)
    {
        detailAuctioneerTabLayout = (TabLayout) view.findViewById(R.id.tab_detail_barang_bidding_auctioneer);
        detailAuctioneerViewPager = (ViewPager) view.findViewById(R.id.viewpager_detail_barang_bidding_auctioneer);
    }
    /*
    * Initialization method end here
    * */

    /*
    * Set detail item value method start here
    * */
    public void setUpDetailItemAndBiddingResources(DetailItemResources detailItem, BiddingResources itemBiddingResources)
    {
        this.detailItem = detailItem;
        this.itemBiddingResources = itemBiddingResources;
    }

    private void setFragmentAndTitleListDetailItemAuctioneer()
    {
        String namaBidder = itemBiddingResources.getNamaBidder();
        if (namaBidder.equals("nouser"))
        {
            auctioneerViewPagerAdapter.addFragment(this.biddingFinishedNoBidFragment, "Tawaran");
            auctioneerViewPagerAdapter.addFragment(this.statisticFragment, "Statistik");
        }
        else
        {
            auctioneerViewPagerAdapter.addFragment(this.biddingFinishedWithBidderFragment, "Tawaran");
            auctioneerViewPagerAdapter.addFragment(this.statisticFragment, "Statistik");
        }
    }

/*    public void setFragments()
    {
        setFragmentAndTitleListDetailItemAuctioneer();
    }*/

    public void setFragmentValue(BiddingResources itemBidResources)
    {
        String namaBidder = itemBiddingResources.getNamaBidder();
        if (!namaBidder.equals("nouser")) setBidderInformation(itemBidResources);
    }

    public void setStatisticInformation(String hargaAwal, String hargaEkspektasi, String hargaTawaran)
    {
        statisticFragment.setStatisticInformation(hargaAwal, hargaEkspektasi, hargaTawaran);
    }
    /*
    * Set detail item value method end here
    * */

    private void setBidderInformation(BiddingResources itemBiddingResources)
    {
        biddingFinishedWithBidderFragment.setBidderInformation(itemBiddingResources);
    }

    /*
    * Viewpager setter method start here
    * */
    private void setViewPagerAdapter()
    {
        initializeAdapter();
        setFragmentAndTitleListDetailItemAuctioneer();
        detailAuctioneerViewPager.setAdapter(auctioneerViewPagerAdapter);
        detailAuctioneerViewPager.getAdapter().notifyDataSetChanged();
    }
    /*
    * Viewpager setter method end here
    * */

    public void setFragmentManager(FragmentManager manager)
    {
        this.manager = manager;
    }

}
