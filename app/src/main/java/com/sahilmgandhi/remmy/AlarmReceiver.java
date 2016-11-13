package com.sahilmgandhi.remmy;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.provider.MediaStore;
import android.support.v4.content.WakefulBroadcastReceiver;
import android.widget.Toast;
import android.app.Activity;
import android.net.Uri;

/**
 * Created by Sahil on 3/23/2016.
 */
public class AlarmReceiver extends WakefulBroadcastReceiver
{
    @Override
    public void onReceive(final Context context, Intent intent) {

        Uri alarmUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);               //this will sound the alarm tone

        if (alarmUri == null){
            alarmUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        }

        Ringtone ringtone = RingtoneManager.getRingtone(context, alarmUri);

        ringtone.play();                                                                        // plays the ringtone of the phone as the alarm

        ComponentName comp = new ComponentName(context.getPackageName(), AlarmService.class.getName());
        startWakefulService(context, (intent.setComponent(comp)));                              // sends the notification message and wakes up the phone
        setResultCode(Activity.RESULT_OK);
    }
}
