package com.cst2335.proj;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.util.ArrayList;

public class KitchenActivity extends AppCompatActivity {

    protected static final String ACTIVITY_NAME = "KitchenActivity";

    private boolean isTablet;

    String message_display_choice1 = "You selected Microwave";
    String message_display_choice2 = "You selected Fridge";
    String message_display_choice3 = "You selected Main Light";


    ListView list;
    Button addButton, deleteButton;
    ArrayList<String> itemname = new ArrayList<String>();
    ArrayList<Integer> imgid = new ArrayList<Integer>();
    CustomListAdapter adapter;
    AlertDialog addDialog, deleteDialog;
    ProgressBar progressBar;

    KitchenDatabaseHelper kDbHelper;
    SQLiteDatabase kDb;
    ContentValues cv = new ContentValues();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kitchen_list);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (findViewById(R.id.kitchen_detail_container) != null) {
            isTablet = true;
        }

        progressBar = (ProgressBar) findViewById(R.id.progress_Bar);
        progressBar.setVisibility(View.VISIBLE);


        kDbHelper = new KitchenDatabaseHelper(this);
        kDb = kDbHelper.getWritableDatabase();

        new LoadData().execute();

/*        String q = "SELECT * FROM KITCHEN_TABLE";
        Cursor c = kDb.rawQuery(q, null);
        Log.i(ACTIVITY_NAME, "row number: " + c.getCount());
        c.moveToFirst();
        while (!c.isAfterLast()){
            String app_type = c.getString(c.getColumnIndex(kDbHelper.APPLIANCE_TYPE));
            itemname.add(app_type);
            if (app_type.equals("Microwave")){
                imgid.add(R.drawable.microwave);
            }
            else if (app_type.equals("Fridge")){
                imgid.add(R.drawable.fridge);
            }
            else {
                imgid.add(R.drawable.light);
            }

            c.moveToNext();
        }
        c.close();*/

        addButton = (Button)findViewById(R.id.add_button);

        addButton.setOnClickListener(new Button.OnClickListener(){
            @Override
            public void onClick(View v) {
                addDialog = new AlertDialog.Builder(KitchenActivity.this).create();
                addDialog.setTitle("Please choose one to add");
                addDialog.setButton(AlertDialog.BUTTON_POSITIVE, "Microwave",
                        new DialogInterface.OnClickListener(){
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        cv.put("appliance", "Microwave");
                        cv.put("setting", "00:00");
                        kDb.insert(kDbHelper.TABLE_NAME, "Null replacement value", cv);
                        cv.clear();
                        itemname.add("Microwave");
                        imgid.add(R.drawable.microwave);
                        adapter.notifyDataSetChanged();
                    }
                });
                addDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "Fridge",
                        new DialogInterface.OnClickListener(){
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        cv.put("appliance", "Fridge");
                        cv.put("setting", "2,-20");
                        kDb.insert(kDbHelper.TABLE_NAME, "Null replacement value", cv);
                        cv.clear();
                         itemname.add("Fridge");
                         imgid.add(R.drawable.fridge);
                        adapter.notifyDataSetChanged();
                    }
                });

                addDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "Main Light",
                        new DialogInterface.OnClickListener(){
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        cv.put("appliance", "Main Light");
                        cv.put("setting", "off,dimmable");
                        kDb.insert(kDbHelper.TABLE_NAME, "Null replacement value", cv);
                        cv.clear();
                         itemname.add("Main Light");
                         imgid.add(R.drawable.light);
                        adapter.notifyDataSetChanged();
                    }
                });

                 addDialog.show();

            }
        });


        deleteButton = (Button)findViewById(R.id.delete_button);

        deleteButton.setOnClickListener(new Button.OnClickListener(){
            @Override
            public void onClick(View v) {
                createDeleteDialog().show();
            }
        });

      /*  FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });*/




        adapter=new CustomListAdapter(this, itemname, imgid);
        list=(ListView)findViewById(R.id.list);
        list.setAdapter(adapter);
        Log.i(ACTIVITY_NAME, "clicked" );
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                final View v = view;

                Log.i(ACTIVITY_NAME, "clicked2" );
                String Selecteditem = itemname.get(position);
                Toast.makeText(getApplicationContext(), Selecteditem, Toast.LENGTH_LONG).show();
                int row=position+1;
       //         String q = "SELECT * FROM KITCHEN_TABLE WHERE id =" + Integer.toString(row);
                String q = "SELECT * FROM KITCHEN_TABLE LIMIT 1 OFFSET " + Integer.toString(position);
                Cursor d = kDb.rawQuery(q, null);
                Log.i(ACTIVITY_NAME, "row number2: " + d.getCount());
                if (d!=null){
                    d.moveToFirst();
                    String type = d.getString(d.getColumnIndex(kDbHelper.APPLIANCE_TYPE));
                    String setting = d.getString(d.getColumnIndex(kDbHelper.APPLIANCE_SETTING));
                    if(isTablet){
                        Bundle arguments = new Bundle();// bundle stores data
                        //pass the messageText to fragment
                        arguments.putString(Kitchen_Detail_Fragment.ARG_ITEM_ID_1, type);
                        arguments.putString(Kitchen_Detail_Fragment.ARG_ITEM_ID_2, setting);
                        Kitchen_Detail_Fragment fragment = new Kitchen_Detail_Fragment();

                        fragment.setArguments(arguments);
                        getSupportFragmentManager().beginTransaction()
                                .replace(R.id.kitchen_detail_container, fragment)
                                .commit();
                    }
                    else{
                        Context context = v.getContext();
                        Intent intent = new Intent(context, Kitchen_Detail_Activity.class);//phone version: start DetailActivity
                        intent.putExtra(Kitchen_Detail_Fragment.ARG_ITEM_ID_1, type);
                        intent.putExtra(Kitchen_Detail_Fragment.ARG_ITEM_ID_2, setting);
                        context.startActivity(intent);
                    }
                }
                d.close();

            }
        });
    }

    private Dialog createDeleteDialog(){


        final AlertDialog.Builder builder = new AlertDialog.Builder(KitchenActivity.this);
        // Get the layout inflater
        final LayoutInflater inflater = KitchenActivity.this.getLayoutInflater();

        final View inf = inflater.inflate(R.layout.kitchen_delete_dialog, null);

        // Inflate and set the layout for the dialog
        // Pass null as the parent view because its going in the dialog layout
        builder.setView(inf);
        //Add action buttons

        builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User clicked OK button

                final EditText ed5 = (EditText) inf.findViewById(R.id.delete_choice);

                String delete_number = ed5.getText().toString();
                int row_number = Integer.parseInt(delete_number);
//                kDb.delete("KITCHEN_TABLE","id =" + delete_number, null);

                String q = "DELETE FROM KITCHEN_TABLE WHERE id = " + delete_number;
                kDb.execSQL(q);
                itemname.remove(row_number-1);
                imgid.remove(row_number-1);
                adapter.notifyDataSetChanged();

            }
        });

        builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User cancelled the dialog
            }
        });
        // Create the AlertDialog
        return builder.create();

    }

    @Override
    public void onDestroy(){
        super.onDestroy();;
        if (kDb!=null){
            kDb.close();
        }

    }
    public boolean onCreateOptionsMenu (Menu m){
        getMenuInflater().inflate(R.menu.kitchen_toolbar_menu, m );
        return true;

    }

    public boolean onOptionsItemSelected(MenuItem mi){

        switch (mi.getItemId()) {
            case R.id.action_one:
                Snackbar.make(this.findViewById(android.R.id.content), message_display_choice1,
                        Snackbar.LENGTH_LONG).setAction("Action", null).show();
                Log.d("Toolbar", "Option 1 selected");
                break;
            case R.id.action_two:
                Snackbar.make(this.findViewById(android.R.id.content), message_display_choice2,
                        Snackbar.LENGTH_LONG).setAction("Action", null).show();
                Log.d("Toolbar", "Option 2 selected");

                break;
            case R.id.action_three:
                Snackbar.make(this.findViewById(android.R.id.content), message_display_choice3,
                        Snackbar.LENGTH_LONG).setAction("Action", null).show();
                Log.d("Toolbar", "Option 3 selected");

                break;
            case R.id.action_setting:
                Toast toast = Toast.makeText(getApplicationContext(),
                        "Kitchen Control App. Version 1.0 by Ji Hong Yao", Toast.LENGTH_LONG);
                toast.show();
                break;
            default:
                return super.onOptionsItemSelected(mi);
        }
        return true;

    }


    class LoadData extends AsyncTask<String, Integer, String> {


        @Override
        protected String doInBackground(String... args) {

            String q = "SELECT * FROM KITCHEN_TABLE";
            Cursor c = kDb.rawQuery(q, null);
            Log.i(ACTIVITY_NAME, "row number: " + c.getCount());
            int counter = 1, totalNumber = c.getCount(), progressNumber = 0;
            if (totalNumber ==0){
                publishProgress(100);
            }
            c.moveToFirst();
            while (!c.isAfterLast()){
                String app_type = c.getString(c.getColumnIndex(kDbHelper.APPLIANCE_TYPE));
                itemname.add(app_type);
                if (app_type.equals("Microwave")){
                    imgid.add(R.drawable.microwave);
                }
                else if (app_type.equals("Fridge")){
                    imgid.add(R.drawable.fridge);
                }
                else {
                    imgid.add(R.drawable.light);
                }
                progressNumber = (int) ((counter*1.0/totalNumber)*100);
                publishProgress(progressNumber);
                Log.i(ACTIVITY_NAME, "progress number =  " + progressNumber);

                counter++;

                c.moveToNext();
            }
            c.close();

            return null;
        }



        @Override
        protected void onProgressUpdate(Integer... values) {

            progressBar.setVisibility(View.VISIBLE);
            progressBar.setProgress(values[0]);


        }

        @Override
        protected void onPostExecute(String result) {

            progressBar.setVisibility(View.INVISIBLE);
        }

    }

}
