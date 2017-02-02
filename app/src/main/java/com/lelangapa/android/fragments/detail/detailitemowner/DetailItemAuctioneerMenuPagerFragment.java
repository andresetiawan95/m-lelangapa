package com.lelangapa.android.fragments.detail.detailitemowner;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.lelangapa.android.R;
import com.lelangapa.android.viewpagers.DetailBarangAuctioneerViewPagerAdapter;

import java.util.ArrayList;

/**
 * Created by andre on 25/01/17.
 */

public class DetailItemAuctioneerMenuPagerFragment extends Fragment {
    private TabLayout detailAuctioneerTabLayout;
    private ViewPager detailAuctioneerViewPager;
    private DetailBarangAuctioneerViewPagerAdapter auctioneerViewPagerAdapter;
    private AuctioneerMenuPagerBiddingNotStartedFragment biddingNotStartedFragment;
    private AuctioneerMenuPagerBiddingStartedFragment biddingStartedFragment;
    private AuctioneerMenuPagerStatisticFragment statisticFragment;
    private ArrayList<Fragment> fragmentViewPagerList = null;
    private ArrayList<String> titleViewPagerList = null;
    private boolean adapterAlreadyExist = false;
    private int bidStatus;
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        auctioneerViewPagerAdapter = new DetailBarangAuctioneerViewPagerAdapter(getFragmentManager());
        biddingNotStartedFragment = new AuctioneerMenuPagerBiddingNotStartedFragment();
        biddingStartedFragment = new AuctioneerMenuPagerBiddingStartedFragment();
        statisticFragment = new AuctioneerMenuPagerStatisticFragment();
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_detail_barang_bidding_auctioneer_layout, container, false);
        setViewPagerAdapter();
        return view;
    }
    public void setFragmentAndTitleListDetailItemAuctioneer(int bidStatus)
    {
        if (fragmentViewPagerList == null && titleViewPagerList == null)
        {
            fragmentViewPagerList = new ArrayList<>();
            titleViewPagerList = new ArrayList<>();
        }
        else
        {
            if (!fragmentViewPagerList.isEmpty()) fragmentViewPagerList.clear();
            if (!titleViewPagerList.isEmpty())titleViewPagerList.clear();
        }

        if (bidStatus == 0)
        {
            fragmentViewPagerList.add(biddingNotStartedFragment);
            fragmentViewPagerList.add(statisticFragment);
            titleViewPagerList.add("Tawar");
            titleViewPagerList.add("Statistik");
        }
        else if (bidStatus == 1)
        {
            fragmentViewPagerList.add(biddingStartedFragment);
            fragmentViewPagerList.add(statisticFragment);
            titleViewPagerList.add("Tawar");
            titleViewPagerList.add("Statistik");
        }
        else
        {
            /*
            * implemented later
            * */
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
}
