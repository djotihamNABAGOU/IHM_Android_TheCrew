package com.example.mybudget.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.TextureView;
import android.widget.TextView;

import com.example.mybudget.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class AddSpending extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_spending);
        setSpendingDate();

    }

    private void setSpendingDate(){
        TextView spendindDate = findViewById(R.id.edSpendingDate);
        SimpleDateFormat dateformat = new SimpleDateFormat("dd-MMM-yyyy");
        spendindDate.setText(dateformat.format(Calendar.getInstance().getTime()));
    }
}
