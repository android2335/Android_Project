package com.cst2335.proj;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;

import java.util.ArrayList;
import java.util.List;

public class AutomobileItemEdit extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.automobileitem_edit);

        //item list
        final Spinner spinAdd = (Spinner)findViewById(R.id.spinner_add);
        Spinner spinRemove = (Spinner)findViewById(R.id.spinner_remove);

        // Spinner Drop down elements
        List<String> categories_add = new ArrayList<String>();
        List<String> categories_remove = new ArrayList<String>();

        ArrayList<Integer> itemNos = AutomobileDatabaseOperate.getItemNo();
        int i = 0;
        for(Integer item: itemNos) {
            if (item > 0) {
                categories_remove.add(AutomobileDatabaseOperate.itemName.get(i));
            }
            else {
                categories_add.add(AutomobileDatabaseOperate.itemName.get(i));
            }
            i++;
        }

        // Creating adapter for spinner
        ArrayAdapter<String> dataAdapter_add = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, categories_add);
        ArrayAdapter<String> dataAdapter_remove = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, categories_remove);

        // Drop down layout style - list view with radio button
        dataAdapter_add.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        dataAdapter_remove.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // attaching data adapter to spinner
                spinAdd.setAdapter(dataAdapter_add);
                spinRemove.setAdapter(dataAdapter_remove);

                Button btnAdd = (Button)findViewById(R.id.add);
                btnAdd.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String strItem = spinAdd.getSelectedItem().toString();
                    }
                });

                Button btnRemove = (Button)findViewById(R.id.remove);
                btnRemove.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

            }
        });
    }
}
