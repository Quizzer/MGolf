package de.neu.mgolf;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;

public class SplashActivity extends AppCompatActivity {

    private Runnable runnable;
    private Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        if(BuildConfig.DEBUG) {
            Log.d(Constants.TAG, "onCreate: SplashActivity");
        }

        // Blendet ActionBar aus, falls vorhanden
        ActionBar supportActionBar = getSupportActionBar();
        if (supportActionBar != null) {
            supportActionBar.hide();
        }

        // Laufzeitumgebung wird aufgefordert, eine andere Activity zu starten
        runnable = new Runnable() {
            public void run() {
                // Laufzeitumgebung wird aufgefordert, eine andere Activity zu starten
                Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                startActivity(intent);

                // Eigene Activity wird beendet
                finish();
            }
        };
        handler = new Handler();
    }

    public void onClick_SplashScreen(View view) {
        Log.d(Constants.TAG, "onClick_SplashScreen: ");
        runnable.run();
    }

    @Override
    protected void onResume() {
        super.onResume();
        handler.postDelayed(runnable, 5000);
    }

    @Override
    protected void onPause() {
        handler.removeCallbacks(runnable);
        super.onPause();
    }

}
