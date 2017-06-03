package com.lelangapa.app.fragments.detail.detailitemowner;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.lelangapa.app.R;
import com.lelangapa.app.interfaces.AuctioneerResponseReceiver;
import com.lelangapa.app.resources.BiddingResources;
import com.lelangapa.app.resources.DetailItemResources;
import com.lelangapa.app.viewpagers.DetailBarangAuctioneerViewPagerAdapter;

/**
 * Created by andre on 13/02/17.
 */

public class AuctioneerMenuPagerStartedFragment extends Fragment {
    private TabLayout detailAuctioneerTabLayout;
    private ViewPager detailAuctioneerViewPager;
    private DetailBarangAuctioneerViewPagerAdapter auctioneerViewPagerAdapter;
    private MenuPagerBiddingStartedFragmentNew biddingStartedFragment;
    private MenuPagerStatisticFragment statisticFragment;

    private DetailItemResources detailItem;
    private BiddingResources itemBiddingResources;
    /*
    private ArrayList<Fragment> fragmentViewPagerList = null;
    private ArrayList<String> titleViewPagerList = null;*/
    private FragmentManager manager;
    private int bidStatus;

    public AuctioneerMenuPagerStartedFragment()
    {
        biddingStartedFragment = new MenuPagerBiddingStartedFragmentNew();
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
        View view = inflater.inflate(R.layout.fragment_detail_barang_bidding_auctioneer_layout_started, container, false);
        initializeViews(view);
        setDetailItemForBiddingStartedFragment();
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

    /*private void setFragmentAndTitleListDetailItemAuctioneer()
    {
        fragmentViewPagerList.add(biddingStartedFragment);
        fragmentViewPagerList.add(statisticFragment);
        titleViewPagerList.add("Tawaran");
        titleViewPagerList.add("Statistik");
    }

    public void setFragments()
    {
        setFragmentAndTitleListDetailItemAuctioneer();
    }*/

    public void setFragmentValue(BiddingResources itemBidResources, AuctioneerResponseReceiver receiver)
    {
        setBidderInformation(itemBidResources);
        setAuctioneerResponseReceiver(receiver);
    }

    public void setStatisticInformation(String hargaAwal, String hargaEkspektasi, String hargaTawaran)
    {
        statisticFragment.setStatisticInformation(hargaAwal, hargaEkspektasi, hargaTawaran);
    }
    /*
    * Set detail item value method end here
    * */

    public void setBidderInformation(BiddingResources itemBiddingResources)
    {
        biddingStartedFragment.setBidderInformation(itemBiddingResources);
    }
    public void setAuctioneerResponseReceiver(AuctioneerResponseReceiver receiver)
    {
        biddingStartedFragment.setAuctioneerResponseReceiver(receiver);
    }
    public void setDetailItemForBiddingStartedFragment()
    {
        biddingStartedFragment.setDetailItem(detailItem);
    }
    /*
    * Viewpager setter method start here
    * */
    private void setViewPagerAdapter()
    {
        initializeAdapter();
        auctioneerViewPagerAdapter.addFragment(this.biddingStartedFragment, "Tawaran");
        auctioneerViewPagerAdapter.addFragment(this.statisticFragment, "Statistik");
        detailAuctioneerViewPager.setAdapter(auctioneerViewPagerAdapter);
        detailAuctioneerViewPager.getAdapter().notifyDataSetChanged();
    }

    /*
    * Viewpager setter method end here
    * */
}
