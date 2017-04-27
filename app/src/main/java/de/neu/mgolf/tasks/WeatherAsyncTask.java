package de.neu.mgolf.tasks;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.SystemClock;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.text.format.DateUtils;
import android.util.Log;
import android.widget.Toast;

import org.apache.commons.io.IOUtils;
import org.json.JSONObject;

import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.Locale;

import de.neu.mgolf.Constants;
import de.neu.mgolf.R;

public class WeatherAsyncTask extends AsyncTask<String, Void, String> {

    private Context context;

    public WeatherAsyncTask(Context context) {
        this.context = context;
    }

    private static final String APPID = "111620814d33ea8a7a560b7c7823bf21";
    private static String url = "http://api.openweathermap.org/data/2.5/weather?q=%s&units=%s&lang=%s&appid=%s";

    @Override
    protected String doInBackground(String... params) {

        String location = params[0];

        try {
            String encodedLocation = URLEncoder.encode(location, "UTF8");

            //  Todo: get units from SharedPreferences
            // SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
            // prefs.getBoolean(Constants.UNITS, false);

            String units = "metric"; // Todo: Auslesen aus SharedPreferences
            // Auslesen der Locale Default Language
            String lang = Locale.getDefault().getLanguage();

            String requestUrl = String.format("http://api.openweathermap.org/data/2.5/weather?" +
                    "q=%s" +
                    "&units=%s" +
                    "&lang=%s" +
                    "&appid=%s", encodedLocation, units, lang, APPID);
            Log.i(Constants.TAG, "doInBackground: url = " + requestUrl);

            //Lookup
            URLConnection urlConnection = new URL(requestUrl).openConnection();
            InputStream inputStream = urlConnection.getInputStream();

            // Apache Commons IO-Utils Bibliothek die ermöglicht jeden Stream als einzeiligen String zu erhalten
            // https://bintray.com/bintray/jcenter/commons-io:commons-io
            String json = IOUtils.toString(inputStream);
            inputStream.close();

            //Meldung bauen
            JSONObject jsonObject = new JSONObject(json);
            String description = jsonObject.getJSONArray("weather").getJSONObject(0).getString("description");
            double temp = jsonObject.getJSONObject("main").getDouble("temp");

            String message = String.format("Das Wetter in %s: %s", location, description + ", " + Math.round(temp) + "°C");

            return message;

        } catch (Exception e) {
            Log.e(Constants.TAG, "doInBackground: ", e);
            return "Fehler: " + e.getMessage();
        }
    }

    @Override
    protected void onPostExecute(String message) {
        // Im UI-Thread
        Toast.makeText(context, message, Toast.LENGTH_LONG).show();
    }
}
