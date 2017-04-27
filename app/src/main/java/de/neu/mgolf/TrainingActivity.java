package de.neu.mgolf;

import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;

import de.neu.mgolf.game.TrainingView;

public class TrainingActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_training);
        // alternativ ohne activity_training.xml wäre auch möglich:
        // setContentView(new TrainingView(this));

        // Blendet ActionBar aus, falls vorhanden
        ActionBar supportActionBar = getSupportActionBar();
        if (supportActionBar != null) {
            supportActionBar.hide();
        }

        // Blendet Benachrichtigungsleiste aus
        getWindow().setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        TrainingView trainingView = (TrainingView) findViewById(R.id.trainingView);

    }
}
