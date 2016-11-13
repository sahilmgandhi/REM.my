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
import android.util.Log;

/**
 * Created by Sahil on 3/23/2016.
 */
public class AlarmReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {

        Uri alarmUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);               //this will sound the alarm tone
        Log.d("Creating Alarm", "Used ALARM for ringtone " + alarmUri);
        System.out.println("logging that it got to this part");
        if (alarmUri == null) {
            Log.d("Creating Alarm", "Used the notification instead of alarm for ringtone");
            alarmUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        }

        Ringtone ringtone = RingtoneManager.getRingtone(context, alarmUri);

        ringtone.play();                                                                        // plays the ringtone of the phone as the alarm

        Intent service_intent = new Intent(context, AlarmService.class);
        context.startService(service_intent);
        //ComponentName comp = new ComponentName(context.getPackageName(), AlarmService.class.getName());
        //startWakefulService(context, (intent.setComponent(comp)));                              // sends the notification message and wakes up the phone
        setResultCode(Activity.RESULT_OK);
    }
}
