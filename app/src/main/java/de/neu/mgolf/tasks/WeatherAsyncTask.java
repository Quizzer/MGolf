package de.neu.mgolf.tasks;

import android.content.Context;
import android.os.AsyncTask;
import android.os.SystemClock;
import android.text.format.DateUtils;
import android.widget.Toast;

public class WeatherAsyncTask extends AsyncTask<String, Void, String> {

    private Context context;

    public WeatherAsyncTask(Context context) {
        this.context = context;
    }

    @Override
    protected String doInBackground(String... params) {

        String location = params[0];
        SystemClock.sleep(DateUtils.SECOND_IN_MILLIS*3); // Keine Exception, im Gegensatz zu Thread.sleep()

        //Lookup

        //Meldung bauen
        String message = String.format("Das Wetter in %s: ...", location);

        return message;
    }

    @Override
    protected void onPostExecute(String message) {
        // Im UI-Thread
        Toast.makeText(context, message, Toast.LENGTH_LONG).show();
    }
}
