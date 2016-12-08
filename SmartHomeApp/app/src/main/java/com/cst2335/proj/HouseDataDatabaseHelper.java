package com.cst2335.proj;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Hou Yu
 */
public class HouseDataDatabaseHelper extends SQLiteOpenHelper {

    public static final String TAG = "HouseDataDatabaseHelper";
    private static final String DATABASE_NAME = "house.db";
    private static final int VERSION_NUM = 1;

    //For the weather
    public static final String CITIES_TABLE_NAME = "cities";
    public static final String CITIES_KEY_ID = "_id";
    public static final String CITIES_NAME = "city_name";

    //For temperature schedule
    public static final String HOUSE_TEMP_TABLE_NAME = "temperatures";
    public static final String HOUSE_TEMP_KEY_ID = "_id";
    public static final String HOUSE_TEMP_RECORD = "temperature_record";

    // Database creation sql statement
    private static final String CITIES_TABLE_CREATE = "create table "
            + CITIES_TABLE_NAME + "( " + CITIES_KEY_ID
            + " integer primary key autoincrement, " + CITIES_NAME
            + " text not null);";

    private static final String HOUSE_TEMP_TABLE_CREATE = "create table "
            + HOUSE_TEMP_TABLE_NAME + "( " + HOUSE_TEMP_KEY_ID
            + " integer primary key autoincrement, " + HOUSE_TEMP_RECORD
            + " text not null);";

    public HouseDataDatabaseHelper(Context ctx) {
        super(ctx, DATABASE_NAME, null, VERSION_NUM);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.i(TAG, "Calling onCreate");
        db.execSQL(CITIES_TABLE_CREATE);
        db.execSQL(HOUSE_TEMP_TABLE_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.i(TAG, "Calling onUpgrade, oldVersion=" + oldVersion + " newVersion=" + newVersion);
        db.execSQL("DROP TABLE IF EXISTS " + CITIES_TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + HOUSE_TEMP_TABLE_NAME);
        onCreate(db);
    }
}
