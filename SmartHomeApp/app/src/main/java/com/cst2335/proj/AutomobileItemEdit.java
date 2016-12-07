package com.cst2335.proj;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;

import com.cst2335.proj.dummy.AutomobileDummyContent;

import java.util.ArrayList;
import java.util.List;

public class AutomobileItemEdit extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.automobileitem_edit);

        setTitle("Edit Setting Items");

        //item list
        final Spinner spinAdd = (Spinner)findViewById(R.id.spinner_add);
        final Spinner spinRemove = (Spinner)findViewById(R.id.spinner_remove);

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
        final ArrayAdapter<String> dataAdapter_add = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, categories_add);
        final ArrayAdapter<String> dataAdapter_remove = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, categories_remove);

        // Drop down layout style - list view with radio button
        dataAdapter_add.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        dataAdapter_remove.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // attaching data adapter to spinner
                spinAdd.setAdapter(dataAdapter_add);
                spinRemove.setAdapter(dataAdapter_remove);

        Button btnAdd = (Button)findViewById(R.id.btn_add);
                btnAdd.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                //add item
                if (spinAdd.getCount() <= 0) {
                    return;
                }
                        String strItem = spinAdd.getSelectedItem().toString();
                if (strItem.isEmpty()) {
                    return;
                }
                dataAdapter_add.remove(strItem);
                dataAdapter_remove.add(strItem);

                //update data
                for (int i = 0; i < AutomobileDatabaseOperate.ITEM_NUM; i++) {
                    if (strItem.equals(AutomobileDatabaseOperate.itemName.get(i))) {
                        AutomobileDatabaseOperate.setOneItemNo(i, dataAdapter_remove.getCount());
                        break;
                    }
                }
                    }
                });

        Button btnRemove = (Button)findViewById(R.id.btn_remove);
                btnRemove.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                //delete item
                if (spinRemove.getCount() <= 0) {
                    return;
                }
                String strItem = spinRemove.getSelectedItem().toString();
                if (strItem.isEmpty()) {
                    return;
                }
                dataAdapter_add.add(strItem);
                dataAdapter_remove.remove(strItem);

                //update data
                int deleteNo = 0;
                for (int i = 0; i < AutomobileDatabaseOperate.ITEM_NUM; i++) {
                    if (strItem.equals(AutomobileDatabaseOperate.itemName.get(i))) {
                        deleteNo = AutomobileDatabaseOperate.getItemNo().get(i);
                        AutomobileDatabaseOperate.setOneItemNo(i, 0);
                        break;
                    }
                }

                //update other sequence number as well
                int tmp = 0;
                for (int i = 0; i < AutomobileDatabaseOperate.ITEM_NUM; i++) {
                    tmp = AutomobileDatabaseOperate.getItemNo().get(i);
                    if (tmp > deleteNo) {
                        AutomobileDatabaseOperate.setOneItemNo(i, tmp - 1);
                    }
                }
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        //update setting items
        AutomobileDummyContent.createItem();

        //save database
        AutomobileDatabaseOperate.write();
    }
}
