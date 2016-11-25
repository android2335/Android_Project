package com.cst2335.proj;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

public class LilianActivity extends AppCompatActivity {
    protected static final String ACTIVITY_NAME = "LilianActivity";

    ListView list;
    String[] itemname ={
            "Microwave",
            "Fridge",
            "Main Light",

    };

    Integer[] imgid={
            R.drawable.microwave,
            R.drawable.fridge,
            R.drawable.light,
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lilian);
        CustomListAdapter adapter=new CustomListAdapter(this, itemname, imgid);
        list=(ListView)findViewById(R.id.list);
        list.setAdapter(adapter);
        Log.i(ACTIVITY_NAME, "clicked" );
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                Log.i(ACTIVITY_NAME, "clicked2" );
                String Selecteditem = itemname[position];
                Toast.makeText(getApplicationContext(), Selecteditem, Toast.LENGTH_LONG).show();


            }
        });
    }
}
