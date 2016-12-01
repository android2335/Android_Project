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

import org.w3c.dom.Text;

public class Automobile extends AppCompatActivity {

    protected SQLiteDatabase dbWrite;   //database for write

    protected TextView tvSpeed;   //speed
    protected TextView tvKm;      //kilometer
    protected boolean kmExist;
    protected boolean gasExist;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_automobile);

            //textviews
            tvSpeed = (TextView)findViewById(R.id.textView_speed);
            tvKm = (TextView)findViewById(R.id.textView_km);

        //button about
        Button btnAbout = (Button)findViewById(R.id.button_about);
        btnAbout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(Automobile.this);
                String strAbout = "Author: Sulin Zhao, Version: 1.0\n\nThis " +
                        "is an Application which simulates driving a car.You could reach the settings of " +
                        "a car by clicking the settings button. After you get all the settings ready," +
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
        AutomobileDatabaseOperate.write();

        //close database
        dbWrite.close();
    }

    protected void sleep() {
        try {
            Thread.sleep(500);
        }
        catch (Exception e) {

        }
    }

    class DatabaseQuery extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String ...args) {
            try {
                //read database
                AutomobileDatabaseHelper dbHelper = new AutomobileDatabaseHelper(Automobile.this);
                dbWrite = dbHelper.getWritableDatabase();
                AutomobileDatabaseOperate.setDatabaseHandle(dbWrite);

                publishProgress(new String[]{"10"});
                sleep();
                publishProgress(new String[]{"40"});

                //read settings from table
                AutomobileDatabaseOperate.read();

                publishProgress(new String[]{"80"});
                sleep();
                publishProgress(new String[]{"100"});
            }
            catch (Exception e) {
                return e.getMessage();
            }
            sleep();
            return "done";
        }

        @Override
        protected void onProgressUpdate(String ...values) {
            ProgressBar pb = (ProgressBar)findViewById(R.id.progressBar);
            pb.setProgress(Integer.parseInt(values[0]));
        }

        @Override
        protected void onPostExecute(String result) {

            //update driving info
            ProgressBar pbGas = (ProgressBar)findViewById(R.id.progressBar_gas);
            pbGas.setProgress(AutomobileDatabaseOperate.getGasLevel());
            TextView tvKm = (TextView)findViewById(R.id.textView_km);
            tvKm.setText("" + AutomobileDatabaseOperate.getOdometer());

            ProgressBar pb = (ProgressBar)findViewById(R.id.progressBar);
            pb.setVisibility(View.INVISIBLE);
        }
    }

}
