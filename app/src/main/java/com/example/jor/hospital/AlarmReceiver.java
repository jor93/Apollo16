package com.example.jor.hospital;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;

/**
 * Created by jor on 01.04.2016.
 */
public class AlarmReceiver extends BroadcastReceiver{
    // alarm data
    public static int id;
    public static String s;
    public static long time;
    public static long [] notValues = {300000,600000,900000,1200000,1500000,1800000,2700000,3600000,7200000,10800000,43200000,86400000};

    @Override
    public void onReceive(Context context, Intent intent) {
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        // create intent for return
        Intent notificationIntent = new Intent(context, Home.class);
        notificationIntent.putExtra("doctor_id", IdCollection.doctor_id);
        notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        // actual intent
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        // get system sound
        Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        // create notification with data
        NotificationCompat.Builder mNotifyBuilder = new NotificationCompat.Builder(context)
                .setSmallIcon(R.drawable.ic_warning_24dp)
                .setContentTitle("Your patient event is starting soon")
                .setContentText(s)
                .setSound(alarmSound)
                .setAutoCancel(true)
                .setWhen(time)
                .setContentIntent(pendingIntent)
                .setVibrate(new long[]{1000, 1000, 1000, 1000, 1000});
        notificationManager.notify(id, mNotifyBuilder.build());
    }
}
