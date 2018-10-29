package com.sahilmgandhi.remmy;

import android.annotation.SuppressLint;
import android.app.Activity;                            // all the imports that my application needs to run smoothly
import android.app.AlarmManager;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.text.format.Time;
import android.view.View;
import android.widget.TimePicker;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Calendar;

import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by Sahil on 3/23/2016.
 */

public class AlarmStartPage extends Activity implements TimeSetListener {
    private AlarmManager alarmManager;
    private PendingIntent pendInt;                          // initialize all the private member variables required for the app
    private static AlarmStartPage inst;
    private Intent myIntent;
    private TextView alarmStatusView;
    private FloatingActionButton startFab;
    private FloatingActionButton deletePrevAlarmFab;
    private TimePickerFragment timeFragment;

    private Calendar calendar;
    private int hourToSet;
    private int minuteToSet;
    private boolean alarmSet = false;

    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;

    protected static AlarmStartPage instance() {
        return inst;                                        // returns an instance of the current Activity
    }

    @Override
    public void onStart() {
        super.onStart();                                    // calls the super classes onStart, and then sets the instance to the current one
        inst = this;
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm_start_page);                             // sets the various buttons and other containers on the website
        alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        alarmStatusView = (TextView) findViewById(R.id.alarmStatus);
        calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());

        TextView instructions = (TextView) findViewById(R.id.Instructions);
        instructions.setText(R.string.instructionsText);

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        editor = sharedPreferences.edit();
        restoreSavedSharedPreferences();

        deletePrevAlarmFab = (FloatingActionButton) findViewById(R.id.floatingActionButton2);
        deletePrevAlarmFab.setEnabled(alarmSet);
        deletePrevAlarmFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deletePrevAlarm();
            }
        });

        startFab = (FloatingActionButton) findViewById(R.id.floatingActionButton1);
        startFab.setEnabled(!alarmSet);
        startFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showTimePickerDialog();
            }
        });

        setVolumeControlStream(AudioManager.STREAM_ALARM);                              // sets the volume to be controlled to the audiomanager so that the user can control the alarm's volume
    }

    public void showTimePickerDialog() {
        timeFragment = new TimePickerFragment();
        timeFragment.setListener(this);
        timeFragment.show(getFragmentManager(), "timePicker");
    }

    public void setTime(int selectedHour, int selectedMinute){
        minuteToSet = selectedMinute;
        hourToSet = selectedHour;
        alarmSet = true;
        startFab.setEnabled((!alarmSet));
        deletePrevAlarmFab.setEnabled(alarmSet);
        addAlarmSetToSharedPreferences(alarmSet);
        setAlarm();
    }

    private void setAlarm() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());

        // this is the code to actually do the "magic" of the REM time
        int currhr = calendar.get(Calendar.HOUR_OF_DAY);                        // gets the current time from the system's clock
        int currmin = calendar.get(Calendar.MINUTE);

        boolean lessThan90 = false;                                             // boolean to check if the current alarm is less than 90 Minutes away (1 REM cycle)
        int hrDiff = 0;
        int minDiff = 0;

        if (hourToSet >= currhr) {
            hrDiff = hourToSet - currhr;                                        // calculating the difference between the current hour and the hour of the alarm to get the difference in the time
            if (hrDiff == 0) {
                if (minuteToSet > currmin)                                      // if the alarm is for after the current time, but same hour, then it is less than 1 hour away
                    minDiff = minuteToSet - currmin;
                else {
                    hrDiff = 23;                                                // otherwise the alarm us for more than 23 hours away (same hour, but earlier time)
                    minDiff = 60 - (currmin - minuteToSet);
                }
            } else {
                if (minuteToSet > currmin)
                    minDiff = minuteToSet - currmin;
                else {
                    hrDiff--;
                    minDiff = 60 - (currmin - minuteToSet);
                }
            }

            if (60 * hrDiff + minDiff < 90)                                       // if prior to the 15 min shift, the alarm time is less than 90 minutes away, then it will be set as the alarm time
                lessThan90 = true;
        }

        currmin += 15;                                                            // add 15 min to the current time, and below, change the hour and minute accordingly
        if (currmin >= 60) {
            currmin = currmin % 60;
            currhr++;
            if (currhr >= 24)
                currhr = currhr % 24;
        }
        if (!lessThan90)                                                        // only if the alarm time is more than 90 minutes away, it will try to do this (which it will try to do
        {                                                                       // by defualt since lessThan90 is initalized to false (or it is set to true by the above if else statement
            if (hourToSet >= currhr) {
                hrDiff = hourToSet - currhr;
                if (hrDiff == 0)                                                // same logic as earlier, checks if the same hour as the alarm, then checks if the alarm is before or after the current time
                {
                    if (minuteToSet > currmin)                                  // if the alarm is set for a later time (which means that it is less than 90 minutes away)
                        minDiff = minuteToSet - currmin;
                    else                                                        // otherwise the alarm is set for 23 hours and some minutes away
                    {
                        minDiff = 60 - (currmin - minuteToSet);
                        hrDiff = 23;
                    }
                } else {
                    if (minuteToSet > currmin)
                        minDiff = minuteToSet - currmin;
                    else {
                        hrDiff--;
                        minDiff = 60 - (currmin - minuteToSet);
                    }
                }
            } else if (hourToSet < currhr)                                        // if the alarm time is before the current time (then it must loop over midnight and restart from 0 again)
                hrDiff = 24 - (currhr - hourToSet);
        }

        int totalMinutesInBetween = 60 * hrDiff + minDiff;

        if (totalMinutesInBetween < 90)                                         // if the total minutes between the alarm and the current time (after the 15 min shift) is less than 90 minutes
            lessThan90 = true;                                                  // it is less than 1 REM shift away

        if (!lessThan90)                                                        // If there are more than 90 minutes of difference, then a REM cycle is ACTUALLY possible
        {
            int possibleRem = totalMinutesInBetween / 90;                         // the possible amount of REM cycles between now and the alarm time
            for (int i = 0; i < possibleRem; i++) {
                currhr++;                                                       // the time is altered by 90 minute cycles (looping around after 60 minutes or after 24 hours) to get the appropiate REM time
                if (currhr >= 24)
                    currhr = currhr % 24;
                currmin += 30;
                if (currmin >= 60) {
                    currmin = currmin % 60;                                       // looping the minutes over 60
                    currhr++;
                    if (currhr >= 24)
                        currhr = currhr % 24;                                     // looping the hours after 24 hours
                }
            }
            hourToSet = currhr;
            minuteToSet = currmin;
        }

        calendar.set(Calendar.HOUR_OF_DAY, hourToSet);                          // the calendar sets the final REM time
        calendar.set(Calendar.MINUTE, minuteToSet);
        calendar.set(Calendar.SECOND, 0);

        myIntent = new Intent(this, AlarmReceiver.class);
        myIntent.putExtra("Hours", String.valueOf(hourToSet));
        myIntent.putExtra("Minutes", String.valueOf(minuteToSet));
        //pendInt = PendingIntent.getBroadcast(this, 0, myIntent, 0);             // new intent as well as a pending intent to notify the system of the alarm (uses Alarm Receiver and Alarm Service)
        pendInt = PendingIntent.getBroadcast(this, AlarmReceiver.REQUEST_CODE, myIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        addIntentToSharedPreferences(myIntent);
        alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendInt);                     // alarmmanager is used to set the alarm

        int properHourTime = calendar.get(Calendar.HOUR);
        boolean isMorning = true;
        if (hourToSet > 11)
            isMorning = false;
        String alarmText;
        if (minuteToSet > 9)
            alarmText = properHourTime + ":" + minuteToSet;
        else
            alarmText = properHourTime + ":0" + minuteToSet;
        if (isMorning)
            alarmText += " AM";
        else
            alarmText += " PM";

        setAlarmText(alarmText);
        addAlarmToSharedPreferences(hourToSet, minuteToSet, alarmText);
    }

    private void deletePrevAlarm() {
        if (alarmSet) {
            alarmManager.cancel(pendInt);                                                //cancels the current Intent (effectively stopping the alarm)
            stopService(myIntent);
            String hourText = myIntent.getExtras().getString("Hours");
            String minuteText = myIntent.getExtras().getString("Minutes");
            Toast.makeText(this, "Canceled the alarm at " + hourText + ":" + minuteText, Toast.LENGTH_LONG).show();
            alarmStatusView.setText(R.string.initAlarmText);                       // changes the text on the textbox under the time picker

            alarmSet = false;
            startFab.setEnabled(!alarmSet);
            deletePrevAlarmFab.setEnabled(alarmSet);
            addAlarmSetToSharedPreferences(alarmSet);
        }
    }

    private void setAlarmText(String textToShow) {
        alarmStatusView.setText(textToShow);
    }

    private void restoreSavedSharedPreferences() {
        alarmSet = sharedPreferences.getBoolean("alarmSet", false);
        hourToSet = sharedPreferences.getInt("HourToSet", 0);
        minuteToSet = sharedPreferences.getInt("MinuteToSet", 0);
        String uriString = sharedPreferences.getString("Intent", null);

        if (alarmSet) {
            try {
                myIntent = Intent.parseUri(uriString,0);
                pendInt = PendingIntent.getBroadcast(this, AlarmReceiver.REQUEST_CODE, myIntent, PendingIntent.FLAG_UPDATE_CURRENT);
                String textToShow = sharedPreferences.getString("textToShow", null);
                alarmStatusView.setText(textToShow);
            } catch (URISyntaxException e) {
                e.printStackTrace();
            }
        }
    }

    private void addAlarmSetToSharedPreferences(boolean alrmSet) {
        editor.putBoolean("alarmSet", alrmSet).apply();
    }

    private void addAlarmToSharedPreferences(int hour, int minute, String textToShow) {
        editor.putInt("HourToSet", hour).apply();
        editor.putInt("MinuteToSet", minute).apply();
        editor.putString("textToShow", textToShow).apply();
    }

    private void addIntentToSharedPreferences(Intent mIntent) {
        editor.putString("Intent", mIntent.toUri(0)).apply();
    }

    @Override
    public void onTimeSet(int selectedHour, int selectedMinute) {
        setTime(selectedHour, selectedMinute);
    }
}

//            Intent openNewAlarm = new Intent(AlarmClock.ACTION_SET_ALARM);
//            openNewAlarm.putExtra(AlarmClock.EXTRA_HOUR, hourToSet);
//            openNewAlarm.putExtra(AlarmClock.EXTRA_MINUTES, minuteToSet);
//            startActivity(openNewAlarm);

//    setAlarmText("An alarm has been placed for " + hourToSet + ":" + minuteToSet + " (in military time). If you shut down" +
//                    " this app, please do not open it again until the alarm that you set is over (otherwise the alarm will reset itself).");    // alarm text is changed to notify the user