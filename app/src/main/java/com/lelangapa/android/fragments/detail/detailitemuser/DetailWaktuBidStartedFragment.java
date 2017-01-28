package com.lelangapa.android.fragments.detail.detailitemuser;

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

import java.text.DecimalFormat;
import java.text.NumberFormat;

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
    private NumberFormat format;
    private CountDownTimer countDownTimer;
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
        format = new DecimalFormat("00");
        setCountDownTimer();
        return view;
    }
    private void setCountDownTimer()
    {
        if (detailItem.getItembidstatus()==1)
        {
            final long timeLeft = detailItem.getTanggaljamselesai_ms() - serverTime;
            countDownTimer = new CountDownTimer(timeLeft, 1000) {
                @Override
                public void onTick(long millisUntilFinished) {
                    timeCountDown = millisUntilFinished;
                    if (millisUntilFinished <= 1000) onFinish();
                    long hour = (timeCountDown / HOURS_MAX);
                    timeCountDown %= HOURS_MAX;
                    long min = (timeCountDown / MINUTES_MAX);
                    timeCountDown %= MINUTES_MAX;
                    long secs = (timeCountDown/ SECONDS_MAX);
                    timeCountDown %= SECONDS_MAX;

                    textView_countdown.setText(format.format(hour) + ":" + format.format(min) + ":" + format.format(secs));
                }
                @Override
                public void onFinish() {
                    textView_countdown.setText("00:00:00");
                    triggerBiddingDone.dataReceived("finish");
                }
            };
            countDownTimer.start();
        }
    }
    public void setInitialDetailItem(DetailItemResources detailItem)
    {
        this.detailItem = detailItem;
    }
    public void setInitialServerTime(Long serverTime)
    {
        this.serverTime = serverTime;
    }
    public void setTriggerBiddingDone(DataReceiver triggerBiddingDone)
    {
        this.triggerBiddingDone = triggerBiddingDone;
    }

    /*
    *   Ini untuk menambah durasi waktu ketika ada tawaran yang masuk
    *   saat lelang menyisakan waktu kurang dari 10 menit lagi
    *
    *
     */
    public void setDetailItemWhenTimeExtended(DetailItemResources detailItem)
    {
        this.detailItem = detailItem;
    }
    public void setServerTimeWhenTimeExtended(Long serverTime)
    {
        this.serverTime = serverTime;
    }
    public void cancelAndStartNewTimerWhenTimeExtended()
    {
        countDownTimer.cancel();
        setCountDownTimer();
    }
}
