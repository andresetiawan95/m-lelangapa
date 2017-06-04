package com.lelangapa.app.fragments.detail.detailitemuser;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.lelangapa.app.R;
import com.lelangapa.app.interfaces.DataReceiver;
import com.lelangapa.app.resources.DateTimeConverter;
import com.lelangapa.app.resources.DetailItemResources;

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
    private long DAY_MAX = 24 * 60 * 60 * 1000, HOURS_MAX = 3600000, MINUTES_MAX = 60000, SECONDS_MAX = 1000;
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
                    long day = (timeCountDown / DAY_MAX);
                    timeCountDown %= DAY_MAX;
                    long hour = (timeCountDown / HOURS_MAX);
                    timeCountDown %= HOURS_MAX;
                    long min = (timeCountDown / MINUTES_MAX);
                    timeCountDown %= MINUTES_MAX;
                    long secs = (timeCountDown/ SECONDS_MAX);
                    timeCountDown %= SECONDS_MAX;

                    textView_countdown.setText(format.format(day) + "d " +format.format(hour) + "h " + format.format(min) + "m " + format.format(secs) + "s");
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

    //DARI SINI KE BAWAH AKAN DIJADIKAN SATU METHOD SAJA NANTI
    public void setInitialDetailItem(DetailItemResources detailItem)
    {
        this.detailItem = detailItem;
        /*
        * Untuk merubah nilai textView Waktu Mulai dan textView Waktu Selesai ketika
        * refresh di trigger dan waktu berubah
        * */
        if (textView_waktuMulai != null && textView_waktuSelesai!= null)
        {
            String waktuMulaiNew = dateTimeConverter.convertUTCToLocalTimeIndonesiaFormat(detailItem.getTanggaljammulai());
            String waktuSelesaiNew = dateTimeConverter.convertUTCToLocalTimeIndonesiaFormat(detailItem.getTanggaljamselesai());
            textView_waktuMulai.setText(waktuMulaiNew);
            textView_waktuSelesai.setText(waktuSelesaiNew);
        }
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
     */
    public void setDetailItemWhenTimeExtended(DetailItemResources detailItem)
    {
        this.detailItem = detailItem;
        if (textView_waktuMulai != null && textView_waktuSelesai!= null)
        {
            String waktuMulaiNew = dateTimeConverter.convertUTCToLocalTimeIndonesiaFormat(detailItem.getTanggaljammulai());
            String waktuSelesaiNew = dateTimeConverter.convertUTCToLocalTimeIndonesiaFormat(detailItem.getTanggaljamselesai());
            textView_waktuMulai.setText(waktuMulaiNew);
            textView_waktuSelesai.setText(waktuSelesaiNew);
        }
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
