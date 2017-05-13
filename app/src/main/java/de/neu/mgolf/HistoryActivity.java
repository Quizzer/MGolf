package de.neu.mgolf;

import android.app.AlertDialog;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Spinner;

import java.util.ArrayList;
import java.util.Date;

import de.neu.mgolf.database.GameColumns;
import de.neu.mgolf.database.MGolfDB;

import static de.neu.mgolf.R.id.lstNames;
import static de.neu.mgolf.R.id.time;

public class HistoryActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {

    private SQLiteDatabase database;
    private Cursor cursor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        if (BuildConfig.DEBUG) {
            Log.d(Constants.TAG, "onCreate: HistoryActivity");
        }

        ListView lstGames = (ListView) findViewById(R.id.lstGames);

        // Datenbehälter einem Adapter geben und diesen der Liste geben
        // String[] games = getResources().getStringArray(R.array.locations);
        // ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, games); // ...list_item_1-einzeilig, ...list_item_2-zweizeilig

        // Datenbank abfragen
        database = new MGolfDB(this).getReadableDatabase(); // ToDo: oder WriteableDatabase()
        cursor = database.query(GameColumns.TABLE, GameColumns.PROJECTION, null, null, null, null, null);

        // Adapter bauen
        String[] from = {GameColumns.LOCATION, GameColumns.NAMES};
        int[] to = {android.R.id.text1, android.R.id.text2};

        SimpleCursorAdapter adapter = new SimpleCursorAdapter(this, android.R.layout.simple_list_item_2, cursor, from, to, 0);
        lstGames.setAdapter(adapter);

        lstGames.setOnItemClickListener(this);
    }

    @Override
    protected void onDestroy() {
        cursor.close();
        database.close();

        super.onDestroy();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        // Anzuzeigende Daten aus Cursor holen
        cursor.moveToPosition(position);
        String names = cursor.getString(cursor.getColumnIndex(GameColumns.NAMES));
        String location = cursor.getString(cursor.getColumnIndex(GameColumns.LOCATION));
        long timestamp = cursor.getLong(cursor.getColumnIndex(GameColumns.TIMESTAMP));

        Log.d(Constants.TAG, "onItemClick: timestamp from DB" + timestamp);

        // Test-Formatierung DateUtils TimeSpanString
        long now = System.currentTimeMillis();
        long in2weeks = now + DateUtils.WEEK_IN_MILLIS*2;
        long tomorrow = now + DateUtils.DAY_IN_MILLIS;
        long in17sec = now + DateUtils.SECOND_IN_MILLIS*17;
        long before2sec = now - DateUtils.SECOND_IN_MILLIS*2;
        long before5min = now - DateUtils.MINUTE_IN_MILLIS*5;
        long before7hours = now - DateUtils.HOUR_IN_MILLIS*7;
        long yesterday = now - DateUtils.DAY_IN_MILLIS;
        long theDayBeforeYesterday = yesterday - DateUtils.DAY_IN_MILLIS;
        long lastWeek = now - DateUtils.WEEK_IN_MILLIS;
        long lastYear = now - DateUtils.YEAR_IN_MILLIS;
        long lastYearPlus1 = now - (DateUtils.YEAR_IN_MILLIS + DateUtils.DAY_IN_MILLIS);
        long[] times = {in2weeks, tomorrow, in17sec, now, before2sec, before5min, before7hours, yesterday, theDayBeforeYesterday, lastWeek, lastYear, lastYearPlus1};

        //String timeString = "";
        for (long time: times) {
            CharSequence relativeTimeSpanString = DateUtils.getRelativeTimeSpanString(time, now, 0);
            //timeString += relativeTimeSpanString + " - ";
        }

        // Formatieren
        // ToDo: ordentliche Time-Formatierung (Anpassung Präfix "before", andere Sprachen, Wechsel von relativer zu fixer Zeitangabe)
        CharSequence timeString = DateUtils.getRelativeTimeSpanString(timestamp, now, 0);
        String message = getResources().getString(R.string.historyDialog, timeString, location, names);

        // Dialog bauen und anzeigen
        new AlertDialog.Builder(this)
                .setMessage(message)
                .setPositiveButton(android.R.string.ok, null)
                .create()
                .show();
    }
}
