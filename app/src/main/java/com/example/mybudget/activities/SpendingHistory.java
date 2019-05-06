package com.example.mybudget.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.content.SharedPreferences;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewStub;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ListView;

import com.example.mybudget.adapters.SpendingListViewAdapter;
import com.example.mybudget.adapters.SpendingGridViewAdapter;

import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import com.example.mybudget.R;

public class SpendingHistory extends AppCompatActivity {
    private Toolbar toolbar;

    //    ### Attrbuts
    private ViewStub stubGrid;
    private ViewStub stubList;
    private ListView listView;
    private GridView gridView;
    private SpendingListViewAdapter listViewAdapter;
    private SpendingGridViewAdapter gridViewAdapter;
    private List<Spending> spendingList;
    private int currentViewMode = 0;

    static final int VIEW_MODE_LISTVIEW = 0;
    static final int VIEW_MODE_GRIDVIEW = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_spending_history);

        /**ToolBar*/
        toolbar = findViewById(R.id.toolBar);
        toolbar.setTitle(R.string.dashboard_planification);
        toolbar.setSubtitle(R.string.sub_spendingHistory);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        stubList = (ViewStub) findViewById(R.id.stub_list);
        stubGrid = (ViewStub) findViewById(R.id.stub_grid);

        //Inflate ViewStub before get view

        stubList.inflate();
        stubGrid.inflate();

        listView = (ListView) findViewById(R.id.spendinglistview);
        gridView = (GridView) findViewById(R.id.spendinggridview);

        //get list of spending
        getSpendingList();

        //Get current view mode in share reference
        SharedPreferences sharedPreferences = getSharedPreferences("ViewMode", MODE_PRIVATE);
        currentViewMode = sharedPreferences.getInt("currentViewMode", VIEW_MODE_LISTVIEW);//Default is view listview


        //Register item lick
//        listView.setOnItemClickListener(onItemClick);
//        gridView.setOnItemClickListener(onItemClick); //Get current view mode in share reference
//        SharedPreferences sharedPreferences = getSharedPreferences("ViewMode", MODE_PRIVATE);
//        currentViewMode = sharedPreferences.getInt("currentViewMode", VIEW_MODE_LISTVIEW);//Default is view listview
//        //Register item lick
//        listView.setOnItemClickListener(onItemClick);
//        gridView.setOnItemClickListener(onItemClick);

        switchView();

    }

    private void switchView() {

        if(VIEW_MODE_LISTVIEW == currentViewMode) {
            //Display listview
            stubList.setVisibility(View.VISIBLE);
            //Hide gridview
            stubGrid.setVisibility(View.GONE);
        }
        else {
            //Hide listview
            stubList.setVisibility(View.GONE);
            //Display gridview
            stubGrid.setVisibility(View.VISIBLE);
        }
        setAdapters();
    }

    private void setAdapters() {
        if(VIEW_MODE_LISTVIEW == currentViewMode) {
            listViewAdapter = new SpendingListViewAdapter(this, R.layout.spending_list_item, spendingList);
            listView.setAdapter(listViewAdapter);
        } else {
            gridViewAdapter = new SpendingGridViewAdapter(this, R.layout.spending_grid_item, spendingList);
            gridView.setAdapter(gridViewAdapter);
        }
    }



    public List<Spending> getSpendingList() {
        spendingList = new ArrayList<>();
        spendingList.add(new Spending("1", "Banane", "01/01/01", "journalier", "5", "5€"));
        spendingList.add(new Spending("1", "Saumon", "01/01/01", "journalier", "5", "40€"));
        spendingList.add(new Spending("1", "KFC", "01/01/01", "Mensuel", "5", "20€"));
        spendingList.add(new Spending("1", "Burger", "01/01/01", "hebdomadaire", "5", "7€"));
        spendingList.add(new Spending("1", "Pizza", "01/01/01", "journalier", "5", "12€"));
        spendingList.add(new Spending("1", "Salade", "01/01/01", "journalier", "5", "5€"));
        spendingList.add(new Spending("1", "Orange", "01/01/01", "Mensuel", "5", "8€"));
//        spendingList.add(new Spending("1", "", "01/01/01", "jour", "5", "200"));
        return spendingList;
    }

}

