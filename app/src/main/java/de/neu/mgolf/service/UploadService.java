package de.neu.mgolf.service;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.RingtoneManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.SystemClock;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.text.format.DateUtils;
import android.util.Log;

import de.neu.mgolf.Constants;
import de.neu.mgolf.HistoryActivity;

public class UploadService extends IntentService {

    public UploadService() {
        super("UploadService");
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        if (!prefs.getBoolean(Constants.UPLOAD_REQUIRED, false)) {
            return;
        }

        String location = intent.getStringExtra("location");

        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this);
        builder.setContentTitle("Upload");
        builder.setSmallIcon(android.R.drawable.stat_sys_upload);
        builder.setAutoCancel(true);

        // Simulierte Aktion, die 10 Sek dauert und nach je 1 Sek den Fortschritt nennt
        for (int i = 0; i < 10; i++) {
            SystemClock.sleep(DateUtils.SECOND_IN_MILLIS/3); // wait 1 second
//            String message = String.format("Upload-Fortschritt für %s: %d %", location, i*10);
            String message = "Upload-Fortschritt für " + location + ": " + i * 10 + "%";
            Log.d(Constants.TAG, "onHandleIntent: " + message);

            builder.setContentText(message);
            Notification notification = builder.build();
            notificationManager.notify(1, notification);
        }

        Log.d(Constants.TAG, "onHandleIntent: ERFOLGREICH HOCHGELADEN");

        // ToDo: PendingIntent bauen und dem builder mitgeben
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, new Intent(this, HistoryActivity.class), PendingIntent.FLAG_UPDATE_CURRENT);
        builder.setContentIntent(pendingIntent);

        builder.setContentText("Erfolgreich hochgeladen in " + location);
        builder.setSmallIcon(android.R.drawable.stat_sys_upload_done);
        builder.setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION));

        Notification notification = builder.build();
        notificationManager.notify(1, notification);

        // Info ablegen, dass jetzt kein Upload mehr nachzuholen ist
        prefs.edit().putBoolean(Constants.UPLOAD_REQUIRED, false).apply();
    }

    @Override
    public void onTaskRemoved(Intent rootIntent) {
        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        notificationManager.cancel(1);
        super.onTaskRemoved(rootIntent);
    }
}
