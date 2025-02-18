package com.hs.nlogger;

import android.app.NotificationChannel;
import android.os.UserHandle;
import android.service.notification.NotificationListenerService;
import android.service.notification.StatusBarNotification;
import android.util.Log;

public class NListener extends NotificationListenerService {
    @Override
    public void onCreate() {
        super.onCreate();
        Log.d("CUSTOM","N LISTENER CREATED");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onListenerConnected() {
        super.onListenerConnected();
        getActiveNotifications();
    }
    @Override
    public StatusBarNotification[] getActiveNotifications() {
        StatusBarNotification []sbnList = super.getActiveNotifications();
        JsonSender jo = new JsonSender();
        for (StatusBarNotification sbn: sbnList) {
            try {
                jo.postNotif(sbn);
            } catch (Exception e) {
                jo.postError(e);
            }
        }
        return sbnList;
    }

    @Override
    public void onNotificationChannelModified(String pkg, UserHandle user, NotificationChannel channel, int modificationType) {
        super.onNotificationChannelModified(pkg, user, channel, modificationType);
        JsonSender js = new JsonSender();
        js.postMessage("package " + pkg);
    }

    @Override
    public void onListenerDisconnected() {
        super.onListenerDisconnected();
    }
    @Override
    public void onNotificationPosted(StatusBarNotification sbn) {
        JsonSender js = new JsonSender();
        try {
            js.postNotif(sbn);
        } catch (Exception e) {
            js.postError(e);
        }
        super.onNotificationPosted(sbn);
    }
    @Override
    public void onNotificationRemoved(StatusBarNotification sbn) {
        super.onNotificationRemoved(sbn);
    }
}
