package com.example.mybudget.activities;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.example.mybudget.R;
import com.example.mybudget.fragments.HomeFragment;
import com.example.mybudget.fragments.PlanningFragment;
import com.example.mybudget.fragments.SettingsFragment;
import com.example.mybudget.fragments.SpendingsFragment;
import com.example.mybudget.services.CheckPlannedSpendindService;
import com.example.mybudget.services.NotifyService;

import java.util.Calendar;

public class MainActivity extends AppCompatActivity {
    private Toolbar toolbar;

    private BottomNavigationView bottomNavigationView;

    private NavigationView navigationView;
    private DrawerLayout drawerLayout;

    private HomeFragment homeFragment = new HomeFragment();
    //private SpendingsFragment spendingsFragment = new SpendingsFragment();
    private PlanningFragment planningFragment = new PlanningFragment();
    private SettingsFragment settingsFragment = new SettingsFragment();

    final static String SERVICE_RECEIVER = "registerReceiver";
    final static int RQS_CHECK_SERVICE = 2;
    final static String SERVICE_BROADCAST_KEY = "CPSService";
    private AlarmManager alarmMgr;
    private PendingIntent alarmIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Intent intentForStartNS = new Intent(getApplicationContext(), NotifyService.class);
        startService(intentForStartNS);
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.set(Calendar.HOUR_OF_DAY, 12);
        calendar.set(Calendar.MINUTE,44 );
        Intent intentForStartCPSS = new Intent(getApplicationContext(), CheckPlannedSpendindService.class);
        alarmIntent = PendingIntent.getService(getApplicationContext(), 0, intentForStartCPSS, 0);
        // With setInexactRepeating(), you have to use one of the AlarmManager interval
        // constants--in this case, AlarmManager.INTERVAL_DAY.
        alarmMgr = (AlarmManager) getApplication().getSystemService(
                Context.ALARM_SERVICE);
        alarmMgr.setInexactRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),
                AlarmManager.INTERVAL_DAY, alarmIntent);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /**ToolBar*/
        toolbar = findViewById(R.id.toolBar);
        toolbar.setTitle(R.string.app_name);
        setSupportActionBar(toolbar);

        final ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeAsUpIndicator(R.drawable.ic_menu_black_24dp);

        /**BottomNavigation*/
        bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                return setAppropriateFragment(menuItem.getItemId());
            }
        });

        drawerLayout = findViewById(R.id.drawer);
        navigationView = findViewById(R.id.navigation_view);

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.drawer_add_spending_imp:
                        menuItem.setChecked(true);
                        drawerLayout.closeDrawers();
                        startActivity(new Intent(getApplicationContext(), AddSpending.class));
                        onRestart();
                        return true;

                    case R.id.drawer_history:
                        menuItem.setChecked(true);
                        drawerLayout.closeDrawers();
                        startActivity(new Intent(getApplicationContext(), SpendingHistory.class));
                        return true;

                    case R.id.drawer_spending_received:
                        menuItem.setChecked(true);
                        drawerLayout.closeDrawers();
                        startActivity(new Intent(getApplicationContext(), SpendingReceived.class));
                        return true;
                }
                return false;
            }
        });

        //set a default fragment depends on the first launching app or a back from another activity
        setDefaultFragment();

    }

    @Override
    protected void onRestart() {
        super.onRestart();
        //setDefaultFragment();

    }

    private void setDefaultFragment(){
        Constants constants = (Constants) this.getApplication();

        if (constants.getFragmentActivated() == -1) {
            //System.out.println(constants.getFragmentActivated());
            setAppropriateFragment(bottomNavigationView.getSelectedItemId());
            constants.setFragmentActivated(bottomNavigationView.getSelectedItemId());
            //System.out.println(constants.getFragmentActivated());
        } else {
            setAppropriateFragment(constants.getFragmentActivated());
            bottomNavigationView.setSelectedItemId(constants.getFragmentActivated());
        }
    }

    private boolean setAppropriateFragment(int item) {
        switch (item) {
            case R.id.dash_acceuil:
                toolbar.setTitle(R.string.toolbar_acceuil);
                setFragment(homeFragment);
                ((Constants) this.getApplication()).setFragmentActivated(R.id.dash_acceuil);
                return true;

                /*
            case R.id.dash_depenses:
                toolbar.setTitle(R.string.toolbar_spendings);
                setFragment(spendingsFragment);
                ((Constants) this.getApplication()).setFragmentActivated(R.id.dash_depenses);
                return true;*/

            case R.id.dash_planification:
                toolbar.setTitle(R.string.toolbar_planification);
                setFragment(planningFragment);
                ((Constants) this.getApplication()).setFragmentActivated(R.id.dash_planification);
                return true;

            case R.id.dash_parametres:
                toolbar.setTitle(R.string.toolbar_param);
                setFragment(settingsFragment);
                ((Constants) this.getApplication()).setFragmentActivated(R.id.dash_parametres);
                return true;

            default:
                return false;
        }
    }

    private void setFragment(Fragment fragment) {

        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container, fragment);
        fragmentTransaction.commitAllowingStateLoss();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){
            case android.R.id.home:
                drawerLayout.openDrawer(GravityCompat.START);
                return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
