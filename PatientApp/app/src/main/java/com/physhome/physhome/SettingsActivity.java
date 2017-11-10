package com.physhome.physhome;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class SettingsActivity extends BaseActivity {
    TextView alarm_tv, bt_tv, profile_tv, website_tv, logout_tv, faq_tv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        alarm_tv = (TextView) findViewById(R.id.settings_alarm_tv);
        bt_tv = (TextView) findViewById(R.id.settings_bt_tv);
        profile_tv = (TextView) findViewById(R.id.settings_profile_tv);
        website_tv = (TextView) findViewById(R.id.settings_website_tv);
        logout_tv = (TextView) findViewById(R.id.settings_logout_tv);
        faq_tv = (TextView) findViewById(R.id.settings_faq_tv);

        alarm_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent;
                intent = new Intent(SettingsActivity.this, DrugAlarmActivity.class);
                startActivity(intent);
            }
        });
        bt_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent;
                intent = new Intent(SettingsActivity.this, BluetoothSettingsActivity.class);
                intent.putExtra(Constants.BT_RETURN_ACTIVITY,Constants.BT_SETTINGS_ACTIVITY);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });
        profile_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent;
                intent = new Intent(SettingsActivity.this, ProfileEditActivity.class);
                startActivity(intent);
            }
        });
        website_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.physhome.com"));
                startActivity(intent);
            }
        });
        logout_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logOut();
            }
        });
        faq_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent;
                intent = new Intent(SettingsActivity.this, FAQActivity.class);
                startActivity(intent);
            }
        });
    }
    void logOut() {
        new android.support.v7.app.AlertDialog.Builder(this)
                .setMessage(R.string.are_you_sure_you_want_to_logout)
                .setCancelable(true)
                .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        editor.putString("userID", "");
                        editor.putString("bluetoothMAC1", "");
                        editor.putString("bluetoothMAC2", "");
                        editor.apply();
                        Intent intent;
                        intent = new Intent(SettingsActivity.this, LoginActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                        finish();
                    }
                })
                .setNegativeButton(R.string.no, null)
                .show();
    }
}
