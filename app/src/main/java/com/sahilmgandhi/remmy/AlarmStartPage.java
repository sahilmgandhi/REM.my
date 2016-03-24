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

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.util.Log;
import android.widget.TextView;
import android.widget.ToggleButton;



public class AlarmStartPage extends Activity
{
    AlarmManager alrmMgr;
    private PendingIntent pendInt;
    private TimePicker alrmTimePicker;
    private static AlarmStartPage inst;

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
    }

    public void onToggleClicked(View view)
    {
        if (((ToggleButton) view).isChecked())
        {
            Log.d("MyActivity", "Alarm On!");
            Calendar calendar = Calendar.getInstance();
            if (Build.VERSION.SDK_INT >= 23)
            {
                calendar.set(Calendar.HOUR_OF_DAY, alrmTimePicker.getHour());
                calendar.set(Calendar.MINUTE, alrmTimePicker.getMinute());
            }
            else
            {
                calendar.set(Calendar.HOUR_OF_DAY, alrmTimePicker.getCurrentHour());
                calendar.set(Calendar.MINUTE, alrmTimePicker.getCurrentMinute());
            }
            Intent myIntent = new Intent(AlarmStartPage.this, AlarmReceiver.class);
            pendInt = PendingIntent.getBroadcast(AlarmStartPage.this, 0, myIntent, 0);

            alrmMgr.set(AlarmManager.RTC, calendar.getTimeInMillis(), pendInt);
        }
        else
        {
            alrmMgr.cancel(pendInt);
            //setAlarmText("");
            Log.d("MyActivity", "Alarm OFF");
        }
    }




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
/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
/*public class AlarmStartPage extends AppCompatActivity {
    *//**
     * Whether or not the system UI should be auto-hidden after
     * {@link #AUTO_HIDE_DELAY_MILLIS} milliseconds.
     *//*
    private static final boolean AUTO_HIDE = true;

    *//**
     * If {@link #AUTO_HIDE} is set, the number of milliseconds to wait after
     * user interaction before hiding the system UI.
     *//*
    private static final int AUTO_HIDE_DELAY_MILLIS = 3000;

    *//**
     * Some older devices needs a small delay between UI widget updates
     * and a change of the status and navigation bar.
     *//*
    private static final int UI_ANIMATION_DELAY = 300;
    private final Handler mHideHandler = new Handler();
    //private View mContentView;
   private final Runnable mHidePart2Runnable = new Runnable() {
        @SuppressLint("InlinedApi")
        @Override
        public void run() {
            // Delayed removal of status and navigation bar

            // Note that some of these constants are new as of API 16 (Jelly Bean)
            // and API 19 (KitKat). It is safe to use them, as they are inlined
            // at compile-time and do nothing on earlier devices.
            *//*mContentView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LOW_PROFILE
                    | View.SYSTEM_UI_FLAG_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                    | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                    | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION); *//*
        }
    };
    private View mControlsView;
    private final Runnable mShowPart2Runnable = new Runnable() {
        @Override
        public void run() {
            // Delayed display of UI elements
            ActionBar actionBar = getSupportActionBar();
            if (actionBar != null) {
                actionBar.show();
            }
            mControlsView.setVisibility(View.VISIBLE);
        }
    };
    private boolean mVisible;
    private final Runnable mHideRunnable = new Runnable() {
        @Override
        public void run() {
            hide();
        }
    };
    *//**
     * Touch listener to use for in-layout UI controls to delay hiding the
     * system UI. This is to prevent the jarring behavior of controls going away
     * while interacting with activity UI.
     *//*
    private final View.OnTouchListener mDelayHideTouchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            if (AUTO_HIDE) {
                delayedHide(AUTO_HIDE_DELAY_MILLIS);
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_alarm_start_page);

        mVisible = true;
        mControlsView = findViewById(R.id.fullscreen_content_controls);
        //mContentView = findViewById(R.id.fullscreen_content);


        // Set up the user interaction to manually show or hide the system UI.
        //mContentView.setOnClickListener(new View.OnClickListener() {
         //   @Override
          //  public void onClick(View view) {
          //      toggle();
          //  }
        //});

        // Upon interacting with UI controls, delay any scheduled hide()
        // operations to prevent the jarring behavior of controls going away
        // while interacting with the UI.
        findViewById(R.id.remCreateButton).setOnTouchListener(mDelayHideTouchListener);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

        // Trigger the initial hide() shortly after the activity has been
        // created, to briefly hint to the user that UI controls
        // are available.
        delayedHide(100);
    }

    private void toggle() {
        if (mVisible) {
            hide();
        } else {
            show();
        }
    }

    private void hide() {
        // Hide UI first
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }
        mControlsView.setVisibility(View.GONE);
        mVisible = false;

        // Schedule a runnable to remove the status and navigation bar after a delay
        mHideHandler.removeCallbacks(mShowPart2Runnable);
        mHideHandler.postDelayed(mHidePart2Runnable, UI_ANIMATION_DELAY);
    }

    @SuppressLint("InlinedApi")
    private void show() {
        // Show the system bar
     *//*   mContentView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION);*//*
        mVisible = true;

        // Schedule a runnable to display UI elements after a delay
        mHideHandler.removeCallbacks(mHidePart2Runnable);
        mHideHandler.postDelayed(mShowPart2Runnable, UI_ANIMATION_DELAY);
    }

    *//**
     * Schedules a call to hide() in [delay] milliseconds, canceling any
     * previously scheduled calls.
     *//*
    private void delayedHide(int delayMillis) {
        mHideHandler.removeCallbacks(mHideRunnable);
        mHideHandler.postDelayed(mHideRunnable, delayMillis);
    }
}*/
