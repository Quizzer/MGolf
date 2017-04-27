package de.neu.mgolf.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.preference.PreferenceManager;

import de.neu.mgolf.Constants;
import de.neu.mgolf.service.UploadService;

public class ConnectivityChangedReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {

        // Ist etwas zu tun? (Preferences auslesen)
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        if (prefs.getBoolean(Constants.UPLOAD_REQUIRED, false)) {

            // Kann man etwas tun? (Nezwerkinfo)
            // Prüfen ob Netzwerkverbindung vorhanden
            ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

            if (networkInfo != null && networkInfo.isConnected()) {

                // Jemand anderes soll etwas tun (Intent-Service)
                context.startService(new Intent(context, UploadService.class));
            }
        }
    }
}
