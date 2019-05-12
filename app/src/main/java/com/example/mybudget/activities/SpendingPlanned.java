package com.example.mybudget.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.icu.text.SimpleDateFormat;
import android.icu.util.Calendar;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewStub;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.mybudget.R;
import com.example.mybudget.adapters.PlanningSpendingGridViewAdapter;
import com.example.mybudget.adapters.PlanningSpendingListViewAdapter;
import com.example.mybudget.database.MyBudgetDB;
import com.example.mybudget.models.PlannedSpending;

import java.text.DateFormatSymbols;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class SpendingPlanned extends AppCompatActivity {
    private Toolbar toolbar;

    //    ### Attrbuts
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

    private Button btH;

    //gestion calendrier des dépenses
    private Button month;
    private ImageView previousMonth;
    private ImageView nextMonth;
    private ArrayList<String> availableMonths;
    private ArrayList<Integer> availableMonthsInt;
    private String actualMonth;
    private int monthSet;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_spending_planned);

        //La BD
        myBudgetDB = new MyBudgetDB(getApplicationContext());

        /**ToolBar*/
        toolbar = findViewById(R.id.include);
        toolbar.setTitle(R.string.dashboard_planification);
        toolbar.setSubtitle(R.string.sub_spendingPlanned);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //Button
        btH = (Button) findViewById(R.id.completeHistory);
        btH.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                System.out.println("ici");
                if (VIEW_MODE_LISTVIEW == currentViewMode) {
                    currentViewMode = VIEW_MODE_GRIDVIEW;
                } else {
                    currentViewMode = VIEW_MODE_LISTVIEW;
                }
                //Switch view
                switchView();
                //Save view mode in share reference
                SharedPreferences sharedPreferences = getSharedPreferences("ViewMode", MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putInt("currentViewMode", currentViewMode);
                editor.commit();
            }
        });

        //gestion des mois
        month = findViewById(R.id.month);
        availableMonths = new ArrayList<String>();
        availableMonthsInt = new ArrayList<Integer>();
        Cursor months = myBudgetDB.getCurrentMonthsPlanningSpending();
        while (months.moveToNext()) {
            availableMonthsInt.add(Integer.parseInt(months.getString(0)));
            String monthString = new DateFormatSymbols().getMonths()[Integer.parseInt(months.getString(0)) - 1];
            availableMonths.add(monthString.toUpperCase() + " " + months.getString(1));
        }
        System.out.println(availableMonths.toString());
        //get the actual month
        Date c = Calendar.getInstance().getTime();
        SimpleDateFormat df = new SimpleDateFormat("MM");
        actualMonth = df.format(c);
        //System.out.println("actualMonth "+actualMonth);
        String edText = (new DateFormatSymbols().getMonths()[Integer.parseInt(actualMonth) - 1]).toUpperCase();
        SimpleDateFormat year = new SimpleDateFormat("yyyy");
        edText = edText + " " + year.format(c);
        month.setText(edText);
        monthSet = Integer.parseInt(actualMonth);
        //requête en fonction du mois actuel
        getSpendingList(actualMonth);

        previousMonth = findViewById(R.id.previousMonth);
        previousMonth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (availableMonths.size()!=0){
                    int indexMonthSet = availableMonthsInt.indexOf(monthSet);
                    if (indexMonthSet!= -1 && indexMonthSet >0){
                        monthSet = availableMonthsInt.get(indexMonthSet-1);//on décale
                        getSpendingList(String.valueOf(monthSet));
                        month.setText(availableMonths.get(indexMonthSet-1));
                        setAdapters();
                    }else {
                        Toast.makeText(getApplicationContext(),
                                "Il n'y a pas de dépenses planifiées en aval",
                                Toast.LENGTH_LONG).show();
                    }
                }else {
                    Toast.makeText(getApplicationContext(),
                            "Il n'y a aucune dépense planifiée",
                            Toast.LENGTH_LONG).show();
                }
            }
        });

        nextMonth = findViewById(R.id.nextMonth);
        nextMonth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (availableMonths.size()!=0){
                    int indexMonthSet = availableMonthsInt.indexOf(monthSet);
                    if (indexMonthSet!= -1 && indexMonthSet <availableMonths.size()-1){
                        monthSet = availableMonthsInt.get(indexMonthSet+1);//on décale
                        getSpendingList(String.valueOf(monthSet));
                        month.setText(availableMonths.get(indexMonthSet+1));
                        setAdapters();
                    }else {
                        Toast.makeText(getApplicationContext(),
                                "Il n'y a pas de dépenses planifiées en amont",
                                Toast.LENGTH_LONG).show();
                    }
                }else {
                    Toast.makeText(getApplicationContext(),
                            "il n'y a aucune dépense planifiée",
                            Toast.LENGTH_LONG).show();
                }
            }
        });

        stubList = (ViewStub) findViewById(R.id.stub_list);
        stubGrid = (ViewStub) findViewById(R.id.stub_grid);

        //Inflate ViewStub before get view

        stubList.inflate();
        stubGrid.inflate();

        listView = (ListView) findViewById(R.id.spendinglistview);
        gridView = (GridView) findViewById(R.id.spendinggridview);


        //Get current view mode in share reference
//        SharedPreferences sharedPreferences = getSharedPreferences("ViewMode", MODE_PRIVATE);
//        currentViewMode = sharedPreferences.getInt("currentViewMode", VIEW_MODE_LISTVIEW);//Default is view listview

        switchView();

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
            listViewAdapter = new PlanningSpendingListViewAdapter(this, R.layout.spending_list_item, PlannedSpendingList);
            listView.setAdapter(listViewAdapter);
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                @Override
                public void onItemClick(AdapterView<?> parent,
                                        View view,
                                        int position,
                                        long id) {

                    Intent bluetoothIntent = new Intent(getApplicationContext(), BluetoothActivity.class);
                    bluetoothIntent.putExtra("Activity", listView.getItemAtPosition(position).toString());
                    bluetoothIntent.putExtra("spending", (Parcelable) PlannedSpendingList.get(position));

                        startActivity(bluetoothIntent);
                    }
        });
        } else {
            gridViewAdapter = new PlanningSpendingGridViewAdapter(this, R.layout.spending_grid_item, PlannedSpendingList);
            gridView.setAdapter(gridViewAdapter);
            gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                @Override
                public void onItemClick(AdapterView<?> parent,
                                        View view,
                                        int position,
                                        long id) {



                    Intent bluetoothIntent = new Intent(getApplicationContext(), BluetoothActivity.class);
                    bluetoothIntent.putExtra("Activity", listView.getItemAtPosition(position).toString());
                    bluetoothIntent.putExtra("spending", (Parcelable) PlannedSpendingList.get(position));

                    startActivity(bluetoothIntent);
                }
            });
        }
    }


    public void getSpendingList(String month) {
        PlannedSpendingList = new ArrayList<>();
        Cursor res = myBudgetDB.getPlanningSpendingOfMonth(month);
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

}
