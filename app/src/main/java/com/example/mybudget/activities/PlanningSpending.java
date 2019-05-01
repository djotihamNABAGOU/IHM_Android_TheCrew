package com.example.mybudget.activities;

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

import java.util.Calendar;

public class PlanningSpending extends AppCompatActivity {
    private Toolbar toolbar;

    private EditText edAliment, edDuree, edCout;
    private Spinner spFrequence;
    private Button btDateDebut, btPlanifier;
    private ProgressDialog progressDialog;

    int year_x, month_x, day_x;
    private String dateDebut;
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
            //dateactuel= year_x+"-"+month_x+"-"+day_x;
            dateDebut = year_x + "-" + month_x + "-" + day_x;
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
            btDateDebut.setText("Date de début: " + dateDebut);
        }
    };

    private boolean verifierDonnees() {
        if (!controlchampsvide(edAliment) && !controlchampsvide(edDuree) && !controlchampsvide(edCout)) {
            return true;
            /*if (dateDebut != null) {


                if (Daybetween(dateactuel, datenaissance, "yyyy-mm-dd") < 6205) {
                    return true;
                } else {
                    //Toasty.error(this, "Vous devez avoir au moins 17 ans.", Toast.LENGTH_LONG, true).show();
                    return false;

                }
            } else {
                //Toasty.error(this, "Veuillez renseigner votre date de naissance.", Toast.LENGTH_LONG, true).show();
                return false;
            }*/
        }
        return false;
    }

    public static boolean controlchampsvide(EditText champ){
        //verifie si le champ est vide
        if (champ.getText().toString().length() ==0){
            champ.setFocusable(true);
            View focus = null;
            focus = champ;
            champ.setError("Ce champ est requis");
            focus.requestFocus();
            return true;

        }else {
            return false;
        }
    }
}
