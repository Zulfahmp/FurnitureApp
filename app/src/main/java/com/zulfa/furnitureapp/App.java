package com.zulfa.furnitureapp;

import android.app.Application;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.os.Build;

public class App extends Application {
    public static final String ORDERAN_ID = "orderan";

    @Override
    public void onCreate() {
        super.onCreate();

        createNotificationChannels();
    }

    private void createNotificationChannels() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            NotificationChannel order = new NotificationChannel(ORDERAN_ID,
                    "orderan",
                    NotificationManager.IMPORTANCE_HIGH);
            order.setDescription("This is orderan");

            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(order);
        }
    }
}
