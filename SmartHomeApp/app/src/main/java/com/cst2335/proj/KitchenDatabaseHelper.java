package com.cst2335.proj;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by Ji Hong Yao on 12/2/2016.
 */


public class KitchenDatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "Kitchen.db";
    private static final int VERSION_NUM = 101;
    public static final String TABLE_NAME = "KITCHEN_TABLE";
    public static final String KEY_ID = "id";
    public static final String APPLIANCE_TYPE = "appliance";
    public static final String APPLIANCE_SETTING = "setting";

    public static final String[] KITCHEN_FIELDS = new String[]{
            KEY_ID, APPLIANCE_TYPE, APPLIANCE_SETTING
    };

    KitchenDatabaseHelper(Context ctx){
        super(ctx, DATABASE_NAME, null, VERSION_NUM);
    }

    private static final String CREATE_TABLE_KITCHEN =
            "create table " + TABLE_NAME + " ("
                    + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + APPLIANCE_TYPE + " text, "
                    + APPLIANCE_SETTING + " text"
                    +");";
    @Override
    public void onCreate(SQLiteDatabase db ){
        db.execSQL(CREATE_TABLE_KITCHEN);
/*        db.execSQL("create table KITCHEN_TABLE " +
                "(id integer primary key autoincrement, appliance text, setting text)");
        Log.i("KitchenDatabaseHelper", "Calling onCreate"); */
    }

    @Override
    public void onUpgrade (SQLiteDatabase db, int oldVersion, int newVersion){
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
        Log.i("KitchenDatabaseHelper", "Calling onUpgrade, oldVersion=" + oldVersion
                + " newVersion=" + newVersion);


    }

}
