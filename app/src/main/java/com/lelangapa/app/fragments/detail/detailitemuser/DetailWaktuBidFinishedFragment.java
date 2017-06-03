package com.lelangapa.app.fragments.detail.detailitemuser;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.lelangapa.app.R;
import com.lelangapa.app.resources.DateTimeConverter;
import com.lelangapa.app.resources.DetailItemResources;

/**
 * Created by Andre on 1/6/2017.
 */

public class DetailWaktuBidFinishedFragment extends Fragment {
    private DetailItemResources detailItem;
    private DateTimeConverter dateTimeConverter;
    private TextView textView_startDate;
    private TextView textView_endDate;
    public DetailWaktuBidFinishedFragment(){}
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        dateTimeConverter = new DateTimeConverter();
        View view = inflater.inflate(R.layout.fragment_detail_barang_waktubid_finished_layout, container, false);
        String waktuMulai = dateTimeConverter.convertUTCToLocalTimeIndonesiaFormat(detailItem.getTanggaljammulai());
        String waktuSelesai = dateTimeConverter.convertUTCToLocalTimeIndonesiaFormat(detailItem.getTanggaljamselesai());
        textView_startDate = (TextView) view.findViewById(R.id.fragment_detail_barang_waktubid_finished_waktumulai);
        textView_endDate = (TextView) view.findViewById(R.id.fragment_detail_barang_waktubid_finished_waktuselesai);
        textView_startDate.setText(waktuMulai);
        textView_endDate.setText(waktuSelesai);
        return view;
    }
    public void setDetailItem(DetailItemResources detailItem)
    {
        this.detailItem = detailItem;
    }
}
