package com.lelangapa.android.fragments.detail.detailitemowner;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.lelangapa.android.R;
import com.lelangapa.android.activities.detail.DetailBarangActivity;
import com.lelangapa.android.apicalls.detail.DetailItemAPI;
import com.lelangapa.android.apicalls.singleton.RequestController;
import com.lelangapa.android.apicalls.socket.BiddingSocket;
import com.lelangapa.android.fragments.detail.detailtawaran.DaftarTawaranFragment;
import com.lelangapa.android.fragments.detail.ownerdialogfragment.ListBidderDialogFragment;
import com.lelangapa.android.interfaces.AuctioneerResponseReceiver;
import com.lelangapa.android.interfaces.DataReceiver;
import com.lelangapa.android.interfaces.SocketReceiver;
import com.lelangapa.android.resources.BiddingResources;
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
    private BiddingResources itemBidResources;
    private boolean onPauseWhenSocketAlreadyConnected, onPauseActivity, biddingAlreadyDone;
    private Context activityContext;
    private ArrayList<BiddingResources> biddingPeringkatList;

    private BiddingSocket biddingSocket;
    private Socket socketBinder;

    private DataReceiver detailReceived, timeTriggerReceived;
    private SocketReceiver onConnectedReceived, onBidSuccessReceived, onBidFailedReceived, onBidCancelledReceived, onWinnerSelected;
    private AuctioneerResponseReceiver auctioneerResponseReceiver;

    private AuctioneerMenuPagerFragment menuPagerFragment;
    private AuctioneerMenuPagerNotStartedFragment menuPagerNotStartedFragment;
    private AuctioneerMenuPagerStartedFragment menuPagerStartedFragment;
    private AuctioneerMenuPagerFinishedFragment menuPagerFinishedFragment;

    private HeaderFragment headerFragment;
    private ImageFragment imageFragment;
    private DescriptionFragment descriptionFragment;
    private CommentaryFragment commentaryFragment;
    private WaktuBidNotStartedFragment waktuBidNotStartedFragment;
    private WaktuBidStartedFragment waktuBidStartedFragment;
    private WaktuBidFinishedFragment waktuBidFinishedFragment;

    private SwipeRefreshLayout swipeRefreshLayout;
    private FragmentManager fragmentManager;

    //nyoba
    private boolean isChangeTawaranFragment = false;

    public DetailItemAuctioneerFragment()
    {
        initializeFragments();
    }
    /*
    * DetailItemAuctioneerFragment lifecycle starts here
    *===================================================
    * */
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        initializeConstants();

        initializeSocketReceiver();
        initializeSocket();
        initializeDataReceiver();
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
        itemBidResources = new BiddingResources();
        onPauseWhenSocketAlreadyConnected = false;
        onPauseActivity = false;
        biddingAlreadyDone = false;
        activityContext = getActivity();
        fragmentManager = getChildFragmentManager();
    }
    private void initializeFragments()
    {
        //menuPagerFragment = new AuctioneerMenuPagerFragment();
        menuPagerNotStartedFragment = new AuctioneerMenuPagerNotStartedFragment();
        menuPagerStartedFragment = new AuctioneerMenuPagerStartedFragment();
        menuPagerFinishedFragment = new AuctioneerMenuPagerFinishedFragment();

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
        Log.v("socket", "SOCKET CONNECT");
        socketBinder.connect();
        socketBinder.on("connected", biddingSocket.onConnected);
        socketBinder.on("bidsuccess", biddingSocket.onSubmitBidSuccess);
        socketBinder.on("bidfailed", biddingSocket.onSubmitBidFailed);
        socketBinder.on("winnerchosen", biddingSocket.onWinnerSelected);
        socketBinder.on("cancelsuccess", biddingSocket.onBidCancelled);
     }
    private void disconnectSocket()
    {
        socketBinder.disconnect();
        socketBinder.off("connected", biddingSocket.onConnected);
        socketBinder.off("bidsuccess", biddingSocket.onSubmitBidSuccess);
        socketBinder.off("bidfailed", biddingSocket.onSubmitBidFailed);
        socketBinder.off("winnerchosen", biddingSocket.onWinnerSelected);
        socketBinder.off("cancelsuccess", biddingSocket.onBidCancelled);
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
                if (!isChangeTawaranFragment)
                {
                    swipeRefreshLayout.setRefreshing(true);
                    getDetailItem(itemID);
                }
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
                            setDataToChildFragments(detailItem, itemBidResources);
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
                            biddingAlreadyDone = true;
                            swipeRefreshLayout.setRefreshing(false);
                            if (socketBinder.connected())
                            {
                                //socketBinder.emit("leave-room", itemID);
                                biddingSocket.IS_JOINED_STATUS = false;
                                disconnectSocket();
                            }

                            setDataToChildFragments(detailItem, itemBidResources);
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
                            if (!socketBinder.connected() && biddingSocket != null)
                            {
                                connectSocket();
                            }
                        }
                        else if (detailItem.getItembidstatus() == -1)
                        {
                            biddingAlreadyDone = true;
                        }
                        swipeRefreshLayout.setRefreshing(false);
                        setDataToChildFragments(detailItem, itemBidResources);
                        setChildFragments(detailItem.getItembidstatus());
                    }
                    ((DetailBarangActivity) getActivity()).changeActionBarTitle(detailItem.getNamabarang());
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
                    if (activityContext != null) setAlertDialogBidStarted();
                }
                if (output.toString().equals("finish"))
                {
                    biddingInformation = "finish";
                    if (activityContext != null) setAlertDialogBidFinished();
                }
            }
        };
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
        onBidSuccessReceived = new SocketReceiver() {
            @Override
            public void socketReceived(Object status, Object response) {
                JSONObject socketResponse = (JSONObject) response;
                try {
                    itemBidResources.setIdBidder(socketResponse.getString("bidder_id_return"));
                    itemBidResources.setNamaBidder(socketResponse.getString("bidder_name_return"));
                    itemBidResources.setHargaBid(socketResponse.getString("bid_price_return"));

                    menuPagerStartedFragment.setBidderInformation(itemBidResources);
                    menuPagerStartedFragment.setStatisticInformation(detailItem.getHargaawal(), detailItem.getHargatarget(), itemBidResources.getHargaBid());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };
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
                biddingInformation = "finish";
                waktuBidStartedFragment.stopCountDownTimer();
                setAlertDialogBidCancelled();
            }
        };
    }
    private void setSocketOnWinnerSelectedReceived()
    {
        onWinnerSelected = new SocketReceiver() {
            @Override
            public void socketReceived(Object status, Object response) {
                biddingInformation = "finish";
                waktuBidStartedFragment.stopCountDownTimer();
                setAlertDialogWinnerSelected();
            }
        };
    }
    private void setAuctioneerResponseReceived()
    {
        //untuk menginisiasi interface memilih pemenang maupun membatalkan lelang
        auctioneerResponseReceiver = new AuctioneerResponseReceiver() {
            @Override
            public void responseCancelReceived(boolean status) {
                socketBinder.emit("cancelauction", itemID);
            }
            @Override
            public void responseDaftarTawaranReceived()
            {
                //showCustomDialog();
                showFragmentTawaran();
            }
            @Override
            public void responseWinnerChosenReceived(boolean status, String idBid) {
                JSONObject winnerChosenObject = new JSONObject();
                try {
                    winnerChosenObject.put("item_id_query", itemID);
                    winnerChosenObject.put("bid_id_query", idBid);
                    Log.v("INFO SEND", idBid);
                    socketBinder.emit("winnerselected", winnerChosenObject.toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
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
        if (onPauseWhenSocketAlreadyConnected && !biddingInformation.equals("finish"))
        {
            getDetailItem(itemID);
            onPauseWhenSocketAlreadyConnected = false;
            if (!socketBinder.connected() && biddingSocket != null)
            {
                connectSocket();
            }
        }
        else if (onPauseActivity)
        {
            Log.v("Masuk onPauseActivity", "MASUK SINIIIIIIIIII");
            if (!biddingAlreadyDone) getDetailItem(itemID);
        }

        if (biddingSocket != null && !biddingSocket.IS_JOINED_STATUS && !biddingInformation.equals("finish"))
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
        //nyoba
        if(isChangeTawaranFragment && detailItem.getItembidstatus() == 1)
        {
            waktuBidStartedFragment.stopCountDownTimer();
        }

        if (biddingSocket != null && biddingSocket.IS_JOINED_STATUS)
        {
            if (socketBinder.connected())
            {
                socketBinder.emit("leave-room", itemID);
                Log.v("Leaving room pause", "Leaving room pause");
                onPauseWhenSocketAlreadyConnected = true;
                biddingSocket.IS_JOINED_STATUS = false;
            }
        }
        onPauseActivity = true;
    }
    private void onDestroyConfiguration()
    {
        if (biddingSocket != null)
        {
            if (socketBinder.connected())
            {
                Log.v("On destroy", "On destroy executed");
                disconnectSocket();
                biddingSocket.IS_JOINED_STATUS = false;
            }
        }
    }
    private void setDataToChildFragments(DetailItemResources detailItem, BiddingResources itemBidResources)
    {
        if (detailItem.getItembidstatus() == 0)
        {
            Long startTime_ms = detailItem.getTanggaljammulai_ms();
            menuPagerNotStartedFragment.setUpDetailItemAndBiddingResources(detailItem, itemBidResources);
            menuPagerNotStartedFragment.setFragmentValue(startTime_ms, serverDateTimeMillisecond, timeTriggerReceived);
            menuPagerNotStartedFragment.setStatisticInformation(detailItem.getHargaawal(), detailItem.getHargatarget(), itemBidResources.getHargaBid());
            waktuBidNotStartedFragment.setDetailItem(detailItem);
        }
        else if (detailItem.getItembidstatus() == 1)
        {
            menuPagerStartedFragment.setUpDetailItemAndBiddingResources(detailItem, itemBidResources);
            menuPagerStartedFragment.setFragmentValue(itemBidResources, auctioneerResponseReceiver);
            menuPagerStartedFragment.setStatisticInformation(detailItem.getHargaawal(), detailItem.getHargatarget(), itemBidResources.getHargaBid());
            waktuBidStartedFragment.setDetailItem(detailItem);
            waktuBidStartedFragment.setTriggerFinished(timeTriggerReceived);
            waktuBidStartedFragment.setServerTime(serverDateTimeMillisecond);

            //nyoba
            if (isChangeTawaranFragment)
            {
                waktuBidStartedFragment.cancelAndStartNewTimerWhenFragmentPaused();
                isChangeTawaranFragment = false;
            }
        }
        else if (detailItem.getItembidstatus() == -1)
        {
            menuPagerFinishedFragment.setUpDetailItemAndBiddingResources(detailItem, itemBidResources);
            menuPagerFinishedFragment.setFragmentValue(itemBidResources);
            menuPagerFinishedFragment.setStatisticInformation(detailItem.getHargaawal(), detailItem.getHargatarget(), itemBidResources.getHargaBid());
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
            setUpUsedFragments(headerFragment, imageFragment, waktuBidNotStartedFragment, menuPagerNotStartedFragment, descriptionFragment, commentaryFragment);
        }
        else if (status == 1)
        {
            setUpUsedFragments(headerFragment, imageFragment, waktuBidStartedFragment, menuPagerStartedFragment, descriptionFragment, commentaryFragment);
        }
        else if (status == -1)
        {
            setUpUsedFragments(headerFragment, imageFragment, waktuBidFinishedFragment, menuPagerFinishedFragment, descriptionFragment, commentaryFragment);
        }
        setStatusBiddingOnHeader(status);
    }
    private void setUpUsedFragments(Fragment header, Fragment gambar, Fragment waktubid, Fragment pager, Fragment description, Fragment comment)
    {
        fragmentManager.beginTransaction()
                .replace(R.id.fragment_detail_barang_header_fragment, header)
                .replace(R.id.fragment_detail_barang_gambar_fragment, gambar)
                .replace(R.id.fragment_detail_barang_waktubid_fragment, waktubid)
                .replace(R.id.fragment_detail_barang_bidding_fragment, pager)
                .replace(R.id.fragment_detail_barang_deskripsi_fragment, description)
                .replace(R.id.fragment_detail_barang_komentar_fragment, comment)
                .commit();
    }
    private void setStatusBiddingOnHeader(int status)
    {
        headerFragment.setStatusBiddingItem(status);
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
                            //detailItem.setNamabidder(itemDataObject.getString("bidder_name"));
                            //detailItem.setHargabid(itemDataObject.getString("item_bid_price"));

                            itemBidResources.setIdBid(itemDataObject.getString("bid_id_q"));
                            itemBidResources.setNamaBidder(itemDataObject.getString("bidder_name"));
                            itemBidResources.setHargaBid(itemDataObject.getString("item_bid_price"));

                            JSONArray detailUrlGambarItemArray = itemDataObject.getJSONArray("url");
                            JSONArray biddingPeringkatArray = itemDataObject.getJSONArray("peringkat");

                            //clear list when load data from server
                            /*biddingPeringkatList.clear();
                            for (int j=0;j<biddingPeringkatArray.length();j++)
                            {
                                BiddingResources bidPeringkat = new BiddingResources();
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

    /*
    * Alert dialog method start here
    * */
    private void setAlertDialogBidCancelled()
    {
        AlertDialog.Builder bidCancelledAlertDialogBuilder = new AlertDialog.Builder(activityContext);
        bidCancelledAlertDialogBuilder.setTitle(R.string.DETAILFRAGMENT_BIDCANCELLED_ALERTDIALOGTITLE)
                .setMessage(R.string.DETAILFRAGMENT_BIDCANCELLED_ALERTDIALOGMSG)
                .setPositiveButton(R.string.DETAILFRAGMENT_BIDCANCELLED_ALERTDIALOGBUTTON, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        getDetailItem(itemID);
                    }
                });
        AlertDialog bidCancelledDialog = bidCancelledAlertDialogBuilder.create();
        bidCancelledDialog.show();
    }
    private void setAlertDialogBidStarted()
    {
        AlertDialog.Builder bidStartedAlertDialogBuilder = new AlertDialog.Builder(activityContext);
        bidStartedAlertDialogBuilder.setTitle(R.string.DETAILFRAGMENT_BIDSTARTED_ALERTDIALOGTITLE)
                .setMessage(R.string.DETAILFRAGMENT_BIDSTARTED_ALERTDIALOGMSG)
                .setPositiveButton(R.string.DETAILFRAGMENT_BIDSTARTED_ALERTDIALOGBUTTON, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        getDetailItem(itemID);
                    }
                });
        AlertDialog bidStartedDialog = bidStartedAlertDialogBuilder.create();
        if(!((Activity) activityContext).isFinishing() && !onPauseActivity)
        {
            //fixing crash saat menampilkan dialog setelah menjalankan countdowntimer
            bidStartedDialog.show();
        }
        else
        {
            //jika activity sudah dihancurkan, maka langsung refresh activity
            if (!onPauseActivity) refreshActivity();
        }
    }
    private void setAlertDialogBidFinished()
    {
        AlertDialog.Builder bidFinishedAlertDialogBuilder = new AlertDialog.Builder(activityContext);
        bidFinishedAlertDialogBuilder.setTitle(R.string.DETAILFRAGMENT_BIDFINISHED_ALERTDIALOGTITLE)
                .setMessage(R.string.DETAILFRAGMENT_BIDFINISHED_ALERTDIALOGMSG)
                .setPositiveButton(R.string.DETAILFRAGMENT_BIDFINISHED_ALERTDIALOGBUTTON, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        getDetailItem(itemID);
                    }
                });
        AlertDialog bidFinishedDialog = bidFinishedAlertDialogBuilder.create();
        if(!((Activity) activityContext).isFinishing() && !onPauseActivity)
        {
            //fixing crash saat menampilkan dialog setelah menjalankan countdowntimer
            bidFinishedDialog.show();
        }
        else
        {
            //jika activity sudah dihancurkan, maka langsung refresh activity
            if(!onPauseActivity) refreshActivity();
        }
    }
    private void setAlertDialogWinnerSelected()
    {
        AlertDialog.Builder winnerSelectedAlertDialogBuilder = new AlertDialog.Builder(activityContext);
        winnerSelectedAlertDialogBuilder.setTitle(R.string.DETAILFRAGMENT_WINNERSELECTED_ALERTDIALOGTITLE)
                .setMessage(R.string.DETAILFRAGMENT_WINNERSELECTED_ALERTDIALOGMSG)
                .setPositiveButton(R.string.DETAILFRAGMENT_WINNERSELECTED_ALERTDIALOGBUTTON, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        getDetailItem(itemID);
                    }
                });
        AlertDialog winnerSelectedDialog = winnerSelectedAlertDialogBuilder.create();
        winnerSelectedDialog.show();
    }
    /*
    * Alert dialog method end here
    * */
    private void refreshActivity()
    {
        getActivity().finish();
        getActivity().startActivity(getActivity().getIntent());
    }

    private void showCustomDialog()
    {
        FragmentManager fm = getFragmentManager();
        ListBidderDialogFragment listBidderDialogFragment = ListBidderDialogFragment.newInstance("Lalala");
        listBidderDialogFragment.show(fm, "fragment_lalala");
    }

    private void showFragmentTawaran()
    {
        isChangeTawaranFragment = true;
        DaftarTawaranFragment tawaranFragment = new DaftarTawaranFragment();
        tawaranFragment.setSocket(socketBinder);
        ((DetailBarangActivity) getActivity()).addFragmentStack(tawaranFragment, "Daftar Tawaran");
        /*getFragmentManager().beginTransaction()
                .replace(R.id.fragment_detail_barang_layout, tawaranFragment)
                .addToBackStack(null)
                .commit();*/
    }
}
