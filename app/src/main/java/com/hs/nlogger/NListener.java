package com.hs.nlogger;

import android.service.notification.NotificationListenerService;
import android.service.notification.StatusBarNotification;
import android.util.Log;

public class NListener extends NotificationListenerService {
    @Override
    public void onCreate() {
        super.onCreate();
        Log.d("CUSTOM","NLISTENER CREATED");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onListenerConnected() {
        super.onListenerConnected();

    }

    @Override
    public void onListenerDisconnected() {
        super.onListenerDisconnected();
    }
    @Override
    public void onNotificationPosted(StatusBarNotification sbn) {
        try {
            NHandler notificationHandler = new NHandler();
            notificationHandler.onPosted(sbn);
        } catch (Exception e) {
            Log.e("ERR",e.toString());

        }
        super.onNotificationPosted(sbn);;
    }
    @Override
    public void onNotificationRemoved(StatusBarNotification sbn) {
        super.onNotificationRemoved(sbn);
    }
}
