package com.example.mybudget.fragments;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.mybudget.R;
import com.example.mybudget.activities.AddSpending;
import com.example.mybudget.activities.SpendingHistory;

/**
 * A simple {@link Fragment} subclass.
 */
public class SpendingsFragment extends Fragment {

    ImageView addSpending;
    ImageView historySpending;

    public SpendingsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View spendingsView = inflater.inflate(R.layout.fragment_spendings, container, false);

        addSpending = (ImageView) spendingsView.findViewById(R.id.savePurchaseImage);
        addSpending.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(), AddSpending.class));
            }
        });

        historySpending = (ImageView) spendingsView.findViewById(R.id.historyImage);
        historySpending.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(), SpendingHistory.class));
            }
        });
        return spendingsView;
    }

}
