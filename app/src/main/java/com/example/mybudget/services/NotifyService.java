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
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.util.Log;

import com.example.mybudget.R;
import com.example.mybudget.database.MyBudgetDB;

import java.util.ArrayList;

public class NotifyService extends Service {
    public static final String CHANNEL_ID = "channelMyBudget";
    public static final int NOTIFICATION_ID = 23456;
    final static String ACTION = "registerReceiver";

    final static String SERVICE_BROADCAST_KEY = "MyBudgetService";
    final static int RQS_STOP_SERVICE = 1;
    final static int RQS_SEND_SERVICE = 2;

    private static final String TAG = "NotificationService";
    private NotifyServiceReceiver notifyServiceReceiver;

    @Override
    public void onDestroy(){
        super.onDestroy();
        this.unregisterReceiver(notifyServiceReceiver);
    }
    @Override
    public IBinder onBind(Intent arg0){
        Log.d(TAG,"NotifyService:onBind()");
        return null;
    }
    @Override
    public void onCreate(){
        super.onCreate();
        Log.d(TAG,"NotifyService:OnCreate");

        if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.O){
            CharSequence name = "Channel MyBudget";
            String description = "Chanel for myBudget";
            int importance = NotificationManager.IMPORTANCE_DEFAULT; //Importance par defaut

            NotificationChannel channel = new NotificationChannel(CHANNEL_ID,name,importance);
            channel.setDescription(description);
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);

        }
        notifyServiceReceiver = new NotifyServiceReceiver();
    }
    @Override
    public int onStartCommand(Intent intent,int flags, int startId){
        Log.d(TAG, "NotifyService.onStartCommand # Thread Name: "+Thread.currentThread().getName()+
                "ID: "+Thread.currentThread().getId()+
                "State: "+Thread.currentThread().getState()
        );
        Log.d(TAG,"Received start id "+ startId+ " " + intent + "\n");

        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(ACTION);

        Log.d(TAG,"NotifyService:registerReceiver");
        registerReceiver(notifyServiceReceiver,intentFilter);
        //sendNotification("NOTIFICATION WORKING WITH SERVICE !","I GOT IT");

        return START_NOT_STICKY;
    }

    public void sendNotification(String notificationText, String title){
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this,CHANNEL_ID)
                .setSmallIcon(R.drawable.warning)
                .setContentTitle(title)
                .setContentText(notificationText)
                .setPriority(NotificationCompat.PRIORITY_HIGH);

        NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(this);
        notificationManagerCompat.notify(NOTIFICATION_ID,notificationBuilder.build());

    }

    /*NOTIFICATION RECEIVER CLASS*/

    public class NotifyServiceReceiver extends BroadcastReceiver{
        private int numberOfNotifications = 0;

        @Override
        public void onReceive(Context context, Intent intent){
            NotificationManager notificationManager=null;
            int rqs = intent.getIntExtra(SERVICE_BROADCAST_KEY,0);

            Log.d(TAG,"NotifyServie->NSReceiver:onReceive() rqs="+rqs);
            if(rqs==RQS_STOP_SERVICE){
                Log.d(TAG,"NTS---> Service will be stopped.");
                notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
                if(notificationManager!=null){
                    notificationManager.cancelAll();
                }
                stopSelf();
            }

            if(rqs==RQS_SEND_SERVICE){
                Log.d(TAG,"NTS---> Notification received by the service.");
                MyBudgetDB myBudgetDB = new MyBudgetDB(getApplicationContext());
                if(myBudgetDB.notifications()){
                    ArrayList<String> notifications =new ArrayList<>();
                    notifications = myBudgetDB.getNotificationsList();
                    if(!notifications.isEmpty()){
                        for ( String n: notifications ) {
                            sendNotification("Dépense planifiée",n);
                        }
                    }
                }
               }

            }

        }

}


