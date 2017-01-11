package com.lelangapa.android.fragments.detail;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.lelangapa.android.R;
import com.lelangapa.android.interfaces.DataReceiver;
import com.lelangapa.android.resources.DateTimeConverter;
import com.lelangapa.android.resources.DetailItemResources;

/**
 * Created by Andre on 12/25/2016.
 */

public class DetailWaktuBidStartedFragment extends Fragment {
    private DataReceiver triggerBiddingDone;
    private String oldDateFormat = "yyyy-MM-dd";
    private String newDateFormat = "dd-MM-yyyy";
    private DetailItemResources detailItem;
    private TextView textView_waktuMulai;
    private TextView textView_waktuSelesai;
    private TextView textView_countdown;
    private DateTimeConverter dateTimeConverter;
    private Long serverTime, timeCountDown, hoursLeftLong, minutesLeftLong, secondsLeftLong;
    private String hoursLeftString, minutesLeftString, secondsLeftString;
    private long HOURS_MAX = 3600000, MINUTES_MAX = 60000, SECONDS_MAX = 1000;
    public DetailWaktuBidStartedFragment(){}
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        dateTimeConverter = new DateTimeConverter();
        View view = inflater.inflate(R.layout.fragment_detail_barang_waktubid_started_layout, container, false);
        String waktuMulai = dateTimeConverter.convertUTCToLocalTimeIndonesiaFormat(detailItem.getTanggaljammulai());
        String waktuSelesai = dateTimeConverter.convertUTCToLocalTimeIndonesiaFormat(detailItem.getTanggaljamselesai());
        textView_waktuMulai = (TextView) view.findViewById(R.id.fragment_detail_barang_waktubid_waktumulai);
        textView_waktuSelesai = (TextView) view.findViewById(R.id.fragment_detail_barang_waktubid_waktuselesai);
        textView_countdown = (TextView) view.findViewById(R.id.fragment_detail_barang_waktubid_countdown);
        textView_waktuMulai.setText(waktuMulai);
        textView_waktuSelesai.setText(waktuSelesai);
        setCountDownTimer();
        return view;
    }
    private void setCountDownTimer()
    {
        if (detailItem.getItembidstatus()==1)
        {
            final long timeLeft = detailItem.getTanggaljamselesai_ms() - serverTime;
            CountDownTimer countDownTimer = new CountDownTimer(timeLeft, 1000) {
                @Override
                public void onTick(long millisUntilFinished) {
                    timeCountDown = millisUntilFinished;
                    if (timeCountDown/HOURS_MAX > 0)
                    {
                        hoursLeftLong = timeCountDown/HOURS_MAX;
                        if (hoursLeftLong >=10)
                        {
                            hoursLeftString = hoursLeftLong.toString();
                        }
                        else
                        {
                            hoursLeftString = "0"+hoursLeftLong.toString();
                        }
                        timeCountDown=timeCountDown%HOURS_MAX;
                    }
                    else
                    {
                        hoursLeftString = "00";
                        timeCountDown=timeCountDown%HOURS_MAX;
                    }
                    if (timeCountDown/MINUTES_MAX > 0)
                    {
                        minutesLeftLong = timeCountDown/MINUTES_MAX;
                        if (minutesLeftLong >=10)
                        {
                            minutesLeftString = minutesLeftLong.toString();
                        }
                        else
                        {
                            minutesLeftString = "0"+minutesLeftLong.toString();
                        }
                        timeCountDown=timeCountDown%MINUTES_MAX;
                    }
                    else
                    {
                        minutesLeftString = "00";
                        timeCountDown = timeCountDown%MINUTES_MAX;
                    }
                    if (timeCountDown/SECONDS_MAX > 0)
                    {
                        secondsLeftLong = timeCountDown/SECONDS_MAX;
                        if (secondsLeftLong >=10)
                        {
                            secondsLeftString = secondsLeftLong.toString();
                        }
                        else
                        {
                            secondsLeftString = "0"+secondsLeftLong.toString();
                        }
                        timeCountDown=timeCountDown%SECONDS_MAX;
                    }
                    else
                    {
                        secondsLeftString = "00";
                        timeCountDown = timeCountDown%SECONDS_MAX;
                    }
                    textView_countdown.setText(hoursLeftString + ":" + minutesLeftString + ":" + secondsLeftString);
                }
                @Override
                public void onFinish() {
                    textView_countdown.setText("00:00:00");
                    triggerBiddingDone.dataReceived("finish");
                }
            }.start();
        }
    }
    public void setDetailItem(DetailItemResources detailItem)
    {
        this.detailItem = detailItem;
    }
    public void setServerTime(Long serverTime)
    {
        this.serverTime = serverTime;
    }
    public void setTriggerBiddingDone(DataReceiver triggerBiddingDone)
    {
        this.triggerBiddingDone = triggerBiddingDone;
    }
}
