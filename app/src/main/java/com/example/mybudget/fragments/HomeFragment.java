package com.example.mybudget.fragments;


import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.content.FileProvider;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewStub;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mybudget.R;
import com.example.mybudget.models.PlannedSpending;
import com.example.mybudget.adapters.PlanningSpendingGridViewAdapter;
import com.example.mybudget.adapters.PlanningSpendingListViewAdapter;
import com.example.mybudget.database.MyBudgetDB;
import com.example.mybudget.services.Screenshot;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.io.File;
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
    private PlanningSpendingListViewAdapter listViewAdapter;
    private PlanningSpendingGridViewAdapter gridViewAdapter;
    private List<PlannedSpending> PlannedSpendingList;
    private int currentViewMode = 0;
    MyBudgetDB myBudgetDB;
    static final int VIEW_MODE_LISTVIEW = 0;
    static final int VIEW_MODE_GRIDVIEW = 1;

    private Button btSeeMore;

    private TextView edDepensesVide;

    private BarChart barChart;
    private PieChart pieChart;



    ImageView addSpending;
    View rootView ;
    private Context context;



    public HomeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View homeView = inflater.inflate(R.layout.fragment_home, container, false);

        context = this.getActivity().getApplicationContext();


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
                startActivity(new Intent(getContext(), PlannedSpending.class));
            }
        });

        //get list of spending
        getSpendingList();

        //Get current view mode in share reference
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("ViewMode", MODE_PRIVATE);
        currentViewMode = sharedPreferences.getInt("currentViewMode", VIEW_MODE_GRIDVIEW);//Default is view listview

        switchView();

        //graphe
        barChart = homeView.findViewById(R.id.bar_chart);
        pieChart = homeView.findViewById(R.id.pieChart);
        ArrayList<BarEntry> barEntries = new ArrayList<>();

        barEntries.add(new BarEntry(44f,0));
        barEntries.add(new BarEntry(88f,1));
        barEntries.add(new BarEntry(32f,2));
        barEntries.add(new BarEntry(53f,3));
        barEntries.add(new BarEntry(12f,4));
        barEntries.add(new BarEntry(6f,5));
        BarDataSet barDataSet = new BarDataSet(barEntries, "Dates");
        barDataSet.setColors(ColorTemplate.COLORFUL_COLORS);

        ArrayList<String> dates = new ArrayList<>();
        dates.add("Janvier");
        dates.add("Février");
        dates.add("Mars");
        dates.add("Avril");
        dates.add("Mai");
        //BarDataSet barDataSet2 = new BarDataSet(dates, "Dates");
        //dates.add("Juin");
        /*dates.add("");
        dates.add("");
        dates.add("");
        dates.add("");
        dates.add("");
        dates.add("");
        dates.add("");
        dates.add("");
        dates.add("");
        dates.add("");
        dates.add("");*/
        BarData barData = new BarData(barDataSet);
        barData.setBarWidth(0.9f);
        //barChart.setData(theData);

        barChart.setVisibility(View.VISIBLE);
        barChart.animateY(5000);
        barChart.setData(barData);
        barChart.setFitBars(true);

        Description description = new Description();
        description.setText("Description test");
        barChart.setDescription(description);
        barChart.invalidate();



        rootView = this.getActivity().getWindow().getDecorView().findViewById(android.R.id.content);

        FloatingActionButton floatingActionButton = homeView.findViewById(R.id.fab);

        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Bitmap bitmap = Screenshot.getScreenShot(rootView);
                File file = Screenshot.saveBitmap(bitmap,"screenshot.png");
                Uri uri= FileProvider.getUriForFile(context,
                        context.getApplicationContext()
                                .getPackageName() + ".fileprovider", file);
                shareImage(uri);

            }
        });


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
            listViewAdapter = new PlanningSpendingListViewAdapter(getActivity(), R.layout.spending_list_item, PlannedSpendingList);
            listView.setAdapter(listViewAdapter);
        } else {
            gridViewAdapter = new PlanningSpendingGridViewAdapter(getActivity(), R.layout.spending_grid_item, PlannedSpendingList);
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
                    res.getString(5),
                    res.getString(6),
                    res.getString(7)
            );
            PlannedSpendingList.add(spending);
        }
        return PlannedSpendingList;
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
