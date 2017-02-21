package com.lelangapa.android.apicalls.socket;

import android.app.Activity;
import android.util.Log;

import com.lelangapa.android.interfaces.SocketReceiver;

import java.net.URISyntaxException;

import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;

/**
 * Created by Andre on 1/3/2017.
 */

public class BiddingSocket {
    private Socket mSocket;
    private Activity activity;
    private SocketReceiver socketConnected;
    private SocketReceiver socketDisconnected;
    private SocketReceiver socketBidSuccessReceiver;
    private SocketReceiver socketBidFailedReceiver;
    private SocketReceiver socketBidCancelled;
    private SocketReceiver socketWinnerSelected;

    public boolean IS_CONNECTED_STATUS = false;
    public boolean IS_JOINED_STATUS = false;
    public BiddingSocket(Activity activity)
    {
        this.activity = activity;
        this.mSocket = createServerSocket();
    }
    public void setSocketConnected(SocketReceiver socketConnected)
    {
        this.socketConnected = socketConnected;
    }
    public void setSocketDisconnected(SocketReceiver socketDisconnected)
    {
        this.socketDisconnected = socketDisconnected;
    }
    public void setSocketBidSuccessReceiver(SocketReceiver socketReceiverSuccess)
    {
        this.socketBidSuccessReceiver = socketReceiverSuccess;
    }
    public void setSocketBidFailedReceiver(SocketReceiver socketReceiverFailed)
    {
        this.socketBidFailedReceiver = socketReceiverFailed;
    }
    public void setSocketBidCancelled(SocketReceiver socketBidCancelled)
    {
        this.socketBidCancelled = socketBidCancelled;
    }
    public void setSocketWinnerSelected(SocketReceiver socketWinnerSelected)
    {
        this.socketWinnerSelected = socketWinnerSelected;
    }
    public Socket getSocket()
    {
        return mSocket;
    }

    private Socket createServerSocket()
    {
        try {
            IO.Options options = new IO.Options();
            options.forceNew = false;
            options.reconnection = true;
            mSocket = IO.socket("http://188.166.179.2:11000", options);
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        return mSocket;
    }
    public Emitter.Listener onConnected = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    IS_JOINED_STATUS = false;
                    socketConnected.socketReceived("connected", args[0]);
                }
            });
        }
    };
    public Emitter.Listener onDisconnected = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    socketDisconnected.socketReceived("disconnected", args[0]);
                }
            });
        }
    };
    public Emitter.Listener onJoinBid = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            //implementedlater
        }
    };
    public Emitter.Listener onTypingBid = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            //implemented later
        }
    };
    public Emitter.Listener onSubmittingBid = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    socketBidSuccessReceiver.socketReceived("submitting", args[0]);
                }
            });
        }
    };
    public Emitter.Listener onSubmitBidSuccess = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (!getSocket().connected())getSocket().connect();
                    Log.v("Bid success", "Bid success");
                    socketBidSuccessReceiver.socketReceived("bidsuccess", args[0]);
                }
            });
        }
    };
    public Emitter.Listener onSubmitBidFailed = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (!getSocket().connected())getSocket().connect();
                    socketBidFailedReceiver.socketReceived("bidfailed", args[0]);
                }
            });
        }
    };
    public Emitter.Listener onBidCancelled = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    socketBidCancelled.socketReceived("cancelauction", args[0]);
                }
            });
        }
    };
    public Emitter.Listener onWinnerSelected = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    socketWinnerSelected.socketReceived("winnerchosen", args[0]);
                }
            });
        }
    };
}
