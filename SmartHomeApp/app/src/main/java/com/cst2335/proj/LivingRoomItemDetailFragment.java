package com.cst2335.proj;

import android.app.Activity;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
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
    private static double blind_1_cover = 1;

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
                appBarLayout.setTitle("Detailed Message");
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
                    switchText.setText("Switch status = ON");

                } else {
                    // save "off" as status, and pass back to Activity
                    lamp_1_switch_status = false;
                    switchText.setText("Switch status = OFF");
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

    /*//move this class to inside the method lamp_2_view()
    private class DBTask_Lamp2_1 extends AsyncTask<String, Integer, Cursor> {

        @Override
        protected Cursor doInBackground(String... args) {
            Cursor bQuery = sqlDB.query(LivingRoomDatabaseHelper.TABLE_NAME, new String[]{args[0]}, "ItemName = ?",new String[]{args[1]}, null,null,null,null );
            //bQuery.moveToFirst();
            int brightness = bQuery.getInt(bQuery.getColumnIndex(args[0]));
            publishProgress(brightness);
            if (bQuery != null) {
                return bQuery;
            } else
                return null;
        }

        @Override
        protected void onProgressUpdate(Integer... b){
            View rootView = inflater.inflate(R.layout.lamp_2_detail, container, false);
            lamp_2_brightness = (SeekBar) rootView.findViewById(R.id.lamp_2_seekbar);
            lamp_2_brightness.setProgress(b[0]);
        }

        @Override
        protected void onPostExecute(Cursor result) {
            if (result != null) {
                result.moveToFirst();
            }
        }
    }//*/

    /*//move this class to inside the method lamp_2_view()
    private class DBTask_Lamp2_2 extends AsyncTask<ContentValues, Void, Boolean> {

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
    }//*/

}
