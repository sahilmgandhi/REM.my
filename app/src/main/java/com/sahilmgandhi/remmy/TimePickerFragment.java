package com.sahilmgandhi.remmy;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.widget.TimePicker;

import java.util.Calendar;

@SuppressLint("ValidFragment")
public class TimePickerFragment extends DialogFragment implements TimePickerDialog.OnTimeSetListener {

    private TimeSetListener timesetlistener;

    public TimePickerFragment(){

    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Create a new instance of DatePickerDialog and return it
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        return new TimePickerDialog(getActivity(), this, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), false);
    }

    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {

        timesetlistener.onTimeSet(selectedHour, selectedMinute);
    }

    public void setListener(TimeSetListener listener) {
        this.timesetlistener = listener;
    }
}