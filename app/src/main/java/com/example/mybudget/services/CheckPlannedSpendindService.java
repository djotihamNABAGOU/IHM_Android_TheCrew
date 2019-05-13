package com.example.mybudget.services;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;
import com.example.mybudget.database.MyBudgetDB;


public class CheckPlannedSpendindService extends Service {
    final static String ACTION = "registerReceiver";
    final static String SERVICE_RECEIVER = "registerReceiver";

    final static String SERVICE_BROADCAST_KEY = "CPSService";
    final static String SERVICE_BROADCAST_KEY2 = "MyBudgetService";
    final static int RQS_STOP_SERVICE = 1;
    final static int RQS_CHECK_SERVICE = 2;
    final static int RQS_SEND_SERVICE = 2;

    private static final String TAG = "CPSService";
    private CPSServiceReceiver cpsServiceReceiver;
    @Override
    public void onDestroy(){
        super.onDestroy();
        //this.unregisterReceiver(cpsServiceReceiver);
    }
    @Override
    public IBinder onBind(Intent arg0){
        Log.d(TAG,"CPSService:onBind()");
        return null;
    }
    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG,"CPSService:OnCreate");


    }
    @Override
    public int onStartCommand(Intent intent,int flags, int startId){
        Log.d(TAG, "CPSService.onStartCommand # Thread Name: "+Thread.currentThread().getName()+
                "ID: "+Thread.currentThread().getId()+
                "State: "+Thread.currentThread().getState()
        );
        Log.d(TAG,"Received start id "+ startId+ " " + intent + "\n");

        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(ACTION);

        Log.d(TAG,"CPSService:registerReceiver");
        registerReceiver(cpsServiceReceiver,intentFilter);
        MyBudgetDB myBudgetDB = new MyBudgetDB(getApplicationContext());
        if(myBudgetDB.notifications()){
            //Intent intentForStartNS = new Intent(getApplicationContext(),NotifyService.class);
            //startService(intentForStartNS);
            Intent intentToSend = new Intent();
            intentToSend.setAction(SERVICE_RECEIVER);
            intentToSend.putExtra(SERVICE_BROADCAST_KEY2,RQS_SEND_SERVICE);
            sendBroadcast(intentToSend);
            Log.d(TAG,"CPSService:rmyBudgetBDCchecked------>yes notifications");

        }
        Log.d(TAG,"CPSService:rmyBudgetBDCchecked");
        return START_NOT_STICKY;
    }


    /*CPS RECEIVER CLASS*/

    public class CPSServiceReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            int rqs = intent.getIntExtra(SERVICE_BROADCAST_KEY, 0);
            Log.d(TAG, "CPSServie->CPSReceiver:onReceive() rqs=" + rqs);
            if (rqs == RQS_STOP_SERVICE) {
                Log.d(TAG, "CPS---> Service will be stopped.");
                stopSelf();
            }

            if (rqs == RQS_CHECK_SERVICE) {
                Log.d(TAG, "CPS---> Service will start checking.");
                /*checking if there is some notifications*
                  if there is notifications-->show them by calling Notification Service
                */
                MyBudgetDB myBudgetDB = new MyBudgetDB(getApplicationContext());
                if(myBudgetDB.notifications()){
                    Intent intentForStartNS = new Intent(getApplicationContext(),NotifyService.class);
                    startService(intentForStartNS);
                    Intent intentToSend = new Intent();
                    intentToSend.setAction(SERVICE_RECEIVER);
                    intentToSend.putExtra(SERVICE_BROADCAST_KEY2,RQS_SEND_SERVICE); //2 MEANS THERE SOMETHING TO NOTIFY
                    sendBroadcast(intentToSend);

                }
            }

        }
    }



}
