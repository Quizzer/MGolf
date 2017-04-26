package de.neu.mgolf.database;

import android.provider.BaseColumns;

public interface GameColumns extends BaseColumns {

    String LOCATION = "LOCATION";
    String NAMES = "NAMES";
    String TIMESTAMP = "TIMESTAMP";

    String[] PROJECTION = {_ID, LOCATION, NAMES, TIMESTAMP};
    String TABLE = "GAME";
}
