package com.hs.nlogger;

import android.service.notification.StatusBarNotification;
import android.util.Log;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.Arrays;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class JsonSender {
    public static final MediaType JSON = MediaType.get("application/json; charset=utf-8");
//    public static final String BASE_URL = "http://10.0.2.2:8000";
    public static final String BASE_URL = "https://nlogger-site.onrender.com";
    public static Executor executor = Executors.newSingleThreadExecutor();
    void postError(Exception e) {
        JSONObject jo = new JSONObject();
        try {
            jo.put("error", Arrays.toString(e.getStackTrace()));
        } catch (JSONException ex) {
            Log.e("ERROR","UNABLE TO PUT ERROR");
        }
        post("/error",jo.toString());
    }
    void postMessage(String msg) {
        JSONObject jo = new JSONObject();
        try {
            jo.put("message", msg);
        } catch (JSONException ex) {
            Log.e("ERROR","UNABLE TO PUT MESSAGE");
        }
        post("/msg",jo.toString());
    }
    void postNotif(StatusBarNotification sbn) {
        String json = new NObject(sbn).getJsonString();
        String route = "/insert";
        post(route,json);
    }
     void post(String route, String json)  {
        executor.execute(() -> {
            OkHttpClient client = new OkHttpClient();
            RequestBody body = RequestBody.create(json,JSON);
            Request req = new Request.Builder().url(BASE_URL + route).post(body).build();
            Log.d("CUSTOM","Sending req at " + BASE_URL + route);
            try  (Response res = client.newCall(req).execute()){
                Log.d("CUSTOM", "post: There are no errors among us _> " + res);
            } catch (Exception e) {
                Log.d("CUSTOM", "post: There is an error among us");
                Log.e("CUSTOM",e.toString());
            }
        });
    }
}
