package com.cst2335.proj;

import android.app.Dialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * An activity representing a list of LivingRoomItems. This activity
 * has different presentations for handset and tablet-size devices. On
 * handsets, the activity presents a list of items, which when touched,
 * lead to a {@link LivingRoomItemDetailActivity} representing
 * item details. On tablets, the activity presents the list of items and
 * item details side-by-side using two vertical panes.
 */
public class LivingRoomItemListActivity extends AppCompatActivity {
    private static final String ACTIVITY_NAME = "LivingRoomListActivity";
    private boolean mTwoPane;//Whether or not the activity is in two-pane mode, i.e. running on a tablet
    private ArrayList<String> livingRoomItemList = new ArrayList<>();
    protected SQLiteDatabase sqlDB;
    private String newMessage;

    private void setMessage(String newMessage){
        this.newMessage = newMessage;
    }

    String getMessage(){
        return newMessage;
    }

    public boolean onCreateOptionsMenu (Menu m){
        getMenuInflater().inflate(R.menu.living_room_menu, m );

        return true;
    }

    public boolean onOptionsItemSelected(MenuItem mi){
        switch(mi.getItemId()){
            case R.id.menu_help:
                Toast.makeText(LivingRoomItemListActivity.this, "Help Menu clicked",Toast.LENGTH_LONG).show();

                final Dialog dialog = new Dialog(this);
                dialog.setContentView(R.layout.dialog_help);
                dialog.setTitle("Help Instruction");

                // set the custom dialog components - text, image and button
                TextView help_header = (TextView) dialog.findViewById(R.id.helpHeader);
                help_header.setText(R.string.help_header);

                TextView help_author = (TextView) dialog.findViewById(R.id.helpAuthor);
                help_author.setText(R.string.help_author);

                TextView help_version = (TextView) dialog.findViewById(R.id.helpVersion);
                help_version.setText(R.string.help_version);

                TextView help_body = (TextView) dialog.findViewById(R.id.helpBody);
                help_body.setText(R.string.help_body);

                Button dialogButton = (Button) dialog.findViewById(R.id.dialogButtonOK);
                // if button is clicked, close the dialog
                dialogButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });

                dialog.show();

                return true;
        }
        return false;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_livingroomitem_list);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle(getTitle());

        /*//snackbar
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, getMessage(), Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();//show newMessage in snackbar
            }
        });//--- not show snackbar in leftside (fragment on the right ---- */

        if (findViewById(R.id.livingroomitem_detail_container) != null) {
            mTwoPane = true;
        }

        LivingRoomDatabaseHelper lrAdapter = new LivingRoomDatabaseHelper(this);
        sqlDB = lrAdapter.getWritableDatabase();
        final ListView listView = (ListView)findViewById(R.id.living_room_listview);//living_room_listview is in livingroomitem_list_content.xml
        /*//ArrayAdapter<Object> subclass = ListView adapter, but using SimpleCursorAdapter is simpler!!!!
        final ArrayAdapter livingRoomAdapter = new LivingRoomAdapter(LivingRoomItemListActivity.this, 0, livingRoomItemList);
        livingRoomAdapter.notifyDataSetChanged();
        listView.setAdapter(livingRoomAdapter);//*/

        Cursor queryResult = sqlDB.query(LivingRoomDatabaseHelper.TABLE_NAME, new String[]{"rowid as _id", "ItemName", "ClickCount"},null, null,null,null,"ClickCount DESC" );
        //must need rowid ! it is automatically added to database by the system
        listView.setAdapter(new SimpleCursorAdapter(LivingRoomItemListActivity.this,
                R.layout.living_room_listview_row, queryResult, new String[]{"ItemName","ClickCount"},
                new int[]{R.id.item_name, R.id.item_click_count}, 0));

        queryResult.moveToFirst();

        final EditText addItemText = (EditText)findViewById(R.id.add_item_text);
        Button addItemButton = (Button)findViewById(R.id.add_item_button);
        addItemButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                ContentValues newValues = new ContentValues();
                newValues.put("ItemName", addItemText.getText().toString());
                sqlDB.insert(LivingRoomDatabaseHelper.TABLE_NAME, null, newValues);
                //Cursor queryResult = sqlDB.rawQuery("select rowid as _id, * from " + LivingRoomDatabaseHelper.TABLE_NAME, null);
                Cursor queryQuery = sqlDB.query(LivingRoomDatabaseHelper.TABLE_NAME, new String[]{"rowid as _id", "ItemName", "ClickCount"},null, null,null,null,"ClickCount DESC" );
                listView.setAdapter(new SimpleCursorAdapter(LivingRoomItemListActivity.this,
                        R.layout.living_room_listview_row,
                        //queryResult, //keep list order after add item
                        queryQuery,
                        new String[]{"ItemName","ClickCount"},
                        new int[]{R.id.item_name, R.id.item_click_count}, 0));
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id){
                AlertDialog.Builder builder = new AlertDialog.Builder(LivingRoomItemListActivity.this);
                final int list_position = position;
                final long table_id = id;
                final View v = view;
                //retrieve the text of item being clicked on
                TextView tv = (TextView)view.findViewById(R.id.item_name);
                final String itemSelected = tv.getText().toString();
                Toast.makeText(LivingRoomItemListActivity.this, "item clicked =" + itemSelected,Toast.LENGTH_LONG).show();
                builder.setMessage(R.string.dialog_message)//.setTitle(R.string.dialog_title)
                        .setPositiveButton(R.string.delete_item, new DialogInterface.OnClickListener(){
                            public void onClick(DialogInterface dialog, int id){
                                sqlDB.delete(LivingRoomDatabaseHelper.TABLE_NAME, "rowid = ?",
                                        new String[]{Long.toString(table_id)});
                                Toast.makeText(LivingRoomItemListActivity.this,"Deleted item at position: " +
                                list_position + ", id = " + table_id, Toast.LENGTH_LONG).show();

                                //refresh listview
                                //Cursor queryResult = sqlDB.rawQuery("select rowid as _id, * from " + LivingRoomDatabaseHelper.TABLE_NAME, null);
                                Cursor queryQuery = sqlDB.query(LivingRoomDatabaseHelper.TABLE_NAME, new String[]{"rowid as _id", "ItemName", "ClickCount"},null, null,null,null,"ClickCount DESC" );
                                listView.setAdapter(new SimpleCursorAdapter(LivingRoomItemListActivity.this,
                                        R.layout.living_room_listview_row,
                                        //queryResult,
                                        queryQuery,
                                        new String[]{"ItemName","ClickCount"},
                                        new int[]{R.id.item_name, R.id.item_click_count}, 0));
                            }
                        })
                        .setNegativeButton(R.string.show_detail, new DialogInterface.OnClickListener(){
                            public void onClick(DialogInterface dialog, int id){
                                //update database
                                Cursor clickcountQuery = sqlDB.query(LivingRoomDatabaseHelper.TABLE_NAME, new String[]{"rowid as _id", "ItemName", "ClickCount"}, "rowid = ?",new String[]{Long.toString(table_id)}, null,null,null,null );
                                clickcountQuery.moveToFirst();//Very very important! before retrieve column and row
                                int clickcount = clickcountQuery.getInt(clickcountQuery.getColumnIndex("ClickCount")) + 1;//Error: android.database.CursorIndexOutOfBoundsException: Index -1 requested, with a size of 1
                                //getInt(columnIndex = -1)!because of no "clickcountQuery.moveToFirst();" line
                                Toast.makeText(LivingRoomItemListActivity.this, "ClickCount =" + clickcount,Toast.LENGTH_LONG).show();

                                //Cursor updateQuery = sqlDB.rawQuery("update " + LivingRoomDatabaseHelper.TABLE_NAME + "set ClickCount = ? where rowid = ?", new String[]{Integer.toString(clickcount),Long.toString(table_id)});
                                ContentValues countUpdate = new ContentValues();
                                countUpdate.put("ClickCount", clickcount);
                                sqlDB.update(LivingRoomDatabaseHelper.TABLE_NAME, countUpdate, "rowid = ?", new String[]{Long.toString(table_id)});
                                //Cursor reloadQuery = sqlDB.rawQuery("select rowid as _id, * from " + LivingRoomDatabaseHelper.TABLE_NAME + "order by ClickCount DESC", null);
                                Cursor reloadQuery = sqlDB.query(LivingRoomDatabaseHelper.TABLE_NAME, new String[]{"rowid as _id", "ItemName", "ClickCount"},null, null,null,null,"ClickCount DESC" );
                                listView.setAdapter(new SimpleCursorAdapter(LivingRoomItemListActivity.this,
                                        R.layout.living_room_listview_row, reloadQuery, new String[]{"ItemName","ClickCount"},
                                        new int[]{R.id.item_name, R.id.item_click_count}, 0));

                                //display second fragment showing detail of item
                                final String messageText = clickcountQuery.getString(clickcountQuery.getColumnIndex("ItemName"));
                                if (mTwoPane) {
                                    Bundle arguments = new Bundle();// bundle stores data
                                    //pass the messageText to fragment
                                    arguments.putString(LivingRoomItemDetailFragment.ARG_ITEM_ID, messageText);
                                    LivingRoomItemDetailFragment fragment = new LivingRoomItemDetailFragment();
                                    //LivingRoomItemDetailFragment fragment = (LivingRoomItemDetailFragment) getSupportFragmentManager().findFragmentById(R.id.what_fragment??);

                                    fragment.setArguments(arguments);
                                    getSupportFragmentManager().beginTransaction()
                                            .replace(R.id.livingroomitem_detail_container, fragment)
                                            .commit();

                                } else {//NullPointerException: Attempt to read from field 'java.lang.String com.example.longquan.smart_home.dummy.DummyContent$DummyItem.content' on a null object reference
                                    Context context = v.getContext();
                                    Intent intent = new Intent(context, LivingRoomItemDetailActivity.class);//phone version: start DetailActivity
                                    intent.putExtra(LivingRoomItemDetailFragment.ARG_ITEM_ID, messageText);

                                    context.startActivity(intent);
                                }
                            }
                        })
                        .show();
            }
        });

    }

}
