package com.lelangapa.android.fragments.detail.detailitemowner;

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
 * Created by andre on 04/02/17.
 */

public class WaktuBidStartedFragment extends Fragment {
    private DetailItemResources detailItem;
    private DataReceiver triggerFinished;
    private DateTimeConverter dateTimeConverter;
    private NumberFormat format;
    private CountDownTimer countDownTimer;

    private TextView textView_waktuMulai;
    private TextView textView_waktuSelesai;
    private TextView textView_countdown;

    private Long serverTime, timeCountDown;
    private long HOURS_MAX, MINUTES_MAX, SECONDS_MAX;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_detail_barang_waktubid_started_layout, container, false);
        initializeConstant();
        initializeViews(view);
        setTimeStartAndTimeFinish(detailItem.getTanggaljammulai(), detailItem.getTanggaljamselesai());
        setCountDownTimer();
        return view;
    }
    /*
    * Initialization method start here
    * */
    private void initializeConstant()
    {
        dateTimeConverter = new DateTimeConverter();
        format = new DecimalFormat("00");
        HOURS_MAX = 3600000;
        MINUTES_MAX = 60000;
        SECONDS_MAX = 1000;
    }
    private void initializeViews(View view)
    {
        textView_waktuMulai = (TextView) view.findViewById(R.id.fragment_detail_barang_waktubid_waktumulai);
        textView_waktuSelesai = (TextView) view.findViewById(R.id.fragment_detail_barang_waktubid_waktuselesai);
        textView_countdown = (TextView) view.findViewById(R.id.fragment_detail_barang_waktubid_countdown);
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
        setTimeStartAndTimeFinish(detailItem.getTanggaljammulai(), detailItem.getTanggaljamselesai());
    }
    public void setTriggerFinished(DataReceiver triggerFinished)
    {
        this.triggerFinished = triggerFinished;
    }
    public void setServerTime(Long serverTime)
    {
        this.serverTime = serverTime;
    }
    private void setTimeStartAndTimeFinish(String startTimeUTC, String endTimeUTC)
    {
        if (textView_waktuMulai != null && textView_waktuSelesai != null)
        {
            String startTime = dateTimeConverter.convertUTCToLocalTimeIndonesiaFormat(startTimeUTC);
            String endTime = dateTimeConverter.convertUTCToLocalTimeIndonesiaFormat(endTimeUTC);
            textView_waktuMulai.setText(startTime);
            textView_waktuSelesai.setText(endTime);
        }
    }
    /*
    * Setter method end here
    * */

    /*
    * Countdown methods start here
    * */
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
                    triggerFinished.dataReceived("finish");
                }
            };
            countDownTimer.start();
        }
    }
    public void cancelAndStartNewTimerWhenTimeExtended()
    {
        countDownTimer.cancel();
        setCountDownTimer();
    }
    /*
    * Countdown methods end here
    * */
}
