package com.example.mybudget.activities;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.mybudget.R;
import com.example.mybudget.database.MyBudgetDB;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class PlanningSpending extends AppCompatActivity {
    private Toolbar toolbar;

    private EditText edAliment, edDuree, edCout;
    private Spinner spFrequence;
    private Button btDateDebut, btPlanifier;
    private ProgressDialog progressDialog;

    int year_x, month_x, day_x;
    //private String dateDebut, dateDuJour;
    private Date beginingDate, dateOfDay;
    static final int DIALOG_ID_NAISSANCE = 1;

    MyBudgetDB myBudgetDB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_planning_spending);

        /**ToolBar*/
        toolbar = findViewById(R.id.toolBar);
        toolbar.setTitle(R.string.dashboard_planification);
        toolbar.setSubtitle(R.string.sub_planningSpending);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //EditText
        edAliment = (EditText) findViewById(R.id.libelleAliment);
        edDuree = (EditText) findViewById(R.id.duree);
        edCout = (EditText) findViewById(R.id.cout);

        //Button
        btDateDebut = (Button) findViewById(R.id.btnDateDebut);
        btDateDebut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog(DIALOG_ID_NAISSANCE);
            }
        });

        btPlanifier = (Button) findViewById(R.id.btnAddSpending);
        btPlanifier.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //check data before save them
                if (v == btPlanifier && verifierDonnees()) {
                    //alert de confirmation ou non
                    alert("INFORMATION","Êtes vous sûr de vouloir planifier cette dépense?",true,2);
                }
            }
        });

        //Spinner
        spFrequence = (Spinner) findViewById(R.id.frequence_spinner);

        //ProgressBar
        progressDialog = new ProgressDialog(this);

        //database
        myBudgetDB = new MyBudgetDB(getApplicationContext());
    }

    private void registerPlanningSpending() {
        final String libelle_aliment = edAliment.getText().toString().trim();
        final String date_debut = android.text.format.DateFormat.format("yyyy-MM-dd",beginingDate).toString();
        //System.out.println(DateFormat.getDateInstance(DateFormat.WEEK_OF_MONTH_FIELD+2).format(beginingDate));
        final String frequence = spFrequence.getSelectedItem().toString().trim();
        final int duree = Integer.parseInt(edDuree.getText().toString().trim());
        final float cout = Float.parseFloat(edCout.getText().toString().trim());

        if (myBudgetDB.insertPlanningSpending("plannifie",libelle_aliment, date_debut, frequence, duree, cout)) {
            alert("INFORMATION", "Dépense planifiée avec succès",true,1);
            //finish();
        } else
            alert("ERREUR", "Données non sauvegardées",false,0);
    }

    @Override
    protected Dialog onCreateDialog(int id) {
        if (id == DIALOG_ID_NAISSANCE) {
            Calendar c = Calendar.getInstance();

            year_x = c.get(Calendar.YEAR);
            month_x = c.get(Calendar.MONTH);
            day_x = c.get(Calendar.DAY_OF_MONTH);
            //dateDuJour = year_x + "-" + month_x + "-" + day_x;
            //dateDebut = year_x + "-" + month_x + "-" + day_x;
            dateOfDay = new Date(year_x - 1900, month_x, day_x);
            //Log.d("Date du jour", dateDuJour);
            //Log.d("Date debut", dateDebut);
            DatePickerDialog datePickerDialog = new DatePickerDialog(this, dpickerListener, year_x, month_x, day_x);
            datePickerDialog.setCanceledOnTouchOutside(false);
            return datePickerDialog;
        }
        return null;
    }

    private DatePickerDialog.OnDateSetListener dpickerListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
            year_x = year;
            month_x = month;
            day_x = dayOfMonth;
            //dateDebut = year_x + "-" + month_x + "-" + day_x;
            //Log.d("Date debut modif", dateDebut);
            //Affichage
            beginingDate = new Date(year_x - 1900, month_x, day_x);
            String dateString = DateFormat.getDateInstance(DateFormat.FULL).format(beginingDate);
            btDateDebut.setText("Date de début: " + dateString);
        }
    };

    private boolean verifierDonnees() {
        if (!controlchampsvide(edAliment) && !controlchampsvide(edDuree) && !controlchampsvide(edCout)) {
            if (beginingDate != null) {
                //System.out.println("Date de test : "+android.text.format.DateFormat.format("yyyy-MM-dd",beginingDate).toString());
                //Log.d("beginning date", beginingDate.toString());
                //Log.d("date of day", dateOfDay.toString());
                int diff = (int) ((beginingDate.getTime() - dateOfDay.getTime()) / (24 * 60 * 60 * 1000));
                //Log.d("Difference :", Integer.toString(diff));
                if (diff != 0) {
                    return true;
                } else {
                    alert("ERREUR", "Vous ne pouvez planifier une dépense aujourd'hui ou antérieure à la date d'aujourd'hui, veuillez modifier la date de début.",false,0);
                    return false;
                }
            } else {
                alert("ERREUR", "Veuillez renseigner la date de début.",false,0);
                return false;
            }
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
        AlertDialog.Builder builder = new AlertDialog.Builder(PlanningSpending.this);
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
                        registerPlanningSpending();
                    }
                }).setNegativeButton("NON", null);
        }
        builder.create();
        builder.show();
    }
}
