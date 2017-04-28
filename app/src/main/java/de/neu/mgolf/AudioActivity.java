package de.neu.mgolf;

import android.app.AlertDialog;
import android.content.ContentUris;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

import de.neu.mgolf.database.GameColumns;
import de.neu.mgolf.database.MGolfDB;

public class AudioActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {

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

        // Content-Provider abfragen
        String[] projection = {MediaStore.Audio.Media._ID, MediaStore.Audio.Media.TITLE, MediaStore.Audio.Media.ARTIST};

        // MediaStore.Audio.MediaEXTERNAL_CONTENT_URI
        cursor = getContentResolver().query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, projection, null, null, null);


        // Adapter bauen
        String[] from = {MediaStore.Audio.Media.TITLE, MediaStore.Audio.Media.ARTIST};
        int[] to = {android.R.id.text1, android.R.id.text2};

        SimpleCursorAdapter adapter = new SimpleCursorAdapter(this, android.R.layout.simple_list_item_2, cursor, from, to, 0);
        lstGames.setAdapter(adapter);

        lstGames.setOnItemClickListener(this);
    }

    @Override
    protected void onDestroy() {
        cursor.close();

        super.onDestroy();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        Uri uri = ContentUris.withAppendedId(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, id);
        // alternativ:
        // Uri uri = Uri.withAppendedPath(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, "" + id);
        startActivity(new Intent(Intent.ACTION_VIEW, uri));
    }
}
