package com.lelangapa.android.fragments.detail.detailitemowner;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.lelangapa.android.R;
import com.lelangapa.android.apicalls.detail.DetailItemAPI;
import com.lelangapa.android.apicalls.singleton.RequestController;
import com.lelangapa.android.apicalls.socket.BiddingSocket;
import com.lelangapa.android.interfaces.DataReceiver;
import com.lelangapa.android.interfaces.SocketReceiver;
import com.lelangapa.android.resources.BiddingPeringkatResources;
import com.lelangapa.android.resources.DetailItemResources;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import io.socket.client.Socket;

/**
 * Created by andre on 25/01/17.
 */

public class DetailItemAuctioneerFragment extends Fragment {
    private String itemID;
    private String biddingInformation;
    private Long serverDateTimeMillisecond;
    private DetailItemResources detailItem;
    private boolean isPaused;
    private ArrayList<BiddingPeringkatResources> biddingPeringkatList;

    private BiddingSocket biddingSocket;
    private Socket socketBinder;

    private DataReceiver detailReceived, timeTriggerReceived, auctioneerResponseReceived;
    private SocketReceiver onConnectedReceived, onBidSuccessReceived, onBidFailedReceived, onBidCancelledReceived, onWinnerSelected;

    private AuctioneerMenuPagerFragment menuPagerFragment;
    private HeaderFragment headerFragment;
    private ImageFragment imageFragment;
    private DescriptionFragment descriptionFragment;
    private CommentaryFragment commentaryFragment;
    private WaktuBidNotStartedFragment waktuBidNotStartedFragment;
    private WaktuBidStartedFragment waktuBidStartedFragment;
    private WaktuBidFinishedFragment waktuBidFinishedFragment;

    private SwipeRefreshLayout swipeRefreshLayout;

    /*
    * DetailItemAuctioneerFragment lifecycle starts here
    *===================================================
    * */
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        initializeConstants();
        initializeFragments();
        initializeDataReceiver();
        initializeSocketReceiver();
        initializeSocket();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_detail_barang_layout, container, false);
        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.fragment_detail_barang_swipe_refreshLayout);
        setUpSwipeRefreshLayout();
        return view;
    }

    @Override
    public void onResume()
    {
        super.onResume();
        onResumeConfiguration();
    }

    @Override
    public void onPause()
    {
        super.onPause();
        onPauseConfiguration();
    }

    @Override
    public void onDestroy()
    {
        super.onDestroy();
        onDestroyConfiguration();
    }

    /*
    * Lifecycle method ends here
    * ==========================
    * */

    /*
    * ==========================
    * Initialization methods here
    * */

    private void initializeConstants()
    {
        itemID = getActivity().getIntent().getExtras().getString("items_id");
        biddingInformation = "opened";
        detailItem = new DetailItemResources();
        isPaused = false;
    }
    private void initializeFragments()
    {
        menuPagerFragment = new AuctioneerMenuPagerFragment();
        headerFragment = new HeaderFragment();
        imageFragment = new ImageFragment();
        descriptionFragment = new DescriptionFragment();
        commentaryFragment = new CommentaryFragment();
        waktuBidNotStartedFragment = new WaktuBidNotStartedFragment();
        waktuBidStartedFragment = new WaktuBidStartedFragment();
        waktuBidFinishedFragment = new WaktuBidFinishedFragment();
    }
    private void initializeSocket()
    {
        if (biddingSocket == null)
        {
            biddingSocket = new BiddingSocket(getActivity());
            biddingSocket.setSocketConnected(onConnectedReceived);
            biddingSocket.setSocketBidSuccessReceiver(onBidSuccessReceived);
            biddingSocket.setSocketBidFailedReceiver(onBidFailedReceived);
            biddingSocket.setSocketBidCancelled(onBidCancelledReceived);
            biddingSocket.setSocketWinnerSelected(onWinnerSelected);
            socketBinder = biddingSocket.getSocket();
        }
    }
    private void initializeDataReceiver()
    {
        setDetailReceived();
        setTimeTriggerReceived();
        setAuctioneerResponseReceived();
    }
    private void initializeSocketReceiver()
    {
        setSocketOnConnectReceived();
        setSocketOnBiddingSuccessReceived();
        setSocketOnBiddingFailedReceived();
        setSocketOnBiddingCancelledReceived();
        setSocketOnWinnerSelectedReceived();
    }
    private void connectSocket()
    {
        socketBinder.connect();
        socketBinder.on("connected", biddingSocket.onConnected);
        socketBinder.on("bidsuccess", biddingSocket.onSubmitBidSuccess);
        socketBinder.on("bidfailed", biddingSocket.onSubmitBidFailed);
        socketBinder.on("winnerselected", biddingSocket.onWinnerSelected);
        socketBinder.on("cancelauction", biddingSocket.onBidCancelled);
     }
    private void disconnectSocket()
    {
        socketBinder.disconnect();
        socketBinder.off("connected", biddingSocket.onConnected);
        socketBinder.off("bidsuccess", biddingSocket.onSubmitBidSuccess);
        socketBinder.off("bidfailed", biddingSocket.onSubmitBidFailed);
        socketBinder.off("winnerselected", biddingSocket.onWinnerSelected);
        socketBinder.off("cancelauction", biddingSocket.onBidCancelled);
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
    /*
    * Initialization methods end here
    * ==========================
    * */

    /*
    * Receiver methods start here
    * */
    private void setDetailReceived()
    {
        detailReceived = new DataReceiver() {
            @Override
            public void dataReceived(Object output) {
                if (output.toString().equals("done"))
                {
                    if (biddingInformation.equals("start"))
                    {
                        if (detailItem.getItembidstatus()==1)
                        {
                            swipeRefreshLayout.setRefreshing(false);
                            if (!socketBinder.connected() && biddingSocket != null)
                            {
                                connectSocket();
                            }
                            setDataToChildFragments(detailItem);
                            setChildFragments(detailItem.getItembidstatus());
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
                            swipeRefreshLayout.setRefreshing(false);

                            if (socketBinder.connected())
                            {
                                socketBinder.emit("leave-room", itemID);
                                biddingSocket.IS_JOINED_STATUS = false;
                                disconnectSocket();
                            }

                            setDataToChildFragments(detailItem);
                            setChildFragments(detailItem.getItembidstatus());
                        }
                        else
                        {
                            getDetailItem(itemID);
                        }
                    }
                    else if (biddingInformation.equals("opened"))
                    {
                        if (detailItem.getItembidstatus() == 1)
                        {
                            if (socketBinder.connected() && biddingSocket != null)
                            {
                                connectSocket();
                            }
                        }
                        swipeRefreshLayout.setRefreshing(false);
                        setDataToChildFragments(detailItem);
                        setChildFragments(detailItem.getItembidstatus());
                    }
                }
            }
        };
    }
    private void setTimeTriggerReceived()
    {
        timeTriggerReceived = new DataReceiver() {
            @Override
            public void dataReceived(Object output) {
                if (output.toString().equals("start"))
                {
                    biddingInformation = "start";
                    //setAlertDialogBidStarted();
                }
                if (output.toString().equals("finish"))
                {
                    biddingInformation = "finish";
                    //setAlertDialogBidFinished();
                }
            }
        };
    }
    private void setAuctioneerResponseReceived()
    {
        //untuk menginisiasi interface memilih pemenang maupun membatalkan lelang
    }
    private void setSocketOnConnectReceived()
    {
        onConnectedReceived = new SocketReceiver() {
            @Override
            public void socketReceived(Object status, Object response) {
                //biddingSocket.IS_CONNECTED_STATUS = true;
                if (!biddingSocket.IS_JOINED_STATUS)
                {
                    Log.v("Socket CONNECTED", "CONNECTED AND INITIALIZED");
                    socketBinder.emit("join-room", itemID);
                    Log.v("Joining room init", "Joining room now");
                    biddingSocket.IS_JOINED_STATUS = true;
                }
            }
        };
    }
    private void setSocketOnBiddingSuccessReceived()
    {

    }
    private void setSocketOnBiddingFailedReceived()
    {
        onBidFailedReceived = new SocketReceiver() {
            @Override
            public void socketReceived(Object status, Object response) {
                //setAlertDialogBidFailed();
            }
        };
    }
    private void setSocketOnBiddingCancelledReceived()
    {
        onBidCancelledReceived = new SocketReceiver() {
            @Override
            public void socketReceived(Object status, Object response) {
                //setAlertDialogBidCancelled();
            }
        };
    }
    private void setSocketOnWinnerSelectedReceived()
    {
        onWinnerSelected = new SocketReceiver() {
            @Override
            public void socketReceived(Object status, Object response) {
                //setAlertDialogWinnerSelected();
            }
        };
    }
    /*
    * Receiver methods end here
    * */

    /*
    * Logic and fragment replacement methods starts here
    * */
    private void onResumeConfiguration()
    {
        if (isPaused)
        {
            getDetailItem(itemID);
            isPaused = false;
            if (!socketBinder.connected() && biddingSocket != null)
            {
                connectSocket();
            }
        }
        if (biddingSocket != null && !biddingSocket.IS_JOINED_STATUS)
        {
            if (socketBinder.connected())
            {
                socketBinder.emit("join-room", itemID);
                Log.v("Joining room resume", "Joining room resume");
                biddingSocket.IS_JOINED_STATUS = true;
            }
        }
    }
    private void onPauseConfiguration()
    {
        if (biddingSocket != null && biddingSocket.IS_JOINED_STATUS)
        {
            if (socketBinder.connected())
            {
                socketBinder.emit("leave-room", itemID);
                Log.v("Leaving room pause", "Leaving room pause");
                isPaused = true;
                biddingSocket.IS_JOINED_STATUS = false;
            }
        }
    }
    private void onDestroyConfiguration()
    {
        if (biddingSocket != null)
        {
            if (socketBinder.connected())
            {
                disconnectSocket();
                biddingSocket.IS_JOINED_STATUS = false;
            }
        }
    }
    private void setDataToChildFragments(DetailItemResources detailItem)
    {
        if (detailItem.getItembidstatus() == 0)
        {
            Long startTime_ms = detailItem.getTanggaljammulai_ms();
            menuPagerFragment.setUpDetailItem(detailItem);
            menuPagerFragment.setFragmentValueBidNotStart(startTime_ms, serverDateTimeMillisecond, timeTriggerReceived);
            menuPagerFragment.setOrUpdateBidStatus(0);
            waktuBidNotStartedFragment.setDetailItem(detailItem);
        }
        else if (detailItem.getItembidstatus() == 1)
        {
            menuPagerFragment.setUpDetailItem(detailItem);
            menuPagerFragment.setFragmentValueBidStart();
            menuPagerFragment.setOrUpdateBidStatus(1);
            waktuBidStartedFragment.setDetailItem(detailItem);
        }
        else if (detailItem.getItembidstatus() == -1)
        {
            menuPagerFragment.setUpDetailItem(detailItem);
            menuPagerFragment.setFragmentValueBidFinish();
            menuPagerFragment.setOrUpdateBidStatus(-1);
            waktuBidFinishedFragment.setDetailItem(detailItem);
        }
        headerFragment.setDetailItem(detailItem);
        imageFragment.setDetailItem(detailItem);
        descriptionFragment.setDetailItem(detailItem);

    }
    private void setChildFragments(int status)
    {
        if (status == 0)
        {
            setUpUsedFragments(headerFragment, imageFragment, waktuBidNotStartedFragment, menuPagerFragment, descriptionFragment, commentaryFragment);
        }
        else if (status == 1)
        {
            setUpUsedFragments(headerFragment, imageFragment, waktuBidStartedFragment, menuPagerFragment, descriptionFragment, commentaryFragment);
        }
        else if (status == -1)
        {
            setUpUsedFragments(headerFragment, imageFragment, waktuBidFinishedFragment, menuPagerFragment, descriptionFragment, commentaryFragment);
        }
    }
    private void setUpUsedFragments(Fragment header, Fragment gambar, Fragment waktubid, Fragment pager, Fragment description, Fragment comment)
    {
        getFragmentManager().beginTransaction()
                .replace(R.id.fragment_detail_barang_header_fragment, header)
                .replace(R.id.fragment_detail_barang_gambar_fragment, gambar)
                .replace(R.id.fragment_detail_barang_waktubid_fragment, waktubid)
                .replace(R.id.fragment_detail_barang_bidding_fragment, pager)
                .replace(R.id.fragment_detail_barang_deskripsi_fragment, description)
                .replace(R.id.fragment_detail_barang_komentar_fragment, comment)
                .commit();
    }
    /*
    * Logic and fragment replacement methods starts here
    * */

    /*
    * API request method start here
    * */
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
                            detailItem.setIdauctioneer(itemDataObject.getString("user_id"));
                            detailItem.setNamaauctioneer(itemDataObject.getString("user_name"));
                            detailItem.setNamabidder(itemDataObject.getString("bidder_name"));
                            detailItem.setHargabid(itemDataObject.getString("item_bid_price"));
                            JSONArray detailUrlGambarItemArray = itemDataObject.getJSONArray("url");
                            JSONArray biddingPeringkatArray = itemDataObject.getJSONArray("peringkat");

                            //clear list when load data from server
                            /*biddingPeringkatList.clear();
                            for (int j=0;j<biddingPeringkatArray.length();j++)
                            {
                                BiddingPeringkatResources bidPeringkat = new BiddingPeringkatResources();
                                JSONObject biddingPeringkatObject = biddingPeringkatArray.getJSONObject(j);
                                bidPeringkat.setIdBidder(biddingPeringkatObject.getString("user_id"));
                                bidPeringkat.setNamaBidder(biddingPeringkatObject.getString("user_name"));
                                bidPeringkat.setHargaBid(biddingPeringkatObject.getString("bid_price"));
                                biddingPeringkatList.add(bidPeringkat);
                            }*/
                            for (int j=0;j<detailUrlGambarItemArray.length();j++)
                            {
                                JSONObject detailUrlGambarItemObject = detailUrlGambarItemArray.getJSONObject(j);
                                detailItem.setUrlgambarbarang("http://es3.lelangapa.com/" +detailUrlGambarItemObject.getString("url"));
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
        RequestController.getInstance(getActivity()).addToRequestQueue(detailItemAPI);
    }
    /*
    * API request method end here
    * */
}
