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

        AlarmStartPage inst = AlarmStartPage.instance();
        inst.setAlarmText("Alarm is ringing!! Wake up!!");

        //this will sound the alarm tone
        //this will sound the alarm once, if you wish to
        //raise alarm in loop continuously then use MediaPlayer and setLooping(true)
        Uri alarmUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);
        if (alarmUri == null) {
            alarmUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        }
        Ringtone ringtone = RingtoneManager.getRingtone(context, alarmUri);
        /*final AudioManager audioMgr = (AudioManager)context.getSystemService(Context.AUDIO_SERVICE);
        int currVol = audioMgr.getStreamVolume(AudioManager.STREAM_RING);

        int maxVol = audioMgr.getStreamMaxVolume(AudioManager.STREAM_RING);

        audioMgr.setStreamVolume(AudioManager.STREAM_RING, AudioManager.ADJUST_RAISE, (maxVol-currVol));*/

        ringtone.play();

        //this will send a notification message
        ComponentName comp = new ComponentName(context.getPackageName(), AlarmService.class.getName());
        startWakefulService(context, (intent.setComponent(comp)));
        setResultCode(Activity.RESULT_OK);
    }
}
