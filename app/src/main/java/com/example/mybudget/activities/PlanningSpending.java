package com.example.mybudget.activities;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.mybudget.R;

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
    private String dateDebut, dateDuJour;
    static final int DIALOG_ID_NAISSANCE = 1;

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

                }
            }
        });

        //Spinner
        spFrequence = (Spinner) findViewById(R.id.frequence_spinner);

        //ProgressBar
        progressDialog = new ProgressDialog(this);
    }

    @Override
    protected Dialog onCreateDialog(int id) {
        if (id == DIALOG_ID_NAISSANCE) {
            Calendar c = Calendar.getInstance();

            year_x = c.get(Calendar.YEAR);
            month_x = c.get(Calendar.MONTH);
            day_x = c.get(Calendar.DAY_OF_MONTH);
            dateDuJour = year_x + "-" + month_x + "-" + day_x;
            dateDebut = year_x + "-" + month_x + "-" + day_x;
            //dateDuJour = DateFormat.getDateInstance(DateFormat.FULL).format(c.getTime());
            //dateDebut = DateFormat.getDateInstance(DateFormat.FULL).format(c.getTime());
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
            dateDebut = year_x + "-" + month_x + "-" + day_x;
            //Affichage
            Date date = new Date(year_x - 1900, month_x, day_x);
            String dateString = DateFormat.getDateInstance(DateFormat.FULL).format(date);
            btDateDebut.setText("Date de début: " + dateString);
        }
    };

    private boolean verifierDonnees() {
        if (!controlchampsvide(edAliment) && !controlchampsvide(edDuree) && !controlchampsvide(edCout)) {
            if (dateDebut != null) {
                int diff = Daybetween(dateDuJour, dateDebut, "yyyy-mm-dd");
                System.out.println("difference: "+diff);
                if ( diff > 0) {
                    return true;
                } else {
                    alert("ERREUR", "Vous ne pouvez planifier une dépense antérieure à la date d'aujourd'hui, veuillez modifier la date de début.");
                    return false;
                }
            } else {
                alert("ERREUR", "Veuillez renseigner la date de début.");
                return false;
            }
        }
        return false;
    }

    public static int Daybetween(String date1, String date2, String pattern) {
        SimpleDateFormat sdf = new SimpleDateFormat(pattern);
        Date Date1 = null, Date2 = null;
        try {
            Date1 = sdf.parse(date1);
            Date2 = sdf.parse(date2);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return (int) ((Date2.getTime() - Date1.getTime()) / (24 * 60 * 60 * 1000));
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

    private void alert(String alertType, String message){
        AlertDialog.Builder builder = new AlertDialog.Builder(PlanningSpending.this);
        builder.setTitle(alertType);
        builder.setMessage(message);
        builder.setNeutralButton("OK", null);
        builder.create();
        builder.show();
    }
}
