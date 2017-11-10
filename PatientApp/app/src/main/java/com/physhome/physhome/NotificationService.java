package com.physhome.physhome;

/**
 * Created by meliksah on 6/5/2017.
 */
import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;

import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

/**
     * Created by andrew on 1/21/14.
     */
    public class NotificationService extends IntentService {
        private NotificationManager notificationManager;
        private PendingIntent pendingIntent;
        public NotificationService() {
            super("NotificationService");
        }

        @Override
        protected void onHandleIntent(Intent intent) {
            Bundle bundle = intent.getExtras();
            String message = bundle.getString("message","");
            String name = bundle.getString("name","");
            int id = bundle.getInt("id",1);
            int hour = bundle.getInt("hour",12);
            String date = bundle.getString("date","");
            sendNotification(id,message, name, hour, date);
            AlarmReceiver.completeWakefulIntent(intent);
        }
    private void sendNotification(int id, String msg, String name, int hour, String date) {
        notificationManager = (NotificationManager)
                this.getSystemService(Context.NOTIFICATION_SERVICE);
        Intent notificationIntent = new Intent(this, NotificationsActivity.class);
        notificationIntent.putExtra("id",id);
        notificationIntent.putExtra("name",name);
        notificationIntent.putExtra("hour",hour);
        notificationIntent.setAction(Constants.NOTIFICATION_ACTION);
        PendingIntent contentIntent = PendingIntent.getActivity(this, id, notificationIntent, 0);
        Intent yesIntent = new Intent(this, NotificationsActivity.class);
        yesIntent.putExtra("id",id);
        yesIntent.putExtra("name",name);
        yesIntent.putExtra("hour",hour);
        yesIntent.putExtra("date",date);
        yesIntent.setAction(Constants.YES_ACTION);
        yesIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent yesPendingIntent = PendingIntent.getActivity(this, id, yesIntent, 0);
        Intent noIntent = new Intent(this, NotificationsActivity.class);
        noIntent.putExtra("id",id);
        noIntent.putExtra("name",name);
        noIntent.putExtra("hour",hour);
        noIntent.putExtra("date",date);
        noIntent.setAction(Constants.NO_ACTION);
        noIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent noPendingIntent = PendingIntent.getActivity(this, id, noIntent, 0);

        Uri soundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);
        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(this)
                        .setSmallIcon(R.drawable.physhome1)
                        .setSound(soundUri)
                        .setContentTitle(getString(R.string.app_name))
                        .setStyle(new NotificationCompat.BigTextStyle()
                                .bigText(msg))
                        .setPriority(NotificationCompat.PRIORITY_HIGH)
                        .setOngoing(true)
                        .setSubText(hour+":00")
                        .setContentText(msg);
        mBuilder.setContentIntent(null);
        NotificationCompat.Action yesAction = new NotificationCompat.Action(R.drawable.ic_tick,"YES",yesPendingIntent);
        NotificationCompat.Action noAction = new NotificationCompat.Action(R.drawable.ic_negative,"NO",noPendingIntent);
        mBuilder.addAction(yesAction);
        mBuilder.addAction(noAction);
        Notification notification = mBuilder.build();
        notification.flags |= Notification.FLAG_AUTO_CANCEL | Notification.FLAG_SHOW_LIGHTS;
        notification.defaults |= Notification.DEFAULT_SOUND | Notification.DEFAULT_VIBRATE;
        notification.ledARGB = 0xFFFFA500;
        notification.ledOnMS = 800;
        notification.ledOffMS = 1000;
        notificationManager.notify(id, notification);
        Log.d("notification","Notifications sent.");
    }
}
