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

import java.text.DecimalFormat;
import java.text.NumberFormat;

/**
 * Created by andre on 25/01/17.
 */

public class MenuPagerBiddingNotStartedFragment extends Fragment {
    private TextView textView_timeCountdown;
    private CountDownTimer countDownTimer;

    private DataReceiver triggerStarted;
    private Long serverTimeNow_milisecond, startTime_milisecond, timeCountDown;
    private long HOURS_MAX = 3600000, MINUTES_MAX = 60000, SECONDS_MAX = 1000;
    private NumberFormat format;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_detail_barang_bidding_notstarted_auctioneer_layout, container, false);
        initializeViews(view);
        setDecimalFormat();
        setCountDownTimer();
        return view;
    }

    /*
    * Initialization method start here
    * */
    private void initializeViews(View view)
    {
        textView_timeCountdown = (TextView) view.findViewById(R.id.fragment_detail_barang_bidding_notstarted_auctioneer_countdown);
    }
    /*
    * Initialization method end here
    * */

    /*
    * Setter value method start here
    * */
    public void setDecimalFormat()
    {
        format = new DecimalFormat("00");
    }
    public void setStartTimeAndServerTime(Long startTime_milisecond, Long serverTimeNow_milisecond)
    {
        this.startTime_milisecond = startTime_milisecond;
        this.serverTimeNow_milisecond = serverTimeNow_milisecond;
    }
    public void setTriggerStarted(DataReceiver triggerStarted)
    {
        this.triggerStarted = triggerStarted;
    }
    /*
    * Setter value method end here
    * */

    /*
    * Countdown method start here
    * */
    private void setCountDownTimer()
    {
        final long timeLeft = startTime_milisecond - serverTimeNow_milisecond;
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

                textView_timeCountdown.setText(format.format(hour) + ":" + format.format(min) + ":" + format.format(secs));
            }

            @Override
            public void onFinish() {
                textView_timeCountdown.setText("00:00:00");
                triggerStarted.dataReceived("start");
            }
        };
        countDownTimer.start();
    }
    public void restartTimerWhenItemEdited() {
        countDownTimer.cancel();
        setCountDownTimer();
    }
    /*
    * Countdown method end here
    * */
}
