package com.lelangapa.android.fragments.detail.detailitemowner;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.lelangapa.android.R;
import com.lelangapa.android.apicalls.socket.BiddingSocket;
import com.lelangapa.android.interfaces.DataReceiver;
import com.lelangapa.android.interfaces.SocketReceiver;

import io.socket.client.Socket;

/**
 * Created by andre on 25/01/17.
 */

public class DetailItemAuctioneerFragment extends Fragment {
    private String itemID;
    private String biddingInformation;
    private boolean isPaused;

    private BiddingSocket biddingSocket;
    private Socket socketBinder;

    private DataReceiver dataReceived, timeTriggerReceived, auctioneerResponseReceived;
    private SocketReceiver onConnectedReceived, onBidSuccessReceived, onBidFailedReceived, onBidCancelledReceived, onWinnerSelected;

    private AuctioneerMenuPagerFragment menuPagerFragment;
    private HeaderFragment headerFragment;
    private ImageFragment imageFragment;
    private DescriptionFragment descriptionFragment;
    private CommentaryFragment commentaryFragment;
    private WaktuBidNotStartedFragment waktuBidNotStartedFragment;
    private WaktuBidStartedFragment waktuBidStartedFragment;
    private WaktuBidFinishedFragment waktuBidFinishedFragment;

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
        return view;
    }

    @Override
    public void onResume()
    {
        super.onResume();
        if (isPaused)
        {
            //implemented later
        }
    }

    @Override
    public void onPause()
    {
        super.onPause();
        socketBinder.emit("leave-room", itemID);
        isPaused = true;
    }

    @Override
    public void onDestroy()
    {
        super.onDestroy();
        setSocketDisconnection();
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

            setSocketConnection();
        }
    }
    private void initializeDataReceiver()
    {
        setDataReceived();
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
    private void setSocketConnection()
    {
        if (!socketBinder.connected() && biddingSocket != null)
        {
            socketBinder.connect();
            socketBinder.on("connected", biddingSocket.onConnected);
            socketBinder.on("bidsuccess", biddingSocket.onSubmitBidSuccess);
            socketBinder.on("bidfailed", biddingSocket.onSubmitBidFailed);
            socketBinder.on("winnerselected", biddingSocket.onWinnerSelected);
            socketBinder.on("cancelauction", biddingSocket.onBidCancelled);
        }
    }
    private void setSocketDisconnection()
    {
        socketBinder.disconnect();
        socketBinder.off("connected", biddingSocket.onConnected);
        socketBinder.off("bidsuccess", biddingSocket.onSubmitBidSuccess);
        socketBinder.off("bidfailed", biddingSocket.onSubmitBidFailed);
        socketBinder.off("winnerselected", biddingSocket.onWinnerSelected);
        socketBinder.off("cancelauction", biddingSocket.onBidCancelled);
        biddingSocket.IS_JOINED_STATUS = false;
    }

    /*
    * Initialization methods end here
    * ==========================
    * */

    /*
    * Receiver methods start here
    * */
    private void setDataReceived()
    {

    }
    private void setTimeTriggerReceived()
    {

    }
    private void setAuctioneerResponseReceived()
    {

    }
    private void setSocketOnConnectReceived()
    {

    }
    private void setSocketOnBiddingSuccessReceived()
    {

    }
    private void setSocketOnBiddingFailedReceived()
    {

    }
    private void setSocketOnBiddingCancelledReceived()
    {

    }
    private void setSocketOnWinnerSelectedReceived()
    {

    }
    /*
    * Receiver methods end here
    * */

    /*
    * Logic methods starts here
    * */

    /*
    * Logic methods end here
    * */

}
