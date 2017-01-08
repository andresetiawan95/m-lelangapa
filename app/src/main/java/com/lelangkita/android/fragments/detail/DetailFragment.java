package com.lelangkita.android.fragments.detail;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.lelangkita.android.R;
import com.lelangkita.android.apicalls.detail.DetailItemAPI;
import com.lelangkita.android.apicalls.socket.BiddingSocket;
import com.lelangkita.android.interfaces.BidReceiver;
import com.lelangkita.android.interfaces.DataReceiver;
import com.lelangkita.android.interfaces.InputReceiver;
import com.lelangkita.android.interfaces.SocketReceiver;
import com.lelangkita.android.preferences.SessionManager;
import com.lelangkita.android.resources.DetailItemResources;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import io.socket.client.Socket;
import io.socket.emitter.Emitter;

/**
 * Created by Andre on 12/24/2016.
 */

public class DetailFragment extends Fragment {
    private DataReceiver detailReceived;
    private DataReceiver triggerReceived;
    private BidReceiver inputBidReceiver;
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
    private DetailBiddingFinishedWithWinnerFragment detailBiddingFinishedWithWinnerFragment;
    private DetailBiddingFinishedNoBidFragment detailBiddingFinishedNoBidFragment;
    private DetailItemResources detailItem;
    private AlertDialog.Builder bidFailedAlertDialogBuilder;
    private SessionManager sessionManager;
    private HashMap<String, String> session;

    private SwipeRefreshLayout swipeRefreshLayout;
    private Long serverDateTimeMillisecond;
    private String itemID, biddingInformation;
    private BiddingSocket biddingSocket;
    private Socket socketBinder;
    private SocketReceiver socketBidSuccessReceiver, socketBidFailedReceiver, socketBidSubmittingReceiver;
    public DetailFragment(){}
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        detailItem = new DetailItemResources();
        sessionManager = new SessionManager(getActivity());
        session = sessionManager.getSession();
        itemID = getActivity().getIntent().getStringExtra("items_id");
        bidFailedAlertDialogBuilder = new AlertDialog.Builder(getActivity());
        setInputBidReceiver();
        setSocketReceiver();
        setInitialSocketBinding();
        biddingInformation = "opened";
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
        detailBiddingFinishedWithWinnerFragment = new DetailBiddingFinishedWithWinnerFragment();
        detailBiddingFinishedNoBidFragment = new DetailBiddingFinishedNoBidFragment();
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
        //free socket
        socketBinder.disconnect();
        socketBinder.off("bidsuccess", biddingSocket.onSubmitBidSuccess);
        socketBinder.off("bidfailed", biddingSocket.onSubmitBidFailed);
    }
    @Override
    public void onResume()
    {
        super.onResume();
        socketBinder.emit("join-room", itemID);
        Log.v("Joining room", "Joining room now");
    }
    @Override
    public void onPause()
    {
        super.onPause();
        socketBinder.emit("leave-room", itemID);
        Log.v("Leaving room", "Leaving room now");
    }
    private void setInitialSocketBinding()
    {
        biddingSocket = new BiddingSocket(getActivity(), socketBidSuccessReceiver, socketBidFailedReceiver);
        socketBinder = biddingSocket.getSocket();
        socketBinder.connect();
        //just receive success bid
        socketBinder.on("bidsuccess", biddingSocket.onSubmitBidSuccess);
        socketBinder.on("bidfailed", biddingSocket.onSubmitBidFailed);
    }
    private void setSocketReceiver()
    {
        socketBidSuccessReceiver = new SocketReceiver() {
            @Override
            public void socketReceived(Object status, Object response) {
                JSONObject socketResponse = (JSONObject) response;
                try {
                    detailItem.setHargabid(socketResponse.getString("bid_price_return"));
                    detailItem.setNamabidder(socketResponse.getString("bidder_name_return"));
                    detailBiddingFragment.updateBidderInformation(detailItem);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };
        socketBidFailedReceiver = new SocketReceiver() {
            @Override
            public void socketReceived(Object status, Object response) {
                setAlertDialogBidFailed();
            }
        };
    }
    private void setInputBidReceiver()
    {
        inputBidReceiver = new BidReceiver() {
            @Override
            public void bidReceived(String price) {
                JSONObject sendBidObject = new JSONObject();
                try {
                    sendBidObject.put("id_bidder", session.get(sessionManager.getKEY_ID()));
                    sendBidObject.put("id_item", itemID);
                    sendBidObject.put("harga_bid", price);

                    //send json to server socket
                    socketBinder.emit("submitbid", sendBidObject.toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };
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
                //akan dieksekusi sesaat ketika fragment DetailFragment aktif
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
            detailBiddingFragment.setDetailItemToBiddingFragment(detailItem);
            detailBiddingFragment.setBidReceiverToBiddingFragment(inputBidReceiver);
            detailWaktuBidStartedFragment.setDetailItem(detailItem);
            detailWaktuBidStartedFragment.setTriggerBiddingDone(triggerReceived);
            detailWaktuBidStartedFragment.setServerTime(serverDateTimeMillisecond);
        }
        else
        {
            Log.v("Bid sudah selesai", "Bid sudah selesai--setdatatochildfragment");
            detailWaktuBidFinishedFragment.setDetailItem(detailItem);
            if (!detailItem.getNamabidder().equals("nouser"))
            {
                detailBiddingFinishedWithWinnerFragment.setDetailItem(detailItem);
            }
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
        else if (detailItem.getItembidstatus() == 1)
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
        else if (detailItem.getItembidstatus() == -1)
        {
            Log.v("Bid sudah selesai", "Bid sudah selesai--setchildfragment");
            if (!detailItem.getNamabidder().equals("nouser"))
            {
                //jika nama bidder bukan "nouser"
                getFragmentManager().beginTransaction()
                        .replace(R.id.fragment_detail_barang_header_fragment, detailHeaderFragment)
                        .replace(R.id.fragment_detail_barang_gambar_fragment, detailGambarFragment)
                        .replace(R.id.fragment_detail_barang_bidding_fragment, detailBiddingFinishedWithWinnerFragment)
                        .replace(R.id.fragment_detail_barang_waktubid_fragment, detailWaktuBidFinishedFragment)
                        .replace(R.id.fragment_detail_barang_deskripsi_fragment, detailDeskripsiFragment)
                        .replace(R.id.fragment_detail_barang_komentar_fragment, detailKomentarFragment)
                        .replace(R.id.fragment_detail_barang_auctioneer_fragment, detailAuctioneerFragment)
                        .commit();
            }
            else
            {
                //jika nama bidder adalah "nouser"
                getFragmentManager().beginTransaction()
                        .replace(R.id.fragment_detail_barang_header_fragment, detailHeaderFragment)
                        .replace(R.id.fragment_detail_barang_gambar_fragment, detailGambarFragment)
                        .replace(R.id.fragment_detail_barang_bidding_fragment, detailBiddingFinishedNoBidFragment)
                        .replace(R.id.fragment_detail_barang_waktubid_fragment, detailWaktuBidFinishedFragment)
                        .replace(R.id.fragment_detail_barang_deskripsi_fragment, detailDeskripsiFragment)
                        .replace(R.id.fragment_detail_barang_komentar_fragment, detailKomentarFragment)
                        .replace(R.id.fragment_detail_barang_auctioneer_fragment, detailAuctioneerFragment)
                        .commit();
            }
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
                            detailItem.setNamabidder(itemDataObject.getString("bidder_name"));
                            detailItem.setHargabid(itemDataObject.getString("item_bid_price"));
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
    private void setAlertDialogBidFailed()
    {
        bidFailedAlertDialogBuilder.setTitle(R.string.DETAILFRAGMENT_BIDFAILED_ALERTDIALOGTITLE)
                .setMessage(R.string.DETAILFRAGMENT_BIDFAILED_ALERTDIALOGMSG)
                .setPositiveButton(R.string.DETAILFRAGMENT_BIDFAILED_ALERTDIALOGOKBUTTON, null);
        AlertDialog bidFailedAlertDialog = bidFailedAlertDialogBuilder.create();
        bidFailedAlertDialog.show();
    }
    /*public Emitter.Listener onSubmitBidSuccess = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Log.v("Socket Received", "YEAY SOCKET RESPONSE IS RECEIVED");

                    JSONObject socketResponse = (JSONObject) args[0];
                    try {
                        detailItem.setHargabid(socketResponse.getString("bid_price_return"));
                        detailItem.setNamabidder(socketResponse.getString("bidder_name_return"));
                        detailBiddingFragment.updateBidderInformation(detailItem);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    //dataReceivedFromSocket.socketReceived("bidsuccess", args[0]);
                }
            });
        }
    };*/
}
