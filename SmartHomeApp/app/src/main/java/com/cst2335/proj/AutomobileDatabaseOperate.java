package com.cst2335.proj;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.cst2335.proj.dummy.AutomobileDummyContent;

import java.util.ArrayList;

/**
 * Created by sulin on 2016-11-30.
 */

public class AutomobileDatabaseOperate {
    private static int speedForward;
    private static int speedBackward;
    private static float gasLevel;
    private static float odometer;
    private static String radioMode;  //AM or FM
    private static float radioFrequency;
    private static int temperature;
    private static boolean light;  //light is on or off
    private static ArrayList<Integer> itemNo;  //item sequence number
    public static ArrayList<String> itemName;  //item Names in sequence

    public static SQLiteDatabase dbHandel;  //handel to operate database

    public final static int FULL_FUEL = 60;  //100L gas capacity

    //item sequence in arraylist "itemNo"
    public final static int ITEM_NUM = 7;
    public final static int SEQ_SPEED = 0;
    public final static int SEQ_FUEL = 1;
    public final static int SEQ_ODOMETER = 2;
    public final static int SEQ_RADIO = 3;
    public final static int SEQ_GPS = 4;
    public final static int SEQ_TEMPERATURE = 5;
    public final static int SEQ_LIGHT = 6;

    //create arraylist
    static {
        itemNo = new ArrayList<>(ITEM_NUM);
        for (int i = 0; i < ITEM_NUM; i++) {
            itemNo.add(0);
        }
        itemName = new ArrayList<>(ITEM_NUM);
        itemName.add(AutomobileDummyContent.SPEED);
        itemName.add(AutomobileDummyContent.FUEL);
        itemName.add(AutomobileDummyContent.ODOMETER);
        itemName.add(AutomobileDummyContent.RADIO);
        itemName.add(AutomobileDummyContent.GPS);
        itemName.add(AutomobileDummyContent.TEMPERATURE);
        itemName.add(AutomobileDummyContent.LIGHT);
    };

    public static int getSpeedForward() {
        return speedForward;
    }
    public static void setSpeedForward(int speed) {
        speedForward = speed;
    }

    public static int getSpeedBackward() {
        return speedBackward;
    }
    public static void setSpeedBackward(int speed) {
        speedBackward = speed;
    }

    public static float getGasLevel() {
        return gasLevel;
    }
    public static void setGasLevel(float gas) {
        gasLevel = gas;
    }

    public static float getOdometer() {
        return odometer;
    }
    public static void setOdometer(float km) {
        odometer = km;
    }

    public static String getRadioMode() {
        return radioMode;
    }
    public static void setRadioMode(String mode) {
        radioMode = mode;
    }

    public static float getRadioFrequency() {
        return radioFrequency;
    }
    public static void setRadioFrequency(float freq) {
        radioFrequency = freq;
    }

    public static int getTemperature() {
        return temperature;
    }
    public static void setTemperature(int temp) {
        temperature = temp;
    }

    public static boolean getLight() {
        return light;
    }
    public static void setLight(boolean b) {
        light = b;
    }

    public static void setDatabaseHandle(SQLiteDatabase db) {
        dbHandel = db;
    }

    public static ArrayList<Integer> getItemNo() {
        return itemNo;
    }
    public static void setItemNo(ArrayList<Integer> arr) {
        itemNo.clear();
        for (Integer item : arr) {
            itemNo.add(item);
        }
    }
    public static void setOneItemNo(int index, int no) {
        itemNo.set(index, no);
    }

    public static void read() {
        //clear data
        for (int i = 0; i < ITEM_NUM; i++) {
            itemNo.set(i, 0);
        }

        Cursor cursor = dbHandel.query(AutomobileDatabaseHelper.TABLE_NAME, new String[]{"*"}, null, null, null, null, null, null);
        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                int num = cursor.getInt(cursor.getColumnIndex(AutomobileDatabaseHelper.ITEM_NO));

                //only deal with item exist
                if (num > 0) {
                    String itemName =  cursor.getString(cursor.getColumnIndex(AutomobileDatabaseHelper.ITEM));
                    String value = "";
                    switch (itemName) {
                        case AutomobileDummyContent.SPEED:

                            //set sequence no
                            itemNo.set(SEQ_SPEED, num);

                            //get value
                            value = cursor.getString(cursor.getColumnIndex(AutomobileDatabaseHelper.VALUE));
                            {
                                //decode speed value
                                setSpeedForward(Integer.parseInt(value.split(",")[0]));
                                setSpeedBackward(Integer.parseInt(value.split(",")[1]));
                            }
                            break;

                        case AutomobileDummyContent.FUEL:

                            //set sequence no
                            itemNo.set(SEQ_FUEL, num);

                            //get value
                            value = cursor.getString(cursor.getColumnIndex(AutomobileDatabaseHelper.VALUE));
                            setGasLevel(Float.parseFloat(value));
                            break;

                        case AutomobileDummyContent.ODOMETER:

                            //set sequence no
                            itemNo.set(SEQ_ODOMETER, num);

                            //get value
                            value = cursor.getString(cursor.getColumnIndex(AutomobileDatabaseHelper.VALUE));
                            setOdometer(Float.parseFloat(value));
                            break;

                        case AutomobileDummyContent.RADIO:

                            //set sequence no
                            itemNo.set(SEQ_RADIO, num);

                            //get value
                            value = cursor.getString(cursor.getColumnIndex(AutomobileDatabaseHelper.VALUE));
                            {
                                //decode radio value
                                setRadioMode(value.split(",")[0]);
                                setRadioFrequency(Float.parseFloat(value.split(",")[1]));
                            }
                            break;

                        case AutomobileDummyContent.GPS:

                            //set sequence no
                            itemNo.set(SEQ_GPS, num);
                        break;

                        case AutomobileDummyContent.TEMPERATURE:

                            //set sequence no
                            itemNo.set(SEQ_TEMPERATURE, num);

                            //get value
                            value = cursor.getString(cursor.getColumnIndex(AutomobileDatabaseHelper.VALUE));
                            setTemperature(Integer.parseInt(value));
                            break;

                        case AutomobileDummyContent.LIGHT:

                            //set sequence no
                            itemNo.set(SEQ_LIGHT, num);

                            //get value
                            value = cursor.getString(cursor.getColumnIndex(AutomobileDatabaseHelper.VALUE));
                            if (Integer.parseInt(value) == 0) {
                                setLight(false);
                            }
                            else {
                                setLight(true);
                            }
                            break;
                        default:
                            break;
                    }
                }
                cursor.moveToNext();
            }
        }
    }

    public static void write() {

        //only write when number is greater than 0
        //speed
        if (itemNo.get(SEQ_SPEED) > 0) {
            ContentValues cValue = new ContentValues();
            cValue.put(AutomobileDatabaseHelper.ITEM, AutomobileDummyContent.SPEED);
            cValue.put(AutomobileDatabaseHelper.ITEM_NO, itemNo.get(SEQ_SPEED));
            cValue.put(AutomobileDatabaseHelper.VALUE, String.format("%d,%d", getSpeedForward(), getSpeedBackward()));
            dbHandel.insert(AutomobileDatabaseHelper.TABLE_NAME, "NULL", cValue);
        }

        //fuel
        if (itemNo.get(SEQ_FUEL) > 0) {
            ContentValues cValue = new ContentValues();
            cValue.put(AutomobileDatabaseHelper.ITEM, AutomobileDummyContent.FUEL);
            cValue.put(AutomobileDatabaseHelper.ITEM_NO, itemNo.get(SEQ_FUEL));
            cValue.put(AutomobileDatabaseHelper.VALUE, String.format("%.1f", getGasLevel()));
            dbHandel.insert(AutomobileDatabaseHelper.TABLE_NAME, "NULL", cValue);
        }

        //odometer
        if (itemNo.get(SEQ_ODOMETER) > 0) {
            ContentValues cValue = new ContentValues();
            cValue.put(AutomobileDatabaseHelper.ITEM, AutomobileDummyContent.ODOMETER);
            cValue.put(AutomobileDatabaseHelper.ITEM_NO, itemNo.get(SEQ_ODOMETER));
            cValue.put(AutomobileDatabaseHelper.VALUE, String.format("%.1f", getOdometer()));
            dbHandel.insert(AutomobileDatabaseHelper.TABLE_NAME, "NULL", cValue);
        }

        //gps
        if (itemNo.get(SEQ_GPS) > 0) {
            ContentValues cValue = new ContentValues();
            cValue.put(AutomobileDatabaseHelper.ITEM, AutomobileDummyContent.GPS);
            cValue.put(AutomobileDatabaseHelper.ITEM_NO, itemNo.get(SEQ_GPS));
            dbHandel.insert(AutomobileDatabaseHelper.TABLE_NAME, "NULL", cValue);
        }

        //radio
        if (itemNo.get(SEQ_RADIO) > 0) {
            ContentValues cValue = new ContentValues();
            cValue.put(AutomobileDatabaseHelper.ITEM, AutomobileDummyContent.RADIO);
            cValue.put(AutomobileDatabaseHelper.ITEM_NO, itemNo.get(SEQ_RADIO));
            cValue.put(AutomobileDatabaseHelper.VALUE, String.format("%s,%f", getRadioMode(), getRadioFrequency()));
            dbHandel.insert(AutomobileDatabaseHelper.TABLE_NAME, "NULL", cValue);
        }

        //temperature
        if (itemNo.get(SEQ_TEMPERATURE) > 0) {
            ContentValues cValue = new ContentValues();
            cValue.put(AutomobileDatabaseHelper.ITEM, AutomobileDummyContent.TEMPERATURE);
            cValue.put(AutomobileDatabaseHelper.ITEM_NO, itemNo.get(SEQ_TEMPERATURE));
            cValue.put(AutomobileDatabaseHelper.VALUE, String.format("%d", getTemperature()));
            dbHandel.insert(AutomobileDatabaseHelper.TABLE_NAME, "NULL", cValue);
        }

        //light
        if (itemNo.get(SEQ_LIGHT) > 0) {
            ContentValues cValue = new ContentValues();
            cValue.put(AutomobileDatabaseHelper.ITEM, AutomobileDummyContent.LIGHT);
            cValue.put(AutomobileDatabaseHelper.ITEM_NO, itemNo.get(SEQ_LIGHT));
            if (getLight()) {
                cValue.put(AutomobileDatabaseHelper.VALUE, String.format("%d", 1));
            }
            else {
                cValue.put(AutomobileDatabaseHelper.VALUE, String.format("%d", 0));
            }
            dbHandel.insert(AutomobileDatabaseHelper.TABLE_NAME, "NULL", cValue);
        }
    }

}
