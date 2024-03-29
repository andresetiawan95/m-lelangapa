package com.lelangapa.android.fragments.detail.detailitemuser;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.lelangapa.android.R;
import com.lelangapa.android.activities.detail.DetailBarangActivity;
import com.lelangapa.android.activities.profile.chat.UserChatRoomActivity;
import com.lelangapa.android.apicalls.detail.DetailItemAPI;
import com.lelangapa.android.apicalls.singleton.RequestController;
import com.lelangapa.android.apicalls.socket.BiddingSocket;
import com.lelangapa.android.fragments.detail.detailitemfavorite.FavoriteStatus;
import com.lelangapa.android.fragments.detail.detailitemfavorite.FavoriteToggler;
import com.lelangapa.android.fragments.detail.detailitemfavorite.UnfavoriteToggler;
import com.lelangapa.android.interfaces.BidReceiver;
import com.lelangapa.android.interfaces.DataReceiver;
import com.lelangapa.android.interfaces.SocketReceiver;
import com.lelangapa.android.interfaces.pageindicator.ScrollEnabler;
import com.lelangapa.android.preferences.SessionManager;
import com.lelangapa.android.resources.BiddingResources;
import com.lelangapa.android.resources.DetailItemResources;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import io.socket.client.Socket;

/**
 * Created by Andre on 12/24/2016.
 */

public class DetailFragment extends Fragment {
    private DataReceiver detailReceived;
    private DataReceiver triggerReceived;
    private DataReceiver favoriteStatusReceived;
    private BidReceiver inputBidReceiver;
    private DetailHeaderFragment detailHeaderFragment;
    private DetailImageFragment detailImageFragment;
    private DetailBiddingNotStartedFragment detailBiddingNotStartedFragment;
    private DetailBiddingFragment detailBiddingFragment;
    private DetailWaktuBidNotStartedFragment detailWaktuBidNotStartedFragment;
    private DetailWaktuBidStartedFragment detailWaktuBidStartedFragment;
    private DetailWaktuBidFinishedFragment detailWaktuBidFinishedFragment;
    private DetailDescriptionFragment detailDescriptionFragment;
    private DetailCommentaryFragment detailCommentaryFragment;
    private DetailAuctioneerFragment detailAuctioneerFragment;
    private DetailBiddingFinishedWithBidderFragment detailBiddingFinishedWithBidderFragment;
    private DetailBiddingFinishedNoBidFragment detailBiddingFinishedNoBidFragment;
    private FavoriteStatus detailFavoriteStatus;
    private FavoriteToggler favoriteMenuToggler;
    private UnfavoriteToggler unfavoriteMenuToggler;

    private DetailItemResources detailItem;
    private AlertDialog.Builder bidFailedAlertDialogBuilder;
    private Context activityContext;
    private SessionManager sessionManager;
    private HashMap<String, String> session;

    private SwipeRefreshLayout swipeRefreshLayout;
    private ScrollEnabler scrollEnabler;
    private Long serverDateTimeMillisecond;
    private String itemID, biddingInformation, favoriteID;
    private BiddingSocket biddingSocket;
    private Socket socketBinder;
    private SocketReceiver socketConnected, socketDisconnected, socketBidSuccessReceiver, socketBidFailedReceiver,
            socketAuctionCancelledReceiver, socketBidCancelledReceiver, socketWinnerSelectedReceiver;

    private ArrayList<BiddingResources> biddingPeringkatList;
    private ArrayList<String> listImageURL;

    private Bundle bundleExtras;
    private Intent intentChatActivity;

    private boolean onPauseWhenSocketAlreadyConnected = false, onPauseActivity = false, biddingAlreadyDone = false, isDoneLoaded=false;

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

        intentChatActivity = new Intent(getActivity(), UserChatRoomActivity.class);

        detailItem = new DetailItemResources();
        sessionManager = new SessionManager(getActivity());
        session = sessionManager.getSession();
        itemID = getActivity().getIntent().getExtras().getString("items_id");
        bidFailedAlertDialogBuilder = new AlertDialog.Builder(getActivity());
        setInputBidReceiver();
        setSocketReceiver();
        setInitialSocketBinding();
        setDetailReceived();
        setTriggerReceived();
        setDataReceiverFavoriteStatus();
        biddingInformation = "opened";
        biddingPeringkatList = new ArrayList<>();
        listImageURL = new ArrayList<>();
        detailHeaderFragment = new DetailHeaderFragment();
        detailImageFragment = new DetailImageFragment();
        detailBiddingFragment = new DetailBiddingFragment();
        detailBiddingNotStartedFragment = new DetailBiddingNotStartedFragment();
        detailWaktuBidNotStartedFragment = new DetailWaktuBidNotStartedFragment();
        detailWaktuBidStartedFragment = new DetailWaktuBidStartedFragment();
        detailWaktuBidFinishedFragment = new DetailWaktuBidFinishedFragment();
        detailDescriptionFragment = new DetailDescriptionFragment();
        detailCommentaryFragment = new DetailCommentaryFragment();
        detailAuctioneerFragment = new DetailAuctioneerFragment();
        detailBiddingFinishedWithBidderFragment = new DetailBiddingFinishedWithBidderFragment();
        detailBiddingFinishedNoBidFragment = new DetailBiddingFinishedNoBidFragment();
    }
    @Override
    public View onCreateView (final LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_detail_barang_layout, container, false);
        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.fragment_detail_barang_swipe_refreshLayout);
        setUpSwipeRefreshLayout();
        setScrollEnabler();
        return view;
    }
    @Override
    public void onActivityCreated(Bundle savedInstanceState)
    {
        super.onActivityCreated(savedInstanceState);
        activityContext = getActivity();
        favoriteMenuToggler = new FavoriteToggler(
                getActivity(),
                session.get(sessionManager.getKEY_ID()),
                itemID,
                favoriteStatusReceived);
        unfavoriteMenuToggler = new UnfavoriteToggler(
                getActivity(),
                session.get(sessionManager.getKEY_ID()),
                itemID,
                favoriteStatusReceived);
    }
    @Override
    public void onResume()
    {
        super.onResume();
        if (onPauseWhenSocketAlreadyConnected && !biddingInformation.equals("finish"))
        {
            /*
            * Logic :
            * branch ini akan dijalankan ketika pada saat bidding sedang LIVE dan socket tersambung namun user mem-pause aplikasi,
            * lalu user kembali lagi ke aplikasi dan bidding masih sedang berjalan (sedang LIVE).
            *
            * namun, ketika user kembali saat bidding sudah selesai (DONE), maka branch ini tidak dijalankan
            * */
            getDetailItem(itemID);
            onPauseWhenSocketAlreadyConnected = false;
            if (!socketBinder.connected() && biddingSocket != null)
            {
                Log.v("SOCKET SEKARANG", "false");
                socketBinder.connect();
                Log.v("CONNECT onRESUME", "connected");
                //just receive success bid
                socketBinder.on("connected", biddingSocket.onConnected);
                socketBinder.on("bidsuccess", biddingSocket.onSubmitBidSuccess);
                socketBinder.on("bidfailed", biddingSocket.onSubmitBidFailed);
                socketBinder.on("winnerchosen", biddingSocket.onWinnerSelected);
                socketBinder.on("cancelsuccess", biddingSocket.onAuctionCancelled);
                socketBinder.on("cancelbidsuccess", biddingSocket.onBidCancelled);
            }
        }
        else if (onPauseActivity)
        {
            /*
            * Logic :
            * branch ini akan dijalankan ketika bidding belum mulai (SOON) dan user mem-pause aplikasi
            * ketika user kembali dan bidding masih belum mulai (SOON) maupun sudah dimulai (LIVE), maupun sudah selesai (DONE).
            *
            * Namun, ketika sejak awal load status bidding sudah selesai (DONE), walaupun user keluar dan masuk lagi, maka
            * aplikasi tidak akan request data baru dari API untuk efisiensi.
            * */
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
        onPauseActivity = false;
    }
    @Override
    public void onPause()
    {
        super.onPause();
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
    @Override
    public void onDestroy()
    {
        super.onDestroy();
        if (socketBinder.connected())
        {
            disconnectSocket();
            biddingSocket.IS_JOINED_STATUS = false;
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater)
    {
        inflater.inflate(R.menu.activity_detail_favorite, menu);
        inflater.inflate(R.menu.activity_detail_unfavorite, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu)
    {
        MenuItem favoriteMenu = menu.findItem(R.id.action_favorite);
        MenuItem chatFavoriteMenu = menu.findItem(R.id.action_chat_favorite);
        MenuItem unfavoriteMenu = menu.findItem(R.id.action_unfavorite);
        MenuItem chatUnfavoriteMenu = menu.findItem(R.id.action_chat_unfavorite);
        if (sessionManager.isLoggedIn() && isDoneLoaded)
        {
            if (favoriteID!=null && favoriteID.equals("0"))
            {
                favoriteMenu.setVisible(true);
                chatFavoriteMenu.setVisible(true);
                unfavoriteMenu.setVisible(false);
                chatUnfavoriteMenu.setVisible(false);
            }
            else if (favoriteID!=null && !favoriteID.equals("0"))
            {
                favoriteMenu.setVisible(false);
                chatFavoriteMenu.setVisible(false);
                unfavoriteMenu.setVisible(true);
                chatUnfavoriteMenu.setVisible(true);
            }
        }
        else {
            favoriteMenu.setVisible(false);
            chatFavoriteMenu.setVisible(false);
            unfavoriteMenu.setVisible(false);
            chatUnfavoriteMenu.setVisible(false);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem)
    {
        if (menuItem.getItemId() == R.id.action_favorite)
        {
            favoriteMenuToggler.showAlertDialog();
            //Toast.makeText(getActivity(), "Favorite", Toast.LENGTH_SHORT).show();
        }
        else if (menuItem.getItemId() == R.id.action_unfavorite)
        {
            unfavoriteMenuToggler.showAlertDialog();
            //Toast.makeText(getActivity(), "Unfavorite", Toast.LENGTH_SHORT).show();
        }

        if (menuItem.getItemId() == R.id.action_chat_favorite ||
                menuItem.getItemId() == R.id.action_chat_unfavorite)
        {
            intentToChatActivity();
        }

        return super.onOptionsItemSelected(menuItem);
    }

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
                            Log.v("Selesai", "Request Selesai");
                            swipeRefreshLayout.setRefreshing(false);
                            connectSocket();
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
                            biddingAlreadyDone = true;
                            swipeRefreshLayout.setRefreshing(false);

                            if (socketBinder.connected())
                            {
                                //di disable karena ketika disconnect, socket otomatis keluar room
                                //socketBinder.emit("leave-room", itemID);
                                biddingSocket.IS_JOINED_STATUS = false;
                                disconnectSocket();
                            }

                            setDataToChildFragments(detailItem);
                            //untuk menampilkan fragment submit bid
                            setChildFragments();
                        }
                        else
                        {
                            getDetailItem(itemID);
                        }
                    }
                    else if (biddingInformation.equals("opened"))
                    {
                        //Log.v("MASUK OPENED", "MASUK OPENED");
                        if (detailItem.getItembidstatus() == 1)
                        {
                            connectSocket();
                        }
                        else if (detailItem.getItembidstatus() == -1)
                        {
                            biddingAlreadyDone = true;
                        }
                        swipeRefreshLayout.setRefreshing(false);
                        setDataToChildFragments(detailItem);
                        //untuk menampilkan fragment submit bid
                        setChildFragments();
                    }
                    if (sessionManager.isLoggedIn())
                    {
                        isDoneLoaded = true;
                        loadFavoriteStatus();
                    }
                    ((DetailBarangActivity) getActivity()).changeActionBarTitle(detailItem.getNamabarang());
                }
            }
        };
    }
    private void setTriggerReceived()
    {
        triggerReceived = new DataReceiver() {
            @Override
            public void dataReceived(Object output) {
                if (output.toString().equals("start"))
                {
                    biddingInformation = "start";
                    if (activityContext != null)
                    {
                        //Log.v("Tidak null", "Tidak NULL");
                        setAlertDialogBidStarted();
                    }

                }
                if (output.toString().equals("finish"))
                {
                    biddingInformation = "finish";
                    if (activityContext != null)
                    {
                        //Log.v("Tidak null", "activityContext tidak NULL");
                        setAlertDialogBidFinished();
                    }
                }
            }
        };
    }
    private void setInitialSocketBinding()
    {
        if (biddingSocket == null)
        {
            biddingSocket = new BiddingSocket(getActivity());
            biddingSocket.setSocketConnected(socketConnected);
            biddingSocket.setSocketBidSuccessReceiver(socketBidSuccessReceiver);
            biddingSocket.setSocketBidFailedReceiver(socketBidFailedReceiver);
            biddingSocket.setSocketAuctionCancelled(socketAuctionCancelledReceiver);
            biddingSocket.setSocketBidCancelled(socketBidCancelledReceiver);
            biddingSocket.setSocketWinnerSelected(socketWinnerSelectedReceiver);
            socketBinder = biddingSocket.getSocket();
            /*if (!socketBinder.connected())
            {
                Log.v("SOCKET INITIAL SEKARANG", "false");
                socketBinder.connect();
                //just receive success bid
                socketBinder.on("connected", biddingSocket.onConnected);
                socketBinder.on("bidsuccess", biddingSocket.onSubmitBidSuccess);
                socketBinder.on("bidfailed", biddingSocket.onSubmitBidFailed);
                socketBinder.on("winnerselected", biddingSocket.onWinnerSelected);
                socketBinder.on("cancelauction", biddingSocket.onAuctionCancelled);
            }*/
        }
    }
    private void connectSocket()
    {
        if (!socketBinder.connected())
        {
            Log.v("SOCKET INITIAL SEKARANG", "false");
            socketBinder.connect();
            socketBinder.on("connected", biddingSocket.onConnected);
            socketBinder.on("bidsuccess", biddingSocket.onSubmitBidSuccess);
            socketBinder.on("bidfailed", biddingSocket.onSubmitBidFailed);
            socketBinder.on("winnerselected", biddingSocket.onWinnerSelected);
            socketBinder.on("cancelsuccess", biddingSocket.onAuctionCancelled);
            socketBinder.on("cancelbidsuccess", biddingSocket.onBidCancelled);
        }
    }
    private void disconnectSocket()
    {
        if (socketBinder.connected())
        {
            socketBinder.disconnect();
            socketBinder.off("connected", biddingSocket.onConnected);
            socketBinder.off("bidsuccess", biddingSocket.onSubmitBidSuccess);
            socketBinder.off("bidfailed", biddingSocket.onSubmitBidFailed);
            socketBinder.off("winnerchosen", biddingSocket.onWinnerSelected);
            socketBinder.off("cancelsuccess", biddingSocket.onAuctionCancelled);
            socketBinder.off("cancelbidsuccess", biddingSocket.onBidCancelled);
        }
    }
    private void setSocketReceiver()
    {
        socketConnected = new SocketReceiver() {
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
        socketBidSuccessReceiver = new SocketReceiver() {
            @Override
            public void socketReceived(Object status, Object response) {
                JSONObject socketResponse = (JSONObject) response;
                try {
                    //Log.v("Response JSON", socketResponse.toString());
                    detailItem.setHargabid(socketResponse.getString("bid_price_return"));
                    detailItem.setNamabidder(socketResponse.getString("bidder_name_return"));

                    int indexBidderBeforeUpdate = getIndexOfElementInBiddingPeringkatList(socketResponse.getString("bidder_id_return"));
                    if (indexBidderBeforeUpdate != -1)
                    {
                        //jika pengguna yang melakukan bidding telah melakukan bid sebelumnya
                        //mendapatkan object BiddingPeringkatResource dari user yang sudah melakukan bid sebelumnya
                        BiddingResources newBiddingFromExistUser = biddingPeringkatList.get(indexBidderBeforeUpdate);
                        //menghapus object tersebut dari list
                        biddingPeringkatList.remove(indexBidderBeforeUpdate);
                        //merubah harga pada object
                        newBiddingFromExistUser.setHargaBid(socketResponse.getString("bid_price_return"));
                        //push kembali ke list
                        biddingPeringkatList.add(0, newBiddingFromExistUser);
                    }
                    else
                    {
                        //jika pengguna belum pernah melakukan bid sebelumnya
                        BiddingResources newBiddingFromNewUser = new BiddingResources();
                        newBiddingFromNewUser.setIdBidder(socketResponse.getString("bidder_id_return"));
                        newBiddingFromNewUser.setNamaBidder(socketResponse.getString("bidder_name_return"));
                        newBiddingFromNewUser.setHargaBid(socketResponse.getString("bid_price_return"));
                        biddingPeringkatList.add(0, newBiddingFromNewUser);

                        //jika setelah ditambah size dari list lebih dari tiga, maka object ke-3 akan dihapus.
                        if (biddingPeringkatList.size()>3)
                        {
                            //menghapus object pada index ke-3
                            Log.v("Yang dihapus", biddingPeringkatList.get(3).getNamaBidder());
                            biddingPeringkatList.remove(3);
                        }
                    }
                    /*
                    * =====================================================================================
                    * Jika bidding yang dimasukkan men-trigger terjadinya penambahan waktu sebesar 10 menit
                    * */
                    boolean isExtendTriggerActive = socketResponse.getBoolean("extend_trigger");
                    if (isExtendTriggerActive)
                    {
                        detailItem.setTanggaljamselesai(socketResponse.getString("end_time_item_return"));
                        detailItem.setTanggaljamselesai_ms(socketResponse.getLong("end_time_item_return_milisecond"));
                        serverDateTimeMillisecond = socketResponse.getLong("server_time_in_millisecond");
                        detailWaktuBidStartedFragment.setDetailItemWhenTimeExtended(detailItem);
                        detailWaktuBidStartedFragment.setServerTimeWhenTimeExtended(serverDateTimeMillisecond);
                        detailWaktuBidStartedFragment.cancelAndStartNewTimerWhenTimeExtended();
                    }
                    /*
                    * TESTED, need improvement in UX
                    * ======================================================================================
                    * */
                    detailBiddingFragment.updateBidderInformation(detailItem);
                    detailBiddingFragment.updateBiddingPeringkatList(biddingPeringkatList);

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
        /*
        * Untuk sementara, bid cancelled dan winner selected akan mengeluarkan alert dialog dulu
        * */
        socketAuctionCancelledReceiver = new SocketReceiver() {
            @Override
            public void socketReceived(Object status, Object response) {
                setAlertDialogBidCancelled();
            }
        };
        socketBidCancelledReceiver = new SocketReceiver() {
            @Override
            public void socketReceived(Object status, Object response) {
                getDetailItem(itemID);
            }
        };
        socketWinnerSelectedReceiver = new SocketReceiver() {
            @Override
            public void socketReceived(Object status, Object response) {
                setAlertDialogWinnerSelected();
            }
        };
        /*
        * =========================================================================================
        * */
    }
    private void setDataReceiverFavoriteStatus()
    {
        favoriteStatusReceived = new DataReceiver() {
            @Override
            public void dataReceived(Object output) {
                favoriteID = output.toString();
                unfavoriteMenuToggler.setFavoriteID(favoriteID);
                getActivity().invalidateOptionsMenu();
            }
        };
    }
    private void loadFavoriteStatus()
    {
        detailFavoriteStatus = new FavoriteStatus(session.get(sessionManager.getKEY_ID()), itemID, getActivity());
        detailFavoriteStatus.setFavoriteStatusReceiver(favoriteStatusReceived);
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
                    //if (!socketBinder.connected()) socketBinder.connect();
                    socketBinder.emit("submitbid", sendBidObject.toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };
    }
    private int getIndexOfElementInBiddingPeringkatList(String idBidder)
    {
        int idx = -1;
        for (int x=0;x<biddingPeringkatList.size();x++)
        {
            if (biddingPeringkatList.get(x).getIdBidder().equals(idBidder))
            {
                idx = x;
                break;
            }
        }
        return idx;
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
    private void setScrollEnabler() {
        scrollEnabler = new ScrollEnabler() {
            @Override
            public void isScrollEnabled(boolean enable) {
                if (swipeRefreshLayout != null) {
                    swipeRefreshLayout.setEnabled(enable);
                }
            }
        };
        detailImageFragment.setScrollEnabler(scrollEnabler);
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
            //Log.v("Bid sudah dimulai", "Bid sudah dimulai--setdatatochildfragment");
            detailBiddingFragment.setDetailItemToBiddingFragment(detailItem);
            detailBiddingFragment.setBiddingPeringkatList(biddingPeringkatList);
            detailBiddingFragment.setBidReceiverToBiddingFragment(inputBidReceiver);
            detailWaktuBidStartedFragment.setInitialDetailItem(detailItem);
            detailWaktuBidStartedFragment.setTriggerBiddingDone(triggerReceived);
            detailWaktuBidStartedFragment.setInitialServerTime(serverDateTimeMillisecond);
        }
        else
        {
            //Log.v("Bid sudah selesai", "Bid sudah selesai--setdatatochildfragment");
            detailWaktuBidFinishedFragment.setDetailItem(detailItem);
            detailBiddingFragment.setBiddingPeringkatList(biddingPeringkatList);
            if (!detailItem.getNamabidder().equals("nouser"))
            {
                detailBiddingFinishedWithBidderFragment.setDetailItem(detailItem);
            }
        }
        detailHeaderFragment.setDetailItem(detailItem);
        detailImageFragment.setDetailItem(detailItem);
        detailDescriptionFragment.setDetailItem(detailItem);
        detailAuctioneerFragment.setAuctioneerInfo(detailItem.getIdauctioneer(), detailItem.getNamaauctioneer());
    }
    private void setChildFragments()
    {
        if (detailItem.getItembidstatus() == 0)
        {
            getFragmentManager().beginTransaction()
                    .replace(R.id.fragment_detail_barang_header_fragment, detailHeaderFragment)
                    .replace(R.id.fragment_detail_barang_gambar_fragment, detailImageFragment)
                    .replace(R.id.fragment_detail_barang_bidding_fragment, detailBiddingNotStartedFragment)
                    .replace(R.id.fragment_detail_barang_waktubid_fragment, detailWaktuBidNotStartedFragment)
                    .replace(R.id.fragment_detail_barang_deskripsi_fragment, detailDescriptionFragment)
                    .replace(R.id.fragment_detail_barang_komentar_fragment, detailCommentaryFragment)
                    .replace(R.id.fragment_detail_barang_auctioneer_fragment, detailAuctioneerFragment)
                    .commit();
        }
        else if (detailItem.getItembidstatus() == 1)
        {
            Log.v("Bid sudah dimulai", "Bid sudah dimulai--setchildfragment");
            getFragmentManager().beginTransaction()
                    .replace(R.id.fragment_detail_barang_header_fragment, detailHeaderFragment)
                    .replace(R.id.fragment_detail_barang_gambar_fragment, detailImageFragment)
                    .replace(R.id.fragment_detail_barang_bidding_fragment, detailBiddingFragment)
                    .replace(R.id.fragment_detail_barang_waktubid_fragment, detailWaktuBidStartedFragment)
                    .replace(R.id.fragment_detail_barang_deskripsi_fragment, detailDescriptionFragment)
                    .replace(R.id.fragment_detail_barang_komentar_fragment, detailCommentaryFragment)
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
                        .replace(R.id.fragment_detail_barang_gambar_fragment, detailImageFragment)
                        .replace(R.id.fragment_detail_barang_bidding_fragment, detailBiddingFinishedWithBidderFragment)
                        .replace(R.id.fragment_detail_barang_waktubid_fragment, detailWaktuBidFinishedFragment)
                        .replace(R.id.fragment_detail_barang_deskripsi_fragment, detailDescriptionFragment)
                        .replace(R.id.fragment_detail_barang_komentar_fragment, detailCommentaryFragment)
                        .replace(R.id.fragment_detail_barang_auctioneer_fragment, detailAuctioneerFragment)
                        .commit();
            }
            else
            {
                //jika nama bidder adalah "nouser"
                getFragmentManager().beginTransaction()
                        .replace(R.id.fragment_detail_barang_header_fragment, detailHeaderFragment)
                        .replace(R.id.fragment_detail_barang_gambar_fragment, detailImageFragment)
                        .replace(R.id.fragment_detail_barang_bidding_fragment, detailBiddingFinishedNoBidFragment)
                        .replace(R.id.fragment_detail_barang_waktubid_fragment, detailWaktuBidFinishedFragment)
                        .replace(R.id.fragment_detail_barang_deskripsi_fragment, detailDescriptionFragment)
                        .replace(R.id.fragment_detail_barang_komentar_fragment, detailCommentaryFragment)
                        .replace(R.id.fragment_detail_barang_auctioneer_fragment, detailAuctioneerFragment)
                        .commit();
            }
        }
        detailHeaderFragment.setStatusBiddingItem(detailItem.getItembidstatus());
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
                            detailItem.setIdauctioneer(itemDataObject.getString("user_id"));
                            detailItem.setNamaauctioneer(itemDataObject.getString("user_name"));
                            detailItem.setNamabidder(itemDataObject.getString("bidder_name"));
                            detailItem.setHargabid(itemDataObject.getString("item_bid_price"));
                            JSONArray detailUrlGambarItemArray = itemDataObject.getJSONArray("url");
                            JSONArray biddingPeringkatArray = itemDataObject.getJSONArray("peringkat");

                            //clear list when load data from server
                            biddingPeringkatList.clear();
                            listImageURL.clear();
                            for (int j=0;j<biddingPeringkatArray.length();j++)
                            {
                                BiddingResources bidPeringkat = new BiddingResources();
                                JSONObject biddingPeringkatObject = biddingPeringkatArray.getJSONObject(j);
                                bidPeringkat.setIdBidder(biddingPeringkatObject.getString("user_id"));
                                bidPeringkat.setNamaBidder(biddingPeringkatObject.getString("user_name"));
                                bidPeringkat.setHargaBid(biddingPeringkatObject.getString("bid_price"));
                                biddingPeringkatList.add(bidPeringkat);
                            }
                            for (int j=0;j<detailUrlGambarItemArray.length();j++)
                            {
                                JSONObject detailUrlGambarItemObject = detailUrlGambarItemArray.getJSONObject(j);
                                listImageURL.add("http://img-s7.lelangapa.com/" +detailUrlGambarItemObject.getString("url"));
                            }
                            detailItem.setListImageURL(listImageURL);
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
//        detailItemAPI.setShouldCache(false);
//        RequestQueue queue = Volley.newRequestQueue(getActivity());
//        queue.add(detailItemAPI);
    }
    private void setAlertDialogBidFailed()
    {
        bidFailedAlertDialogBuilder.setTitle(R.string.DETAILFRAGMENT_BIDFAILED_ALERTDIALOGTITLE)
                .setMessage(R.string.DETAILFRAGMENT_BIDFAILED_ALERTDIALOGMSG)
                .setPositiveButton(R.string.DETAILFRAGMENT_BIDFAILED_ALERTDIALOGOKBUTTON, null);
        AlertDialog bidFailedAlertDialog = bidFailedAlertDialogBuilder.create();
        bidFailedAlertDialog.show();
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
    private void setAlertDialogBidCancelled()
    {
        AlertDialog.Builder bidCancelledAlertDialogBuilder = new AlertDialog.Builder(activityContext);
        bidCancelledAlertDialogBuilder.setTitle(R.string.DETAILFRAGMENT_AUCTIONCANCELLED_ALERTDIALOGTITLE)
                .setMessage(R.string.DETAILFRAGMENT_AUCTIONCANCELLED_ALERTDIALOGMSG)
                .setPositiveButton(R.string.DETAILFRAGMENT_AUCTIONCANCELLED_ALERTDIALOGBUTTON, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        getDetailItem(itemID);
                    }
                });
        AlertDialog bidCancelledDialog = bidCancelledAlertDialogBuilder.create();
        bidCancelledDialog.show();
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
    private void refreshActivity()
    {
        getActivity().finish();
        getActivity().startActivity(getActivity().getIntent());
    }

    private void intentToChatActivity()
    {
        bundleExtras = new Bundle();
        bundleExtras.putString("your_user_id", session.get(sessionManager.getKEY_ID()));
        bundleExtras.putString("your_friend_user_id", detailItem.getIdauctioneer());
        bundleExtras.putString("your_friend_name", detailItem.getNamaauctioneer());
        intentChatActivity.putExtras(bundleExtras);
        startActivity(intentChatActivity);
    }
}
