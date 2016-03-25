package com.sahilmgandhi.remmy;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.Dialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.app.TimePickerDialog.OnTimeSetListener;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.media.AudioManager;
import android.media.MediaPlayer;
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
                int currsec = calendar.get(Calendar.MINUTE);

                calendar.set(Calendar.HOUR_OF_DAY, hourToSet);
                calendar.set(Calendar.MINUTE, minuteToSet);
            }
            else
            {
                hourToSet = alrmTimePicker.getCurrentHour();
                minuteToSet = alrmTimePicker.getCurrentMinute();

                // this is the code to actually do the "magic" of the REM times
                int currhr = calendar.get(Calendar.HOUR_OF_DAY);
                int currsec = calendar.get(Calendar.MINUTE);

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

    /*public void onDestroy()
    {
        super.onDestroy();
    }
*/

}


/*public class AlarmStartPage extends AppCompatActivity
{
    Button remMaker;

    TimePickerDialog alarmTimeDialog;

    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm_start_page);

        remMaker = (Button) findViewById(R.id.remCreateButton);

        remMaker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar c = Calendar.getInstance();
                int hour = c.HOUR_OF_DAY;                           // these are the system hour and minute
                int minutes = c.MINUTE;
                openTimePickerDialog(false, hour, minutes);

                //Calendar c = Calendar.getInstance();
                //c.set(getAlarmTime.getHour(), getAlarmTime.getMinute(), 00);
                //setAlarm(c);
            }
        });
    }

    private void openTimePickerDialog(boolean is24r, int sysHour, int sysMin)
    {
        Calendar cal = Calendar.getInstance();
        *//*int hour = cal.HOUR_OF_DAY;               // we actually NEED to get the system time ... and then perform the REM time shift on it!
        int minutes = cal.MINUTE;
        boolean increaseDay = false;
        if (minutes + 15 >= 60)
        {
            hour++;
            minutes = minutes% 60;

            if (hour >= 24)
            {
                increaseDay = true;
                hour = hour% 24;
            }
        }*//*
        alarmTimeDialog = new TimePickerDialog(AlarmStartPage.this, onTimeSetListener, cal.get(Calendar.HOUR_OF_DAY), cal.get(Calendar.MINUTE), is24r);

        alarmTimeDialog.setTitle("Set Alarm Time");

        alarmTimeDialog.show();
    }

    OnTimeSetListener onTimeSetListener = new OnTimeSetListener()
    {
        @Override
        public void onTimeSet(TimePicker view, int hourOfDay, int minute)
        {
            Calendar currCal = Calendar.getInstance();
            Calendar setCal = (Calendar) currCal.clone();

            setCal.set(Calendar.HOUR_OF_DAY, hourOfDay);
            setCal.set(Calendar.MINUTE, minute);
            setCal.set(Calendar.SECOND, 0);
            setCal.set(Calendar.MILLISECOND,0);

            if (setCal.compareTo(currCal) <= 0)
            {
                setCal.add(Calendar.DATE, 1);
            }
            setAlarm(setCal);
        }
    };

    private void setAlarm(Calendar targetCal)
    {
        Intent intent = new Intent(getBaseContext(), AlarmReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(getBaseContext(), 1, intent, 0);
        AlarmManager alarmManager = (AlarmManager)getSystemService(Context.ALARM_SERVICE);
        alarmManager.set(AlarmManager.RTC_WAKEUP, targetCal.getTimeInMillis(), pendingIntent);
    }

    *//*private void setAlarm(Calendar cal)
    {

    }*//*


    *//*public static class TimePickerFragment implements TimePickerDialog.onTimeSetListener
    {
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState)
        {

        }
    }*//*

}*/