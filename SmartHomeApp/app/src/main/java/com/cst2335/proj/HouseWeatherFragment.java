package com.cst2335.proj;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

public class HouseWeatherFragment extends Fragment {

    private static final String TAG = HouseWeatherFragment.class.getSimpleName();
    private Button mainButton;

    protected EditText editText;
    protected ListView cityList;
    private int listItemPosition = -1;
    protected Button   addCityButton;
    protected Button   removeCityButton;
    public final ArrayList<String> list = new ArrayList<String>();

    private HouseDataDatabaseHelper houseDataDatabaseHelper;
    private SQLiteDatabase sqlDB;
    private String[] allCities = { HouseDataDatabaseHelper.CITIES_KEY_ID,
            HouseDataDatabaseHelper.CITIES_NAME };

    private String weatherCity = "";

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
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.i(TAG, "onCreateView()");

        View theView = inflater.inflate(R.layout.fragment_house_weather, container, false);

        final EditText editText = (EditText) theView.findViewById(R.id.houseCityEditText);
        Button addCityButton = (Button) theView.findViewById(R.id.houseAddCityButton);
        Button removeCityButton = (Button) theView.findViewById(R.id.houseRemoveCityButton);
        ListView cityList = (ListView) theView.findViewById(R.id.houseCitiesListView);
        //cityList.setChoiceMode(ListView.CHOICE_MODE_SINGLE);

        //it creates a temporary HouseDataDatabaseHelper object,
        //which then gets a writeable database and stores that as an instance variable.
        houseDataDatabaseHelper = new HouseDataDatabaseHelper(getContext());
        sqlDB = houseDataDatabaseHelper.getWritableDatabase();

        //After opening the database, execute a query for any existing cities and add them
        //into the ArrayList of cities
        Cursor cursor = sqlDB.query(HouseDataDatabaseHelper.CITIES_TABLE_NAME,
                allCities, null, null, null, null, null);

        cursor.moveToFirst();

        while(!cursor.isAfterLast()) {
            String city = cursor.getString( cursor.getColumnIndex( HouseDataDatabaseHelper.CITIES_NAME) );
            Log.i(TAG, "SQL city name :" + city );
            list.add(city);
            cursor.moveToNext();
        }

        int cursorColumnCount = cursor.getColumnCount();
        Log.i(TAG, "Cursor’s column count =" + cursorColumnCount );
        for(int j=0; j<cursorColumnCount; j++) {
            Log.i(TAG, "Cursor’s column name =" + cursor.getColumnName(j) );
        }

        // make sure to close the cursor
        cursor.close();

        final ArrayAdapter<String> cityAdapter = new CityAdapter(getContext(), list);
        cityList.setAdapter(cityAdapter);
        cityList.setChoiceMode(ListView.CHOICE_MODE_SINGLE);

        cityList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                weatherCity = list.get(position);
            }
        });

        addCityButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String city = editText.getText().toString().trim();
                if( city.length() == 0 || existCityRecord(city)) {
                    AlertDialog.Builder builder1 = new AlertDialog.Builder(getContext());
                    builder1.setMessage("Please write a valid city name, and no duplicate city");
                    builder1.setCancelable(true);

                    AlertDialog alert11 = builder1.create();
                    alert11.show();
                }else {
                    list.add(city.trim());
                    editText.setText(""); //clear the text
                    cityAdapter.notifyDataSetChanged();

                    ContentValues values = new ContentValues();
                    values.put(HouseDataDatabaseHelper.CITIES_NAME, city);
                    sqlDB.insert(HouseDataDatabaseHelper.CITIES_TABLE_NAME, null,
                            values);
                }
            }
        });

        removeCityButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String city = editText.getText().toString().trim();

                if( city.length() == 0 || !existCityRecord(city)) {
                    AlertDialog.Builder builder1 = new AlertDialog.Builder(getContext());
                    builder1.setMessage("Please write a valid city name");
                    builder1.setCancelable(true);

                    AlertDialog alert11 = builder1.create();
                    alert11.show();
                }
                else {
                    list.remove(city.trim());
                    editText.setText(""); //clear the text
                    cityAdapter.notifyDataSetChanged();

                    sqlDB.delete(HouseDataDatabaseHelper.CITIES_TABLE_NAME,
                            HouseDataDatabaseHelper.CITIES_NAME + "=\'" + city + "\'", null);
                }
            }
        });

        //========  weather display part =====
        

        //========  last part =====
        mainButton = (Button) theView.findViewById(R.id.houseWeatherMainButton);
        mainButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), NavigateToolbar.class);
                startActivity(intent);
            }
        });

        return theView;
    }

    private boolean existCityRecord(String city) {
        return list.contains(city);
    }
}

//Inner class
class CityAdapter extends ArrayAdapter<String> {

    private final ArrayList<String> list;
    private final Context context;

    CityAdapter(Context context, ArrayList<String> list) {
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
        View result = inflater.inflate(R.layout.house_city_row, null);
        TextView city = (TextView)result.findViewById(R.id.city_text);
        city.setText( getItem(position) ); // get the string at position



        return result;
    }
}
