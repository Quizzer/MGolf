package de.neu.mgolf;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;

import de.neu.mgolf.database.GameColumns;
import de.neu.mgolf.database.MGolfDB;

public class PlayersActivity extends AppCompatActivity implements TextWatcher {

    public static final String NAMES = "names";
    private Spinner spnLocation;
    private EditText edtName;
    private Button btnAdd;
    private ArrayList<String> names;
    private ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play);

        if (BuildConfig.DEBUG) {
            Log.d(Constants.TAG, "onCreate: PlayersActivity");
        }

        // View-Holder-Pattern: Alle View-Referenzen vorab sammeln und halten
        spnLocation = (Spinner) findViewById(R.id.spnLocation);
        edtName = (EditText) findViewById(R.id.edtName);
        ListView lstNames = (ListView) findViewById(R.id.lstNames);
        btnAdd = (Button) findViewById(R.id.btnAdd);
        btnAdd.setEnabled(false);

        // Datenbehälter einem Adapter geben und diesen der Liste geben
        if(savedInstanceState != null) {
            names = savedInstanceState.getStringArrayList(NAMES);
        } else {
            names = new ArrayList<String>();
        }

        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, names); // ...list_item_1-einzeilig, ...list_item_2-zweizeilig
        lstNames.setAdapter(adapter);

        // Listener für Änderungen im Namensfeld
        edtName.addTextChangedListener(this);

    }

    public void onClick_Questionmark(View view) {
        if (BuildConfig.DEBUG) {
            Log.d(Constants.TAG, "onClick: Questionmark");
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            Uri uri = data.getData();
            Log.d(Constants.TAG, "onActivityResult: getData = " + uri);

            String[] projection = {ContactsContract.Contacts.DISPLAY_NAME}; // Columns aus der Datenbank
            Cursor cursor = getContentResolver().query(uri, projection, null, null, null);
            if (cursor != null && cursor.moveToFirst()) {
                String name = cursor.getString(0);
                Log.d(Constants.TAG, "onActivityResult:" + name);
                names.add(name);
                adapter.notifyDataSetChanged();
                // ToDo: Freature Change and Delete listed players, duplicate contacts?
                cursor.close();
            }

        }
    }

    @Override
    protected void onSaveInstanceState(Bundle bundle) {
        super.onSaveInstanceState(bundle);

        bundle.putStringArrayList(NAMES, names);
    }

    public void onClick_Ellipsis(View view) {
        if (BuildConfig.DEBUG) {
            Log.d(Constants.TAG, "onClick: Ellipsis");
        }

        // Zu Kontakte-App wechseln und Kontakt auswählen lassen
        Intent intent = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
        startActivityForResult(intent, 0);
    }

    public void onClick_add(View view) {
        if (BuildConfig.DEBUG) {
            Log.d(Constants.TAG, "onClick: add");
        }
        // Name auslesen
        String name = edtName.getText().toString().trim();
        // Name und in Liste anzeigen, Adapter der Liste über Änderung informieren, Eingabefeld leeren
        if (!TextUtils.isEmpty(name)) { // checkt null oder "" ohne Gefahr einer NullPointer
            names.add(name);
            adapter.notifyDataSetChanged();
            // ToDo: updated item to top of list?
            edtName.setText("");
        }
    }

    public void onClick_ok(View view) {
        if (BuildConfig.DEBUG) {
            Log.d(Constants.TAG, "onClick: add");
        }
        // Daten für Datenbank sammeln
        String location = (String) spnLocation.getSelectedItem();
        String nameString = TextUtils.join(", ", names);
        long timestamp = System.currentTimeMillis() - DateUtils.DAY_IN_MILLIS*365;

        Log.d(Constants.TAG, "onClick_ok: " + location + " - " + nameString + " - " + timestamp);

        // Daten in Datenbank einfügen
        ContentValues values = new ContentValues();
        values.put(GameColumns.LOCATION, location);
        values.put(GameColumns.NAMES, nameString);
        values.put(GameColumns.TIMESTAMP, timestamp);

        SQLiteDatabase database = new MGolfDB(this).getWritableDatabase();
        long id = database.insert(GameColumns.TABLE, null, values); // oder: insertOrThrow, um Exception im Fehlerfall zu erhalten.
        if (id > 0) {
            Toast.makeText(this, "Datensatz erzeugt mit id=" + id, Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Es ist ein Fehler aufgetreten.", Toast.LENGTH_SHORT).show();
        }
        database.close();
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        btnAdd.setEnabled(!TextUtils.isEmpty(s.toString().trim()));
    }
}