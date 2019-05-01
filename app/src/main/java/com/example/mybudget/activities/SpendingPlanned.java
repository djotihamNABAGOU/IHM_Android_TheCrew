package com.example.mybudget.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;

import com.example.mybudget.R;

public class SpendingPlanned extends AppCompatActivity {
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_spending_planned);

        /**ToolBar*/
        toolbar = findViewById(R.id.toolBar);
        toolbar.setTitle(R.string.dashboard_planification);
        toolbar.setSubtitle(R.string.sub_spendingPlanned);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }
}
