package com.sahilmgandhi.remmy;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.SystemClock;

/**
 * Created by Sahil on 3/23/2016.
 */
public class OnBootReceiver extends BroadcastReceiver {
    private static final int WAITING_PERIOD = 10000;    // 10 seconds (aka 10000 milliseconds)

    @Override
    public void onReceive(Context context, Intent intent)
    {
        AlarmManager aMgr = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
        Intent i = new Intent(context, AlarmReceiver.class);
        PendingIntent pI = PendingIntent.getBroadcast(context, 0, i, 0);

        aMgr.setRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP, SystemClock.elapsedRealtime(), WAITING_PERIOD, pI);
    }

}
