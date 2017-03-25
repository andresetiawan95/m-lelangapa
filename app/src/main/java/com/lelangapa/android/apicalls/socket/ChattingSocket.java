package com.lelangapa.android.apicalls.socket;

import android.app.Activity;

import com.lelangapa.android.interfaces.SocketReceiver;
import com.lelangapa.android.resources.SocketSSLResources;

import java.io.IOException;
import java.net.URISyntaxException;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;

import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;

/**
 * Created by andre on 22/03/17.
 */

public class ChattingSocket {
    private Socket mSocket;
    private Activity activity;

    private SocketReceiver socketConnectedHandshake;
    private SocketReceiver socketDisconnected;
    private SocketReceiver socketOnNewMessageReceived;
    private SocketReceiver socketChatHistory;
    private SocketReceiver socketSendStatus;

    public boolean IS_JOINED_ROOM_STATUS;
    private SocketSSLResources socketSSLResources;
    public ChattingSocket(Activity activity)
    {
        this.activity = activity;
        socketSSLResources = new SocketSSLResources(this.activity);
        createServerSocket();
    }
    public void setSocketOnNewMessageReceived(SocketReceiver receiver)
    {
        this.socketOnNewMessageReceived = receiver;
    }
    public void setSocketConnectedHandshake(SocketReceiver socketConnectedHandshake) {
        this.socketConnectedHandshake = socketConnectedHandshake;
    }

    public void setSocketDisconnected(SocketReceiver socketDisconnected) {
        this.socketDisconnected = socketDisconnected;
    }

    public void setSocketChatHistory(SocketReceiver socketChatHistory) {
        this.socketChatHistory = socketChatHistory;
    }

    public void setSocketSendStatus(SocketReceiver socketSendStatus) {
        this.socketSendStatus = socketSendStatus;
    }
    private void createServerSocket()
    {
        try {
            IO.Options options = new IO.Options();
            options.forceNew = false;
            options.reconnection = true;
            options.sslContext = socketSSLResources.getSSLSocketContext();
            options.hostnameVerifier = socketSSLResources.getHostnameVerifier();
            mSocket = IO.socket("https://188.166.179.2:12000", options);
        }
        catch (URISyntaxException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (CertificateException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (KeyStoreException e) {
            e.printStackTrace();
        } catch (KeyManagementException e) {
            e.printStackTrace();
        }
    }
    public Socket getSocket()
    {
        return mSocket;
    }
    public Emitter.Listener onConnectedHandshake = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    IS_JOINED_ROOM_STATUS = false;
                    socketConnectedHandshake.socketReceived("handshake", args[0]);
                }
            });
        }
    };
    public Emitter.Listener onNewMessage = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (!getSocket().connected())getSocket().connect();
                    socketOnNewMessageReceived.socketReceived("new-msg", args[0]);
                }
            });
        }
    };
    public Emitter.Listener onChatHistory = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (!getSocket().connected())getSocket().connect();
                    socketChatHistory.socketReceived("chathistory", args[0]);
                }
            });
        }
    };
    public Emitter.Listener onSendStatus = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (!getSocket().connected())getSocket().connect();
                    socketSendStatus.socketReceived("send-status", args[0]);
                }
            });
        }
    };
}
