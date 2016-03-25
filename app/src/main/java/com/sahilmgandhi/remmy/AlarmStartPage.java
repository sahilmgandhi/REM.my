package com.sahilmgandhi.remmy;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.Dialog;
import android.app.PendingIntent;
import android.content.Intent;
import android.media.AudioManager;
import android.os.Build;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.AppCompatAutoCompleteTextView;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TimePicker;
import android.graphics.PorterDuff;
import java.util.Calendar;
import android.util.Log;
import android.widget.TextView;
import android.widget.ToggleButton;



public class AlarmStartPage extends Activity
{
    AlarmManager alrmMgr;
    private PendingIntent pendInt;
    private TimePicker alrmTimePicker;
    private static AlarmStartPage inst;
    private Intent myIntent;
    private TextView alrmStatusView;

    public static AlarmStartPage instance()
    {
        return inst;
    }

    @Override
    public void onStart()
    {
        super.onStart();
        inst = this;
    }

    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm_start_page);
        alrmTimePicker = (TimePicker) findViewById(R.id.alarmTimePicker);
        ToggleButton alrmTogg = (ToggleButton) findViewById(R.id.toggleAlarmButton);
        alrmMgr = (AlarmManager) getSystemService(ALARM_SERVICE);
        alrmStatusView = (TextView) findViewById(R.id.alarmStatus);

        setVolumeControlStream(AudioManager.STREAM_ALARM);
    }

    public void onToggleClicked(View view)
    {
        if (((ToggleButton) view).isChecked())
        {
            Log.d("MyActivity", "Alarm On!");
            int hourToSet, minuteToSet;
            Calendar calendar = Calendar.getInstance();
            if (Build.VERSION.SDK_INT >= 23)
            {
                hourToSet = alrmTimePicker.getHour();
                minuteToSet = alrmTimePicker.getMinute();
                // this is the code to actually do the "magic" of the REM time
                int currhr = calendar.get(Calendar.HOUR_OF_DAY);
                int currmin = calendar.get(Calendar.MINUTE);

                currmin+=15;
                if (currmin >= 60)
                {
                    currmin = currmin%60;
                    currhr++;
                    if (currhr >= 24)
                    {
                        currhr = currhr%24;
                    }
                }
                boolean lessThan90 = false;
                int hrDiff = 0;
                int minDiff = 0;
                if (hourToSet >= currhr)
                {
                    hrDiff = hourToSet - currhr;
                    if (hrDiff == 0)                // if the alarm is set for the same hour
                    {
                        if (minuteToSet > currmin)      // if the alarm is set for a later time (which means that it is less than 90 minutes away)
                        {
                            minDiff = minuteToSet - currmin;
                        }
                        else                            // otherwise the alarm is set for 23 hours and some minutes away
                        {
                            minDiff = 60-currmin + minuteToSet;
                            hrDiff = 23;                // 23 hours and minDiff away!
                        }
                    }
                    else
                    {
                        minDiff = 60-currmin + minuteToSet;
                    }
                }
                else if (hourToSet < currhr)
                {
                    hrDiff = 24-currhr + hourToSet;
                }

                int totalMinutesInBetween = 60*hrDiff + minDiff;

                if (totalMinutesInBetween < 90)
                {
                    lessThan90 = true;
                }
                if (!lessThan90)            // If there are more than 90 minutes of difference, then a REM cycle is ACTUALLY possible
                {
                    int possibleRem = totalMinutesInBetween/90;
                    for (int i = 0; i < possibleRem; i++)
                    {
                        currhr++;
                        if (currhr >= 24)
                            currhr = currhr%24;
                        currmin+=30;
                        if (currmin >= 60)
                        {
                            currmin = currmin%60;
                            currhr++;
                            if (currhr >= 24)
                            {
                                currhr = currhr%24;
                            }
                        }
                    }
                    hourToSet = currhr;
                    minuteToSet = currmin;
                }

                calendar.set(Calendar.HOUR_OF_DAY, hourToSet);
                calendar.set(Calendar.MINUTE, minuteToSet);
            }
            else
            {
                hourToSet = alrmTimePicker.getCurrentHour();
                minuteToSet = alrmTimePicker.getCurrentMinute();

                // this is the code to actually do the "magic" of the REM times
                int currhr = calendar.get(Calendar.HOUR_OF_DAY);
                int currmin = calendar.get(Calendar.MINUTE);

                currmin+=15;
                if (currmin >= 60)
                {
                    currmin = currmin%60;
                    currhr++;
                    if (currhr >= 24)
                    {
                        currhr = currhr%24;
                    }
                }
                boolean lessThan90 = false;
                int hrDiff = 0;
                int minDiff = 0;
                if (hourToSet >= currhr)
                {
                    hrDiff = hourToSet - currhr;
                    if (hrDiff == 0)                // if the alarm is set for the same hour
                    {
                        if (minuteToSet > currmin)      // if the alarm is set for a later time (which means that it is less than 90 minutes away)
                        {
                            minDiff = minuteToSet - currmin;
                        }
                        else                            // otherwise the alarm is set for 23 hours and some minutes away
                        {
                            minDiff = 60-currmin + minuteToSet;
                            hrDiff = 23;                // 23 hours and minDiff away!
                        }
                    }
                    else
                    {
                        minDiff = 60-currmin + minuteToSet;
                    }
                }
                else if (hourToSet < currhr)
                {
                    hrDiff = 24-currhr + hourToSet;
                }

                int totalMinutesInBetween = 60*hrDiff + minDiff;

                if (totalMinutesInBetween < 90)
                {
                    lessThan90 = true;
                }
                if (!lessThan90)            // If there are more than 90 minutes of difference, then a REM cycle is ACTUALLY possible
                {
                    int possibleRem = totalMinutesInBetween/90;
                    for (int i = 0; i < possibleRem; i++)
                    {
                        currhr++;
                        if (currhr >= 24)
                            currhr = currhr%24;
                        currmin+=30;
                        if (currmin >= 60)
                        {
                            currmin = currmin%60;
                            currhr++;
                            if (currhr >= 24)
                            {
                                currhr = currhr%24;
                            }
                        }
                    }
                    hourToSet = currhr;
                    minuteToSet = currmin;
                }
                calendar.set(Calendar.HOUR_OF_DAY, hourToSet);
                calendar.set(Calendar.MINUTE, minuteToSet);
            }
            myIntent = new Intent(AlarmStartPage.this, AlarmReceiver.class);
            pendInt = PendingIntent.getBroadcast(AlarmStartPage.this, 0, myIntent, 0);

            alrmMgr.set(AlarmManager.RTC, calendar.getTimeInMillis(), pendInt);
            setAlarmText("An alarm has been placed for " + hourToSet + " hours and " + minuteToSet + " minutes (in military time). If you shut down" +
                    " this app, please do not open it again until the alarm that you set is over (otherwise the app will reset itself).");
        }
        else
        {
            alrmMgr.cancel(pendInt);
            //stopService(myIntent);
            setAlarmText("");
            Log.d("MyActivity", "Alarm OFF");
        }
    }

    public void setAlarmText(String textToShow)
    {
        alrmStatusView.setText(textToShow);
    }

    public void onDestroy()
    {
        super.onDestroy();
    }
}