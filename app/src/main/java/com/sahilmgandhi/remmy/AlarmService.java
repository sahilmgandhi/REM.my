package com.sahilmgandhi.remmy;

/**
 * Created by Sahil on 3/23/2016.
 */

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.Toast;

public class AlarmService extends IntentService {
    private NotificationManager alarmNotificationManager;
    Ringtone ringtone;

    public AlarmService() {
        super("AlarmService");
    }

    @Override
    public void onHandleIntent(Intent intent) {
        sendNotification("Wake Up! Your alarm has been rung!!!!");                      // sends the notification to the phone that the alarm is ringing
    }

    private void sendNotification(String msg) {
        Log.d("AlarmService", "Preparing to send notification...: " + msg);
        alarmNotificationManager = (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);

        PendingIntent contentIntent = PendingIntent.getActivity(this, 0, new Intent(this, AlarmStartPage.class), 0);                        // creates the notification and sets the icon for the notification

        Uri soundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);
        NotificationCompat.Builder alarmNotificationBuilder = new NotificationCompat.Builder(
                this)
                .setContentTitle("Alarm")
                .setSmallIcon(R.mipmap.ic_launcher)
                .setStyle(new NotificationCompat.BigTextStyle().bigText(msg))
                .setContentText(msg)
                .setVibrate(new long[] {1000,1000})
                .setSound(soundUri);

        alarmNotificationBuilder.setContentIntent(contentIntent);

        Notification note = alarmNotificationBuilder.build();
        note.flags |= Notification.FLAG_INSISTENT;
        note.flags |= Notification.FLAG_AUTO_CANCEL;
        note.flags |= Notification.VISIBILITY_PUBLIC;
        note.flags |= Notification.PRIORITY_MAX;

        alarmNotificationManager.notify(1, note);
    }
}
