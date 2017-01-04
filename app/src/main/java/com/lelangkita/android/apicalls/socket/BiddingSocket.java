package com.lelangkita.android.apicalls.socket;

import java.net.URISyntaxException;

import io.socket.client.IO;
import io.socket.client.Socket;

/**
 * Created by Andre on 1/3/2017.
 */

public class BiddingSocket {
    private Socket mSocket;
    public BiddingSocket()
    {
        try {
            mSocket = IO.socket("http://bid.alphav1.lelangkita.com");
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }
    public Socket getmSocket()
    {
        return mSocket;
    }
}
