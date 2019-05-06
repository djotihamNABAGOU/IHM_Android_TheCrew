package com.example.mybudget.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import com.example.mybudget.R;
import com.example.mybudget.database.MyBudgetDB;

public class LaunchingActivity extends AppCompatActivity {

    RelativeLayout logo;
    MyBudgetDB myBudgetDB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launching);

        myBudgetDB = new MyBudgetDB(getApplicationContext());
        myBudgetDB.UpdateSpendingHistory();

        logo = (RelativeLayout) findViewById(R.id.loadingPanel);
        //findViewById(R.id.loadingPanel).setVisibility(View.GONE);

        Animation anim = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.launching);
        logo.setAnimation(anim);

        anim.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                logo.setVisibility(View.GONE);
                finish();
                startActivity(new Intent(getApplicationContext(), MainActivity.class));

            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }
}
