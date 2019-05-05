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
    }


}

