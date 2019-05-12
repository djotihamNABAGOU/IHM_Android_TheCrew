package com.example.mybudget.activities;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.example.mybudget.R;
import com.example.mybudget.database.MyBudgetDB;

public class Revenus extends AppCompatActivity {

    private Toolbar toolbar;

    private EditText edAnnee;
    private Spinner spMois;
    private EditText edRevenu;
    private EditText edEpargne;

    private Button btSave;

    MyBudgetDB myBudgetDB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_revenus);


        /**ToolBar**/

        Bundle bundle = getIntent().getExtras();
        toolbar = (Toolbar) findViewById(R.id.toolBar);
        toolbar.setTitle(bundle.getString("Activity"));
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        edAnnee = findViewById(R.id.edAnnee);
        edRevenu = findViewById(R.id.salaire);
        edEpargne = findViewById(R.id.editEpargne);

        myBudgetDB = new MyBudgetDB(getApplicationContext());

        btSave = findViewById(R.id.btSavePref);
        btSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (v == btSave && verifierDonnees()) {
                    //alert de confirmation ou non
                    alert("INFORMATION", "Êtes vous sûr de vouloir enregistrer ce revenu?", true, 2);
                }
            }
        });

        spMois = findViewById(R.id.spMois);
        /**Spinner**/

        /*Spinner mySpinner = (Spinner) findViewById(R.id.mySpinner);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(Revenus.this, android.R.layout.simple_list_item_1, getResources().getStringArray(R.array.money));

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        mySpinner.setAdapter(adapter);
*/

        /**Spinner2**/
        /*
        Spinner mySpinner2 = (Spinner) findViewById(R.id.mySpinner2);

        ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(Revenus.this, android.R.layout.simple_list_item_1, getResources().getStringArray(R.array.frequence));

        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        mySpinner2.setAdapter(adapter2);
*/
    }

    private void registerRevenue() {
        final int annee = Integer.valueOf(edAnnee.getText().toString().trim());
        final String mois = spMois.getSelectedItem().toString().trim();
        final int moisDeDate;
        switch (mois){
            case "Janvier": moisDeDate = 1;break;
            case "Février": moisDeDate = 2;break;
            case "Mars": moisDeDate = 3;break;
            case "Avril": moisDeDate = 4;break;
            case "Mai": moisDeDate = 5;break;
            case "Juin": moisDeDate = 6;break;
            case "Juillet": moisDeDate = 7;break;
            case "Août": moisDeDate = 8;break;
            case "Septembre": moisDeDate = 9;break;
            case "Octobre": moisDeDate = 10;break;
            case "Novembre": moisDeDate = 11;break;
            case "Décembre": moisDeDate = 12;break;
            default: moisDeDate = -1;
        }
        final float revenu = Float.parseFloat(edRevenu.getText().toString().trim());
        final float epargne = Float.parseFloat(edEpargne.getText().toString().trim());

        if (myBudgetDB.insertRevenu(moisDeDate,annee,revenu,epargne)) {
            alert("INFORMATION", "Revenu enregistré avec succès",true,1);
            //finish();
        } else
            alert("ERREUR", "Données non sauvegardées",false,0);
    }

    private boolean verifierDonnees() {
        if (!controlchampsvide(edAnnee) && !controlchampsvide(edRevenu) && !controlchampsvide(edEpargne))
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
        AlertDialog.Builder builder = new AlertDialog.Builder(Revenus.this);
        builder.setTitle(alertType);
        builder.setMessage(message);
        if (!doAction)
            builder.setNeutralButton("OK", null);
        else {
            if (nbActions == 1)
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                });
            if (nbActions == 2)
                builder.setPositiveButton("OUI", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        registerRevenue();
                    }
                }).setNegativeButton("NON", null);
        }
        builder.create();
        builder.show();
    }
}
