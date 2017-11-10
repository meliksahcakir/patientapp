package com.physhome.physhome;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends BaseActivity {
    Toolbar toolbar;
    ImageView settings_button;
    TextView settings_tv;
    TextView exercise_button, achievements_button, notifications_button, treatment_button, faq_button, analytics_button;
    TextView age_tv, height_tv, blood_tv, name_tv;
    CircleImageView profile_iv;
    String file_path = "";
    ImageView edit_button;
    int unsaved_length=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.d("MainDebug","id:"+userID);
        toolbar = (Toolbar) findViewById(R.id.main_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        settings_button = (ImageView) toolbar.findViewById(R.id.main_settings_iv);
        settings_tv = (TextView) toolbar.findViewById(R.id.main_settings_tv);
        exercise_button = (TextView) findViewById(R.id.main_exercise_button);
        achievements_button = (TextView) findViewById(R.id.main_achievements_button);
        notifications_button = (TextView) findViewById(R.id.main_notifications_button);
        treatment_button = (TextView) findViewById(R.id.main_treatment_button);
        faq_button = (TextView) findViewById(R.id.main_faq_button);
        analytics_button = (TextView) findViewById(R.id.main_analytics_button);
        age_tv = (TextView) findViewById(R.id.main_age_tv);
        height_tv = (TextView) findViewById(R.id.main_height_tv);
        blood_tv = (TextView) findViewById(R.id.main_blood_tv);
        name_tv = (TextView) findViewById(R.id.main_name_tv);
        profile_iv = (CircleImageView) findViewById(R.id.main_profile_iv);
        edit_button = (ImageView) findViewById(R.id.main_edit_button);
        buttonEffect(exercise_button);
        buttonEffect(treatment_button);
        buttonEffect(achievements_button);
        buttonEffect(notifications_button);
        buttonEffect(faq_button);
        buttonEffect(analytics_button);
        settings_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent;
                intent = new Intent(MainActivity.this, SettingsActivity.class);
                startActivity(intent);
            }
        });
        settings_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent;
                intent = new Intent(MainActivity.this, SettingsActivity.class);
                startActivity(intent);
            }
        });
        exercise_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent;
                intent = new Intent(MainActivity.this, SelectExerciseActivity.class);
                startActivity(intent);
            }
        });
        achievements_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent;
                intent = new Intent(MainActivity.this, ProgressActivity.class);
                startActivity(intent);
            }
        });
        notifications_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent;
                intent = new Intent(MainActivity.this, NotificationsActivity.class);
                startActivity(intent);
            }
        });
        treatment_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent;
                intent = new Intent(MainActivity.this, TreatmentActivity.class);
                startActivity(intent);
            }
        });
        faq_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent;
                intent = new Intent(MainActivity.this, FAQActivity.class);
                startActivity(intent);
            }
        });
        analytics_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        edit_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent;
                intent = new Intent(MainActivity.this, ProfileEditActivity.class);
                startActivity(intent);
            }
        });
        profile_iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pickImage();
            }
        });

        if(checkConnection()){
            getPatientDeviceInfo(this,userModel.getDoctor_id(),userID);
            String str_jsonarray = getFromPref(Constants.PREF_SUMMARY);
            String str_jsonarray_id = getFromPref(Constants.PREF_SUMMARY_ID);
            try {
                JSONArray jsonArray = new JSONArray(str_jsonarray);
                JSONArray jsonArray_id = new JSONArray(str_jsonarray_id);
                unsaved_length = jsonArray.length();
                for(int i=0;i<jsonArray.length();i++){
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    String id = jsonArray_id.getString(i);
                    sendUnsavedSummary(jsonObject,id);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        getStoredValues();
    }

    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(this)
                .setMessage(R.string.are_you_sure_you_want_to_exit)
                .setCancelable(false)
                .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        finish();
                    }
                })
                .setNegativeButton(R.string.no, null)
                .show();
    }
    void getStoredValues(){
        String name = userModel.getName_surname();
        int age = userModel.getAge();
        String blood = userModel.getBlood_type();
        int height = userModel.getHeight();
        file_path = sharedpreferences.getString("profile_photo"+userID,"");
        if(!name.equals("")){
            name_tv.setText(name);
        }
        if(!blood.equals("")){
            blood_tv.setText(blood);
        }
        age_tv.setText(age+" "+getString(R.string.years));
        height_tv.setText(height+" "+getString(R.string.cm));
        if(!file_path.equals("")){
            loadImage(file_path);
        }else{
            profile_iv.setImageResource(R.drawable.physhome_round_bg);
        }
    }
    void loadImage(String path){
        Log.d("MainDebug",path);
        Glide
                .with(MainActivity.this)
                .load(Uri.parse(path))
                .dontAnimate()
                .into(profile_iv);
    }
    void pickImage(){
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION | Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
        startActivityForResult(intent,1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d("MainDebug",""+requestCode+" "+resultCode+" "+ RESULT_OK);
        switch (requestCode){
            case 1:
                if(resultCode == RESULT_OK){
                    Uri uri = data.getData();
                    file_path = uri.toString();
                    editor.putString("profile_photo"+userID,file_path);
                    editor.commit();
                    loadImage(file_path);
                }
                break;
        }
    }


    void sendUnsavedSummary(JSONObject jsonObject, String id){
        RequestQueue mQueue = Volley.newRequestQueue(getApplicationContext());

        JsonObjectRequest jsObjRequest = new JsonObjectRequest
                (Request.Method.POST,Constants.URL_USERS+"/"+userID+"/exercises/"+id, jsonObject, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("VOLLEYTAG","session_finish");
                        Log.d("VOLLEYTAG",response.toString());
                        unsaved_length--;
                        if(unsaved_length==0){
                            saveToPref(Constants.PREF_SUMMARY,"");
                            saveToPref(Constants.PREF_SUMMARY_ID,"");
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
        mQueue.add(jsObjRequest);
    }
    public void getPatientDeviceInfo(Context context, String doctor_id, String user_id){
        if(!userDevices.isEmpty()){
            userDevices = new ArrayList<>();
        }
        Log.d("VOLLEYTAG","getting device info");
        RequestQueue mQueue = Volley.newRequestQueue(context.getApplicationContext());
        JsonArrayRequest jsArrayRequest = new JsonArrayRequest
                (Request.Method.GET, Constants.URL_DEVICES+"/"+doctor_id+"/"+user_id, null, new Response.Listener<JSONArray>() {

                    @Override
                    public void onResponse(JSONArray response) {
                        Log.d("VOLLEYTAG",response.toString());
                        try {
                            for(int i=0;i<response.length();i++){
                                JSONObject object = response.getJSONObject(i);
                                PhyshomePatientDeviceModel model = gson.fromJson(object.toString(),PhyshomePatientDeviceModel.class);
                                userDevices.add(model);
                            }
                            saveToPref(Constants.USER_DEVICES,gson.toJson(userDevices));
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
}