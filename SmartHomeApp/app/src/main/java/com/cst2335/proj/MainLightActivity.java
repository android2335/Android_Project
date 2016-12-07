package com.cst2335.proj;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.Toast;

public class MainLightActivity extends AppCompatActivity {

    protected static final String ACTIVITY_NAME = "MainLightActivity";

    Switch lightSwitch;
    CheckBox dimCheck;

    Intent intent;
    String setting;
    String switch_setting, dimmable_setting;

    KitchenDatabaseHelper kDbHelper;
    SQLiteDatabase kDb;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_light);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        lightSwitch = (Switch) findViewById(R.id.switch1);
        dimCheck = (CheckBox) findViewById(R.id.checkBox);

        kDbHelper = new KitchenDatabaseHelper(this);
        kDb = kDbHelper.getWritableDatabase();

        intent = getIntent();
        setting = intent.getStringExtra("SETTING");
        String[] ffset = setting.split(",");

        if (ffset[0].equals("off")){
            lightSwitch.setChecked(false);
        }
        else {
            lightSwitch.setChecked(true);
        }

        if (ffset[1].equals("dimmable")){
            dimCheck.setChecked(true);
        }
        else {
            dimCheck.setChecked(false);
        }

        lightSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener(){
            @Override
            public void onCheckedChanged(CompoundButton cb, boolean on){

                if(on)
                {
                    switch_setting = "on";
                }
                else
                {
                    switch_setting = "off";
                }

            }
        });

        dimCheck.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener(){
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked ){
                if (dimCheck.isChecked())
                {
                    dimmable_setting = "dimmable";
                }
                else
                {
                    dimmable_setting = "nondimmable";
                }
            }
        });

    }


    @Override
    protected void onStop(){


        setting = switch_setting + "," + dimmable_setting;

        String q = "UPDATE KITCHEN_TABLE SET setting = '" + setting
                + "' WHERE APPLIANCE = 'Main Light'";
        kDb.execSQL(q);
        super.onStop();
        Log.i(ACTIVITY_NAME, "In onStop()");
    }
    @Override
    protected void onDestroy(){
        if (kDb!=null){
            kDb.close();
        }
        super.onDestroy();
        Log.i(ACTIVITY_NAME, "In onDestroy()");
    }


}
