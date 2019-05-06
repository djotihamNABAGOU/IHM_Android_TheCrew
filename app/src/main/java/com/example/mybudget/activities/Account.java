package com.example.mybudget.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.example.mybudget.R;

public class Account extends AppCompatActivity {

    private Toolbar toolbar;

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_account);


        /**ToolBar**/

        Bundle bundle = getIntent().getExtras();
        toolbar =(Toolbar) findViewById(R.id.toolBar);
        toolbar.setTitle(bundle.getString("Activity"));
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        /**Content**/

        String[] settings = {"Consulter le profil",
                "Modifier vos informations",
                "Déconnexion",
                "Supprimer votre compte",
        };

        final ListView listView = (ListView) findViewById(R.id.accountListview);

        ListAdapter adapter = new ArrayAdapter<String>(Account.this,
                android.R.layout.simple_list_item_1,
                settings);

        listView.setAdapter(adapter);


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {


                if (position == 0) {
                    Intent intent = new Intent(Account.this, Account.class);
                    intent.putExtra("Activity", listView.getItemAtPosition(position).toString());
                    startActivity(intent);
                }
            }
        }


        );


    }
}
