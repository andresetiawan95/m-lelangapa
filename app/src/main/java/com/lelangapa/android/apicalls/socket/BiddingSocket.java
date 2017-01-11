package com.lelangapa.android.apicalls.socket;

import android.app.Activity;

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
    private SocketReceiver socketBidSuccessReceiver;
    private SocketReceiver socketBidFailedReceiver;
    public BiddingSocket(Activity activity, SocketReceiver socketReceiverSuccess, SocketReceiver socketReceiverFailed)
    {
        this.socketBidSuccessReceiver = socketReceiverSuccess;
        this.socketBidFailedReceiver = socketReceiverFailed;
        this.activity = activity;
        try {
            mSocket = IO.socket("http://bid.alphav1.lelangapa.com");
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
                    socketBidFailedReceiver.socketReceived("bidfailed", args[0]);
                }
            });
        }
    };
}
