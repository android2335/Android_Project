package com.cst2335.proj;


import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Vibrator;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.util.concurrent.TimeUnit;

public class MicrowaveActivity extends AppCompatActivity {
    protected static final String ACTIVITY_NAME = "MicroWaveActivity";
    EditText cooking_time;
    Button resetButton;
    Button startButton;
    Button stopButton;
    String time_string;
    CounterClass timer;
    Intent intent;

    KitchenDatabaseHelper kDbHelper;
    SQLiteDatabase kDb;
    String setting;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_microwave);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        kDbHelper = new KitchenDatabaseHelper(this);
        kDb = kDbHelper.getWritableDatabase();

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });


        resetButton = (Button)findViewById(R.id.resetButton);
        startButton = (Button)findViewById(R.id.startButton);
        stopButton = (Button)findViewById(R.id.stopButton);
        cooking_time = (EditText) findViewById(R.id.time_number) ;

        intent = getIntent();
        setting = intent.getStringExtra("SETTING");
        cooking_time.setText(setting);


        startButton.setOnClickListener(
                new Button.OnClickListener(){
                    @Override
                    public void onClick(View v){
                        time_string = cooking_time.getText().toString();
                        Log.i(ACTIVITY_NAME, time_string);
                        int colon = time_string.indexOf(":");
                        long countdownmillis = Integer.parseInt(time_string.substring(0,colon))*60*1000 +
                                Integer.parseInt(time_string.substring(colon+1))*1000;
                        timer = new CounterClass(countdownmillis, 1000);
                        timer.start();
                        startButton.setEnabled(false);
                        resetButton.setEnabled(false);

                    }
                }
        );

        stopButton.setOnClickListener(
                new Button.OnClickListener(){
                    @Override
                    public void onClick(View v){
                        timer.cancel();
                        startButton.setEnabled(true);
                        resetButton.setEnabled(true);

                    }
                }
        );

        resetButton.setOnClickListener(
                new Button.OnClickListener(){
                    @Override
                    public void onClick(View v){
                        cooking_time.setText("00:00");
                        timer.cancel();

                    }
                }
        );

    }

    public class CounterClass extends CountDownTimer{

        public CounterClass(long millisInFuture, long countDownInterval){
            super(millisInFuture,countDownInterval);
        }

        @Override
        public void onTick(long millisUntilFinished){
            long millis = millisUntilFinished;

 /*           String ms = String.format("%02d:%02d", TimeUnit.MILLISECONDS.toMinutes(millis),
                    TimeUnit.MILLISECONDS.toSeconds(millis) -
                            TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millis)));
*/
            String ms = String.format("%02d:%02d", (long) ((millis / (1000*60)) % 60),
                    (long) (millis / 1000) % 60);
            cooking_time.setText(ms);




        }

        @Override
        public void onFinish(){
            cooking_time.setText("00:00");
            Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
            v.vibrate(500);       // Vibrate for 500 milliseconds
            startButton.setEnabled(true);
            resetButton.setEnabled(true);
        }

    }

    @Override
    protected void onResume(){
        super.onResume();
        Log.i(ACTIVITY_NAME, "In onResume()");
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.i(ACTIVITY_NAME, "In onStart()");


    }

    @Override
    protected void onPause(){
        super.onPause();
        Log.i(ACTIVITY_NAME, "In onPause()");
    }

    @Override
    protected void onStop(){

        String setting = cooking_time.getText().toString();
        String q = "UPDATE KITCHEN_TABLE SET setting = '" + setting
                + "' WHERE APPLIANCE = 'Microwave'";
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
