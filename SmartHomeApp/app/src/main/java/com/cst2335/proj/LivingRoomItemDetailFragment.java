package com.cst2335.proj;

import android.app.Activity;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

/**
 * A fragment representing a single LivingRoomItem detail screen.
 * This fragment is either contained in a {@link LivingRoomItemListActivity}
 * in two-pane mode (on tablets) or a {@link LivingRoomItemDetailActivity}
 * on handsets.
 */
public class LivingRoomItemDetailFragment extends Fragment {

    public static final String ARG_ITEM_ID = "item_id";//The fragment argument representing the item ID that this fragment represents
    private SQLiteDatabase sqlDB;
    //status variables of each item:
    //can be stored as class static member,==> problem! if re-run the app (not just go-back), then status becomes default
    //or stored in another database Table, like in ListActivity.class
    //Note: these vatiables must be static (but not final, because will change)
    private static boolean lamp_1_switch_status;
    //private SeekBar lamp_2_brightness;//for AsyncTask access: BUT if put Task as nested class inside of method then this isn't needed
    private static String lamp_3_color = "Color = ";
    private static boolean tv_1_power_status;
    private static int channelNumber;
    private static int laudness;

    //The dummy content mItem this fragment is presenting
    private String mItem;

    void setMItem(String mItem){
        this.mItem = mItem;
    }
    String getMItem(){
        return mItem;
    }

    public LivingRoomItemDetailFragment() {//Mandatory empty constructor for the fragment manager
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments().containsKey(ARG_ITEM_ID)) {
            // Load the dummy content specified by the fragment
            // arguments. In a real-world scenario, use a Loader
            // to load content from a content provider.
            //mItem = DummyContent.ITEM_MAP.get(getArguments().getString(ARG_ITEM_ID));
            //mItem = getArguments().getString(ARG_ITEM_ID);
            setMItem(getArguments().getString(ARG_ITEM_ID));

            Activity activity = this.getActivity();
            CollapsingToolbarLayout appBarLayout = (CollapsingToolbarLayout) activity.findViewById(R.id.toolbar_layout);
            if (appBarLayout != null) {
                //appBarLayout.setTitle(mItem.content);
                appBarLayout.setTitle("Living Room Item Details");
            }
        }

        //LivingRoomDatabaseHelper lrAdapter = new LivingRoomDatabaseHelper(this);
        LivingRoomDatabaseHelper lrAdapter = new LivingRoomDatabaseHelper(getActivity());
        sqlDB = lrAdapter.getWritableDatabase();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        String mItem = getMItem();
        View rootView = null;
        switch(mItem){
            case "Lamp 1":
                rootView = lamp_1_view(inflater, container);
                break;
            case "Lamp 2":
                rootView = lamp_2_view(inflater, container);
                break;
            case "Lamp 3":
                rootView = lamp_3_view(inflater, container);
                break;
            case "TV 1":
                rootView = tv_1_view(inflater, container);
                break;
            case "Blind 1":
                rootView = blind_1_view(inflater, container);
                break;
            default:
                rootView = inflater.inflate(R.layout.livingroomitem_detail, container, false);
                break;
        }

        return rootView;
    }

    private View lamp_1_view(LayoutInflater inflater, ViewGroup container){
        View rootView = inflater.inflate(R.layout.lamp_1_detail, container, false);

        // Show the dummy content as text in a TextView.
        if (mItem != null) {
            //((TextView) rootView.findViewById(R.id.livingroomitem_detail)).setText(mItem.details);
            ((TextView) rootView.findViewById(R.id.lamp_1_textview)).setText(mItem);
        }

        Switch lampSwitch = (Switch) rootView.findViewById(R.id.lamp_1_switch);
        Cursor switchQuery = sqlDB.query(LivingRoomDatabaseHelper.TABLE_NAME, new String[]{"Switch"}, "ItemName = ?",new String[]{"Lamp 1"}, null,null,null,null );
        switchQuery.moveToFirst();
        int switch_status = switchQuery.getInt(switchQuery.getColumnIndex("Switch"));
        lamp_1_switch_status = (switch_status == 0)? false: true;
        lampSwitch.setChecked(lamp_1_switch_status);
        final TextView switchText = (TextView)rootView.findViewById(R.id.lamp_1_switch_textView);
        switchText.setText("Switch = " + (lamp_1_switch_status? "ON": "OFF"));
        lampSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                //Toast.makeText(LivingRoomItemDetailFragment.this, "The Switch is " + (isChecked ? "on" : "off"), Toast.LENGTH_SHORT).show();
                Toast.makeText(getContext(), "The Switch is " + (isChecked ? "on" : "off"), Toast.LENGTH_SHORT).show();
                //Toast.makeText(LivingRoomItemDetailActivity.class, "The Switch is " + (isChecked ? "on" : "off"), Toast.LENGTH_SHORT).show();

                if (isChecked) {
                    // save "on" as status, and pass back to Activity
                    lamp_1_switch_status = true;
                    switchText.setText("Switch = ON");

                } else {
                    // save "off" as status, and pass back to Activity
                    lamp_1_switch_status = false;
                    switchText.setText("Switch = OFF");
                }

                ContentValues cv = new ContentValues();
                cv.put("Switch", (lamp_1_switch_status? 1:0));
                sqlDB.update(LivingRoomDatabaseHelper.TABLE_NAME, cv, "ItemName = ?", new String[]{"Lamp 1"});
            }
        });

        return rootView;
    }

    private View lamp_2_view(LayoutInflater inflater, ViewGroup container){
        View rootView = inflater.inflate(R.layout.lamp_2_detail, container, false);

        // Show the dummy content as text in a TextView.
        if (mItem != null) {
            ((TextView) rootView.findViewById(R.id.lamp_2_textview)).setText(mItem);
        }

        final SeekBar lampBrightness = (SeekBar) rootView.findViewById(R.id.lamp_2_seekbar);
        /*
        Cursor bQuery = sqlDB.query(LivingRoomDatabaseHelper.TABLE_NAME, new String[]{"Progress"}, "ItemName = ?",new String[]{"Lamp 2"}, null,null,null,null );
        bQuery.moveToFirst();
        int brightness = bQuery.getInt(bQuery.getColumnIndex("Progress"));
        lampBrightness.setProgress(brightness);*/

        final TextView tv = (TextView)rootView.findViewById(R.id.lamp_2_seekbar_textview);
        //tv.setText("Brightness = " + brightness + "/" + lampBrightness.getMax());

        class DBTask_Lamp2_1 extends AsyncTask<String, Integer, Cursor> {
            @Override
            protected Cursor doInBackground(String... args) {
                Cursor bQuery = sqlDB.query(LivingRoomDatabaseHelper.TABLE_NAME, new String[]{args[0]}, "ItemName = ?",new String[]{args[1]}, null,null,null,null );
                bQuery.moveToFirst();//must have this line before getInt(index), otherwise
                //android.database.CursorIndexOutOfBoundsException: Index -1 requested, with a size of 1
                int brightness = bQuery.getInt(bQuery.getColumnIndex(args[0]));
                publishProgress(brightness);
                if (bQuery != null) {
                    return bQuery;
                } else
                    return null;
            }

            @Override
            protected void onProgressUpdate(Integer... b){
                lampBrightness.setProgress(b[0]);
                tv.setText("Brightness = " + b[0] + "/" + lampBrightness.getMax());
            }

            @Override
            protected void onPostExecute(Cursor result) {
                if (result != null) {
                    result.close();
                }
            }
        }
        new DBTask_Lamp2_1().execute("Progress", "Lamp 2");

        lampBrightness.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener(){
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser){
                //fromUser = true;
                //Toast.makeText(getContext(), "Blind coverage:  "+ progress + "/" + lampBrightness.getMax(), Toast.LENGTH_SHORT).show();
                tv.setText("Brightness = " + progress + "/" + lampBrightness.getMax());

                ContentValues cv = new ContentValues();
                cv.put("Progress", progress);
                //sqlDB.update(LivingRoomDatabaseHelper.TABLE_NAME, cv, "ItemName = ?", new String[]{"Lamp 2"});

                class DBTask_Lamp2_2 extends AsyncTask<ContentValues, Void, Boolean> {

                    @Override
                    protected Boolean doInBackground(ContentValues... args) {
                        sqlDB.update(LivingRoomDatabaseHelper.TABLE_NAME, args[0], "ItemName = ?", new String[]{"Lamp 2"});
                        return true;
                    }

                    @Override
                    protected void onPostExecute(Boolean result) {
                        if (result) {
                            setMItem("Update successfully");
                        }
                    }
                }
                new DBTask_Lamp2_2().execute(cv);

            }

            public void onStartTrackingTouch(SeekBar seekBar){
                //Toast.makeText(getContext(), "Start tracking blind coverage", Toast.LENGTH_SHORT).show();
            }

            public void onStopTrackingTouch(SeekBar seekBar){
                //Toast.makeText(getContext(), "Stop tracking blind coverage", Toast.LENGTH_SHORT).show();
            }
        });

        return rootView;
    }

    private View lamp_3_view(LayoutInflater inflater, ViewGroup container){
        View rootView = inflater.inflate(R.layout.lamp_3_detail, container, false);

        // Show the dummy content as text in a TextView.
        if (mItem != null) {
            //((TextView) rootView.findViewById(R.id.livingroomitem_detail)).setText(mItem.details);
            ((TextView) rootView.findViewById(R.id.lamp_3_textview)).setText(mItem);
        }

        final SeekBar lampBrightness = (SeekBar) rootView.findViewById(R.id.lamp_3_seekbar);
        Cursor bcQuery = sqlDB.query(LivingRoomDatabaseHelper.TABLE_NAME, new String[]{"Progress", "Color"}, "ItemName = ?",new String[]{"Lamp 3"}, null,null,null,null );
        bcQuery.moveToFirst();
        int brightness = bcQuery.getInt(bcQuery.getColumnIndex("Progress"));
        lampBrightness.setProgress(brightness);

        final TextView tv = (TextView)rootView.findViewById(R.id.lamp_3_seekbar_textview);
        tv.setText("Brightness = " + brightness + "/" + lampBrightness.getMax());
        lampBrightness.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener(){
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser){
                //fromUser = true;
                //Toast.makeText(getContext(), "Blind coverage:  "+ progress + "/" + lampBrightness.getMax(), Toast.LENGTH_SHORT).show();
                tv.setText("Brightness = " + progress + "/" + lampBrightness.getMax());

                ContentValues cv = new ContentValues();
                cv.put("Progress", progress);
                sqlDB.update(LivingRoomDatabaseHelper.TABLE_NAME, cv, "ItemName = ?", new String[]{"Lamp 3"});
            }

            public void onStartTrackingTouch(SeekBar seekBar){
                //Toast.makeText(getContext(), "Start tracking blind coverage", Toast.LENGTH_SHORT).show();
            }

            public void onStopTrackingTouch(SeekBar seekBar){
                //Toast.makeText(getContext(), "Stop tracking blind coverage", Toast.LENGTH_SHORT).show();
            }
        });

        final TextView colorTextView = ((TextView) rootView.findViewById(R.id.lamp_3_color_textview));
        lamp_3_color = bcQuery.getString(bcQuery.getColumnIndex("Color"));
        colorTextView.setText(lamp_3_color);
        int[] colorButtons = new int[]{R.id.colorButton_1, R.id.colorButton_2, R.id.colorButton_3, R.id.colorButton_4};
        for(int cB_id: colorButtons){
            final Button cB = (Button)rootView.findViewById(cB_id);
            cB.setOnClickListener(new View.OnClickListener() {
                //@Override
                public void onClick(View v) {
                    //setColor("Color = " + cB.getText().toString());
                    //colorTextView.setText(getColor());
                    String color = "Color = " + cB.getText().toString();
                    colorTextView.setText(color);
                    ContentValues cv = new ContentValues();
                    cv.put("Color", color);
                    sqlDB.update(LivingRoomDatabaseHelper.TABLE_NAME, cv, "ItemName = ?", new String[]{"Lamp 3"});
                }
            });
        }


        return rootView;
    }

    private View tv_1_view(LayoutInflater inflater, ViewGroup container){
        View rootView = inflater.inflate(R.layout.tv_1_detail, container, false);

        // Show the dummy content as text in a TextView.
        if (mItem != null) {
            //((TextView) rootView.findViewById(R.id.livingroomitem_detail)).setText(mItem.details);
            ((TextView) rootView.findViewById(R.id.tv_1_textview)).setText(mItem);
        }

        Switch tvPower = (Switch) rootView.findViewById(R.id.tv_1_power);
        Cursor powerQuery = sqlDB.query(LivingRoomDatabaseHelper.TABLE_NAME, new String[]{"Switch"}, "ItemName = ?",new String[]{"TV 1"}, null,null,null,null );
        powerQuery.moveToFirst();
        int switch_status = powerQuery.getInt(powerQuery.getColumnIndex("Switch"));
        tv_1_power_status = (switch_status == 0)? false: true;
        tvPower.setChecked(tv_1_power_status);
        final TextView switchText = (TextView)rootView.findViewById(R.id.tv_1_power_textView);
        switchText.setText("Power = " + (tv_1_power_status? "ON": "OFF"));
        tvPower.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                setMItem("The power is " + (isChecked ? "on" : "off"));

                if (isChecked) {
                    // save "on" as status, and pass back to Activity
                    tv_1_power_status = true;
                    switchText.setText("Power = ON");

                } else {
                    // save "off" as status, and pass back to Activity
                    tv_1_power_status = false;
                    switchText.setText("Power = OFF");
                }

                ContentValues cv = new ContentValues();
                cv.put("Switch", (tv_1_power_status? 1:0));
                sqlDB.update(LivingRoomDatabaseHelper.TABLE_NAME, cv, "ItemName = ?", new String[]{"TV 1"});
            }
        });

        //retrieve channel number from database and then display
        Cursor channelQuery = sqlDB.query(LivingRoomDatabaseHelper.TABLE_NAME, new String[]{"Channel"}, "ItemName = ?",new String[]{"TV 1"}, null,null,null,null );
        channelQuery.moveToFirst();
        channelNumber = channelQuery.getInt(channelQuery.getColumnIndex("Channel"));
        final TextView channelText = (TextView)rootView.findViewById(R.id.tv_1_channel_textView);
        channelText.setText("Channel = " + channelNumber);

        //Up button and Down button change the channel number, and store new value to database
        final Button channelUp = (Button)rootView.findViewById(R.id.upButton);
        channelUp.setOnClickListener(new View.OnClickListener() {
            //@Override
            public void onClick(View v) {
                channelNumber++;
                if(channelNumber > 10) channelNumber = 0;//the highest channel is 10
                channelText.setText("Channel = " + channelNumber);
                setMItem("Channel is " + channelNumber);

                ContentValues cv = new ContentValues();
                cv.put("Channel", channelNumber);
                sqlDB.update(LivingRoomDatabaseHelper.TABLE_NAME, cv, "ItemName = ?", new String[]{"TV 1"});
            }
        });

        final Button channelDown = (Button)rootView.findViewById(R.id.downButton);
        channelDown.setOnClickListener(new View.OnClickListener() {
            //@Override
            public void onClick(View v) {
                channelNumber--;
                if(channelNumber < 0) channelNumber = 10; //the lowest channel is 0
                channelText.setText("Channel = " + channelNumber);
                setMItem("Channel is " + channelNumber);

                ContentValues cv = new ContentValues();
                cv.put("Channel", channelNumber);
                sqlDB.update(LivingRoomDatabaseHelper.TABLE_NAME, cv, "ItemName = ?", new String[]{"TV 1"});
            }
        });

        //volumn slider
        final SeekBar volumn = (SeekBar)rootView.findViewById(R.id.tv_1_seekbar);
        //retrieve volumn value (in Progress column) from database, and display
        Cursor volQuery = sqlDB.query(LivingRoomDatabaseHelper.TABLE_NAME, new String[]{"Progress"}, "ItemName = ?",new String[]{"TV 1"}, null,null,null,null );
        volQuery.moveToFirst();
        laudness = volQuery.getInt(volQuery.getColumnIndex("Progress"));
        volumn.setProgress(laudness);

        //left button reduces and right button increases volumn, and store value to database
        final Button leftButton = (Button)rootView.findViewById(R.id.leftButton);
        leftButton.setOnClickListener(new View.OnClickListener() {
            //@Override
            public void onClick(View v) {
                laudness = laudness - 10;
                if(laudness < 0) laudness = 0;
                volumn.setProgress(laudness);
                setMItem("Volumn is " + laudness + "/" + volumn.getMax());

                ContentValues cv = new ContentValues();
                cv.put("Progress", laudness);
                sqlDB.update(LivingRoomDatabaseHelper.TABLE_NAME, cv, "ItemName = ?", new String[]{"TV 1"});
            }
        });

        final Button rightButton = (Button)rootView.findViewById(R.id.rightButton);
        rightButton.setOnClickListener(new View.OnClickListener() {
            //@Override
            public void onClick(View v) {
                laudness = laudness + 10;
                if(laudness > 100) laudness = 100;
                volumn.setProgress(laudness);
                setMItem("Volumn is " + laudness + "/" + volumn.getMax());

                ContentValues cv = new ContentValues();
                cv.put("Progress", laudness);
                sqlDB.update(LivingRoomDatabaseHelper.TABLE_NAME, cv, "ItemName = ?", new String[]{"TV 1"});
            }
        });

        //press enter button show a message
        final Button enterButton = (Button)rootView.findViewById(R.id.enterButton);
        enterButton.setOnClickListener(new View.OnClickListener() {
            //@Override
            public void onClick(View v) {
                Toast.makeText(getContext(), "Enter button pressed!", Toast.LENGTH_SHORT).show();
            }
        });

        //snackbar
        FloatingActionButton fab = (FloatingActionButton)rootView.findViewById(R.id.fab_tv_1_detail);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, getMItem(), Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();//show message in snackbar
            }
        });

        return rootView;
    }

    private View blind_1_view(LayoutInflater inflater, ViewGroup container){
        View rootView = inflater.inflate(R.layout.blind_1_detail, container, false);

        // Show the dummy content as text in a TextView.
        if (mItem != null) {
            //((TextView) rootView.findViewById(R.id.livingroomitem_detail)).setText(mItem.details);
            ((TextView) rootView.findViewById(R.id.blind_1_textview)).setText(mItem);
        }

        final SeekBar blind = (SeekBar)rootView.findViewById(R.id.vertical_blind);//declared as final, so can be accessed by anonymous' inner class methods
        Cursor coverQuery = sqlDB.query(LivingRoomDatabaseHelper.TABLE_NAME, new String[]{"Progress"}, "ItemName = ?",new String[]{"Blind 1"}, null,null,null,null );
        coverQuery.moveToFirst();
        int coverage = coverQuery.getInt(coverQuery.getColumnIndex("Progress"));
        blind.setProgress(coverage);
        final TextView tv = (TextView)rootView.findViewById(R.id.blind_1_seekbar_textview);
        tv.setText("Blind coverage = " + coverage + "/" + blind.getMax());

        blind.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener(){
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser){
                //fromUser = true;
                //Toast.makeText(getContext(), "Blind coverage =  "+ progress + "/" + blind.getMax(), Toast.LENGTH_SHORT).show();
                tv.setText("Blind coverage =  "+ progress + "/" + blind.getMax());

                ContentValues cv = new ContentValues();
                cv.put("Progress", progress);
                sqlDB.update(LivingRoomDatabaseHelper.TABLE_NAME, cv, "ItemName = ?", new String[]{"Blind 1"});
            }

            public void onStartTrackingTouch(SeekBar seekBar){
                //Toast.makeText(getContext(), "Start tracking blind coverage", Toast.LENGTH_SHORT).show();
            }

            public void onStopTrackingTouch(SeekBar seekBar){
                //Toast.makeText(getContext(), "Stop tracking blind coverage", Toast.LENGTH_SHORT).show();
            }
        });

        return rootView;
    }

}
