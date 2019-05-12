package com.example.mybudget.activities;

import android.app.AlertDialog;
import android.app.FragmentManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.TextureView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.mybudget.R;
import com.example.mybudget.database.MyBudgetDB;
import com.example.mybudget.fragments.HomeFragment;
import com.example.mybudget.services.NotifyService;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class AddSpending extends AppCompatActivity {
    private Toolbar toolbar;

    private EditText edAliment, edCout;
    private Button btEnregistrer;


    MyBudgetDB myBudgetDB;
    private static final String TAG = "AddSpending";
    public static final String CHANNEL_ID = "channelSpendind";
    public static final int NOTIFICATION_ID = 12345;


    final static String SERVICE_RECEIVER = "registerReceiver";
    final static String SERVICE_BROADCAST_KEY = "MyBudgetService";
    final static int RQS_SEND_SERVICE = 2;

    private NotifyService.NotifyServiceReceiver notifyServiceReceiver;


    @Override
    protected void onStart(){
        super.onStart();
        // Create Notification (API 26+)
        if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.O){
            CharSequence name = "Channel Spending";
            String description = "Chanel for Spending Notification";
            int importance = NotificationManager.IMPORTANCE_DEFAULT; //Importance par defaut

            NotificationChannel channel = new NotificationChannel(CHANNEL_ID,name,importance);
            channel.setDescription(description);
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);

        }
    }

    public void showNotification(String title,String content){
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this,CHANNEL_ID)
                .setSmallIcon(R.drawable.saved)
                .setContentTitle(title)
                .setContentText(content)
                .setPriority(NotificationCompat.PRIORITY_HIGH);

        NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(this);
        notificationManagerCompat.notify(NOTIFICATION_ID,notificationBuilder.build());

    }

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

        /*Intent intent = new Intent(this,NotifyService.class);
        startService(intent);
        Button btnTesting = (Button) findViewById(R.id.btnTesting);
        btnTesting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                Log.d(TAG,"TEST STARTED");
                Intent intent = new Intent();
                intent.setAction(SERVICE_RECEIVER);
                intent.putExtra(SERVICE_BROADCAST_KEY,RQS_SEND_SERVICE);
                sendBroadcast(intent);
            }

        });*/
        btEnregistrer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //check data before save them
                if (v == btEnregistrer) {
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

        if (myBudgetDB.insertSpending("imprevu",libelle_aliment, date_debut, cout, true)) {
            showNotification("Enregistrement Dépense","Dépense enregitrée avec succes");
            alert("INFORMATION", "Dépense enregistrée avec succès", true, 1);
            //finish();
        } else
            alert("ERREUR", "Données non sauvegardées", false, 0);


    }
}
