package com.example.mybudget.activities;

import android.app.Application;

public class Constants extends Application {
    public int fragmentActivated = -1;

    public int getFragmentActivated() {
        return fragmentActivated;
    }

    public void setFragmentActivated(int fragmentActivated) {
        this.fragmentActivated = fragmentActivated;
    }
}
