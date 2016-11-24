package com.cst2335.proj;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

public class HouseActivity extends AppCompatActivity
        implements ListView.OnItemClickListener {

    protected static final String ACTIVITY_NAME = "HouseActivity";
    public static final String EXTRA_HOUSE_OPTION = "house option";
    private static final int HOUSE_OPTIONS_NOT_SET = -1;

    public static final int HOUSE_OPTION_GARAGE = 0;
    public static final int HOUSE_OPTION_TEMPERATURE = 1;
    public static final int HOUSE_OPTION_WEATHER = 2;

    HousePagerAdapter mHousePpagerAdapter;

    ViewPager mHouseViewPager;
    HouseNavigationDrawerHelper mHouseNavigationDrawerHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.house_activity_main);

        mHousePpagerAdapter = new HousePagerAdapter(getSupportFragmentManager(), this);

        // Set up the ViewPager with the sections adapter.
        //mHouseViewPager = (ViewPager) findViewById(R.id.house_pager);
        //mHouseViewPager.setAdapter(mHousePpagerAdapter);
        mHouseNavigationDrawerHelper = new HouseNavigationDrawerHelper();
        mHouseNavigationDrawerHelper.init(this, this);

        Intent startupIntent = getIntent();
        int houseOption = startupIntent.getIntExtra(EXTRA_HOUSE_OPTION, HOUSE_OPTIONS_NOT_SET);
        if (houseOption != HOUSE_OPTIONS_NOT_SET) {
            mHousePpagerAdapter.setHouseOptions(houseOption);
            mHouseNavigationDrawerHelper.setSelection(houseOption);
        }
    }


    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int option, long l) {
        //mHousePpagerAdapter.setHouseOptions(option);
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
                break;
        }
    }

    private void commitFragment(Fragment fragment, String tagFragment) {

        //Log.i(TAG, String.format("HouseActivity.commitFragment Tag(%s) isInLayout(%s) isAdded(%s)",tagFragment, fragment.isInLayout()? "true":"false", fragment.isAdded()? "true":"false"  ));

        if (!fragment.isInLayout() && !fragment.isAdded()) {
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

            transaction.replace(R.id.fragment_container, fragment, tagFragment);

            if (isOnBackStack(tagFragment)) {  // if on the back stack remove all of the fragments 'above' it
                getSupportFragmentManager().popBackStack(tagFragment,0);
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

        }
//        Log.i(TAG, String.format("HouseActivity.isOnBackStack Tag(%s) (%s)", tag, result?"true":"false"));

        return result;
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