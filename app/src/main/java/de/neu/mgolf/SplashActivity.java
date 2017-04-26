package de.neu.mgolf;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        // Blendet ActionBar aus, falls vorhanden
        ActionBar supportActionBar = getSupportActionBar();
        if (supportActionBar != null) {
            supportActionBar.hide();
        }

        // Blendet Benachrichtigungsleiste aus
        getWindow().setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        if(BuildConfig.DEBUG) {
            Log.d(Constants.TAG, "onCreate: SplashActivity");
        }
    }

    public void onClick_SplashScreen(View view) {
        Log.d(Constants.TAG, "onClick_SplashScreen: ");

        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);

        finish();
    }
}
