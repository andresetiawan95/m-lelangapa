package com.lelangkita.android.fragments.detail;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.lelangkita.android.R;
import com.lelangkita.android.apicalls.detail.DetailItemAPI;
import com.lelangkita.android.apicalls.socket.BiddingSocket;
import com.lelangkita.android.interfaces.DataReceiver;
import com.lelangkita.android.resources.DetailItemResources;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import io.socket.client.Socket;

/**
 * Created by Andre on 12/24/2016.
 */

public class DetailFragment extends Fragment {
    private DataReceiver detailReceived;
    private DataReceiver triggerReceived;
    private DetailHeaderFragment detailHeaderFragment;
    private DetailGambarFragment detailGambarFragment;
    private DetailBiddingNotStartedFragment detailBiddingNotStartedFragment;
    private DetailBiddingFragment detailBiddingFragment;
    private DetailWaktuBidNotStartedFragment detailWaktuBidNotStartedFragment;
    private DetailWaktuBidStartedFragment detailWaktuBidStartedFragment;
    private DetailWaktuBidFinishedFragment detailWaktuBidFinishedFragment;
    private DetailDeskripsiFragment detailDeskripsiFragment;
    private DetailKomentarFragment detailKomentarFragment;
    private DetailAuctioneerFragment detailAuctioneerFragment;
    private DetailItemResources detailItem;

    private SwipeRefreshLayout swipeRefreshLayout;
    private Long serverDateTimeMillisecond;
    private String itemID, biddingInformation;
    private Socket biddingSocket;
    public DetailFragment(){}
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        detailItem = new DetailItemResources();
        biddingSocket = new BiddingSocket().getSocket();
        biddingInformation = "opened";
        itemID = getActivity().getIntent().getStringExtra("items_id");
        detailHeaderFragment = new DetailHeaderFragment();
        detailGambarFragment = new DetailGambarFragment();
        detailBiddingFragment = new DetailBiddingFragment();
        detailBiddingNotStartedFragment = new DetailBiddingNotStartedFragment();
        detailWaktuBidNotStartedFragment = new DetailWaktuBidNotStartedFragment();
        detailWaktuBidStartedFragment = new DetailWaktuBidStartedFragment();
        detailWaktuBidFinishedFragment = new DetailWaktuBidFinishedFragment();
        detailDeskripsiFragment = new DetailDeskripsiFragment();
        detailKomentarFragment = new DetailKomentarFragment();
        detailAuctioneerFragment = new DetailAuctioneerFragment();
    }
    @Override
    public View onCreateView (final LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_detail_barang_layout, container, false);
        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.fragment_detail_barang_swipe_refreshLayout);
        setUpSwipeRefreshLayout();
        detailReceived = new DataReceiver() {
            @Override
            public void dataReceived(Object output) {
                if (output.toString().equals("done"))
                {
                    if (biddingInformation.equals("start"))
                    {
                        if (detailItem.getItembidstatus()==1)
                        {
                            Log.v("Selesai", "Request Selesai");
                            swipeRefreshLayout.setRefreshing(false);
                            setDataToChildFragments(detailItem);
                            //untuk menampilkan fragment submit bid
                            setChildFragments();
                        }
                        else
                        {
                            getDetailItem(itemID);
                        }
                    }
                    else if (biddingInformation.equals("finish"))
                    {
                        if (detailItem.getItembidstatus()==-1)
                        {
                            Log.v("Selesai", "Request Selesai");
                            swipeRefreshLayout.setRefreshing(false);
                            setDataToChildFragments(detailItem);
                            //untuk menampilkan fragment submit bid
                            setChildFragments();
                        }
                        else
                        {
                            getDetailItem(itemID);
                        }
                    }
                    else
                    {
                        Log.v("Selesai", "Request Selesai");
                        swipeRefreshLayout.setRefreshing(false);
                        setDataToChildFragments(detailItem);
                        //untuk menampilkan fragment submit bid
                        setChildFragments();
                    }
                }
            }
        };
        triggerReceived = new DataReceiver() {
            @Override
            public void dataReceived(Object output) {
                if (output.toString().equals("start"))
                {
                    biddingInformation = "start";
                    getDetailItem(itemID);
                }
                if (output.toString().equals("finish"))
                {
                    biddingInformation = "finish";
                    getDetailItem(itemID);
                }
            }
        };
        return view;
    }
    @Override
    public void onDestroy()
    {
        super.onDestroy();
    }



    private void setUpSwipeRefreshLayout()
    {
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                Log.v("Refresh status", "Refresh jalan");
                getDetailItem(itemID);
            }
        });
        swipeRefreshLayout.setColorSchemeResources(android.R.color.holo_red_light);
        swipeRefreshLayout.post(new Runnable() {
            @Override
            public void run() {
                Log.v("Refresh post status", "Refresh post jalan");
                swipeRefreshLayout.setRefreshing(true);
                getDetailItem(itemID);
            }
        });
    }
    private void setDataToChildFragments(DetailItemResources detailItem)
    {
        if (detailItem.getItembidstatus() == 0)
        {
            detailBiddingNotStartedFragment.setStartTimeAndServerTime(detailItem.getTanggaljammulai_ms(),serverDateTimeMillisecond);
            detailBiddingNotStartedFragment.setTriggerSender(triggerReceived);
            detailWaktuBidNotStartedFragment.setDetailItem(detailItem);
        }
        else if (detailItem.getItembidstatus() == 1)
        {
            Log.v("Bid sudah dimulai", "Bid sudah dimulai--setdatatochildfragment");
            detailWaktuBidStartedFragment.setDetailItem(detailItem);
            detailWaktuBidStartedFragment.setTriggerBiddingDone(triggerReceived);
            detailWaktuBidStartedFragment.setServerTime(serverDateTimeMillisecond);
        }
        else
        {
            Log.v("Bid sudah selesai", "Bid sudah selesai--setdatatochildfragment");
            detailWaktuBidFinishedFragment.setDetailItem(detailItem);
        }
        detailHeaderFragment.setDetailItem(detailItem);
        detailGambarFragment.setDetailItem(detailItem);
        detailDeskripsiFragment.setDetailItem(detailItem);
    }
    private void setChildFragments()
    {
        if (detailItem.getItembidstatus() == 0)
        {
            getFragmentManager().beginTransaction()
                    .replace(R.id.fragment_detail_barang_header_fragment, detailHeaderFragment)
                    .replace(R.id.fragment_detail_barang_gambar_fragment, detailGambarFragment)
                    .replace(R.id.fragment_detail_barang_bidding_fragment, detailBiddingNotStartedFragment)
                    .replace(R.id.fragment_detail_barang_waktubid_fragment, detailWaktuBidNotStartedFragment)
                    .replace(R.id.fragment_detail_barang_deskripsi_fragment, detailDeskripsiFragment)
                    .replace(R.id.fragment_detail_barang_komentar_fragment, detailKomentarFragment)
                    .replace(R.id.fragment_detail_barang_auctioneer_fragment, detailAuctioneerFragment)
                    .commit();
        }
        else if (detailItem.getItembidstatus()==1)
        {
            Log.v("Bid sudah dimulai", "Bid sudah dimulai--setchildfragment");
            getFragmentManager().beginTransaction()
                    .replace(R.id.fragment_detail_barang_header_fragment, detailHeaderFragment)
                    .replace(R.id.fragment_detail_barang_gambar_fragment, detailGambarFragment)
                    .replace(R.id.fragment_detail_barang_bidding_fragment, detailBiddingFragment)
                    .replace(R.id.fragment_detail_barang_waktubid_fragment, detailWaktuBidStartedFragment)
                    .replace(R.id.fragment_detail_barang_deskripsi_fragment, detailDeskripsiFragment)
                    .replace(R.id.fragment_detail_barang_komentar_fragment, detailKomentarFragment)
                    .replace(R.id.fragment_detail_barang_auctioneer_fragment, detailAuctioneerFragment)
                    .commit();
        }
        else
        {
            Log.v("Bid sudah selesai", "Bid sudah selesai--setchildfragment");
            getFragmentManager().beginTransaction()
                    .replace(R.id.fragment_detail_barang_header_fragment, detailHeaderFragment)
                    .replace(R.id.fragment_detail_barang_gambar_fragment, detailGambarFragment)
                    .replace(R.id.fragment_detail_barang_bidding_fragment, detailBiddingFragment)
                    .replace(R.id.fragment_detail_barang_waktubid_fragment, detailWaktuBidFinishedFragment)
                    .replace(R.id.fragment_detail_barang_deskripsi_fragment, detailDeskripsiFragment)
                    .replace(R.id.fragment_detail_barang_komentar_fragment, detailKomentarFragment)
                    .replace(R.id.fragment_detail_barang_auctioneer_fragment, detailAuctioneerFragment)
                    .commit();
        }
    }
    private void getDetailItem(String itemID)
    {
        Log.v("Dipanggil", "Called");
        swipeRefreshLayout.setRefreshing(true);
        DataReceiver dataReceiver = new DataReceiver() {
            @Override
            public void dataReceived(Object output) {
                String response = output.toString();
                try {
                    JSONObject jsonResponse = new JSONObject(response);
                    String status = jsonResponse.getString("status");
                    if (status.equals("success"))
                    {
                        serverDateTimeMillisecond = jsonResponse.getLong("server_time_in_millisecond");
                        JSONArray responseData = jsonResponse.getJSONArray("data");
                        for (int i=0;i<responseData.length();i++)
                        {
                            JSONObject itemDataObject = responseData.getJSONObject(i);
                            detailItem.setIdbarang(itemDataObject.getString("item_id"));
                            detailItem.setNamabarang(itemDataObject.getString("item_name"));
                            detailItem.setDeskripsibarang(itemDataObject.getString("item_description"));
                            detailItem.setHargaawal(itemDataObject.getString("starting_price"));
                            detailItem.setHargatarget(itemDataObject.getString("expected_price"));
                            String startTimeBeforeSplit = itemDataObject.getString("start_time");
                            String endTimeBeforeSplit = itemDataObject.getString("end_time");
                            detailItem.setTanggaljammulai(startTimeBeforeSplit);
                            detailItem.setTanggaljamselesai(endTimeBeforeSplit);
                            detailItem.setTanggaljammulai_ms(itemDataObject.getLong("start_time_millisecond"));
                            detailItem.setTanggaljamselesai_ms(itemDataObject.getLong("end_time_millisecond"));
                            detailItem.setItembidstatus(itemDataObject.getInt("item_bid_status"));
                            String[] startTimePart = startTimeBeforeSplit.split("T");
                            String[] endTimePart = endTimeBeforeSplit.split("T");
                            String startDate = startTimePart[0];
                            String endDate = endTimePart[0];
                            String [] startHourPart = startTimePart[1].split("\\.");
                            String [] endHourPart = endTimePart[1].split("\\.");
                            String startHour = startHourPart[0];
                            String endHour = endHourPart[0];
                            detailItem.setTanggalmulai(startDate);
                            detailItem.setJammulai(startHour);
                            detailItem.setTanggalselesai(endDate);
                            detailItem.setJamselesai(endHour);
                            detailItem.setNamapengguna(itemDataObject.getString("user_name"));
                            JSONArray detailUrlGambarItemArray = itemDataObject.getJSONArray("url");
                            for (int j=0;j<detailUrlGambarItemArray.length();j++)
                            {
                                JSONObject detailUrlGambarItemObject = detailUrlGambarItemArray.getJSONObject(j);
                                detailItem.setUrlgambarbarang("http://es3.lelangkita.com/" +detailUrlGambarItemObject.getString("url"));
                            }
                        }
                        detailReceived.dataReceived("done");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };
        DetailItemAPI detailItemAPI = new DetailItemAPI(itemID, dataReceiver);
        RequestQueue queue = Volley.newRequestQueue(getActivity());
        queue.add(detailItemAPI);
    }
}
