package com.lelangapa.app.fragments.detail.detailitemowner;

import android.support.v4.app.Fragment;

/**
 * Created by andre on 25/01/17.
 */

public class AuctioneerMenuPagerFragment extends Fragment {
    /*private TabLayout detailAuctioneerTabLayout;
    private ViewPager detailAuctioneerViewPager;
    private DetailBarangAuctioneerViewPagerAdapter auctioneerViewPagerAdapter;
    private MenuPagerBiddingNotStartedFragment biddingNotStartedFragment;
    private MenuPagerBiddingStartedFragmentOld biddingStartedFragment;
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
        biddingStartedFragment = new MenuPagerBiddingStartedFragmentOld();
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
        View view = inflater.inflate(R.layout.fragment_detail_barang_bidding_auctioneer_layout_notstarted, container, false);
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
        }
        else if (bidStatus == 1)
        {
            fragmentViewPagerList.add(biddingStartedFragment);
            fragmentViewPagerList.add(statisticFragment);
            titleViewPagerList.add("Tawaran");
            titleViewPagerList.add("Statistik");
            Log.v("FRAGMENT added", "FRAGMENT added " + fragmentViewPagerList.size());
        }
        else if (bidStatus == -1)
        {
            Log.v("Bid status", "Bid status = -1");
            String namaBidder = itemBiddingResources.getNamaBidder();
            if (namaBidder.equals("nouser"))
            {
                Log.v("Tidak ada pemenang", "Tidak ada pemenang lelang");
                fragmentViewPagerList.add(biddingFinishedNoBidFragment);
                fragmentViewPagerList.add(statisticFragment);
                titleViewPagerList.add("Tawaran");
                titleViewPagerList.add("Statistik");
            }
            else
            {
                Log.v("Ada pemenang", "Ada pemenang lelang");
                fragmentViewPagerList.add(biddingFinishedWithBidderFragment);
                fragmentViewPagerList.add(statisticFragment);
                titleViewPagerList.add("Tawaran");
                titleViewPagerList.add("Statistik");
            }
        }
        *//*
        * Untuk mengecek apakah adapter sudah di set atau belum
        * Jika sudah di set, maka tinggal meng-update fragment pada adapter saja
        * Dimana ada fungsi notifySetDataChanged() untuk merubah adapter
        * *//*
        if (adapterAlreadyExist)
        {
            auctioneerViewPagerAdapter.setFragmentList(fragmentViewPagerList);
            auctioneerViewPagerAdapter.setTitleList(titleViewPagerList);
            auctioneerViewPagerAdapter.notifyDataSetChanged();
            Log.v("Adapter sudah ada", "Adapter sudah ada");
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
        *//*
        * Sekaligus melakukan update terhadap fragmentList dan titleList
        * *//*
        setFragmentAndTitleListDetailItemAuctioneer(bidStatus);
    }

    *//*
    * Update value on child fragments methods start here
    * *//*
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
    *//*
    * Update value on child fragments methods end here
    * *//*

    *//*
    * Set detail item value method start here
    * *//*
    public void setUpDetailItemAndBiddingResources(DetailItemResources detailItem, BiddingResources itemBiddingResources)
    {
        this.detailItem = detailItem;
        this.itemBiddingResources = itemBiddingResources;
    }
    *//*
    * Set detail item value method end here
    * *//*

    *//*
    * Set or update value on child method fragment start here
    * *//*
    public void setBidderInformation(BiddingResources itemBiddingResources)
    {
        biddingStartedFragment.setBidderInformation(itemBiddingResources);
    }
    public void setAuctioneerResponseReceiver(AuctioneerResponseReceiver receiver)
    {
        biddingStartedFragment.setAuctioneerResponseReceiver(receiver);
    }
    public void setStatisticInformation(String hargaAwal, String hargaEkspektasi, String hargaTawaran)
    {
        statisticFragment.setStatisticInformation(hargaAwal, hargaEkspektasi, hargaTawaran);
    }
    *//*
    * Set or update value on child method end here
    * */
}
