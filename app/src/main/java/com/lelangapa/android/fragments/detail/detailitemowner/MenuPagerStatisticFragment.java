package com.lelangapa.android.fragments.detail.detailitemowner;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.lelangapa.android.R;

import java.text.DecimalFormat;

/**
 * Created by andre on 25/01/17.
 */

public class MenuPagerStatisticFragment extends Fragment {
    private TextView textView_hargaAwal, textView_hargaEkspektasi, textView_hargaTawaran, textView_kenaikan;

    private DecimalFormat df;
    private String hargaAwal, hargaEkspektasi, hargaTawaran, kenaikan;
    public MenuPagerStatisticFragment()
    {
        initializeConstant();
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_detail_barang_bidding_statistic_auctioneer_layout, container, false);
        initializeViews(view);
        setTextViews();
        return view;
    }

    /*
    * Initialization method start here
    * */
    private void initializeConstant()
    {
        df = new DecimalFormat("#.##");
    }
    private void initializeViews(View view)
    {
        textView_hargaAwal = (TextView) view.findViewById(R.id.fragment_detail_barang_bidding_statistic_auctioneer_hargaawal);
        textView_hargaEkspektasi = (TextView) view.findViewById(R.id.fragment_detail_barang_bidding_statistic_auctioneer_hargaekspektasi);
        textView_hargaTawaran = (TextView) view.findViewById(R.id.fragment_detail_barang_bidding_statistic_auctioneer_tawaran);
        textView_kenaikan = (TextView) view.findViewById(R.id.fragment_detail_barang_bidding_statistic_auctioneer_kenaikan);
    }
    /*
    * Initialization method end here
    * */

    /*
    * Setter method start here
    * */
    public void setStatisticInformation(String hargaAwal, String hargaEkspektasi, String hargaTawaran)
    {
        this.hargaAwal = hargaAwal;
        this.hargaEkspektasi = hargaEkspektasi;
        this.hargaTawaran = hargaTawaran;
        this.kenaikan = hitungKenaikan(hargaAwal, hargaTawaran);
        if (textView_hargaAwal != null && textView_hargaEkspektasi != null && textView_hargaTawaran!= null && textView_kenaikan != null)
        {
            setTextViews();
        }
    }
    public void setTextViews()
    {
        textView_hargaAwal.setText(this.hargaAwal);
        textView_hargaEkspektasi.setText(this.hargaEkspektasi);
        textView_hargaTawaran.setText(this.hargaTawaran);
        textView_kenaikan.setText(this.kenaikan);
    }
    /*
    * Setter method end here
    * */

    /*
    * Logic method start here
    * */
    private String hitungKenaikan(String hargaAwal, String hargaTawaran)
    {
        long hargaAwal_long = Long.parseLong(hargaAwal);
        long hargaTawaran_long = Long.parseLong(hargaTawaran);
        double lipat = (double) (hargaTawaran_long / hargaAwal_long);
        String outputKenaikan = df.format(lipat);
        return outputKenaikan;
    }
    /*
    * Logic method end here
    * */
}
