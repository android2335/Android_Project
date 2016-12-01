package com.cst2335.proj;

import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
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
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
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

    String minTep, maxTep, currentTep;
    TextView first, second, third;
    ProgressBar loadingImageBar;
    ImageView weatherView;
    Bitmap currentWeatherBitMap;
    String iconName;
    private int state;

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
        inflater.inflate(R.menu.house_menu, menu );
    }

    public boolean onOptionsItemSelected(MenuItem mi) {

        switch (mi.getItemId()) {

            case R.id.HowToRun:
                Log.d("Toolbar", "HowToRun selected");
                break;

            case R.id.About:
                Context context = getContext();
                CharSequence text = "Version 1.0, by Yu Hou";
                int duration = Toast.LENGTH_SHORT;

                Toast toast = Toast.makeText(context, text, duration);
                toast.show();
                break;
        }
        return true;
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
                if(weatherCity.trim().length() > 0) {
                    String query = "http://api.openweathermap.org/data/2.5/weather?q=" + weatherCity.trim() + ",ca&APPID=d99666875e0e51521f0040a3d97d0f6a&mode=xml&units=metric";
                    new ForecastQuery().execute(query);
                }
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
        first = (TextView) theView.findViewById(R.id.weather_current_temperature_textview);
        second = (TextView) theView.findViewById(R.id.weather_min_temperature_textview);
        third = (TextView) theView.findViewById(R.id.weather_max_temperature_textview);

        loadingImageBar = (ProgressBar) theView.findViewById(R.id.weather_progressbar);
        loadingImageBar.setMax(3);
        loadingImageBar.setVisibility(View.VISIBLE);

        weatherView = (ImageView) theView.findViewById(R.id.weather_image_view);

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

    public boolean fileExistance(String fname) {
        File file = getContext().getFileStreamPath(fname);
        return file.exists();
    }

    private class ForecastQuery extends AsyncTask<String, Integer, String> {

        public String doInBackground(String... args) {
            state = 0;

            HttpURLConnection urlConnection = null;
            try {
                URL url = new URL(args[0]);
                urlConnection = (HttpURLConnection) url.openConnection();
                InputStream istream = urlConnection.getInputStream();

                XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
                factory.setNamespaceAware(false);
                XmlPullParser xpp = factory.newPullParser();

                xpp.setInput(istream, "UTF8");
                int type = XmlPullParser.START_DOCUMENT;

                while (type != XmlPullParser.END_DOCUMENT) {

                    switch (type) {
                        case XmlPullParser.START_DOCUMENT:
                            break;
                        case XmlPullParser.END_DOCUMENT:
                            break;
                        case XmlPullParser.START_TAG:
                            String name = xpp.getName();
                            if (name.equals("temperature")) {
                                currentTep = xpp.getAttributeValue(null, "value");
                                publishProgress(25);
                                minTep = xpp.getAttributeValue(null, "min");
                                publishProgress(50);
                                maxTep = xpp.getAttributeValue(null, "max");
                                publishProgress(75);
                            }
                            if (name.equals("weather")) {
                                iconName = xpp.getAttributeValue(null, "icon");
                            }
                            break;
                        case XmlPullParser.END_TAG:
                            break;
                        case XmlPullParser.TEXT:
                            break;
                    }
                    type = xpp.next(); //advances to next xml event
                }
            } catch (Exception e) {
                Log.e("XML PARSING", e.getMessage());
            } finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
            }

            return null;
        }

        public void onProgressUpdate(Integer... updateInfo) {
            loadingImageBar.setVisibility(View.VISIBLE);

            switch (state++) {
                case 0:
                    loadingImageBar.setProgress(updateInfo[0]);
                    break;
                case 1:
                    loadingImageBar.setProgress(updateInfo[0]);
                    break;
                case 2:
                    loadingImageBar.setProgress(updateInfo[0]);
                    break;
            }

            if (iconName != null) {
                String imageURL = "http://openweathermap.org/img/w/" + iconName + ".png";
                String fileName = iconName + ".png";
                boolean exist = fileExistance(fileName);
                if (exist) {
                    Log.i(TAG, fileName + " exists and no need to download again!");
                    FileInputStream fis = null;
                    File file = getContext().getFileStreamPath(fileName);
                    try {
                        fis = new FileInputStream(file);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                        currentWeatherBitMap = null;
                    }
                    currentWeatherBitMap = BitmapFactory.decodeStream(fis);
                } else {
                    Log.i(TAG, fileName + " does not exist and need to download!");
                    new DownloadBitmap().execute(imageURL);
                }

                if (currentWeatherBitMap == null) {
                    new DownloadBitmap().execute(imageURL);
                }
            }
        }

        public void onPostExecute(String result) {
            first.setText("current temperature " + currentTep + "°C");
            second.setText("min temperature " + minTep + "°C");
            third.setText("max temperature " + maxTep + "°C");
            if (currentWeatherBitMap != null) {
                weatherView.setImageBitmap(currentWeatherBitMap);
            }
            loadingImageBar.setVisibility(View.INVISIBLE);
        }
    }


    private class DownloadBitmap extends AsyncTask<String, Integer, String> {

        public String doInBackground(String... args) {
            HttpURLConnection connection = null;
            try {
                URL url = new URL(args[0]);
                connection = (HttpURLConnection) url.openConnection();
                connection.setDoInput(true);
                connection.connect();
                int responseCode = connection.getResponseCode();
                if (responseCode == 200) {
                    currentWeatherBitMap = BitmapFactory.decodeStream(connection.getInputStream());
                    try {
                        FileOutputStream fos = getContext().openFileOutput(iconName + ".png", Context.MODE_PRIVATE);
                        ByteArrayOutputStream outstream = new ByteArrayOutputStream();
                        currentWeatherBitMap.compress(Bitmap.CompressFormat.PNG, 80, outstream);
                        byte[] byteArray = outstream.toByteArray();
                        fos.write(byteArray);
                        fos.close();
                        publishProgress(100);
                    } catch (Exception e) {
                        Log.i(TAG, "DownloadBitmap doInBackground with Exception " + e.getMessage());
                    }
                } else {
                    return null;
                }
                return null;
            } catch (Exception e) {
                Log.i(TAG, "DownloadBitmap doInBackground with Exception " + e.getMessage());
                return null;
            } finally {
                if (connection != null) {
                    connection.disconnect();
                }
            }
        }

        public void onProgressUpdate(Integer... updateInfo) {
            loadingImageBar.setProgress(updateInfo[0]);
        }

        public void onPostExecute(String result) {
            weatherView.setImageBitmap(currentWeatherBitMap);
        }
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
