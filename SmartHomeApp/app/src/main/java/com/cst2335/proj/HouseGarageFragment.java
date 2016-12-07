package com.cst2335.proj;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.Toast;

public class HouseGarageFragment extends Fragment {

    private static final String TAG = HouseGarageFragment.class.getSimpleName();
    private Switch switchDoorButton;
    private Switch switchLightButton;
    private ImageView doorImage;
    private ImageView doorlightImage;
    protected SharedPreferences garageStatusPref;
    protected SharedPreferences.Editor editor;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        Log.i(TAG, "onAttach()");
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.house_menu, menu);
    }

    public boolean onOptionsItemSelected(MenuItem mi) {
        Toast toast;

        switch (mi.getItemId()) {

            case R.id.HowToRun:
                Log.d("Toolbar", "HowToRun selected");
                if (getView() != null) {
                    Snackbar snackbar = Snackbar
                            .make(getView(), getResources().getString(R.string.house_menu_garage_howtorun), Snackbar.LENGTH_LONG);
                    snackbar.show();
                }
                break;


            case R.id.About:
                Context context = getContext();
                CharSequence text = getResources().getString(R.string.house_menu_about_details);
                int duration = Toast.LENGTH_SHORT;

                toast = Toast.makeText(context, text, duration);
                toast.show();
                break;
        }
        return true;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.i(TAG, "onCreateView()");

        View theView = inflater.inflate(R.layout.fragment_house_garage, container, false);

        //house_garage_door switch & house_garage_light switch with images
        switchDoorButton = (Switch) theView.findViewById(R.id.house_garage_door_switch);
        switchLightButton = (Switch) theView.findViewById(R.id.house_garage_light_switch);
        doorImage = (ImageView) theView.findViewById(R.id.garageDoorImage);
        doorlightImage = (ImageView) theView.findViewById(R.id.garageLightImage);

        garageStatusPref = getActivity().getPreferences(Context.MODE_PRIVATE);
        editor = garageStatusPref.edit();

        if(!garageStatusPref.getBoolean("HouseGarageDoorStatus", false)) {
            //garage door close
            switchDoorButton.setChecked(false);
        }
        else
        {
            switchDoorButton.setChecked(true);
        }

        if(!garageStatusPref.getBoolean("HouseGarageLightStatus", false)) {
            switchLightButton.setChecked(false);
        }
        else {
            switchLightButton.setChecked(true);
        }

        if (switchDoorButton.isChecked()) {
            doorImage.setImageResource(R.drawable.house_garage_open);
            doorlightImage.setImageResource(R.drawable.house_lighton);
            editor.putBoolean("HouseGarageDoorStatus", true);
            editor.commit();
        } else {
            doorImage.setImageResource(R.drawable.house_garage_closed);
            doorlightImage.setImageResource(R.drawable.house_lightoff);
            editor.putBoolean("HouseGarageDoorStatus", false);
            editor.commit();
        }

        if (switchLightButton.isChecked()) {
            doorlightImage.setImageResource(R.drawable.house_lighton);
            editor.putBoolean("HouseGarageLightStatus", true);
            editor.commit();
        } else {
            doorlightImage.setImageResource(R.drawable.house_lightoff);
            editor.putBoolean("HouseGarageLightStatus", false);
            editor.commit();
        }

        switchDoorButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean bChecked) {
                if (bChecked) {
                    doorImage.setImageResource(R.drawable.house_garage_open);
                    doorlightImage.setImageResource(R.drawable.house_lighton);
                    switchLightButton.setChecked(true);
                    editor.putBoolean("HouseGarageDoorStatus", true);
                    editor.putBoolean("HouseGarageLightStatus", true);
                    editor.commit();
                } else {
                    doorImage.setImageResource(R.drawable.house_garage_closed);
                    doorlightImage.setImageResource(R.drawable.house_lightoff);
                    switchLightButton.setChecked(false);
                    editor.putBoolean("HouseGarageDoorStatus", false);
                    editor.putBoolean("HouseGarageLightStatus", false);
                    editor.commit();
                }
            }
        });

        switchLightButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean bChecked) {
                if (bChecked) {
                    doorlightImage.setImageResource(R.drawable.house_lighton);
                    editor.putBoolean("HouseGarageLightStatus", true);
                    editor.commit();
                } else {
                    doorlightImage.setImageResource(R.drawable.house_lightoff);
                    editor.putBoolean("HouseGarageLightStatus", false);
                    editor.commit();
                }
            }
        });

        //mainButton
        Button mainButton = (Button) theView.findViewById(R.id.houseGarageMainButton);
        mainButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), NavigateToolbar.class);
                startActivity(intent);
            }
        });

        return theView;
    }
}
