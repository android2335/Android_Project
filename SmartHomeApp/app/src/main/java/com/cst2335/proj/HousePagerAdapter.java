package com.cst2335.proj;

import android.content.Context;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.widget.Toast;

public class HousePagerAdapter extends FragmentStatePagerAdapter {

    public static final int HOUSE_OPTION_GARAGE = 0;
    public static final int HOUSE_OPTION_TEMPERATURE = 1;
    public static final int HOUSE_OPTION_WEATHER = 2;

    String mHouseOptionTitle;
    String mHouseOptionDescription;
    int mHouseOptionLogoResourceId;

    Context mContext;

    public HousePagerAdapter(FragmentManager fm, Context context) {
        super(fm);
        mContext = context;
        Resources resources = mContext.getResources(); //get all resources
        //TODO some logo files
        //mHouseOptionLogoResourceId = R.drawable.ps_android_logo; //some logo files
    }

    //Used to set specific properties
    public void setHouseOptions(int houseOption) {
        boolean isValid = true;
        Resources resources = mContext.getResources();

        switch (houseOption) {
            case HOUSE_OPTION_GARAGE:
                mHouseOptionTitle = "Garage";
                mHouseOptionDescription = "garage description";
                //TODO: replace some logo
                //mHouseOptionLogoResourceId = R.drawable.ps_android_logo;
                getItem(HOUSE_OPTION_GARAGE);
                break;
            case HOUSE_OPTION_TEMPERATURE:
                mHouseOptionTitle = "Temperature";
                mHouseOptionDescription = "house tempterature";
                //TODO: replace some logo
                //mHouseOptionLogoResourceId = R.drawable.ps_android_logo;
                getItem(HOUSE_OPTION_TEMPERATURE);
                break;
            case HOUSE_OPTION_WEATHER:
                mHouseOptionTitle = "Weather";
                mHouseOptionDescription = "weather";
                //TODO: replace some logo
                //mHouseOptionLogoResourceId = R.drawable.ps_android_logo;
                getItem(HOUSE_OPTION_WEATHER);
                break;
            default:
                Toast.makeText(mContext, "Invalid option", Toast.LENGTH_LONG).show();
                isValid = false;
                break;
        }
        if(isValid)
            notifyDataSetChanged();
    }

    @Override
    public Fragment getItem(int i) {

        if( i == HOUSE_OPTION_GARAGE ) {
            HouseGarageFragment houseGarageFragment = new HouseGarageFragment();
            mHouseOptionTitle = "Garage";
            mHouseOptionDescription = "garage description";
            //TODO: replace some logo
            //mHouseOptionLogoResourceId = R.drawable.ps_android_logo;
            return houseGarageFragment;
        }
        else if( i == HOUSE_OPTION_TEMPERATURE ) {
            mHouseOptionTitle = "Temperature";
            mHouseOptionDescription = "house tempterature";
            //TODO: replace some logo
            //mHouseOptionLogoResourceId = R.drawable.ps_android_logo;
            HouseTempFragment houseTempFragment = new HouseTempFragment();
            return houseTempFragment;
        }
        else if( i == HOUSE_OPTION_WEATHER ) {
            mHouseOptionTitle = "Weather";
            mHouseOptionDescription = "weather";
            //TODO: replace some logo
            //mHouseOptionLogoResourceId = R.drawable.ps_android_logo;
            HouseWeatherFragment houseWeatherFragment = new HouseWeatherFragment();
            return houseWeatherFragment;
        }
        else {
            Toast.makeText(mContext, "Invalid option", Toast.LENGTH_LONG).show();
        }
        return null;
    }

    @Override
    public int getCount() {
        return 3;
    }

    @Override
    public int getItemPosition(Object object) {
        // Causes adapter to reload all Fragments when
        //  notifyDataSetChanged is called
        return POSITION_NONE;
    }
}
