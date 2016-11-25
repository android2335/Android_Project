package com.cst2335.proj;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by LongQuan on 11/6/2016.
 */

public class LivingRoomDatabaseHelper extends SQLiteOpenHelper {
    protected static String DATABASE_NAME = "SmartHome.db";
    static int VERSION_NUM = 1;
    public static final String TABLE_NAME = "LivingRoomItem_Table";

    public LivingRoomDatabaseHelper(Context ctx) {//constructor
        super(ctx, DATABASE_NAME, null, VERSION_NUM);
    }

    @Override
    public void onCreate(SQLiteDatabase sqlDB){
        sqlDB.execSQL("CREATE TABLE " + TABLE_NAME +
                "( ItemName text not null, ClickCount integer default 0," +
                "Switch integer default 0, Progress integer default 0, " +
                "Color text, Channel integer default 0);");

        //Initialize the listview with basic items: Lamp1,2,3, TV1, Blind1
        String[] basicItems = new String[]{"Lamp 1", "Lamp 2", "Lamp 3", "TV 1", "Blind 1"};
        ContentValues basicValues = new ContentValues();
        for(int i = 0; i < 5; i++){
            basicValues.put("ItemName", basicItems[i]);
            sqlDB.insert(LivingRoomDatabaseHelper.TABLE_NAME, null, basicValues);
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqlDB, int oldVersion, int newVersion){
        sqlDB.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(sqlDB);
    }
    @Override
    public void onDowngrade(SQLiteDatabase sqlDB, int oldVersion, int newVersion){
        sqlDB.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(sqlDB);
    }
}