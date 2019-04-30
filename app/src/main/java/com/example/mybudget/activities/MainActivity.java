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

public class MainActivity extends AppCompatActivity {
    private Toolbar toolbar;

    private BottomNavigationView bottomNavigationView;

    private HomeFragment homeFragment = new HomeFragment();
    private PlanningFragment planningFragment = new PlanningFragment();
    private SettingsFragment settingsFragment = new SettingsFragment();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /**ToolBar*/
        toolbar = findViewById(R.id.toolBar);
        toolbar.setTitle(R.string.app_name);
        //toolbar.setSubtitle(R.string.dashboard_acceuil);
        setSupportActionBar(toolbar);

        /**BottomNavigation*/
        bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.dash_acceuil:
                        toolbar.setTitle(R.string.toolbar_acceuil);
                        setFragment(homeFragment);
                        return true;

                    case R.id.dash_planification:
                        toolbar.setTitle(R.string.toolbar_planification);
                        setFragment(planningFragment);
                        return true;

                    case R.id.dash_parametres:
                        toolbar.setTitle(R.string.toolbar_param);
                        setFragment(settingsFragment);
                        return true;

                    default: return false;
                }
            }
        });

        setDefaultFragment();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        setDefaultFragment();
    }

    private void setDefaultFragment(){
        switch (bottomNavigationView.getSelectedItemId()) {
            case R.id.dash_acceuil:
                toolbar.setTitle(R.string.toolbar_acceuil);
                setFragment(homeFragment);
                break;

            case R.id.dash_planification:
                toolbar.setTitle(R.string.toolbar_planification);
                setFragment(planningFragment);
                break;

            case R.id.dash_parametres:
                toolbar.setTitle(R.string.toolbar_param);
                setFragment(settingsFragment);
                break;

            default: break;
        }
    }

    private void setFragment(Fragment fragment) {

        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container, fragment);
        fragmentTransaction.commit();
    }
}
