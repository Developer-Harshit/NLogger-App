package com.hs.nlogger;

import android.app.Notification;
import android.os.Bundle;
import android.os.Parcelable;
import android.service.notification.StatusBarNotification;
import android.text.SpannableString;
import android.util.Log;
import androidx.core.app.NotificationCompat;
import org.json.JSONObject;

import java.util.Set;
import java.util.TimeZone;
class NObject {
    private final Notification n;

    // General
    private final String packageName;
    private final long postTime;
    private final long systemTime;
    private final boolean isOngoing;

    private long when;
    private int flags;
    private final int nid;
    private final String tag;
 private String tickerText;
    private String title;
    private String titleBig;
    private String text;
    private String textBig;
    private String messages;
    private String textInfo;
    private String textSub;
    private String textSummary;
    private String textLines;

    NObject(StatusBarNotification sbn) {
        n           = sbn.getNotification();
        packageName = sbn.getPackageName();
        postTime    = sbn.getPostTime();
        systemTime  = System.currentTimeMillis();
        isOngoing   = sbn.isOngoing();
        nid         = sbn.getId();
        tag         = sbn.getTag();
        extract();
    }
    private void extract()  {
        // General
        when           = n.when;
        flags          = n.flags;
        Bundle extras = NotificationCompat.getExtras(n);

            if (n.tickerText == null) {
                tickerText = "";
            } else {
                tickerText = (String) n.tickerText;
            }
            if(extras != null) {
                title       = getField(extras,NotificationCompat.EXTRA_TITLE);
                titleBig    = getField(extras,NotificationCompat.EXTRA_TITLE_BIG);
                text        = getField(extras,NotificationCompat.EXTRA_TEXT);
                textBig     = getField(extras,NotificationCompat.EXTRA_BIG_TEXT);
                textInfo    = getField(extras,NotificationCompat.EXTRA_INFO_TEXT);
                textSub     = getField(extras,NotificationCompat.EXTRA_SUB_TEXT);
                textSummary = getField(extras,NotificationCompat.EXTRA_SUMMARY_TEXT);
                Parcelable[] b = (Parcelable[]) extras.get(Notification.EXTRA_MESSAGES);
                if(b != null){
                    StringBuilder sb = new StringBuilder();
                    for (Parcelable tmp : b){
                        Bundle msgBundle = (Bundle) tmp;
                        sb.append(msgBundle.getString("text"));
                        sb.append("\n");
                        Set<String> io = msgBundle.keySet();
                    }
                    messages = sb.toString();
                }
                CharSequence[] lines = extras.getCharSequenceArray(NotificationCompat.EXTRA_TEXT_LINES);
                if(lines != null) {
                    StringBuilder builder = new StringBuilder();
                    for(CharSequence line : lines) {
                        builder.append(line);
                        builder.append("\n");
                    }
                    textLines = "\n"+builder.toString().trim();
                }
            }
        }

    public static String getField(Bundle extras,String key) {
        CharSequence cs = extras.getCharSequence(key);
        if(cs == null) {
            return "";
        } else {
            return (String) cs;
        }
    }

    public String getJsonString() {
        JSONObject json = new JSONObject();
        try {
            json.put("packageName",       packageName);
            json.put("postTime",          postTime);
            json.put("systemTime",        systemTime);
            json.put("offset",            TimeZone.getDefault().getOffset(systemTime));
            json.put("isOngoing",         isOngoing);
            json.put("when",              when);
            json.put("flags",             flags);
            json.put("tickerText",        tickerText);
            json.put("title",             title);
            json.put("titleBig",          titleBig);
            json.put("text",              text);
            json.put("messages",          messages);
            json.put("textBig",           textBig);
            json.put("textInfo",          textInfo);
            json.put("textSub",           textSub);
            json.put("textSummary",       textSummary);
            json.put("textLines",         textLines);
            json.put("nid",               nid);
            json.put("tag",               tag);
        } catch (Exception e) {
            Log.e("ERR",e.toString());
        }
        return json.toString();
    }
}
