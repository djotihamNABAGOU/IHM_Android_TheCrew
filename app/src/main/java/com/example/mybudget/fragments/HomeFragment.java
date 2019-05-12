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
import com.example.mybudget.activities.SpendingPlanned;
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
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.text.DateFormatSymbols;
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
    private TextView edRevenu;
    private TextView edEpargne;
    private TextView edSolde;
    private String actualRevenu;
    private String actualEpargne;
    private String actualSolde;
    private BarChart barChart;
    private PieChart pieChart;
    private ArrayList<String> spendingMonths;
    private ArrayList<String> prizeSpengingMonths;


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
                startActivity(new Intent(getContext(), SpendingPlanned.class));
            }
        });

        //get list of spending
        getPlanningSpendingList();

        //Get current view mode in share reference
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("ViewMode", MODE_PRIVATE);
        currentViewMode = sharedPreferences.getInt("currentViewMode", VIEW_MODE_GRIDVIEW);//Default is view listview

        switchView();

        // Text
        edRevenu = homeView.findViewById(R.id.edRevenuA);
        edRevenu.setText(getActualRevenu());
        edEpargne = homeView.findViewById(R.id.edEpargne);
        edEpargne.setText(getActualEpargne());
        edSolde = homeView.findViewById(R.id.edSolde);
        edSolde.setText(getActualSolde());



        //Graphes
        barChart = homeView.findViewById(R.id.bar_chart);
        pieChart = homeView.findViewById(R.id.pieChart);
        //Pour le graph normal
        setGraph();

        //Pour le pie graph
        setPieGraph();

        // Amine
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

    private void setGraph() {
        //graphe
        ArrayList<BarEntry> barEntries = new ArrayList<>();
        //BarEntry barEntryTest = new BarEntry(31f,7);
        //barEntryTest.setData("Mai 2018");
        //barEntryTest.describeContents("");
        //barEntries.add(barEntryTest);

        barEntries.add(new BarEntry(44f, 0, "June"));
        barEntries.add(new BarEntry(88f, 1));
        barEntries.add(new BarEntry(32f, 2));
        barEntries.add(new BarEntry(53f, 3));
        barEntries.add(new BarEntry(12f, 4));
        barEntries.add(new BarEntry(6f, 5));
        BarDataSet barDataSet = new BarDataSet(barEntries, "Dates");
        barDataSet.setColors(ColorTemplate.COLORFUL_COLORS);

        ArrayList<String> dates = new ArrayList<>();
        dates.add("Janvier");
        dates.add("Février");
        dates.add("Mars");
        dates.add("Avril");
        dates.add("Mai");
        //barEntries.addAll(dates);
        //BarDataSet barDataSet2 = new BarDataSet(dates,"date2");
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
        //String datesStr[] = (String[]) dates.toArray();
        String datesStr[] = new String[6];
        for (int i = 0; i < 6; i++) {
            datesStr[i] = "Janvier";
        }
        barDataSet.setStackLabels(datesStr);
        System.out.println("*************************************" + barDataSet.getStackLabels().length);
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
    }

    private void setPieGraph() {
        //Pour le graphe Pie
        List<PieEntry> pieEntries = new ArrayList<>();

        getSpendingMonths();
        System.out.println("********************" + spendingMonths.toString());
        for (int i = 0; i < spendingMonths.size(); i++) {
            String totalPrice = getSpendingPrizeOfMonth(spendingMonths.get(i));
            String monthString = new DateFormatSymbols().getMonths()[Integer.parseInt(spendingMonths.get(i)) - 1];
            pieEntries.add(new PieEntry(Integer.valueOf(totalPrice), monthString.toUpperCase()));
        }
        //pieEntries.add(new PieEntry(12,"Janvier"));
        //pieEntries.add(new PieEntry(18,"Février"));
        //pieEntries.add(new PieEntry(20,"Dec"));

        pieChart.setVisibility(View.VISIBLE);
        pieChart.animateXY(5000, 5000);

        PieDataSet pieDataSet = new PieDataSet(pieEntries, "Pie Graph");
        pieDataSet.setColors(ColorTemplate.COLORFUL_COLORS);

        PieData pieData = new PieData(pieDataSet);
        pieChart.setData(pieData);

        Description description1 = new Description();
        description1.setText("Dépenses par mois");
        pieChart.setDescription(description1);
        pieChart.invalidate();
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
            listViewAdapter = new PlanningSpendingListViewAdapter(getActivity(), R.layout.spending_list_item, PlannedSpendingList);
            listView.setAdapter(listViewAdapter);
        } else {
            gridViewAdapter = new PlanningSpendingGridViewAdapter(getActivity(), R.layout.spending_grid_item, PlannedSpendingList);
            gridView.setAdapter(gridViewAdapter);
        }
    }


    public void getPlanningSpendingList() {
        PlannedSpendingList = new ArrayList<>();
        //database
        myBudgetDB = new MyBudgetDB(getContext());
        Cursor res = myBudgetDB.getCurrentPlanningSpending();
        if (res.getCount() == 0) {
            edDepensesVide.setVisibility(View.VISIBLE);
        }
        if (res.getCount() > 3) {
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
    }

    public void getSpendingMonths() {
        spendingMonths = new ArrayList<>();
        //database
        myBudgetDB = new MyBudgetDB(getContext());
        Cursor res = myBudgetDB.getCurrentMonthsSpending();
        while (res.moveToNext()) {
            spendingMonths.add(res.getString(0));
        }
    }

    public String getActualRevenu() {
        //database
        myBudgetDB = new MyBudgetDB(getContext());
        actualRevenu = Integer.toString(myBudgetDB.getRevenusForActualMonth()) + " €";
        return actualRevenu;
    }

    public String getActualEpargne() {
        //database
        myBudgetDB = new MyBudgetDB(getContext());
        actualEpargne = Integer.toString(myBudgetDB.getEpargneForActualMonth()) + " €";
        return actualEpargne;
    }

    public String getActualSolde() {
        //database
        myBudgetDB = new MyBudgetDB(getContext());
        actualSolde = Integer.toString(myBudgetDB.getSoldeForActualMonth()) + " €";
        return actualSolde;
    }

    private String getSpendingPrizeOfMonth(String month) {
        prizeSpengingMonths = new ArrayList<>();
        //database
        myBudgetDB = new MyBudgetDB(getContext());
        Cursor res = myBudgetDB.getSumSpendingOfMonth(month);
        String cout = new String();
        while (res.moveToNext()) {
            cout = res.getString(0);
        }
        return cout;
    }


    private void shareImage(Uri uri){
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_SEND);
        intent.setType("image/*");

        intent.putExtra(android.content.Intent.EXTRA_SUBJECT, "");
        intent.putExtra(android.content.Intent.EXTRA_TEXT, "");
        intent.putExtra(Intent.EXTRA_STREAM, uri);
        try {
            startActivity(Intent.createChooser(intent, "Share Data"));
        } catch (ActivityNotFoundException e) {
            Toast.makeText(context, "No App Available", Toast.LENGTH_SHORT).show();
        }
    }

}
