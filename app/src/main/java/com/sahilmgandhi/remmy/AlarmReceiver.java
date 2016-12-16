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
public class AlarmReceiver extends WakefulBroadcastReceiver {
    public static final int REQUEST_CODE = 111;

    @Override
    public void onReceive(Context context, Intent intent) {

        Intent service_intent = new Intent(context, AlarmService.class);
        context.startService(service_intent);
        ComponentName comp = new ComponentName(context.getPackageName(), AlarmService.class.getName());
        startWakefulService(context, (intent.setComponent(comp)));                              // sends the notification message and wakes up the phone
        setResultCode(Activity.RESULT_OK);
    }
}
