package com.cst2335.proj;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

public class Kitchen_Detail_Activity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kitchen_detail);
        Toolbar toolbar = (Toolbar) findViewById(R.id.detail_toolbar);
        setSupportActionBar(toolbar);

        // Show the Up button in the action bar.
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        if (savedInstanceState == null) {
            Bundle arguments = new Bundle();
            arguments.putString(Kitchen_Detail_Fragment.ARG_ITEM_ID_1,
                    getIntent().getStringExtra(Kitchen_Detail_Fragment.ARG_ITEM_ID_1));
            arguments.putString(Kitchen_Detail_Fragment.ARG_ITEM_ID_2,
                    getIntent().getStringExtra(Kitchen_Detail_Fragment.ARG_ITEM_ID_2));
            Kitchen_Detail_Fragment fragment = new Kitchen_Detail_Fragment();
            fragment.setArguments(arguments);//pass variable arguments to fragment
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.kitchen_detail_container, fragment)
                    .commit();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {//the menu item in App Bar
        int id = item.getItemId();
        if (id == android.R.id.home) {//if select home menu in App Bar
            navigateUpTo(new Intent(this, LivingRoomItemListActivity.class));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
