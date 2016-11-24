package com.cst2335.proj;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.database.sqlite.SQLiteDatabase;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

public class Automobile extends AppCompatActivity {

    protected SQLiteDatabase dbWrite;   //database for write

    protected TextView tvSpeed;   //speed
    protected TextView tvKm;      //kilometer
    protected boolean kmExist;
    protected TextView tvGas;     //gas level
    protected boolean gasExist;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_automobile);

        //textviews
        tvSpeed = (TextView)findViewById(R.id.textView_speed);
        tvKm = (TextView)findViewById(R.id.textView_km);
        tvGas = (TextView)findViewById(R.id.textView_gas);

        //button about
        Button btnAbout = (Button)findViewById(R.id.button_about);
        btnAbout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(Automobile.this);
                String strAbout = "This is an Application which simulates driving a car.You could reach the settings of a car by clicking the settings button. After you get all the settings ready," +
                        "you could change the shift to start driving.";
                builder.setMessage(strAbout);
                builder.setTitle("About");
                builder.create().show();
            }
        });

        //button settings
        Button btnSetting = (Button)findViewById(R.id.button_setting);
        btnSetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Automobile.this, AutomobileItemListActivity.class);
                startActivity(intent);
            }
        });

        //radio buttons
        RadioGroup rg = (RadioGroup)findViewById(R.id.radioGroup);
        rg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                int nTmp = 0;
                String strTmp = "";
                switch (checkedId) {
                    case R.id.radioButton_P:
                        break;

                    case R.id.radioButton_D:
                        try {
                            strTmp = tvKm.getText().toString();
                            nTmp = Integer.parseInt(strTmp.substring(0, strTmp.length()-3));
                        }
                        catch (Exception e) {

                        }
                        tvKm.setText((nTmp + 50) + " Km");
                        break;

                    case R.id.radioButton_R:
                        try {
                            strTmp = tvKm.getText().toString();
                            nTmp = Integer.parseInt(strTmp.substring(0, strTmp.length()-3));
                        }
                        catch (Exception e) {

                        }
                        final int nStep = 20;
                        if (nTmp > nStep) {
                            tvKm.setText((nTmp - nStep) + " Km");
                        }
                        else {
                            tvKm.setText("0 Km");
                        }
                        break;
                }
            }
        });

        ProgressBar pb = (ProgressBar)findViewById(R.id.progressBar);
        pb.setVisibility(View.VISIBLE);

        //start asynctask
        DatabaseQuery dbQuery = new DatabaseQuery();
        dbQuery.execute();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        //save database (update table)
        ContentValues cValue = new ContentValues();
        cValue.put(AutomobileDatabaseHelper.ITEM, AutomobileDatabaseHelper.ITEM_KM);
        cValue.put(AutomobileDatabaseHelper.VALUE, tvKm.getText().toString());
        if (kmExist) {
            dbWrite.update(AutomobileDatabaseHelper.TABLE_NAME, cValue, AutomobileDatabaseHelper.ITEM + " is ?", new String[]{AutomobileDatabaseHelper.ITEM_KM});
        }
        else {
            dbWrite.insert(AutomobileDatabaseHelper.TABLE_NAME, "NULL", cValue);
        }
        cValue.put(AutomobileDatabaseHelper.ITEM, AutomobileDatabaseHelper.ITEM_GAS);
        cValue.put(AutomobileDatabaseHelper.VALUE, tvGas.getText().toString());
        if (gasExist) {
            dbWrite.update(AutomobileDatabaseHelper.TABLE_NAME, cValue, AutomobileDatabaseHelper.ITEM + " is ?", new String[]{AutomobileDatabaseHelper.ITEM_GAS});
        }
        else {
            dbWrite.insert(AutomobileDatabaseHelper.TABLE_NAME, "NULL", cValue);
        }

        //close database
        dbWrite.close();
    }

    protected void sleep() {
        try {
            Thread.sleep(1000);
        }
        catch (Exception e) {

        }
    }

    class DatabaseQuery extends AsyncTask<String, String, String> {
        protected String minTemp;
        protected String maxTemp;
        protected String curTemp;
        protected String bmpFileName;

        @Override
        protected String doInBackground(String ...args) {
            try {
                //read database
                AutomobileDatabaseHelper dbHelper = new AutomobileDatabaseHelper(Automobile.this);
                dbWrite = dbHelper.getWritableDatabase();

                //read settings from table
                String strTmp = "";
                Cursor cursor = dbWrite.query(AutomobileDatabaseHelper.TABLE_NAME, new String[]{AutomobileDatabaseHelper.VALUE},
                        AutomobileDatabaseHelper.ITEM + " = ?",  new String[]{AutomobileDatabaseHelper.ITEM_KM}, null, null, null, null);
                if (cursor.getCount() > 0) {
                    cursor.moveToFirst();
                    strTmp = cursor.getString(0);
                    kmExist = true;
                }
                else {
                    strTmp = "0 Km";
                    kmExist = false;
                }
                sleep();
                publishProgress(new String[]{"1", strTmp, "50"});
                cursor = dbWrite.query(AutomobileDatabaseHelper.TABLE_NAME, new String[]{AutomobileDatabaseHelper.VALUE},
                        AutomobileDatabaseHelper.ITEM + " = ?",  new String[]{AutomobileDatabaseHelper.ITEM_GAS}, null, null, null, null);
                if (cursor.getCount() > 0) {
                    cursor.moveToFirst();
                    strTmp = cursor.getString(0);
                    gasExist = true;
                }
                else {
                    strTmp = "0 Gas";
                    gasExist = false;
                }
                sleep();
                publishProgress(new String[]{"2", strTmp, "100"});
            }
            catch (Exception e) {
                return e.getMessage();
            }
            sleep();
            return "done";
        }

        @Override
        protected void onProgressUpdate(String ...values) {
            int seq = 0;
            int progress = 0;
            try {
                seq = Integer.parseInt(values[0]);
                progress = Integer.parseInt(values[2]);
            }
            catch (Exception e) {

            }
            switch (seq) {
                case 1:
                    tvKm.setText(values[1]);
                    break;
                case 2:
                    tvGas.setText(values[1]);
                    break;
            }
            ProgressBar pb = (ProgressBar)findViewById(R.id.progressBar);
            pb.setProgress(progress);
        }

        @Override
        protected void onPostExecute(String result) {
            ProgressBar pb = (ProgressBar)findViewById(R.id.progressBar);
            pb.setVisibility(View.INVISIBLE);
        }
    }

}
