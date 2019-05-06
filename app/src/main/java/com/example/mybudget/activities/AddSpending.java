package com.example.mybudget.activities;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.TextureView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.mybudget.R;
import com.example.mybudget.database.MyBudgetDB;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class AddSpending extends AppCompatActivity {
    private Toolbar toolbar;

    private EditText edAliment, edCout;
    private Button btEnregistrer;

    MyBudgetDB myBudgetDB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_spending);

        /**ToolBar*/
        toolbar = findViewById(R.id.toolBar);
        toolbar.setTitle(R.string.app_name);
        toolbar.setSubtitle(R.string.sub_addSpending);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //EditText
        edAliment = (EditText) findViewById(R.id.libelleAlimentImprevu);
        edCout = (EditText) findViewById(R.id.coutImprevu);

        //Button
        btEnregistrer = (Button) findViewById(R.id.btnAddSpendingImprevu);
        btEnregistrer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //check data before save them
                if (v == btEnregistrer && verifierDonnees()) {
                    //alert de confirmation ou non
                    alert("INFORMATION", "Êtes vous sûr de vouloir enregistrer cette dépense comme imprévue?", true, 2);
                }
            }
        });

        //database
        myBudgetDB = new MyBudgetDB(getApplicationContext());
    }

    private boolean verifierDonnees() {
        if (!controlchampsvide(edAliment) && !controlchampsvide(edCout)) {
            return true;
        }
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
        AlertDialog.Builder builder = new AlertDialog.Builder(AddSpending.this);
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
                        registerAddSpending();
                    }
                }).setNegativeButton("NON", null);
        }
        builder.create();
        builder.show();
    }

    private void registerAddSpending() {
        final String libelle_aliment = edAliment.getText().toString().trim();
        final String date_debut = android.text.format.DateFormat.format("yyyy-MM-dd", Calendar.getInstance().getTime()).toString();
        //System.out.println(DateFormat.getDateInstance(DateFormat.WEEK_OF_MONTH_FIELD+2).format(beginingDate));
        final String frequence = "-";
        final int duree = 0;
        final float cout = Float.parseFloat(edCout.getText().toString().trim());

        if (myBudgetDB.insertUnexpectedSpending("imprevu",libelle_aliment, date_debut, cout)) {
            alert("INFORMATION", "Dépense enregistrée avec succès", true, 1);
            //finish();
        } else
            alert("ERREUR", "Données non sauvegardées", false, 0);
    }
}
