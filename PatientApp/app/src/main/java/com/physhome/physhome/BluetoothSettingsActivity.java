package com.physhome.physhome;

import android.Manifest;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Set;

import de.hdodenhof.circleimageview.CircleImageView;

public class BluetoothSettingsActivity extends BaseActivity {
    final String TAG = "BTDiscoveryDebug";
    Button scan_button;
    RadarView radarView;
    TextView scanning_tv, bt_limb_tv, bt_chest_tv, pin_tv;
    CircleImageView bt_limb_iv, bt_chest_iv;
    boolean scan=false;
    private BroadcastReceiver mPairingRequestReceiver;
    private BluetoothAdapter BA;
    private Set<BluetoothDevice> pairedDevices;
    private ArrayList<BluetoothDevice> unpairedDevices;
    boolean bt_open=false;
    boolean limb=false, chest=false;
    final int PERMISSIONS_CODE = 1;
    boolean discovery_done = false;
    int activity_id = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bluetooth_settings);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        scan_button = (Button) findViewById(R.id.scan_button);
        radarView = (RadarView) findViewById(R.id.radar_view);
        scanning_tv = (TextView) findViewById(R.id.scanning_tv);
        bt_limb_tv = (TextView) findViewById(R.id.bt_limb_tv);
        bt_chest_tv = (TextView) findViewById(R.id.bt_chest_tv);
        pin_tv = (TextView) findViewById(R.id.pin_tv);
        bt_limb_iv = (CircleImageView) findViewById(R.id.bt_limb_iv);
        bt_chest_iv = (CircleImageView) findViewById(R.id.bt_chest_iv);

        Intent intent = getIntent();
        if (null != intent) {
            Bundle bundle = intent.getExtras();
            activity_id = bundle.getInt(Constants.BT_RETURN_ACTIVITY,0);
        }


        scan_button.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onClick(View v) {
                if (ContextCompat.checkSelfPermission(BluetoothSettingsActivity.this,
                        Manifest.permission.ACCESS_COARSE_LOCATION)
                        != PackageManager.PERMISSION_GRANTED  ) {
                    ActivityCompat.requestPermissions(BluetoothSettingsActivity.this,
                            new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},
                            PERMISSIONS_CODE);
                }else{
                    IntentFilter filter = new IntentFilter();
                    filter.addAction(BluetoothDevice.ACTION_FOUND);
                    filter.addAction(BluetoothAdapter.ACTION_DISCOVERY_STARTED);
                    filter.addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
                    registerReceiver(mDiscoveryReceiver, filter);
                    BA.startDiscovery();
                }

            }
        });
        mPairingRequestReceiver = new BroadcastReceiver() {
            public void onReceive(Context context, Intent intent) {
                String action = intent.getAction();
                if (action.equals(BluetoothDevice.ACTION_PAIRING_REQUEST)) {
                    try {
                        BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                        int pin=intent.getIntExtra("android.bluetooth.device.extra.PAIRING_KEY", 1234);
                        //the pin in case you need to accept for an specific pin
                        Log.d(TAG, "Start Auto Pairing. PIN = " + intent.getIntExtra("android.bluetooth.device.extra.PAIRING_KEY",1234));
                        byte[] pinBytes;
                        pinBytes = (""+pin).getBytes("UTF-8");
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                            device.setPin(pinBytes);
                            //setPairing confirmation if needed
                            device.setPairingConfirmation(true);
                        }
                    } catch (Exception e) {

                        Log.e(TAG, "Error occurs when trying to auto pair "+e.getMessage());
                        e.printStackTrace();
                    }
                }
            }
        };
        BA = BluetoothAdapter.getDefaultAdapter();
        checkBTState();
    }
    private void pairDevice(BluetoothDevice device) {
        try {
            Log.d(TAG, "Start Pairing... with: " + device.getName());
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                device.createBond();
            }else {
                Toast.makeText(this, R.string.higher_android_version_warning, Toast.LENGTH_SHORT).show();
            }
            Log.d(TAG, "Pairing finished.");
        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
    }
    @Override
    public void onPause() {
        if (BA != null) {
            if (BA.isDiscovering()) {
                BA.cancelDiscovery();
            }
        }
        super.onPause();
    }
    void checkBTState(){
        if(BA==null) {
            Toast.makeText(BluetoothSettingsActivity.this, R.string.bluetooth_not_support, Toast.LENGTH_SHORT).show();
            bt_open=false;
        }else{
            if (BA.isEnabled()) {
                Log.d(TAG, getString(R.string.bluetooth_on));
                bt_open=true;
                pairedDevices = BA.getBondedDevices();

                if (pairedDevices.size() > 0){
                    for(BluetoothDevice bt : pairedDevices){
                        setBTVisual(bt,false);
                    }
                    if(!limb){
                        bt_limb_tv.setText(R.string.no_device);
                        bt_limb_iv.setBorderColor(Color.RED);
                        bt_limb_iv.setImageResource(R.drawable.ic_bt_disabled);
                    }
                    if(!chest){
                        bt_chest_tv.setText(R.string.no_device);
                        bt_chest_iv.setBorderColor(Color.RED);
                        bt_chest_iv.setImageResource(R.drawable.ic_bt_disabled);
                    }
                }
            } else {
                //Prompt user to turn on Bluetooth
                bt_open=false;
                Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                startActivityForResult(enableBtIntent, 1);
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == 1){
            if (resultCode == RESULT_OK) {
                checkBTState();
            } else {
                finish();
            }
        }

    }
    private final BroadcastReceiver mDiscoveryReceiver = new BroadcastReceiver() {
        @RequiresApi(api = Build.VERSION_CODES.KITKAT)
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();

            if (BluetoothAdapter.ACTION_DISCOVERY_STARTED.equals(action)) {
                Log.d(TAG,"Discovery started");
                unpairedDevices = new ArrayList<>();
                scan_button.setEnabled(false);
                discovery_done = false;
                if (radarView != null){
                    radarView.startAnimation();
                    scanning_tv.setVisibility(View.VISIBLE);
                }

            } else if (BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(action)) {
                Log.d(TAG,"Discovery finished");
                if(!discovery_done){
                    scan_button.setEnabled(true);
                    IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_BOND_STATE_CHANGED);
                    registerReceiver(mPairReceiver, filter);
                    unregisterReceiver(mDiscoveryReceiver);
                    if (radarView != null){
                        radarView.stopAnimation();
                        scanning_tv.setVisibility(View.GONE);
                    }
                    if(!unpairedDevices.isEmpty()){
                        //IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_PAIRING_REQUEST);
                        //registerReceiver(mPairingRequestReceiver, filter);
                        pairDevice(unpairedDevices.get(0));
                    }

                    discovery_done = true;
                }

            } else if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                Log.d(TAG,"Found!");
                BluetoothDevice bt = (BluetoothDevice) intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                Log.d(TAG,bt.getName()+" "+bt.getAddress());
                if(bt.getAddress().equals(limb_device.getMac_address())||bt.getAddress().equals(chest_device.getMac_address())){
                    unpairedDevices.add(bt);
                }
            }
        }
    };
    private final BroadcastReceiver mPairReceiver = new BroadcastReceiver() {
        @RequiresApi(api = Build.VERSION_CODES.KITKAT)
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();

            if (BluetoothDevice.ACTION_BOND_STATE_CHANGED.equals(action)) {
                final int state =  intent.getIntExtra(BluetoothDevice.EXTRA_BOND_STATE, BluetoothDevice.ERROR);
                final int prevState = intent.getIntExtra(BluetoothDevice.EXTRA_PREVIOUS_BOND_STATE, BluetoothDevice.ERROR);
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                if (state == BluetoothDevice.BOND_BONDED && prevState == BluetoothDevice.BOND_BONDING) {
                    setBTVisual(device,true);
                    unpairedDevices.remove(0);
                    //unregisterReceiver(mPairingRequestReceiver);
                    if(!unpairedDevices.isEmpty()){
                        //IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_PAIRING_REQUEST);
                        //registerReceiver(mPairingRequestReceiver, filter);
                        pairDevice(unpairedDevices.get(0));
                    }else{
                        unregisterReceiver(mPairReceiver);
                    }
                }
            }
        }
    };
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case PERMISSIONS_CODE: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    IntentFilter filter = new IntentFilter();
                    filter.addAction(BluetoothDevice.ACTION_FOUND);
                    filter.addAction(BluetoothAdapter.ACTION_DISCOVERY_STARTED);
                    filter.addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
                    registerReceiver(mDiscoveryReceiver, filter);
                    BA.startDiscovery();
                } else {
                    Toast.makeText(this, R.string.bt_request_not_accepted, Toast.LENGTH_LONG).show();
                }
                break;
            }
        }
    }

    void setBTVisual(BluetoothDevice device, boolean pairing){
        if(device.getAddress().equals(limb_device.getMac_address())){
            Log.d("BtDebug","LIMB OK!");
            bt_limb_iv.setImageResource(R.drawable.ic_bt_enabled);
            bt_limb_iv.setBorderColor(Color.GREEN);
            bt_limb_tv.setText(device.getName());
            limb = true;

        }
        else if(device.getAddress().equals(chest_device.getMac_address())){
            Log.d("BtDebug","CHEST OK!");
            bt_chest_iv.setImageResource(R.drawable.ic_bt_enabled);
            bt_chest_iv.setBorderColor(Color.GREEN);
            bt_chest_tv.setText(device.getName());
            chest = true;
        }
        if(limb&chest){
            showAlertDialog(activity_id);
        }
    }

    void showAlertDialog(final int activity_id){
        String limb_str=limb_device.toString();
        String chest_str=chest_device.toString();
        String message = getString(R.string.both_devices_are_paired)+"\n";
        message += getString(R.string.limb_device)+": "+limb_str;
        message += getString(R.string.chest_device)+": "+chest_str;
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(message)
                .setCancelable(false)
                .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                        goToOtherActivity(activity_id);
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
    }
    void goToOtherActivity(int id){
        Class<? extends Activity> ActivityToOpen = null;
        switch (id){
            case Constants.BT_MAIN_ACTIVITY:
                ActivityToOpen = MainActivity.class;
                break;
            case Constants.BT_SETTINGS_ACTIVITY:
                ActivityToOpen = SettingsActivity.class;
                break;
            case Constants.BT_SELECT_EXERCISE_ACTIVITY:
                ActivityToOpen = SelectExerciseActivity.class;
                break;
        }
        Intent intent;
        intent = new Intent(BluetoothSettingsActivity.this,ActivityToOpen);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
    }
}
