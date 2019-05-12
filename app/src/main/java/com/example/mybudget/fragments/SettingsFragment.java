package com.example.mybudget.fragments;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.example.mybudget.R;
import com.example.mybudget.activities.About;
import com.example.mybudget.activities.Account;
import com.example.mybudget.activities.Help;
import com.example.mybudget.activities.Preferences;
import com.example.mybudget.activities.Revenus;
import com.example.mybudget.activities.Share;

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
        final View settingsView = inflater.inflate(R.layout.fragment_settings,
                container,
                false);

        String[] settings = {"Compte",
                "Ajouter un revenu",
                "Consulter/Modifier les préférences",
                "Aide",
                "Á propos",
                "Partager",
        };

        final ListView listView = (ListView) settingsView.findViewById(R.id.paramListview);

        ListAdapter adapter = new ArrayAdapter<String>(getActivity(),
                android.R.layout.simple_list_item_1,
                settings);

        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent,
                                    View view,
                                    int position,
                                    long id) {


                if (position==0){
                    Intent intent = new Intent(getActivity(), Account.class);

                    intent.putExtra("Activity", listView.getItemAtPosition(position).toString());

                    startActivity(intent);
                }
                if (position==1){
                    Intent intent = new Intent(getActivity(), Revenus.class);

                    intent.putExtra("Activity", listView.getItemAtPosition(position).toString());

                    startActivity(intent);
                }
                if (position==2){
                    Intent intent = new Intent(getActivity(), Preferences.class);

                    intent.putExtra("Activity", listView.getItemAtPosition(position).toString());

                    startActivity(intent);
                }
                if (position==3){
                    Intent intent = new Intent(getActivity(), Help.class);

                    intent.putExtra("Activity", listView.getItemAtPosition(position).toString());

                    startActivity(intent);
                }
                if (position==4){
                    Intent intent = new Intent(getActivity(), About.class);

                    intent.putExtra("Activity", listView.getItemAtPosition(position).toString());

                    startActivity(intent);
                }
                if (position==5){
                    Intent intent = new Intent(getActivity(), Share.class);

                    intent.putExtra("Activity", listView.getItemAtPosition(position).toString());

                    startActivity(intent);
                }


            }
        }
        );

        return settingsView;
    }





}
