package com.cst2335.proj;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class HouseTempFragment extends Fragment {

    private static final String TAG = HouseTempFragment.class.getSimpleName();

    public final ArrayList<String> tempSchedulelist = new ArrayList<String>();

    private HouseDataDatabaseHelper houseDataDatabaseHelper;
    private SQLiteDatabase sqlDB;
    private String[] allSchedules = { HouseDataDatabaseHelper.HOUSE_TEMP_KEY_ID,
            HouseDataDatabaseHelper.HOUSE_TEMP_RECORD };

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        Log.i(TAG, "onAttach()");
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.house_menu, menu);
    }

    public boolean onOptionsItemSelected(MenuItem mi) {
        Toast toast;

        switch (mi.getItemId()) {

            case R.id.HowToRun:
                Log.d("Toolbar", "HowToRun selected");

                break;

            case R.id.About:
                Context context = getContext();
                CharSequence text = getResources().getString(R.string.house_menu_about_details);
                int duration = Toast.LENGTH_SHORT;

                toast = Toast.makeText(context, text, duration);
                toast.show();
                break;
        }
        return true;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.i(TAG, "onCreateView()");

        View theView = inflater.inflate(R.layout.fragment_house_temperature, container, false);

        TextView currentTempTextView = (TextView) theView.findViewById(R.id.house_temperature_editText);
        Button addScheduleButton = (Button) theView.findViewById(R.id.house_schedule_plus_button);
        Button removeScheduleButton = (Button) theView.findViewById(R.id.house_schedule_minus_button);
        ListView scheduleList = (ListView) theView.findViewById(R.id.house_temp_listView);
        scheduleList.setChoiceMode(ListView.CHOICE_MODE_SINGLE);

        //it creates a temporary HouseDataDatabaseHelper object,
        //which then gets a writeable database and stores that as an instance variable.
        houseDataDatabaseHelper = new HouseDataDatabaseHelper(getContext());
        sqlDB = houseDataDatabaseHelper.getWritableDatabase();

        //After opening the database, execute a query for any existing cities and add them
        //into the ArrayList of schedules
        Cursor cursor = sqlDB.query(HouseDataDatabaseHelper.HOUSE_TEMP_TABLE_NAME,
                allSchedules, null, null, null, null, null);

        cursor.moveToFirst();

        while(!cursor.isAfterLast()) {
            String schedule = cursor.getString( cursor.getColumnIndex( HouseDataDatabaseHelper.HOUSE_TEMP_TABLE_NAME) );
            Log.i(TAG, "SQL schedule details :" + schedule );
            tempSchedulelist.add(schedule);
            cursor.moveToNext();
        }

        int cursorColumnCount = cursor.getColumnCount();
        Log.i(TAG, "Cursor’s column count =" + cursorColumnCount );
        for(int j=0; j<cursorColumnCount; j++) {
            Log.i(TAG, "Cursor’s column name =" + cursor.getColumnName(j) );
        }

        // make sure to close the cursor
        cursor.close();

        final ArrayAdapter<String> houseTempAdapter = new HouseTempAdapter(getContext(), tempSchedulelist);
        scheduleList.setAdapter(houseTempAdapter);
        scheduleList.setChoiceMode(ListView.CHOICE_MODE_SINGLE);

        addScheduleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                String city = editText.getText().toString().trim();
//                if( city.length() == 0 || existCityRecord(city)) {
//                    AlertDialog.Builder builder1 = new AlertDialog.Builder(getContext());
//                    builder1.setMessage("Please write a valid city name, and no duplicate city");
//                    builder1.setCancelable(true);
//
//                    AlertDialog alert11 = builder1.create();
//                    alert11.show();
//                }else {
//                    list.add(city.trim());
//                    editText.setText(""); //clear the text
//                    cityAdapter.notifyDataSetChanged();
//
//                    ContentValues values = new ContentValues();
//                    values.put(HouseDataDatabaseHelper.CITIES_NAME, city);
//                    sqlDB.insert(HouseDataDatabaseHelper.CITIES_TABLE_NAME, null,
//                            values);
//                }
            }
        });


        //========  last part =====
        Button mainButton = (Button) theView.findViewById(R.id.houseTempMainButton);
        mainButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), NavigateToolbar.class);
                startActivity(intent);
            }
        });

        return theView;
    }
}

//Inner class
class HouseTempAdapter extends ArrayAdapter<String> {

    private final ArrayList<String> list;
    private final Context context;

    HouseTempAdapter(Context context, ArrayList<String> list) {
        super(context, 0, list);
        this.list = list;
        this.context = context;
    }

    public int getCount() {
        return list.size();
    }

    public String getItem(int position) {
        return list.get(position);
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View result = inflater.inflate(R.layout.house_list_row, null);
        TextView tempSchedule = (TextView)result.findViewById(R.id.list_text);
        tempSchedule.setText( getItem(position) ); // get the string at position

        return result;
    }
}
