package com.example.mybudget.activities;

import android.database.Cursor;
import android.icu.text.SimpleDateFormat;
import android.icu.util.Calendar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.content.SharedPreferences;
import android.view.View;
import android.view.ViewStub;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.mybudget.adapters.PlanningSpendingListViewAdapter;
import com.example.mybudget.adapters.PlanningSpendingGridViewAdapter;

import java.text.DateFormatSymbols;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import com.example.mybudget.R;
import com.example.mybudget.adapters.SpendingGridViewAdapter;
import com.example.mybudget.adapters.SpendingListViewAdapter;
import com.example.mybudget.database.MyBudgetDB;
import com.example.mybudget.models.Spending;

public class SpendingHistory extends AppCompatActivity {
    private Toolbar toolbar;

    //    ### Attrbuts
    private ViewStub stubGrid;
    private ViewStub stubList;
    private ListView listView;
    private GridView gridView;
    private SpendingListViewAdapter listViewAdapter;
    private SpendingGridViewAdapter gridViewAdapter;
    private List<Spending> SpendingList;
    private int currentViewMode = 0;
    MyBudgetDB myBudgetDB;
    static final int VIEW_MODE_LISTVIEW = 0;
    static final int VIEW_MODE_GRIDVIEW = 1;
    private String currentText;

    private Button btH;
    private Button btTextH;
    private boolean actived = false;
    private int sortApply = 1;

    //gestion calendrier des dépenses
    private Button month;
    private ImageView previousMonth;
    private ImageView nextMonth;
    private ImageView sortSpending;
    private ArrayList<String> availableMonths;
    private ArrayList<Integer> availableMonthsInt;
    private String actualMonth;
    private String currentMonth;
    private int monthSet;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_spending_history);

//        SimpleDateFormat month_date = new SimpleDateFormat("MMMM", Locale.US);
//        System.out.println(month_date.toString());

        //La BD
        myBudgetDB = new MyBudgetDB(getApplicationContext());

        /**ToolBar*/
        toolbar = findViewById(R.id.include);
        toolbar.setTitle(R.string.app_name);
        toolbar.setSubtitle(R.string.sub_spendingHistory);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //Button
        btH = (Button) findViewById(R.id.completeHistory);
        btTextH = (Button) findViewById(R.id.testHistory);
//
        btH.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (actived == false) {
                    actived = true;
                    previousMonth.setVisibility(View.GONE);
                    nextMonth.setVisibility(View.GONE);
                    currentText = month.getText().toString();
                    month.setText("Complet");
//                  month.setVisibility(View.GONE);
                    btH.setText("Historique Normal");
                    getSpendingListH();
                    currentViewMode = VIEW_MODE_GRIDVIEW;
                    //Switch view
                    switchView();
                } else {
                    actived = false;
                    btH.setText("Historique Complet");
                    previousMonth.setVisibility(View.VISIBLE);
                    nextMonth.setVisibility(View.VISIBLE);
                    month.setText(currentText);
//                    month.setVisibility(View.VISIBLE);
                    currentViewMode = VIEW_MODE_LISTVIEW;
                    getSpendingList(currentMonth);
                    //Switch view
                    switchView();
                    //Save view mode in share reference
//                    SharedPreferences sharedPreferences = getSharedPreferences("ViewMode", MODE_PRIVATE);
//                    SharedPreferences.Editor editor = sharedPreferences.edit();
//                    editor.putInt("currentViewMode", currentViewMode);
//                    editor.commit();
                }
            }
        });

        //gestion des mois
        month = findViewById(R.id.month);
        availableMonths = new ArrayList<String>();
        availableMonthsInt = new ArrayList<Integer>();
        Cursor months = myBudgetDB.getCurrentMonthsSpending();
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
        currentMonth = actualMonth;
        //System.out.println("actualMonth "+actualMonth);
        String edText = (new DateFormatSymbols().getMonths()[Integer.parseInt(actualMonth) - 1]).toUpperCase();
        SimpleDateFormat year = new SimpleDateFormat("yyyy");
        edText = edText + " " + year.format(c);
        month.setText(edText);
        monthSet = Integer.parseInt(actualMonth);
        //requête en fonction du mois actuel
        getSpendingList(actualMonth);

        sortSpending = findViewById(R.id.sortSpendings);
        sortSpending.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                refreshSort();
            }
        });

        previousMonth = findViewById(R.id.previousMonth);
        previousMonth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (availableMonths.size() != 0) {
                    int indexMonthSet = availableMonthsInt.indexOf(monthSet);
                    if (indexMonthSet != -1 && indexMonthSet > 0) {
                        monthSet = availableMonthsInt.get(indexMonthSet - 1);//on décale
                        getSpendingList(String.valueOf(monthSet));
                        month.setText(availableMonths.get(indexMonthSet - 1));
                        setAdapters();
                    } else {
                        Toast.makeText(getApplicationContext(),
                                "Il n'y a pas de dépenses éffectuées en aval",
                                Toast.LENGTH_LONG).show();
                    }
                } else {
                    Toast.makeText(getApplicationContext(),
                            "Il n'y a aucune dépense éffectuée",
                            Toast.LENGTH_LONG).show();
                }
            }
        });

        nextMonth = findViewById(R.id.nextMonth);
        nextMonth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (availableMonths.size() != 0) {
                    int indexMonthSet = availableMonthsInt.indexOf(monthSet);
                    if (indexMonthSet != -1 && indexMonthSet < availableMonths.size() - 1) {
                        monthSet = availableMonthsInt.get(indexMonthSet + 1);//on décale
                        getSpendingList(String.valueOf(monthSet));
                        month.setText(availableMonths.get(indexMonthSet + 1));
                        setAdapters();
                    } else {
                        Toast.makeText(getApplicationContext(),
                                "Il n'y a pas de dépenses éffectuées en amont",
                                Toast.LENGTH_LONG).show();
                    }
                } else {
                    Toast.makeText(getApplicationContext(),
                            "il n'y a aucune dépense éffectuée",
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

        //get list of spending
        //getSpendingList();

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


    private void refreshSort() {
        if(sortApply!=4) {
            sortApply += 1;
        }else{
            sortApply = 1;
        }

        if (actived == true) {
            if (sortApply == 1) {
                getSpendingListH();
                btTextH.setText("↗DATE");
            } else if (sortApply == 2) {
                getSpendingListH();
                btTextH.setText("↙DATE");
            } else if (sortApply == 3) {
                getSpendingListH();
                btTextH.setText("↗COût");
            } else if (sortApply == 4) {
                getSpendingListH();
                btTextH.setText("↙COût");
            }
            setAdapters();
        } else {
            if (sortApply == 1) {
                getSpendingList(currentMonth);
                btTextH.setText("↗DATE");
            } else if (sortApply == 2) {
                getSpendingList(currentMonth);
                btTextH.setText("↙DATE");
            } else if (sortApply == 3) {
                getSpendingList(currentMonth);
                btTextH.setText("↗COût");
            } else if (sortApply == 4) {
                getSpendingList(currentMonth);
                btTextH.setText("↙COût");
            }
            setAdapters();
        }
    }


    private void setAdapters() {
        if (VIEW_MODE_LISTVIEW == currentViewMode) {
            listViewAdapter = new SpendingListViewAdapter(this, R.layout.spending_list_item, SpendingList);
            listView.setAdapter(listViewAdapter);
        } else {
            gridViewAdapter = new SpendingGridViewAdapter(this, R.layout.spending_grid_item, SpendingList);
            gridView.setAdapter(gridViewAdapter);
        }
    }


    public void getSpendingList(String month) {
        currentMonth = month;
        SpendingList = new ArrayList<>();
        Cursor res = null;
        if(sortApply==1){
            res = myBudgetDB.getSpendingOfMonth(month);
        }else if(sortApply==2){
            res = myBudgetDB.getSpendingOfMonthDateDesc(month);
        }else if(sortApply==3){
            res = myBudgetDB.getSpendingOfMonthPrice(month);
        }else if(sortApply==4){
            res = myBudgetDB.getSpendingOfMonthPriceDesc(month);
        }

        while (res.moveToNext()) {
            Spending spending = new Spending(
                    res.getString(0),
                    res.getString(1),
                    res.getString(2),
                    res.getString(3),
                    res.getString(4),
                    res.getString(5)
            );
            SpendingList.add(spending);
        }
    }

    public void getSpendingListH() {
        SpendingList = new ArrayList<>();
        Cursor res = null;
        if(sortApply==1){
            res = myBudgetDB.getSpending();
        }else if(sortApply==2){
            res = myBudgetDB.getSpendingByDateDesc();
        }else if(sortApply==3){
            res = myBudgetDB.getSpendingByPrice();
        }else if(sortApply==4){
            res = myBudgetDB.getSpendingByPriceDesc();
        }

        while (res.moveToNext()) {
            Spending spending = new Spending(
                    res.getString(0),
                    res.getString(1),
                    res.getString(2),
                    res.getString(3),
                    res.getString(4),
                    res.getString(5)
            );
            SpendingList.add(spending);
        }
    }

    /*
    public List<PlannedSpending> getSpendingList() {
        SpendingList = new ArrayList<>();
        //database
        myBudgetDB = new MyBudgetDB(getApplicationContext());
        myBudgetDB.UpdateSpendingHistory();
        Cursor res = myBudgetDB.getPastPlanningSpending();
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
          SpendingList.add(spending);
        }
        return SpendingList;
    }*/

}

