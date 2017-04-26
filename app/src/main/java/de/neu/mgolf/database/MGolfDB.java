package de.neu.mgolf.database;


import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class MGolfDB extends SQLiteOpenHelper implements GameColumns{

    public static final String DB_NAME = "mgolf.db";
    public static final int VERSION = 1;

    public MGolfDB(Context context) {
        super(context, DB_NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) { // ToDo: Sauber wäre 1:n Beziehung für jeden Spieler
        String sql = String.format("CREATE TABLE %s (" +
                "%s INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "%s TEXT, " +
                "%s TEXT, " +
                "%s TIMESTAMP ); ",TABLE, _ID, LOCATION, NAMES, TIMESTAMP );
        db.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
