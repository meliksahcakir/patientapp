package com.physhome.physhome;

import android.content.SharedPreferences;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

/**
 * Created by Meliksah on 7/1/2017.
 */

public class BaseActivity extends AppCompatActivity implements ConnectivityReceiver.ConnectivityReceiverListener{
    SharedPreferences sharedpreferences;
    SharedPreferences.Editor editor;
    String userID;
    UserModel userModel;
    ArrayList<PhyshomePatientDeviceModel> userDevices;
    PhyshomePatientDeviceModel limb_device, chest_device;
    Gson gson;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        gson = new Gson();
        sharedpreferences =  PreferenceManager.getDefaultSharedPreferences(this);
        editor = sharedpreferences.edit();
        userID = sharedpreferences.getString("userID", "");
        String user_json = getFromPref("user","");
        if(!user_json.equals("")){
            Type type = new TypeToken<UserModel>() {}.getType();
            userModel = gson.fromJson(user_json, type);
        }else{
            userModel = new UserModel();
        }
        String device_json = getFromPref(Constants.USER_DEVICES,"");
        if(!device_json.equals("")){
            Type type = new TypeToken<ArrayList<PhyshomePatientDeviceModel>>() {}.getType();
            userDevices = gson.fromJson(device_json, type);
            for(PhyshomePatientDeviceModel model:userDevices){
                if(model.getChe_limb().equals(Constants.LIMB)){
                    limb_device = model;
                }else if(model.getChe_limb().equals(Constants.CHEST)){
                    chest_device = model;
                }
            }
        }else{
            userDevices = new ArrayList<>();
        }
        Log.d("BtDebug","Devices"+userDevices.toString());
    }

    @Override
    protected void onResume() {
        super.onResume();
        // register connection status listener
        MyApplication.getInstance().setConnectivityListener(this);
    }

    // Method to manually check connection status
    public boolean checkConnection() {
        return ConnectivityReceiver.isConnected();
    }
    @Override
    public void onNetworkConnectionChanged(boolean isConnected) {
        if (!isConnected) {
            Toast.makeText(this, R.string.no_internet_connection, Toast.LENGTH_SHORT).show();
        }
    }

    public static void buttonEffect(View button){
        button.setOnTouchListener(new View.OnTouchListener() {

            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN: {
                        v.getBackground().setColorFilter(0xe0f47521, PorterDuff.Mode.SRC_ATOP);
                        v.invalidate();
                        break;
                    }
                    case MotionEvent.ACTION_UP: {
                        v.getBackground().clearColorFilter();
                        v.invalidate();
                        break;
                    }
                }
                return false;
            }
        });
    }
    void saveToPref(String title, String json){
        editor.putString(title+userID,json);
        editor.apply();
    }
    String getFromPref(String title){
        return sharedpreferences.getString(title+userID,"");
    }
    String getFromPref(String title, String defaultValue){
        return sharedpreferences.getString(title+userID,defaultValue);
    }
}
