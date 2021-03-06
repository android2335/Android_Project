package com.cst2335.proj;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

/**
 * Hou Yu
 */
public class HouseActivity extends AppCompatActivity
        implements ListView.OnItemClickListener {

    public static final String EXTRA_HOUSE_OPTION = "house option";
    public static final int HOUSE_OPTION_GARAGE = 0;
    public static final int HOUSE_OPTION_TEMPERATURE = 1;
    public static final int HOUSE_OPTION_WEATHER = 2;
    protected static final String ACTIVITY_NAME = "HouseActivity";
    private static final int HOUSE_OPTIONS_NOT_SET = -1;
    HouseNavigationDrawerHelper mHouseNavigationDrawerHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.house_activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mHouseNavigationDrawerHelper = new HouseNavigationDrawerHelper();
        mHouseNavigationDrawerHelper.init(this, this);

        Intent startupIntent = getIntent();
        int houseOption = startupIntent.getIntExtra(EXTRA_HOUSE_OPTION, HOUSE_OPTIONS_NOT_SET);
        if (houseOption != HOUSE_OPTIONS_NOT_SET) {
            mHouseNavigationDrawerHelper.setSelection(houseOption);
        } else {
            displayFragment(HOUSE_OPTIONS_NOT_SET);
        }
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int option, long l) {
        mHouseNavigationDrawerHelper.handleSelect(option);
        displayFragment(option);
    }

    private void displayFragment(int option) {
        switch (option) {
            case HOUSE_OPTION_GARAGE:
                HouseGarageFragment houseGarageFragment = new HouseGarageFragment();
                commitFragment(houseGarageFragment, "GARAGE");
                break;
            case HOUSE_OPTION_TEMPERATURE:
                HouseTempFragment houseTempFragment = new HouseTempFragment();
                commitFragment(houseTempFragment, "TEMP");
                break;
            case HOUSE_OPTION_WEATHER:
                HouseWeatherFragment houseWeatherFragment = new HouseWeatherFragment();
                commitFragment(houseWeatherFragment, "WEATHER");
                break;
            default:
                HouseDefaultFragment houseDefaultFragment = new HouseDefaultFragment();
                commitFragment(houseDefaultFragment, "HOUSE");
                break;
        }
    }

    private void commitFragment(Fragment fragment, String tagFragment) {

        Log.i(ACTIVITY_NAME, String.format("HouseActivity.commitFragment Tag(%s) isInLayout(%s) isAdded(%s)", tagFragment, fragment.isInLayout() ? "true" : "false", fragment.isAdded() ? "true" : "false"));

        if (!fragment.isInLayout() && !fragment.isAdded()) {
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

            transaction.replace(R.id.fragment_container, fragment, tagFragment);

            if (isOnBackStack(tagFragment)) {  // if on the back stack remove all of the fragments 'above' it
                getSupportFragmentManager().popBackStack(tagFragment, 0);
            } else if (!"GARAGE".equals(tagFragment)) {
                transaction.addToBackStack(tagFragment);
            }

            transaction.commit();

            getSupportFragmentManager().executePendingTransactions();   // execute any pending transactions
        }
    }

    private boolean isOnBackStack(String tag) {
        boolean result = false;
        try {
            FragmentManager fm = getSupportFragmentManager();
            if (null != fm) {
                int count = fm.getBackStackEntryCount();
                if (count > 0) {
                    FragmentManager.BackStackEntry entry;
                    for (int i = 0; i < count; i++) {
                        entry = fm.getBackStackEntryAt(i);
                        if (null != entry) {
                            String name = entry.getName();
                            if (tag.equals(name)) {
                                result = true;
                            }
                        }
                    }

                }
            }
        } catch (Exception e) {
            Log.d(ACTIVITY_NAME, e.getMessage());
        }
        Log.i(ACTIVITY_NAME, String.format("HouseActivity.isOnBackStack Tag(%s) (%s)", tag, result ? "true" : "false"));

        return result;
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        mHouseNavigationDrawerHelper.syncState();
    }

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