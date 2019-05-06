package com.example.mybudget.activities;

import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.example.mybudget.R;
import com.example.mybudget.fragments.HomeFragment;
import com.example.mybudget.fragments.PlanningFragment;
import com.example.mybudget.fragments.SettingsFragment;
import com.example.mybudget.fragments.SpendingsFragment;

public class MainActivity extends AppCompatActivity {
    private Toolbar toolbar;

    private BottomNavigationView bottomNavigationView;

    private HomeFragment homeFragment = new HomeFragment();
    private SpendingsFragment spendingsFragment = new SpendingsFragment();
    private PlanningFragment planningFragment = new PlanningFragment();
    private SettingsFragment settingsFragment = new SettingsFragment();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /**ToolBar*/
        toolbar = findViewById(R.id.toolBar);
        toolbar.setTitle(R.string.app_name);
        setSupportActionBar(toolbar);

        /**BottomNavigation*/
        bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                return setAppropriateFragment(menuItem.getItemId());
            }
        });

        //set a default fragment depends on the first launching app or a back from another activity
        setDefaultFragment();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        setDefaultFragment();
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

            case R.id.dash_depenses:
                toolbar.setTitle(R.string.toolbar_spendings);
                setFragment(spendingsFragment);
                ((Constants) this.getApplication()).setFragmentActivated(R.id.dash_depenses);
                return true;

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
        fragmentTransaction.commit();
    }
}
