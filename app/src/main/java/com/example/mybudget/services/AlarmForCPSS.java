package com.example.mybudget.services;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import com.example.mybudget.activities.MainActivity;
import android.util.Log;

import java.util.Calendar;

import static android.app.PendingIntent.getActivity;

public class AlarmForCPSS { //Alarm for check planned notification service class

    private AlarmManager alarmMgr;
    private PendingIntent alarmIntent;

    public void main(){
        Log.d("YOOOO","yoooo");
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.set(Calendar.HOUR_OF_DAY, 5);
        Intent intentForStartCPSS = new Intent(null, CheckPlannedSpendindService.class);
        alarmIntent = PendingIntent.getService(null, 0, intentForStartCPSS, 0);
        // With setInexactRepeating(), you have to use one of the AlarmManager interval
        // constants--in this case, AlarmManager.INTERVAL_DAY.

        alarmMgr.setInexactRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),
        AlarmManager.INTERVAL_DAY, alarmIntent);
    }
}
