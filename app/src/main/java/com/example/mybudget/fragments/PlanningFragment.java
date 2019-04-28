package com.example.mybudget.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.mybudget.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class PlanningFragment extends Fragment {


    public PlanningFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View planningView = inflater.inflate(R.layout.fragment_planning, container, false);
        return planningView;
    }

}
