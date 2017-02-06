package com.lelangapa.android.fragments.detail.detailitemowner;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.lelangapa.android.R;
import com.lelangapa.android.resources.DateTimeConverter;
import com.lelangapa.android.resources.DetailItemResources;

/**
 * Created by andre on 04/02/17.
 */

public class WaktuBidNotStartedFragment extends Fragment {
    private DetailItemResources detailItem;
    private DateTimeConverter dateTimeConverter;

    private TextView textView_startDate;
    private TextView textView_endDate;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_detail_barang_waktubid_notstarted_layout, container, false);
        initializeConstant();
        initializeViews(view);
        setTimeStartAndTimeFinish(detailItem.getTanggaljammulai(), detailItem.getTanggaljamselesai());
        return view;
    }
    /*
    * Initialization method start here
    * */
    private void initializeConstant()
    {
        dateTimeConverter = new DateTimeConverter();
    }
    private void initializeViews(View view)
    {
        textView_startDate = (TextView) view.findViewById(R.id.fragment_detail_barang_waktubid_finished_waktumulai);
        textView_endDate = (TextView) view.findViewById(R.id.fragment_detail_barang_waktubid_finished_waktuselesai);
    }
    /*
    * Initialization method end here
    * */

    /*
    * Setter method start here
    * */
    public void setDetailItem(DetailItemResources detailItem)
    {
        this.detailItem = detailItem;
    }
    public void setTimeStartAndTimeFinish(String startTimeUTC, String finishTimeUTC)
    {
        String startTime = dateTimeConverter.convertUTCToLocalTimeIndonesiaFormat(startTimeUTC);
        String endTime = dateTimeConverter.convertUTCToLocalTimeIndonesiaFormat(finishTimeUTC);
        if (textView_startDate != null && textView_endDate != null)
        {
            textView_startDate.setText(startTime);
            textView_endDate.setText(endTime);
        }
    }
    /*
    * Setter method end here
    * */
}
