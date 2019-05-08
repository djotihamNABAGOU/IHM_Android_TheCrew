package com.example.mybudget.fragments;


import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewStub;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.mybudget.R;
import com.example.mybudget.models.PlannedSpending;
import com.example.mybudget.activities.SpendingHistory;
import com.example.mybudget.adapters.SpendingGridViewAdapter;
import com.example.mybudget.adapters.SpendingListViewAdapter;
import com.example.mybudget.database.MyBudgetDB;

import java.util.ArrayList;
import java.util.List;

import static android.content.Context.MODE_PRIVATE;

/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment {

    private ViewStub stubGrid;
    private ViewStub stubList;
    private ListView listView;
    private GridView gridView;
    private SpendingListViewAdapter listViewAdapter;
    private SpendingGridViewAdapter gridViewAdapter;
    private List<PlannedSpending> PlannedSpendingList;
    private int currentViewMode = 0;
    MyBudgetDB myBudgetDB;
    static final int VIEW_MODE_LISTVIEW = 0;
    static final int VIEW_MODE_GRIDVIEW = 1;

    private Button btSeeMore;

    private TextView edDepensesVide;


    public HomeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View homeView = inflater.inflate(R.layout.fragment_home, container, false);

        stubList = (ViewStub) homeView.findViewById(R.id.stub_list_home);
        stubGrid = (ViewStub) homeView.findViewById(R.id.stub_grid_home);

        //Inflate ViewStub before get view

        stubList.inflate();
        stubGrid.inflate();

        listView = (ListView) homeView.findViewById(R.id.spendinglistview);
        gridView = (GridView) homeView.findViewById(R.id.spendinggridview);

        edDepensesVide = homeView.findViewById(R.id.edDepensesVides);

        btSeeMore = homeView.findViewById(R.id.btnSeeMore);
        btSeeMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(), SpendingHistory.class));
            }
        });

        //get list of spending
        getSpendingList();

        //Get current view mode in share reference
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("ViewMode", MODE_PRIVATE);
        currentViewMode = sharedPreferences.getInt("currentViewMode", VIEW_MODE_GRIDVIEW);//Default is view listview

        switchView();

        return homeView;
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
            listViewAdapter = new SpendingListViewAdapter(getActivity(), R.layout.spending_list_item, PlannedSpendingList);
            listView.setAdapter(listViewAdapter);
        } else {
            gridViewAdapter = new SpendingGridViewAdapter(getActivity(), R.layout.spending_grid_item, PlannedSpendingList);
            gridView.setAdapter(gridViewAdapter);
        }
    }



    public List<PlannedSpending> getSpendingList() {
        PlannedSpendingList = new ArrayList<>();
        //database
        myBudgetDB = new MyBudgetDB(getContext());
        Cursor res = myBudgetDB.getCurrentPlanningSpending();
        if (res.getCount() == 0){
            edDepensesVide.setVisibility(View.VISIBLE);
        }
        if (res.getCount() > 3){
            btSeeMore.setVisibility(View.VISIBLE);
        }
        while (res.moveToNext()) {
            PlannedSpending spending = new PlannedSpending(
                    res.getString(0),
                    res.getString(1),
                    res.getString(2),
                    res.getString(3),
                    res.getString(4),
                    res.getString(7),
                    res.getString(5),
                    res.getString(6)
            );
            PlannedSpendingList.add(spending);
        }
        return PlannedSpendingList;
    }

}
