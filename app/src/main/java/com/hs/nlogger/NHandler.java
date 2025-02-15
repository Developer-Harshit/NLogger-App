package com.hs.nlogger;

import android.service.notification.StatusBarNotification;

public class NHandler {
    public JsonSender sender = new JsonSender();

    public void onPosted(StatusBarNotification sbn) {

        String json = new NObject(sbn).getJsonString();
        String route = "/send";
        sender.post(route,json);

    }

}
