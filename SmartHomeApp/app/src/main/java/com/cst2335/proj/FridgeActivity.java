package com.cst2335.proj;


import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import static com.cst2335.proj.R.id.toolbar;

public class FridgeActivity extends AppCompatActivity {

    protected static final String ACTIVITY_NAME = "FridgeActivity";

    SeekBar fridgeSeekBar, freezerSeekBar;
    TextView fridgeTemp, freezerTemp;
    Intent intent;
    String setting;

    KitchenDatabaseHelper kDbHelper;
    SQLiteDatabase kDb;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fridge);

        kDbHelper = new KitchenDatabaseHelper(this);
        kDb = kDbHelper.getWritableDatabase();

        intent = getIntent();
        setting = intent.getStringExtra("SETTING");
        String[] ffset = setting.split(",");

        fridgeTemp = (TextView) findViewById(R.id.fridge_temp);
        freezerTemp = (TextView) findViewById(R.id.freezer_temp);
        fridgeTemp.setText(ffset[0]);
        freezerTemp.setText(ffset[1]);

        fridgeSeekBar = (SeekBar) findViewById(R.id.fridge_seekbar);
        freezerSeekBar = (SeekBar) findViewById(R.id.freezer_seekbar);

        fridgeSeekBar.setProgress((int)(Integer.parseInt(ffset[0])*100/7.0));
        freezerSeekBar.setProgress((int)(Integer.parseInt(ffset[1])*100/(-40.0)));

        fridgeSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener(){
            int progressChangedValue1 = 0;

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser){
                progressChangedValue1 = progress;
            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
 //               Toast.makeText(FridgeActivity.this, "Seek bar progress is :" + progressChangedValue1,
 //                       Toast.LENGTH_SHORT).show();
                fridgeTemp.setText(Integer.toString((int)((progressChangedValue1/100.0)*7)));
            }


        });

        freezerSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener(){
            int progressChangedValue2 = 0;

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser){
                progressChangedValue2 = progress;
            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                //               Toast.makeText(FridgeActivity.this, "Seek bar progress is :" + progressChangedValue1,
                //                       Toast.LENGTH_SHORT).show();
                freezerTemp.setText(Integer.toString((int)((progressChangedValue2/100.0)*(-40))));
            }


        });

 //       Toolbar toolbar = (Toolbar) findViewById(toolbar);
  //      setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }



    @Override
    protected void onStop(){

        String setting = fridgeTemp.getText().toString() + "," + freezerTemp.getText().toString();
        String q = "UPDATE KITCHEN_TABLE SET setting = '" + setting
                + "' WHERE APPLIANCE = 'Fridge'";
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
