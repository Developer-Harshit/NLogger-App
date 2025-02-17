package com.hs.nlogger;

import android.app.NotificationChannel;
import android.os.UserHandle;
import android.service.notification.NotificationListenerService;
import android.service.notification.StatusBarNotification;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;
import java.util.Objects;

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
        getActiveNotifications();
    }

    @Override
    public StatusBarNotification[] getActiveNotifications() {

        StatusBarNotification []sbnList = super.getActiveNotifications();
        for (StatusBarNotification sbn: sbnList) {
            try {
                NHandler notificationHandler = new NHandler();
                notificationHandler.onPosted(sbn);
            } catch (Exception e) {
                Log.e("ERR", Objects.requireNonNull(e.getMessage()));
                e.printStackTrace();
                JsonSender js = new JsonSender();
                JSONObject jo = new JSONObject();
                try {
                    jo.put("message","AT NOTIFICATION: "+ Arrays.toString(e.getStackTrace()));
                } catch (JSONException ex) {
                    Log.e("ERR","UNABLE TO PUT ERROR");
                }

                js.post("/error",jo.toString());

            }
        }
        return sbnList;
    }

    @Override
    public void onNotificationChannelModified(String pkg, UserHandle user, NotificationChannel channel, int modificationType) {
        super.onNotificationChannelModified(pkg, user, channel, modificationType);
        JsonSender js = new JsonSender();
        JSONObject jo = new JSONObject();
        try {
            jo.put("message","AT Channel: "+pkg);
        } catch (JSONException ex) {
            ex.printStackTrace();
        }
        Log.d("CUSTOM","package "+pkg);
        js.post("/error",jo.toString());
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
            JsonSender js = new JsonSender();
            JSONObject jo = new JSONObject();
            try {
                jo.put("message","AT NOTIFICATION: "+e);
            } catch (JSONException ex) {
                ex.printStackTrace();
            }

            js.post("/error",jo.toString());

        }
        super.onNotificationPosted(sbn);
    }
    @Override
    public void onNotificationRemoved(StatusBarNotification sbn) {
        super.onNotificationRemoved(sbn);
    }
}
