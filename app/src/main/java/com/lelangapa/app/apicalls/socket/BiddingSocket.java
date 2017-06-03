package com.lelangapa.app.apicalls.socket;

import android.app.Activity;
import android.util.Log;

import com.lelangapa.app.interfaces.SocketReceiver;
import com.lelangapa.app.preferences.SessionManager;
import com.lelangapa.app.resources.SocketSSLResources;

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
    private SocketReceiver socketAuctionCancelled;
    private SocketReceiver socketBidCancelled;
    private SocketReceiver socketWinnerSelected;

    private SocketReceiver socketBidSuccessFromDetailTawaran;
    private SocketReceiver socketBidCancelledFromDetailTawaran;
    private SocketReceiver socketWinnerSelectedFromDetailTawaran;

    public boolean IS_CONNECTED_STATUS = false;
    public boolean IS_JOINED_STATUS = false;
    public boolean IS_CHANGE_TO_DETAIL_TAWARAN_FRAGMENT;

    private SocketSSLResources socketSSLResources;
    public BiddingSocket(Activity activity)
    {
        this.activity = activity;
        socketSSLResources = new SocketSSLResources(this.activity);
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

    //yang baru
    public void setSocketBidSuccessFromDetailTawaran(SocketReceiver socketReceiver)
    {
        this.socketBidSuccessFromDetailTawaran = socketReceiver;
    }
    public void setSocketBidCancelledFromDetailTawaran(SocketReceiver socketReceiver)
    {
        this.socketBidCancelledFromDetailTawaran = socketReceiver;
    }
    public void setSocketWinnerSelectedFromDetailTawaran(SocketReceiver socketReceiver)
    {
        this.socketWinnerSelectedFromDetailTawaran = socketReceiver;
    }

    public void setSocketBidFailedReceiver(SocketReceiver socketReceiverFailed)
    {
        this.socketBidFailedReceiver = socketReceiverFailed;
    }
    public void setSocketAuctionCancelled(SocketReceiver socketAuctionCancelled)
    {
        this.socketAuctionCancelled = socketAuctionCancelled;
    }
    public void setSocketBidCancelled(SocketReceiver socketBidCancelled) {
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
            //sepertinya sementara ini masih aman dengan URL.
            //kalau ada error, bisa ganti lagi ke IP address
            //dan uncomment semua yang di comment
            IO.Options options = new IO.Options();
            options.forceNew = false;
            options.reconnection = true;
            //options.sslContext = socketSSLResources.getSSLSocketContext();
            //options.hostnameVerifier = socketSSLResources.getHostnameVerifier();
            options.query = "token=" + SessionManager.getUserTokenStatic();
            //mSocket = IO.socket("https://188.166.179.2:15000", options);
            options.secure = true;
            mSocket = IO.socket("https://bidding.lelangapa.com", options);
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        /*catch (IOException e) {
            e.printStackTrace();
        } catch (CertificateException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (KeyStoreException e) {
            e.printStackTrace();
        } catch (KeyManagementException e) {
            e.printStackTrace();
        }*/
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
                    if (IS_CHANGE_TO_DETAIL_TAWARAN_FRAGMENT)
                        socketBidSuccessFromDetailTawaran.socketReceived("bidsuccess", args[0]);
                    else socketBidSuccessReceiver.socketReceived("bidsuccess", args[0]);
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
    public Emitter.Listener onAuctionCancelled = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    socketAuctionCancelled.socketReceived("cancelauction", args[0]);
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
                    if (IS_CHANGE_TO_DETAIL_TAWARAN_FRAGMENT)
                        socketWinnerSelectedFromDetailTawaran.socketReceived("winnerchosen", args[0]);
                    socketWinnerSelected.socketReceived("winnerchosen", args[0]);
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
                    Log.v("Cancel bid success", "cancel bid success");
                    if (IS_CHANGE_TO_DETAIL_TAWARAN_FRAGMENT)
                        socketBidCancelledFromDetailTawaran.socketReceived("cancelbidsuccess", args[0]);
                    else socketBidCancelled.socketReceived("cancelbidsuccess", args[0]);
                }
            });
        }
    };
}
