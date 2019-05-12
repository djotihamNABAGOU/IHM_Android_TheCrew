package com.example.mybudget.activities;

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

    final static String SERVICE_BROADCAST_KEY = "CPSService";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Intent intentForStartCPSS = new Intent(getApplicationContext(), CheckPlannedSpendindService.class);
        startService(intentForStartCPSS);
        Intent intentToSend = new Intent();
        intentToSend.setAction(SERVICE_RECEIVER);
        intentToSend.putExtra(SERVICE_BROADCAST_KEY,2); //2 MEANS START CHECKING
        sendBroadcast(intentToSend);

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
                setFragment(homeFragment,"HOME");
                ((Constants) this.getApplication()).setFragmentActivated(R.id.dash_acceuil);
//                HomeFragment fragm = (HomeFragment)getSupportFragmentManager().findFragmentByTag("HOME");
//                fragm.refreshActualSolde();
                return true;

                /*
            case R.id.dash_depenses:
                toolbar.setTitle(R.string.toolbar_spendings);
                setFragment(spendingsFragment);
                ((Constants) this.getApplication()).setFragmentActivated(R.id.dash_depenses);
                return true;*/

            case R.id.dash_planification:
                toolbar.setTitle(R.string.toolbar_planification);
                setFragment(planningFragment,"PLANNIFICATION");
                ((Constants) this.getApplication()).setFragmentActivated(R.id.dash_planification);
                return true;

            case R.id.dash_parametres:
                toolbar.setTitle(R.string.toolbar_param);
                setFragment(settingsFragment,"PARAMETRE");
                ((Constants) this.getApplication()).setFragmentActivated(R.id.dash_parametres);
                return true;

            default:
                return false;
        }
    }

    private void setFragment(Fragment fragment, String tag) {

        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container, fragment,tag);
        fragmentTransaction.commitAllowingStateLoss();
//        HomeFragment.refreshActualSolde();
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
