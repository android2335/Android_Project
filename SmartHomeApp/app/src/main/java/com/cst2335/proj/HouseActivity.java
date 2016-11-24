package com.cst2335.proj;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

public class HouseActivity extends AppCompatActivity
        implements ListView.OnItemClickListener {

    protected static final String ACTIVITY_NAME = "HouseActivity";
    public static final String EXTRA_HOUSE_OPTION = "house option";
    private static final int HOUSE_OPTIONS_NOT_SET = -1;

    HousePagerAdapter mHousePpagerAdapter;

    ViewPager mHouseViewPager;
    HouseNavigationDrawerHelper mHouseNavigationDrawerHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.house_activity_main);

        mHousePpagerAdapter = new HousePagerAdapter(getSupportFragmentManager(), this);

        // Set up the ViewPager with the sections adapter.
        mHouseViewPager = (ViewPager) findViewById(R.id.house_pager);
        mHouseViewPager.setAdapter(mHousePpagerAdapter);
        mHouseNavigationDrawerHelper = new HouseNavigationDrawerHelper();
        mHouseNavigationDrawerHelper.init(this, this);

        Intent startupIntent = getIntent();
        int houseOption = startupIntent.getIntExtra(EXTRA_HOUSE_OPTION, HOUSE_OPTIONS_NOT_SET);
        if (houseOption != HOUSE_OPTIONS_NOT_SET) {
            mHousePpagerAdapter.getItem(houseOption);
            mHouseNavigationDrawerHelper.setSelection(houseOption);
        }
    }


    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int optionLib, long l) {
        mHousePpagerAdapter.getItem(optionLib);
        mHouseNavigationDrawerHelper.handleSelect(optionLib);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

        mHouseNavigationDrawerHelper.syncState();
    }

//    @Override
//    public boolean onPrepareOptionsMenu(Menu menu) {
//        mHouseNavigationDrawerHelper.handleOnPrepareOptionsMenu(menu);
//        return super.onPrepareOptionsMenu(menu);
//    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        mHouseNavigationDrawerHelper.handleOnOptionsItemSelected(item);
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        mHouseNavigationDrawerHelper.syncState();
        super.onConfigurationChanged(newConfig);
    }
}