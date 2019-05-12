package com.example.mybudget.activities;

import android.content.Intent;
import android.icu.text.SimpleDateFormat;
import android.icu.util.Calendar;
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

import java.util.ArrayList;
import java.util.Date;

public class LaunchingActivity extends AppCompatActivity {

    RelativeLayout logo;
    MyBudgetDB myBudgetDB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launching);

        myBudgetDB = new MyBudgetDB(getApplicationContext());
        myBudgetDB.UpdateSpendingHistory();




        Date c = Calendar.getInstance().getTime();
//        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
//        String formattedDate = df.format(c);
//        System.out.println(formattedDate);
        System.out.println("Reponse "+myBudgetDB.notifications());
        ArrayList<String> notif = myBudgetDB.getNotificationsList();
        for (int i=0;i<notif.size();i++) {
            System.out.println(notif.get(i));
        }


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
