package com.cst2335.proj;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.support.design.widget.CollapsingToolbarLayout;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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
                rootView = inflater.inflate(R.layout.automobileitem_speed, container, false);
                break;
            case AutomobileDummyContent.FULE:
                rootView = inflater.inflate(R.layout.automobileitem_fuel, container, false);
                break;
            case AutomobileDummyContent.ODOMETER:
                rootView = inflater.inflate(R.layout.automobileitem_odometer, container, false);
                break;
            case AutomobileDummyContent.RADIO:
                rootView = inflater.inflate(R.layout.automobileitem_radio, container, false);
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
            case AutomobileDummyContent.TEMPERATURE:
                rootView = inflater.inflate(R.layout.automobileitem_temperature, container, false);
                break;
            case AutomobileDummyContent.LIGHT:
                rootView = inflater.inflate(R.layout.automobileitem_light, container, false);
                break;
            default:
                break;
        }

        return rootView;
    }
}
