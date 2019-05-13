package com.example.mybudget.activities;

import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewStub;
import android.widget.GridView;
import android.widget.ListView;

import com.example.mybudget.R;
import com.example.mybudget.adapters.PlanningSpendingGridViewAdapter;
import com.example.mybudget.adapters.PlanningSpendingListViewAdapter;
import com.example.mybudget.adapters.SpendingGridViewAdapter;
import com.example.mybudget.adapters.SpendingListViewAdapter;
import com.example.mybudget.database.MyBudgetDB;
import com.example.mybudget.models.PlannedSpending;
import com.example.mybudget.models.Spending;

import java.util.ArrayList;
import java.util.List;

public class SpendingReceived extends AppCompatActivity {
    private Toolbar toolbar;

    //    ### Attrbuts
    private ViewStub stubGrid;
    private ViewStub stubList;
    private ListView listView;
    private GridView gridView;
    private PlanningSpendingListViewAdapter listViewAdapter;
    private PlanningSpendingGridViewAdapter gridViewAdapter;
    private List<PlannedSpending> SpendingSharedList;
    private int currentViewMode = 0;
    MyBudgetDB myBudgetDB;
    static final int VIEW_MODE_LISTVIEW = 0;
    static final int VIEW_MODE_GRIDVIEW = 1;
    private String currentText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_spending_received);

        //La BD
        myBudgetDB = new MyBudgetDB(getApplicationContext());

        /**ToolBar*/
        toolbar = findViewById(R.id.include);
        toolbar.setTitle(R.string.app_name);
        toolbar.setSubtitle("Dépenses partagées");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        getSpendingSharedList();

        stubList = (ViewStub) findViewById(R.id.stub_list);
        stubGrid = (ViewStub) findViewById(R.id.stub_grid);

        //Inflate ViewStub before get view

        stubList.inflate();
        stubGrid.inflate();

        listView = (ListView) findViewById(R.id.spendinglistview);
        gridView = (GridView) findViewById(R.id.spendinggridview);

        switchView();
    }

    private void getSpendingSharedList() {
        SpendingSharedList = new ArrayList<>();
        Cursor res = null;
        res = myBudgetDB.getSharedPlanningSpending();

        while (res.moveToNext()) {
            PlannedSpending spending = new PlannedSpending(
                    res.getString(0),
                    res.getString(1),
                    res.getString(2),
                    res.getString(3),
                    res.getString(4),
                    res.getString(5),
                    res.getString(6),
                    res.getString(7)
            );
            SpendingSharedList.add(spending);
        }
    }

    private void switchView() {

        if (VIEW_MODE_LISTVIEW == currentViewMode) {
            //Display listview
            stubList.setVisibility(View.VISIBLE);
            //Hide gridview
            stubGrid.setVisibility(View.GONE);
        } else {
            //Hide listview
            stubList.setVisibility(View.GONE);
            //Display gridview
            stubGrid.setVisibility(View.VISIBLE);
        }
        setAdapters();
    }

    private void setAdapters() {
        if (VIEW_MODE_LISTVIEW == currentViewMode) {
            listViewAdapter = new PlanningSpendingListViewAdapter(this, R.layout.spending_list_item, SpendingSharedList);
            listView.setAdapter(listViewAdapter);
        } else {
            gridViewAdapter = new PlanningSpendingGridViewAdapter(this, R.layout.spending_grid_item, SpendingSharedList);
            gridView.setAdapter(gridViewAdapter);
        }
    }
}
