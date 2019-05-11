package com.example.mybudget.activities;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.mybudget.R;
import com.example.mybudget.database.MyBudgetDB;

import java.text.DateFormatSymbols;
import java.util.ArrayList;

public class Preferences extends AppCompatActivity {

    private Toolbar toolbar;

    private Spinner spPeriode;
    private EditText edRevenu;
    private EditText edEpargne;

    private Button btSave;
    private Button btModifSave;
    private Button btCancel;

    MyBudgetDB myBudgetDB;

    private ArrayList<String> availableMonths;
    private ArrayList<Integer> availableMonthsInt;
    private ArrayList<Integer> availableYearsInt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preferences);

        /**ToolBar**/

        Bundle bundle = getIntent().getExtras();
        toolbar = (Toolbar) findViewById(R.id.toolBar);
        toolbar.setTitle(bundle.getString("Activity"));
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        spPeriode = findViewById(R.id.spPeriode);
        edRevenu = findViewById(R.id.salairePref);
        edEpargne = findViewById(R.id.epargnePref);

        availableMonths = new ArrayList<String>();
        availableMonthsInt = new ArrayList<Integer>();
        availableYearsInt = new ArrayList<Integer>();
        myBudgetDB = new MyBudgetDB(getApplicationContext());
        Cursor months = myBudgetDB.getRevenueMonths();
        if (months.getCount() == 0){
            Toast.makeText(getApplicationContext(),
                    "Aucun revenu enregistré",
                    Toast.LENGTH_LONG).show();
            finish();
        }
        //sinon il y en a
        while (months.moveToNext()) {
            availableMonthsInt.add(Integer.parseInt(months.getString(0)));
            availableYearsInt.add(Integer.parseInt(months.getString(1)));
            System.out.println("********************"+months.getString(0));
            String monthString = new DateFormatSymbols().getMonths()[Integer.parseInt(months.getString(0)) - 1];
            availableMonths.add(monthString.toUpperCase() + " " + months.getString(1));
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, availableMonths);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spPeriode.setAdapter(adapter);

        spPeriode.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                for (int i=0; i<availableMonths.size(); i++){
                    if (availableMonths.get(i).equals(parent.getItemAtPosition(position))){
                        Cursor revenues = myBudgetDB.getRevenuesOfMonth(availableMonthsInt.get(i));
                        while (revenues.moveToNext()) {
                            edRevenu.setText(revenues.getString(3));
                            edEpargne.setText(revenues.getString(4));
                        }
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        //Boutons
        btSave = findViewById(R.id.btSavePref);
        btSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (v == btSave && verifierDonnees()) {
                    //alert de confirmation ou non
                    alert("INFORMATION", "Êtes vous sûr de vouloir modifier ce revenu?", true, 2);

                }
            }
        });

        btModifSave = findViewById(R.id.btModifPref);
        btModifSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //On active les champs
                edRevenu.setEnabled(true);
                edEpargne.setEnabled(true);
                btModifSave.setVisibility(View.INVISIBLE);
                btSave.setVisibility(View.VISIBLE);
                btCancel.setVisibility(View.VISIBLE);
            }
        });

        btCancel = findViewById(R.id.btCancelPref);
        btCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //On active les champs
                edRevenu.setEnabled(false);
                edEpargne.setEnabled(false);
                btSave.setVisibility(View.INVISIBLE);
                btCancel.setVisibility(View.INVISIBLE);
                btModifSave.setVisibility(View.VISIBLE);
            }
        });

    }

    private void updateRevenue() {
        final String mois_annee = spPeriode.getSelectedItem().toString().trim();
        for (int i=0; i<availableMonths.size(); i++){
            if (availableMonths.get(i).equals(mois_annee)){
                final int mois = availableMonthsInt.get(i);
                final int annee = availableYearsInt.get(i);
                final float revenu = Float.parseFloat(edRevenu.getText().toString().trim());
                final float epargne = Float.parseFloat(edEpargne.getText().toString().trim());
                System.out.println("revenu****************"+revenu);
                System.out.println("ep****************"+epargne);

                if (myBudgetDB.updateRevenu(mois,annee,revenu,epargne)) {
                    alert("INFORMATION", "Revenu modifié avec succès",true,1);
                    //finish();
                } else
                    alert("ERREUR", "Données non sauvegardées",false,0);
            }
        }
    }

    private boolean verifierDonnees() {
        if (!controlchampsvide(edRevenu) && !controlchampsvide(edEpargne))
            return true;

        return false;
    }

    public static boolean controlchampsvide(EditText champ) {
        //verifie si le champ est vide
        if (champ.getText().toString().length() == 0) {
            champ.setFocusable(true);
            View focus = null;
            focus = champ;
            champ.setError("Ce champ est requis");
            focus.requestFocus();
            return true;

        } else {
            return false;
        }
    }

    private void alert(String alertType, String message, boolean doAction, int nbActions) {
        AlertDialog.Builder builder = new AlertDialog.Builder(Preferences.this);
        builder.setTitle(alertType);
        builder.setMessage(message);
        if (!doAction)
            builder.setNeutralButton("OK", null);
        else {
            if (nbActions == 1)
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                       // finish();
                        //On désactive les champs
                        edRevenu.setEnabled(false);
                        edEpargne.setEnabled(false);
                        btSave.setVisibility(View.INVISIBLE);
                        btCancel.setVisibility(View.INVISIBLE);
                        btModifSave.setVisibility(View.VISIBLE);
                    }
                });
            if (nbActions == 2)
                builder.setPositiveButton("OUI", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        updateRevenue();
                    }
                }).setNegativeButton("NON", null);
        }
        builder.create();
        builder.show();
    }
}
