package com.lelangkita.android.apicalls.socket;

import com.lelangkita.android.interfaces.DataReceiver;

import java.net.URISyntaxException;

import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;

/**
 * Created by Andre on 1/3/2017.
 */

public class BiddingSocket {
    private Socket mSocket;
    private DataReceiver dataReceivedFromSocket;
    public BiddingSocket()
    {
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
        public void call(Object... args) {

        }
    };
    public Emitter.Listener onSubmitBidSuccess = new Emitter.Listener() {
        @Override
        public void call(Object... args) {

        }
    };
    public Emitter.Listener onSubmitBidFailed = new Emitter.Listener() {
        @Override
        public void call(Object... args) {

        }
    };
}
