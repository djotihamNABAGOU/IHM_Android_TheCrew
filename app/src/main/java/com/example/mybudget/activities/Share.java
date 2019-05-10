package com.example.mybudget.activities;

import android.bluetooth.BluetoothAdapter;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.mybudget.R;

import java.io.File;
import java.util.UUID;

import static android.icu.lang.UCharacter.GraphemeClusterBreak.V;

public class Share extends AppCompatActivity {


    private Toolbar toolbar;

    Button share;

    View rootView ;


    private Context context;






    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_share);

        context = getApplicationContext();


        /**ToolBar*/

        Bundle bundle = getIntent().getExtras();
        toolbar =(Toolbar) findViewById(R.id.toolBar);
        toolbar.setTitle(bundle.getString("Activity"));
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        /**SHARE**/

        rootView = getWindow().getDecorView().findViewById(android.R.id.content);

        share = findViewById(R.id.button4);
        share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Intent myIntent = new Intent(Intent.ACTION_SEND);
                myIntent.setType("text/plain");
                String shareBody = "myBudget by team THE CREW";
                String shareSub = "Subject";
                myIntent.putExtra(Intent.EXTRA_SUBJECT,shareSub);
                myIntent.putExtra(Intent.EXTRA_TEXT,shareBody);
                startActivity(Intent.createChooser(myIntent,"Share using"));

            }
        });


    }

    private void shareImage(Uri uri){
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_SEND);
        intent.setType("image/*");

        intent.putExtra(android.content.Intent.EXTRA_SUBJECT, "");
        intent.putExtra(android.content.Intent.EXTRA_TEXT, "");
        intent.putExtra(Intent.EXTRA_STREAM, uri);
        try {
            startActivity(Intent.createChooser(intent, "Share Screenshot"));
        } catch (ActivityNotFoundException e) {
            Toast.makeText(context, "No App Available", Toast.LENGTH_SHORT).show();
        }
    }
}
