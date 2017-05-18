package de.neu.mgolf.tasks;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.Toast;

import org.apache.commons.io.IOUtils;
import org.json.JSONObject;

import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Locale;

import de.neu.mgolf.Constants;
import de.neu.mgolf.R;

public class WeatherAsyncTask extends AsyncTask<String, Void, String> {

    private Context context;

    public WeatherAsyncTask(Context context) {
        this.context = context;
    }

    @Override
    protected String doInBackground(String... params) {

        String location = params[0];
        HashMap<String, String> temp_units = new HashMap<>();
        temp_units.put("imperial", "°F");
        temp_units.put("metric","°C");
        temp_units.put("kelvin", " K");

        try {
            String encodedLocation = URLEncoder.encode(location, "UTF8");

            // get units from Shared Preferences
            SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);

            String units = prefs.getString(context.getResources().getString(R.string.units_key), context.getResources().getString(R.string.metric_key));

            // Auslesen der Locale Default Language
            String lang = Locale.getDefault().getLanguage();

            String url = "http://api.openweathermap.org/data/2.5/weather?q=%s&units=%s&lang=%s&appid=%s";
            String requestUrl = String.format(url, encodedLocation, units, lang, Constants.APPID);
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

            return context.getResources().getString(R.string.weather_text, location, description, Math.round(temp) + temp_units.get(units));

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
