package com.example.mybudget.fragments;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.mybudget.R;
import com.example.mybudget.activities.PlanningSpending;

/**
 * A simple {@link Fragment} subclass.
 */
public class PlanningFragment extends Fragment {

    ImageView planningSpending;

    public PlanningFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View planningView = inflater.inflate(R.layout.fragment_planning, container, false);
        planningSpending = (ImageView) planningView.findViewById(R.id.addPurchaseImage);
        planningSpending.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(), PlanningSpending.class));
            }
        });
        return planningView;
    }

}
