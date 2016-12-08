package com.cst2335.proj;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Vibrator;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

public class Kitchen_Detail_Fragment extends Fragment {

    public static final String ARG_ITEM_ID_1 = "kitchen_item_type";
    public static final String ARG_ITEM_ID_2 = "kitchen_item_setting";

    private KitchenDatabaseHelper kDbHelper;
    private SQLiteDatabase kDb;
    private String setting;
    private String type;

    private EditText cooking_time;
    private Button resetButton;
    private Button startButton;
    private Button stopButton;
    private String time_string;
    private CounterClass timer;

    private SeekBar fridgeSeekBar, freezerSeekBar;
    private TextView fridgeTemp, freezerTemp;

    private Switch lightSwitch;
    private CheckBox dimCheck;
    private String switch_setting, dimmable_setting;

    public Kitchen_Detail_Fragment() {
        //Mandatory empty constructor for the fragment manager
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        kDbHelper = new KitchenDatabaseHelper(getContext());
        kDb = kDbHelper.getWritableDatabase();

        if (getArguments().containsKey(ARG_ITEM_ID_1) && getArguments().containsKey(ARG_ITEM_ID_2)) {
            //setMItem(getArguments().getString(ARG_ITEM_ID_1));
            type = getArguments().getString(ARG_ITEM_ID_1);
            setting = getArguments().getString(ARG_ITEM_ID_2);

            Activity activity = this.getActivity();
            CollapsingToolbarLayout appBarLayout = (CollapsingToolbarLayout) activity.findViewById(R.id.toolbar_layout);
            if (appBarLayout != null) {
                //appBarLayout.setTitle(mItem.content);
                appBarLayout.setTitle("Kitchen Item Detailed Settings");
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //String mItem = getMItem();
        View rootView = null;
        switch (type) {
            case "Microwave":
                rootView = microwave(inflater, container);
                break;
            case "Fridge":
                rootView = fridge(inflater, container);
                break;
            case "Main Light":
                rootView = main_light(inflater, container);
                break;
            default:
                break;
        }

        return rootView;
    }

    private View microwave(LayoutInflater inflater, ViewGroup container) {
        View rootView = inflater.inflate(R.layout.activity_microwave, container, false);

        resetButton = (Button) rootView.findViewById(R.id.resetButton);
        startButton = (Button) rootView.findViewById(R.id.startButton);
        stopButton = (Button) rootView.findViewById(R.id.stopButton);
        cooking_time = (EditText) rootView.findViewById(R.id.time_number);

        cooking_time.setText(setting);


        startButton.setOnClickListener(
                new Button.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        time_string = cooking_time.getText().toString();
                        int colon = time_string.indexOf(":");
                        long countdownmillis = Integer.parseInt(time_string.substring(0, colon)) * 60 * 1000 +
                                Integer.parseInt(time_string.substring(colon + 1)) * 1000;
                        timer = new Kitchen_Detail_Fragment.CounterClass(countdownmillis, 1000);
                        timer.start();
                        startButton.setEnabled(false);
                        resetButton.setEnabled(false);

                    }
                }
        );

        stopButton.setOnClickListener(
                new Button.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        timer.cancel();
                        startButton.setEnabled(true);
                        resetButton.setEnabled(true);

                    }
                }
        );

        resetButton.setOnClickListener(
                new Button.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        cooking_time.setText("00:00");
                        timer.cancel();

                    }
                }
        );

        String new_setting = cooking_time.getText().toString();
        String q = "UPDATE KITCHEN_TABLE SET setting = '" + new_setting
                + "' WHERE APPLIANCE = 'Microwave'";
        kDb.execSQL(q);
        //kDb.close();

        return rootView;
    }

    public class CounterClass extends CountDownTimer {

        public CounterClass(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        @Override
        public void onTick(long millisUntilFinished) {
            long millis = millisUntilFinished;

 /*           String ms = String.format("%02d:%02d", TimeUnit.MILLISECONDS.toMinutes(millis),
                    TimeUnit.MILLISECONDS.toSeconds(millis) -
                            TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millis)));
*/
            String ms = String.format("%02d:%02d", (long) ((millis / (1000 * 60)) % 60),
                    (long) (millis / 1000) % 60);
            cooking_time.setText(ms);


        }

        @Override
        public void onFinish() {
            cooking_time.setText("00:00");
            startButton.setEnabled(true);
            resetButton.setEnabled(true);
        }

    }

    private View fridge(LayoutInflater inflater, ViewGroup container) {
        View rootView = inflater.inflate(R.layout.activity_fridge, container, false);

        String[] ffset = setting.split(",");

        fridgeTemp = (TextView)rootView.findViewById(R.id.fridge_temp);
        freezerTemp = (TextView)rootView.findViewById(R.id.freezer_temp);
        fridgeTemp.setText(ffset[0]);
        freezerTemp.setText(ffset[1]);

        fridgeSeekBar = (SeekBar)rootView.findViewById(R.id.fridge_seekbar);
        freezerSeekBar = (SeekBar)rootView.findViewById(R.id.freezer_seekbar);

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
                freezerTemp.setText(Integer.toString((int)((progressChangedValue2/100.0)*(-40))));
            }


        });

        String new_setting = fridgeTemp.getText().toString() + "," + freezerTemp.getText().toString();
        String q = "UPDATE KITCHEN_TABLE SET setting = '" + new_setting
                + "' WHERE APPLIANCE = 'Fridge'";
        kDb.execSQL(q);
        //kDb.close();

        return rootView;
    }

    private View main_light(LayoutInflater inflater, ViewGroup container) {
        View rootView = inflater.inflate(R.layout.activity_main_light, container, false);
        lightSwitch = (Switch)rootView.findViewById(R.id.switch1);
        dimCheck = (CheckBox)rootView.findViewById(R.id.checkBox);
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

        //setting = switch_setting + "," + dimmable_setting;
        String new_setting= switch_setting + "," + dimmable_setting;

        //String q = "UPDATE KITCHEN_TABLE SET setting = '" + setting
        String q = "UPDATE KITCHEN_TABLE SET setting = '" + new_setting
                + "' WHERE APPLIANCE = 'Main Light'";
        kDb.execSQL(q);
        //kDb.close();

        return rootView;
    }

}
