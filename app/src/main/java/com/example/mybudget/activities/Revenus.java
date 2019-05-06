package com.example.mybudget.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.example.mybudget.R;

public class Revenus extends AppCompatActivity {

    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_revenus);


        /**ToolBar**/

        Bundle bundle = getIntent().getExtras();
        toolbar =(Toolbar) findViewById(R.id.toolBar);
        toolbar.setTitle(bundle.getString("Activity"));
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        /**Spinner**/

        Spinner mySpinner = (Spinner) findViewById(R.id.mySpinner);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(Revenus.this, android.R.layout.simple_list_item_1, getResources().getStringArray(R.array.money));

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        mySpinner.setAdapter(adapter);


        /**Spinner2**/

        Spinner mySpinner2 = (Spinner) findViewById(R.id.mySpinner2);

        ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(Revenus.this, android.R.layout.simple_list_item_1, getResources().getStringArray(R.array.frequence));

        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        mySpinner2.setAdapter(adapter2);

    }
}
