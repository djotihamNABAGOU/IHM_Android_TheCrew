package com.example.mybudget.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.example.mybudget.R;

public class LaunchingActivity extends AppCompatActivity {

    ImageView logo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launching);

        logo = (ImageView)findViewById(R.id.logo);

        Animation anim = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.launching);
        logo.setAnimation(anim);

        anim.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                finish();

                startActivity(new Intent(getApplicationContext(),MainActivity.class));

            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }
}
