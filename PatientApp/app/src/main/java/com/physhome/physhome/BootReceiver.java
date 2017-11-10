package com.physhome.physhome;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * Created by meliksah on 6/5/2017.
 */

public class BootReceiver extends BroadcastReceiver {
    AlarmReceiver alarm = new AlarmReceiver();
    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals("android.intent.action.BOOT_COMPLETED"))
        {
            alarm.setExerciseAlarm(context);
            //Intent service_intent = new Intent(context,FallService.class);
            //context.startService(service_intent);
        }
    }

}
