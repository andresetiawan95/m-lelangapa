package com.lelangkita.android.fragments.detail;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.lelangkita.android.R;
import com.lelangkita.android.resources.DetailItemResources;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Andre on 12/25/2016.
 */

public class DetailWaktuBidFragment extends Fragment {
    private String oldDateFormat = "yyyy-MM-dd";
    private String newDateFormat = "dd-MM-yyyy";
    private DetailItemResources detailItem;
    private TextView textView_waktuMulai;
    private TextView textView_waktuSelesai;
    public DetailWaktuBidFragment(){}
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_detail_barang_waktubid_layout, container, false);
        String waktuMulai = parseDateFormatWaktu(detailItem.getTanggalmulai()) + " " + detailItem.getJammulai();
        String waktuSelesai = parseDateFormatWaktu(detailItem.getTanggalselesai()) + " " + detailItem.getJamselesai();
        textView_waktuMulai = (TextView) view.findViewById(R.id.fragment_detail_barang_waktubid_waktumulai);
        textView_waktuSelesai = (TextView) view.findViewById(R.id.fragment_detail_barang_waktubid_waktuselesai);
        textView_waktuMulai.setText(waktuMulai);
        textView_waktuSelesai.setText(waktuSelesai);
        return view;
    }
    private String parseDateFormatWaktu(String time)
    {
        SimpleDateFormat sdf = new SimpleDateFormat(oldDateFormat);
        try {
            Date date = sdf.parse(time);
            Date now = new Date();
            Log.v("Now time", now.toString());
            String nowDate = sdf.format(now);
            Date nowDateParsed = sdf.parse(nowDate);
            Integer check = nowDateParsed.compareTo(date);
            Log.v("Date Checker", check.toString());
            sdf.applyPattern(newDateFormat);
            String output = sdf.format(date);
            return output;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }
    public void setDetailItem(DetailItemResources detailItem)
    {
        this.detailItem = detailItem;
    }
}
