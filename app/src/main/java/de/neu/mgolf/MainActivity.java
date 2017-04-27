package de.neu.mgolf;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (BuildConfig.DEBUG) {
            Log.d(Constants.TAG, "onCreate: MainActivity");
        }

    }

    public void onClick_search(View view) {
        Log.d(Constants.TAG, "onClick_Main_search: ");

        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("geo:0,0?q=Minigolf"));
        startActivityForResult(intent, 0);
    }

//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        if (resultCode == RESULT_OK) {
//            Uri ui = data.getData();
//
//        }
//    }

    public void onClick_play(View view) {
        if (BuildConfig.DEBUG) {
            Log.d(Constants.TAG, "onClick_Main_play: ");
        }

        Intent intent = new Intent(this, PlayersActivity.class);
        startActivity(intent);
    }

    public void onClick_history(View view) {
        Log.d(Constants.TAG, "onClick_Main_history: ");

        Intent intent = new Intent(this, HistoryActivity.class);
        startActivity(intent);
    }

    public void onClick_training(View view) {
        Log.d(Constants.TAG, "onClick_Main_training: ");

        Intent intent = new Intent(this, TrainingActivity.class);
        startActivity(intent);
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    public void onClickSettings(MenuItem item) {
        Log.d(Constants.TAG, "onClickSettings: MenuItem");

        Intent intent = new Intent(this, SettingsActivity.class);
        startActivity(intent);

    }
}
