package com.sahilmgandhi.remmy;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.media.Ringtone;
import android.media.RingtoneManager;
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

        //this will sound the alarm tone
        //this will sound the alarm once, if you wish to
        //raise alarm in loop continuously then use MediaPlayer and setLooping(true)
        Uri alarmUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);
        if (alarmUri == null) {
            alarmUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        }
        Ringtone ringtone = RingtoneManager.getRingtone(context, alarmUri);
        ringtone.play();

        //this will send a notification message
        ComponentName comp = new ComponentName(context.getPackageName(),
                AlarmService.class.getName());
        startWakefulService(context, (intent.setComponent(comp)));
        setResultCode(Activity.RESULT_OK);
    }

    //@Override
    /*public void onReceive(Context context, Intent intent)
    {
        PendingIntent sender = PendingIntent.getBroadcast(context, 0, intent, 0);
        NotificationManager manager =    (NotificationManager)context.getSystemService(android.content.Context.NOTIFICATION_SERVICE);
        Notification wakeUP = new Notification(android.R.drawable.stat_notify_more, "Wake up alarm", System.currentTimeMillis());
        wakeUP.flags = Notification.FLAG_AUTO_CANCEL;
        manager.notify(R.string.app_name, wakeUP);

        //intent to call the activity which shows on ringing
        Intent myIntent = new Intent(context, AlarmStartPage.class);
        myIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(myIntent);

        //display that alarm is ringing
        Toast.makeText(context, "Alarm Ringing...!!!", Toast.LENGTH_LONG).show();
    }*/



}
