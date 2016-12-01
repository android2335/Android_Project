package com.cst2335.proj;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

public class HouseTempFragment extends Fragment {

    private static final String TAG = HouseTempFragment.class.getSimpleName();
    private Button mainButton;

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
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.i(TAG, "onCreateView()");

        View theView = inflater.inflate(R.layout.fragment_house_temperature, container, false);
        mainButton = (Button) theView.findViewById(R.id.houseTempMainButton);

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
