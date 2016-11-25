package com.cst2335.proj;

import android.app.ActionBar;
import android.app.Activity;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class HouseNavigationDrawerHelper {

    DrawerLayout mHouseDrawerLayout;
    ListView mHouseDrawerListView;
    private ActionBarDrawerToggle mHouseDrawerToggle;

    public void init(Activity theActivity, ListView.OnItemClickListener listener) {
        mHouseDrawerLayout = (DrawerLayout) theActivity.findViewById(R.id.house_drawer_layout);
        mHouseDrawerListView = (ListView) theActivity.findViewById(R.id.house_left_drawer);

        String[] navigationHouseDrawerOptions =
                theActivity.getResources().getStringArray(R.array.navigation_house_drawer_options);
        ArrayAdapter<String> navigationHouseDrawerAdapter =
                new ArrayAdapter<>(theActivity, R.layout.house_drawer_option_item, navigationHouseDrawerOptions);
        mHouseDrawerListView.setAdapter(navigationHouseDrawerAdapter);
        mHouseDrawerListView.setOnItemClickListener(listener);
        mHouseDrawerLayout.setDrawerShadow(R.drawable.house_drawer_shadow, GravityCompat.START);

        mHouseDrawerListView.setItemChecked(0, true);
        setupActionBar(theActivity);
    }

    private void setupActionBar(Activity theActivity) {
        final Activity activity = theActivity;
        ActionBar actionBar = theActivity.getActionBar();

        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        mHouseDrawerToggle = new ActionBarDrawerToggle(
                theActivity,
                mHouseDrawerLayout,
                R.string.open_house_drawer_message,
                R.string.close_house_drawer_message
        ) {
            @Override
            public void onDrawerClosed(View drawerView) {
                activity.invalidateOptionsMenu();
                super.onDrawerClosed(drawerView);
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                activity.invalidateOptionsMenu();
                super.onDrawerOpened(drawerView);
            }
        };
    }

    public void handleSelect(int option) {
        mHouseDrawerListView.setItemChecked(option, true);
        mHouseDrawerLayout.closeDrawer(mHouseDrawerListView);
    }

    public void handleOnOptionsItemSelected(MenuItem item) {
        mHouseDrawerToggle.onOptionsItemSelected(item);
    }

    public void syncState() {
        mHouseDrawerToggle.syncState();
    }

    public void setSelection(int option) {
        mHouseDrawerListView.setItemChecked(option, true);
    }
}
