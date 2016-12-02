package com.cst2335.proj;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.support.design.widget.CollapsingToolbarLayout;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Switch;
import android.widget.TextView;

import com.cst2335.proj.dummy.AutomobileDummyContent;

/**
 * A fragment representing a single automobileItem detail screen.
 * This fragment is either contained in a {@link AutomobileItemListActivity}
 * in two-pane mode (on tablets) or a {@link AutomobileItemDetailActivity}
 * on handsets.
 */
public class AutomobileItemDetailFragment extends Fragment {
    /**
     * The fragment argument representing the item ID that this fragment
     * represents.
     */
    public static final String ARG_ITEM_ID = "item_id";

    /**
     * The dummy content this fragment is presenting.
     */
    private AutomobileDummyContent.DummyItem mItem;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public AutomobileItemDetailFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments().containsKey(ARG_ITEM_ID)) {
        // Load the dummy content specified by the fragment
        // arguments. In a real-world scenario, use a Loader
        // to load content from a content provider.
        mItem = AutomobileDummyContent.ITEM_MAP.get(getArguments().getString(ARG_ITEM_ID));

        Activity activity = this.getActivity();
        CollapsingToolbarLayout appBarLayout = (CollapsingToolbarLayout) activity.findViewById(R.id.toolbar_layout);
        if (appBarLayout != null) {
            appBarLayout.setTitle(mItem.content);
        }
    }
}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = null;
        switch (mItem.details) {
            case AutomobileDummyContent.SPEED:
                {
                    rootView = inflater.inflate(R.layout.automobileitem_speed, container, false);
                    final EditText etForward = (EditText)rootView.findViewById(R.id.et_forwardSpeed);
                    etForward.setText(AutomobileDatabaseOperate.getSpeedForward() + "");
                    final EditText etBackward = (EditText)rootView.findViewById(R.id.et_backwardSpeed);
                    etBackward.setText(AutomobileDatabaseOperate.getSpeedBackward() + "");
                    Button btnSet = (Button)rootView.findViewById(R.id.btn_set);
                    btnSet.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            AutomobileDatabaseOperate.setSpeedForward(Integer.parseInt(etForward.getText().toString()));
                            AutomobileDatabaseOperate.setSpeedBackward(Integer.parseInt(etBackward.getText().toString()));
                        }
                    });
                }
                break;
            case AutomobileDummyContent.FUEL: {
                rootView = inflater.inflate(R.layout.automobileitem_fuel, container, false);
                final TextView tvGasLeft = (TextView)rootView.findViewById(R.id.tv_gasLeftValue);
                tvGasLeft.setText(AutomobileDatabaseOperate.getGasLevel() + "");
                final TextView tvEstimatedKm = (TextView)rootView.findViewById(R.id.tv_estimatedKmValue);
                tvEstimatedKm.setText(AutomobileDatabaseOperate.getGasLevel() * 10 + "");

                final EditText etFill = (EditText) rootView.findViewById(R.id.et_gasFill);
                Button btnFill = (Button)rootView.findViewById(R.id.btn_fill);
                btnFill.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        float gas = Float.parseFloat(etFill.getText().toString()) + AutomobileDatabaseOperate.getGasLevel();
                        if (gas > AutomobileDatabaseOperate.FULL_FUEL) {
                            gas = AutomobileDatabaseOperate.FULL_FUEL;
                        }
                        AutomobileDatabaseOperate.setGasLevel(gas);
                        tvGasLeft.setText(AutomobileDatabaseOperate.getGasLevel() + "");
                        tvEstimatedKm.setText(AutomobileDatabaseOperate.getGasLevel() * 10 + "");
                        etFill.setText("0");
                    }
                });
            }
                break;
            case AutomobileDummyContent.ODOMETER:
                {
                    rootView = inflater.inflate(R.layout.automobileitem_odometer, container, false);
                    Button btnReset = (Button)rootView.findViewById(R.id.btn_reset);
                    btnReset.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            AutomobileDatabaseOperate.setOdometer(0);
                        }
                    });
                }
                break;
            case AutomobileDummyContent.RADIO: {
                rootView = inflater.inflate(R.layout.automobileitem_radio, container, false);
                final RadioButton btnAM = (RadioButton)rootView.findViewById(R.id.radioButton_AM);
                final RadioButton btnFM = (RadioButton)rootView.findViewById(R.id.radioButton_FM);
                if (AutomobileDatabaseOperate.getRadioMode().equals("AM")) {
                    btnAM.setChecked(true);
                    btnFM.setChecked(false);
                }
                else {
                    btnAM.setChecked(false);
                    btnFM.setChecked(true);
                }
                final EditText etFreq = (EditText) rootView.findViewById(R.id.et_frequency);
                etFreq.setText(AutomobileDatabaseOperate.getRadioFrequency() + "");

                Button btnSet = (Button)rootView.findViewById(R.id.btn_set);
                btnSet.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        AutomobileDatabaseOperate.setRadioFrequency(Float.parseFloat(etFreq.getText().toString()));
                        if (btnAM.isChecked()) {
                            AutomobileDatabaseOperate.setRadioMode("AM");
                        }
                        else {
                            AutomobileDatabaseOperate.setRadioMode("FM");
                        }
                    }
                });
            }
                break;
            case AutomobileDummyContent.GPS:
                {
                    rootView = inflater.inflate(R.layout.automobileitem_gps, container, false);

                    Button btnSetGps = (Button)rootView.findViewById(R.id.button_setGps);
                    btnSetGps.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            //start google map
                            Intent intent = new Intent(android.content.Intent.ACTION_VIEW, Uri.parse("http://maps.google.com/maps"));
                            startActivity(intent);
                        }
                    });
                }
                break;
            case AutomobileDummyContent.TEMPERATURE: {
                rootView = inflater.inflate(R.layout.automobileitem_temperature, container, false);
                final EditText etTemp = (EditText) rootView.findViewById(R.id.et_temperature);
                etTemp.setText(AutomobileDatabaseOperate.getTemperature() + "");
                Button btnSet = (Button)rootView.findViewById(R.id.btn_set);
                btnSet.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        AutomobileDatabaseOperate.setTemperature(Integer.parseInt(etTemp.getText().toString()));
                    }
                });
            }
                break;
            case AutomobileDummyContent.LIGHT: {
                rootView = inflater.inflate(R.layout.automobileitem_light, container, false);
                final Switch swLight = (Switch) rootView.findViewById(R.id.switch_light);
                swLight.setChecked(AutomobileDatabaseOperate.getLight());
                swLight.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        AutomobileDatabaseOperate.setLight(isChecked);
                    }
                });
            }
                break;
            default:
                break;
        }

        return rootView;
    }
}
