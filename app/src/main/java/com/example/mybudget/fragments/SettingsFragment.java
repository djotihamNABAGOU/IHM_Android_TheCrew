package com.example.mybudget.fragments;


import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.example.mybudget.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class SettingsFragment extends Fragment {


    public SettingsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        final View settingsView = inflater.inflate(R.layout.fragment_settings, container, false);

        String[] settings = {"Compte","Revenus et préférences", "Aide", "Á propos","Partager"};

        ListView listView = (ListView) settingsView.findViewById(R.id.paramLystview);


        ListAdapter adapter = new ArrayAdapter<String>(getActivity(),android.R.layout.simple_list_item_1,settings);


        listView.setAdapter(adapter);

        return settingsView;
    }





}
