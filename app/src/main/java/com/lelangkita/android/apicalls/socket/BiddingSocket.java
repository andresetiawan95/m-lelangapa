package com.lelangkita.android.apicalls.socket;

import android.app.Activity;
import android.util.Log;

import com.lelangkita.android.interfaces.DataReceiver;
import com.lelangkita.android.interfaces.SocketReceiver;

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
    private SocketReceiver dataReceivedFromSocket;
    public BiddingSocket(Activity activity, SocketReceiver socketReceiver)
    {
        this.dataReceivedFromSocket = socketReceiver;
        this.activity = activity;
        try {
            mSocket = IO.socket("http://bid.alphav1.lelangkita.com");
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }
    public Socket getSocket()
    {
        return mSocket;
    }
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
                    dataReceivedFromSocket.socketReceived("submitting", args[0]);
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
                    Log.v("Socket Received", "YEAY SOCKET RESPONSE IS RECEIVED");
                    dataReceivedFromSocket.socketReceived("bidsuccess", args[0]);
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
                    dataReceivedFromSocket.socketReceived("bidfailed", args[0]);
                }
            });
        }
    };
}
