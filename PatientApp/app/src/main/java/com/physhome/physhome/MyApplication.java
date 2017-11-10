package com.physhome.physhome;

import android.app.Application;

/**
 * Created by Meliksah on 7/1/2017.
 */

public class MyApplication extends Application {
    private static MyApplication mInstance;
    @Override
    public void onCreate() {
        super.onCreate();

        mInstance = this;
    }
    public static synchronized MyApplication getInstance() {
        return mInstance;
    }
    public void setConnectivityListener(ConnectivityReceiver.ConnectivityReceiverListener listener) {
        ConnectivityReceiver.connectivityReceiverListener = listener;
    }
}
