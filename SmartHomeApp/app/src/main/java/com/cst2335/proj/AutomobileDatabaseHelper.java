package com.cst2335.proj;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * class AutomobileDatabaseHelper, used to create table
 */
public class AutomobileDatabaseHelper extends SQLiteOpenHelper {
    /***/
    protected static String DATABASE_NAME = "Automobile.db";
    /***/
    public static String TABLE_NAME = "MyTable";
    /***/
    protected static int VERSION_NUM = 1;

    /**table column name - ID*/
    public final static String KEY_ID = "ID";
    /**table column name - ITEM*/
    public final static String ITEM = "ITEM";
    /**table column name - ITEMNO, item number in sequence, if doesn't exist, then is 0*/
    public final static String ITEM_NO = "ITEMNO";
    /**table column name - VALUE, it may include multi-values for some item, and you need to code and decode for these items.*/
    public final static String VALUE = "VALUE";

    public AutomobileDatabaseHelper(Context ctx) {
        super(ctx, DATABASE_NAME, null, VERSION_NUM);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + TABLE_NAME + " (" + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + ITEM + " TEXT, "  + ITEM_NO + " INTEGER, " + VALUE + " TEXT" + ");");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }
}
