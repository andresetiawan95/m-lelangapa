package com.lelangapa.android.fragments.detail.detailtawaran;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.lelangapa.android.R;

import io.socket.client.Socket;

/**
 * Created by andre on 28/03/17.
 */

public class DaftarTawaranFragment extends Fragment {
    //PERCOBAAN
    //socket masih connected ketika
    private Socket mSocket;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_detail_barang_daftar_tawaran_layout, container, false);
        if (mSocket.connected())
        {
            Log.v("CONNECTED", "SOCKET MASIH CONNECTED");
        }
        else
        {
            Log.v("DISCONNECT", "SOCKET SUDAH KOID");
        }
        return view;
    }
    public void setSocket(Socket socket)
    {
        this.mSocket = socket;
    }
}
