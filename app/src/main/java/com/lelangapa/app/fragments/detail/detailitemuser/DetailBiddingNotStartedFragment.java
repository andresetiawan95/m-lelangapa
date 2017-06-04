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

import java.text.DecimalFormat;
import java.text.NumberFormat;

/**
 * Created by Andre on 1/4/2017.
 */

public class DetailBiddingNotStartedFragment extends Fragment {
    private DataReceiver triggerSender;
    private TextView textView_countdown;
    private Long serverTimeNow_milisecond;
    private Long startTime_milisecond;
    private Long serverTime, timeCountDown, hoursLeftLong, minutesLeftLong, secondsLeftLong;
    private String hoursLeftString, minutesLeftString, secondsLeftString;
    private long DAY_MAX = 24 * 60 * 60 * 1000, HOURS_MAX = 3600000, MINUTES_MAX = 60000, SECONDS_MAX = 1000;
    private NumberFormat format;
    public DetailBiddingNotStartedFragment(){}
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_detail_barang_bidding_notstarted_layout, container, false);
        textView_countdown = (TextView) view.findViewById(R.id.fragment_detail_barang_bidding_notstarted_countdown);
        format = new DecimalFormat("00");
        setCountDownTimer();
        return view;
    }
    public void setStartTimeAndServerTime(Long startTime, Long serverTime)
    {
        serverTimeNow_milisecond = serverTime;
        startTime_milisecond = startTime;
    }
    public void setTriggerSender(DataReceiver triggerSender)
    {
        this.triggerSender = triggerSender;
    }
    private void setCountDownTimer()
    {
        final long timeLeft = startTime_milisecond - serverTimeNow_milisecond;
        CountDownTimer countDownTimer = new CountDownTimer(timeLeft, 1000) {
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
                /*if(millisUntilFinished <= 1000) onFinish();
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
                if (hoursLeftString.equals("00") && minutesLeftString.equals("00") && secondsLeftString.equals("00"))
                {
                    //textView_countdown.setText("00:00:00");
                    onFinish();
                }
                else
                {
                    textView_countdown.setText(hoursLeftString + ":" + minutesLeftString + ":" + secondsLeftString);
                }*/
            }
            @Override
            public void onFinish() {
                textView_countdown.setText("00:00:00");
                triggerSender.dataReceived("start");
            }
        };
        countDownTimer.start();
    }
}
