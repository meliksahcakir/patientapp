package com.physhome.physhome;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.WakefulBroadcastReceiver;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

/**
 * Created by meliksah on 6/5/2017.
 */

public class AlarmReceiver extends WakefulBroadcastReceiver {
    // The app's AlarmManager, which provides access to the system alarm services.
    private AlarmManager alarmMgr;
    // The pending intent that is triggered when the alarm fires.
    private PendingIntent pendingIntent;
    String TAG = "AlarmDebug";
    SharedPreferences sharedPreferences;
    HashMap<String,ExerciseInfoModel> exerciseInfoList;
    HashMap<String,ExerciseModel> exerciseList;
    ArrayList<String> dates;
    Gson gson;
    int delay = 0;
    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d(TAG,"Alarm Received");
        gson = new Gson();
        sharedPreferences =  PreferenceManager.getDefaultSharedPreferences(context);
        String userID = sharedPreferences.getString("userID", "");
        checkExercises(context, userID);
    }
    void sortDates(ArrayList<String> list){
        Collections.sort(list, new Comparator<String>(){
            public int compare(String obj1, String obj2) {
                return obj2.compareToIgnoreCase(obj1);
            }
        });
    }

    void checkExercises(final Context context, String userID){
        exerciseList = new HashMap<>();
        exerciseInfoList = new HashMap<>();
        dates = new ArrayList<>();
        Log.d("VOLLEYTAG","check_exercise_start");
        RequestQueue mQueue = Volley.newRequestQueue(context.getApplicationContext());
        JsonArrayRequest jsArrayRequest = new JsonArrayRequest
                (Request.Method.GET,Constants.URL_USERS+"/"+userID+"/exercises", null, new Response.Listener<JSONArray>() {

                    @Override
                    public void onResponse(JSONArray response) {
                        Log.d("VOLLEYTAG","check_exercise_finish");
                        Log.d("VOLLEYTAG",response.toString());
                        try {
                            int flag=0;
                            for(int i=0;i<response.length();i++){
                                JSONObject object = response.getJSONObject(i);
                                ExerciseModel model = gson.fromJson(object.toString(),ExerciseModel.class);

                                if(model.getExercise_id()==null||model.getExercise_id().equals("")){
                                    if(flag==0){
                                        model.setExercise_id("599c12884f2a8949901abf63");
                                        flag=1;
                                    }else{
                                        model.setExercise_id("599c12884f2a8949901abf64");
                                        flag=0;
                                    }
                                }
                                String id = model.getExercise_id();
                                exerciseList.put(id,model);
                                String createdAt = model.getCreated_at();
                                createdAt = createdAt.substring(0,createdAt.indexOf('T'));
                                if(!dates.contains(createdAt)){
                                    dates.add(createdAt);
                                }
                            }
                            if(!dates.isEmpty()){
                                sortDates(dates);
                                for(String key:exerciseList.keySet()){
                                    ExerciseModel model = exerciseList.get(key);
                                    String model_date = model.getCreated_at();
                                    if(model_date.contains(dates.get(0))){
                                        delay++;
                                    }
                                }

                                for(String key:exerciseList.keySet()){
                                    ExerciseModel model = exerciseList.get(key);
                                    String model_date = model.getCreated_at();
                                    String id = model.getExercise_id();
                                    if(model_date.contains(dates.get(0))){
                                        getExerciseInfo(context,id);
                                    }
                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("VOLLEYTAG", error.getMessage(), error);
                    }
                }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("Content-Type", "application/json");
                return params;
            }
        };
        mQueue.add(jsArrayRequest);
    }

    void getExerciseInfo(final Context context, final String exercise_id){
        Log.d("VOLLEYTAG","info_start");
        RequestQueue req = Volley.newRequestQueue(context.getApplicationContext());
        JsonObjectRequest jsObjectRequest = new JsonObjectRequest
                (Request.Method.GET,Constants.URL_EXERCISES+"/"+exercise_id, null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("VOLLEYTAG","info_finish");
                        Log.d("VOLLEYTAG","info:"+response.toString());
                        ExerciseInfoModel model = gson.fromJson(response.toString(),ExerciseInfoModel.class);
                        exerciseInfoList.put(exercise_id,model);
                        delay--;
                        if(delay==0){
                            int i=0;
                            for(String key: exerciseInfoList.keySet()){
                                ExerciseModel exerciseModel = exerciseList.get(key);
                                int total_day = exerciseModel.getTotal_day();
                                int current_day = exerciseModel.getCurrent_day();
                                if(current_day<total_day){
                                    int remain = total_day-current_day;
                                    String name = exerciseInfoList.get(key).getName();
                                    String message;
                                    String language = Locale.getDefault().getLanguage();
                                    if(language.equals("tr")){
                                        message = context.getString(R.string.to_finish_the_exercise)+" "+context.getString(R.string.only)+" "+remain+" "+context.getString(R.string.day)+" "+context.getString(R.string.left)+". "+context.getString(R.string.lets_start);
                                    }else{
                                        if(remain==1){
                                            message = context.getString(R.string.only)+" 1 "+context.getString(R.string.day)+" "+context.getString(R.string.left)+" "+context.getString(R.string.to_finish_the_exercise)+". "+context.getString(R.string.lets_start);
                                        }else{
                                            message = context.getString(R.string.only)+" "+ remain +" "+context.getString(R.string.days)+" "+context.getString(R.string.left)+" "+context.getString(R.string.to_finish_the_exercise)+". "+context.getString(R.string.lets_start);
                                        }
                                    }
                                    sendNotification(context,i,name,message);
                                    i++;
                                }
                            }
                        }
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("VOLLEYTAG", error.getMessage(), error);
                    }
                }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("Content-Type", "application/json");
                return params;
            }
        };
        req.add(jsObjectRequest);
    }

    public void sendNotification(Context context, int id, String name, String message){
        Log.d(TAG,"notification sent");
        NotificationManager notificationManager = (NotificationManager)
                context.getSystemService(Context.NOTIFICATION_SERVICE);
        Intent notificationIntent = new Intent(context, MainActivity.class);
        PendingIntent contentIntent = PendingIntent.getActivity(context, id, notificationIntent, 0);
        Uri soundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);
        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(context)
                        .setSmallIcon(R.drawable.physhome1)
                        .setColor(Color.rgb(84,182,229))
                        .setSound(soundUri)
                        .setContentTitle(context.getString(R.string.app_name))
                        .setStyle(new NotificationCompat.BigTextStyle()
                                .bigText(name+"\n"+message))
                        .setPriority(NotificationCompat.PRIORITY_HIGH)
                        //.setSubText(message)
                        .setContentIntent(contentIntent)
                        .setContentText(name+"\n"+message);

        Notification notification = mBuilder.build();
        notification.flags |= Notification.FLAG_AUTO_CANCEL | Notification.FLAG_SHOW_LIGHTS;
        notification.defaults |= Notification.DEFAULT_SOUND | Notification.DEFAULT_VIBRATE;
        notification.ledARGB = 0xFFFFA500;
        notification.ledOnMS = 800;
        notification.ledOffMS = 1000;
        notificationManager.notify(id, notification);
        Log.d("notification","Notifications sent.");
    }

    public void scheduleAlarm(Context context, int id, String name, String message, int day, int hour, int minute){
        Log.d(TAG,"alarm scheduled");
        alarmMgr = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, AlarmReceiver.class);
        intent.putExtra("subject","drug");
        intent.putExtra("name",name);
        intent.putExtra("message",message);
        pendingIntent = PendingIntent.getBroadcast(context,id,intent,0);
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.set(Calendar.DAY_OF_MONTH,day);
        calendar.set(Calendar.HOUR_OF_DAY, hour);
        calendar.set(Calendar.MINUTE, minute);
        alarmMgr.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY*7, pendingIntent);
    }

    public void setExerciseAlarm(Context context) {
        Log.d(TAG,"alarm set");
        alarmMgr = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, AlarmReceiver.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        pendingIntent = PendingIntent.getBroadcast(context,0,intent,0);
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.set(Calendar.HOUR_OF_DAY, 10);
        calendar.set(Calendar.MINUTE, 0);
        alarmMgr.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pendingIntent);


        //alarmIntent = PendingIntent.getBroadcast(context, 0, intent, 0);
        /*
         * If you don't have precise time requirements, use an inexact repeating alarm
         * the minimize the drain on the device battery.
         *
         * The call below specifies the alarm type, the trigger time, the interval at
         * which the alarm is fired, and the alarm's associated PendingIntent.
         * It uses the alarm type RTC_WAKEUP ("Real Time Clock" wake up), which wakes up
         * the device and triggers the alarm according to the time of the device's clock.
         *
         * Alternatively, you can use the alarm type ELAPSED_REALTIME_WAKEUP to trigger
         * an alarm based on how much time has elapsed since the device was booted. This
         * is the preferred choice if your alarm is based on elapsed time--for example, if
         * you simply want your alarm to fire every 60 minutes. You only need to use
         * RTC_WAKEUP if you want your alarm to fire at a particular date/time. Remember
         * that clock-based time may not translate well to other locales, and that your
         * app's behavior could be affected by the user changing the device's time setting.
         *
         * Here are some examples of ELAPSED_REALTIME_WAKEUP:
         *
         * // Wake up the device to fire a one-time alarm in one minute.
         * alarmMgr.set(AlarmManager.ELAPSED_REALTIME_WAKEUP,
         *         SystemClock.elapsedRealtime() +
         *         60*1000, alarmIntent);
         *
         * // Wake up the device to fire the alarm in 30 minutes, and every 30 minutes
         * // after that.
         * alarmMgr.setInexactRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP,
         *         AlarmManager.INTERVAL_HALF_HOUR,
         *         AlarmManager.INTERVAL_HALF_HOUR, alarmIntent);
         */

        // Set the alarm to fire at approximately 8:30 a.m., according to the device's
        // clock, and to repeat once a day.

        // Enable {@code SampleBootReceiver} to automatically restart the alarm when the
        // device is rebooted.
        ComponentName receiver = new ComponentName(context, BootReceiver.class);
        PackageManager pm = context.getPackageManager();

        pm.setComponentEnabledSetting(receiver,
                PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
                PackageManager.DONT_KILL_APP);
    }
    public void cancelAlarm(Context context, int id) {
        Log.d(TAG,"alarm cancelled");
        alarmMgr = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, AlarmReceiver.class);
        pendingIntent = PendingIntent.getBroadcast(context,id,intent,0);
        alarmMgr.cancel(pendingIntent);
        // Disable {@code SampleBootReceiver} so that it doesn't automatically restart the
        // alarm when the device is rebooted.
        ComponentName receiver = new ComponentName(context, BootReceiver.class);
        PackageManager pm = context.getPackageManager();

        pm.setComponentEnabledSetting(receiver,
                PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
                PackageManager.DONT_KILL_APP);
    }


}
