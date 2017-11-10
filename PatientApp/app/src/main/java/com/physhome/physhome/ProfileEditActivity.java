package com.physhome.physhome;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Meliksah on 7/15/2017.
 */

public class ProfileEditActivity extends BaseActivity {
    Button save_button;
    EditText name_et, age_et, height_et, blood_et;
    ImageView profile_iv;
    String file_path = "";
    String stored_name, stored_blood;
    int stored_age, stored_height;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_edit);
        save_button = (Button) findViewById(R.id.profile_save_button);
        name_et = (EditText) findViewById(R.id.profile_name_et);
        age_et = (EditText) findViewById(R.id.profile_age_et);
        height_et = (EditText) findViewById(R.id.profile_height_et);
        blood_et = (EditText) findViewById(R.id.profile_blood_et);
        profile_iv = (ImageView) findViewById(R.id.profile_edit_profile_iv);
        buttonEffect(save_button);
        save_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveChanges();
            }
        });
        profile_iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pickImage();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        getStoredValues();
    }

    void getStoredValues(){
        stored_name = userModel.getName_surname();
        stored_age = userModel.getAge();
        stored_blood = userModel.getBlood_type();
        stored_height = userModel.getHeight();
        file_path = sharedpreferences.getString("profile_photo"+userID,"");
        if(!stored_name.equals("")){
            name_et.setText(stored_name);
        }
        if(!stored_blood.equals("")){
            blood_et.setText(stored_blood);
        }
        age_et.setText(String.valueOf(stored_age));
        height_et.setText(String.valueOf(stored_height));
        if(!file_path.equals("")){
            loadImage(file_path);
        }else{
            profile_iv.setImageResource(R.drawable.physhome_round_bg);
        }
    }
    void saveChanges(){
        String name = name_et.getText().toString();
        String blood = blood_et.getText().toString();
        String age_str = age_et.getText().toString();
        String height_str = height_et.getText().toString();
        int age=0, height=0;
        if(!age_str.equals("")){
            age = Integer.parseInt(age_str);
        }
        if(!height_str.equals("")){
            height = Integer.parseInt(height_str);
        }
        serverUpdatePatientInfo(name,height,blood,age);
    }
    void pickImage(){
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
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
    void loadImage(String path){
        Log.d("ProfileDebug",path);
        Glide
                .with(ProfileEditActivity.this)
                .load(Uri.parse(path))
                .dontAnimate()
                .into(profile_iv);
    }
    void serverUpdatePatientInfo(final String name, final int height, final String blood, final int age){
        RequestQueue mQueue = Volley.newRequestQueue(getApplicationContext());
        JSONObject jsonObject = new JSONObject();

        try {
            jsonObject.put("name_surname",name);
            jsonObject.put("height",height);
            jsonObject.put("blood_type",blood);
            jsonObject.put("age",age);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        Log.d("VOLLEYTAG","edit_start");
        Log.d("VOLLEYTAG",name+" "+height+" "+blood+" "+age);
        JsonObjectRequest jsObjRequest = new JsonObjectRequest
                (Request.Method.PUT,Constants.URL_USERS+"/"+userID, jsonObject, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("VOLLEYTAG","edit_finish");
                        Log.d("VOLLEYTAG",response.toString());
                        userModel = gson.fromJson(response.toString(),UserModel.class);
                        saveToPref("user",gson.toJson(userModel));
                        Toast.makeText(ProfileEditActivity.this, R.string.changes_are_saved, Toast.LENGTH_SHORT).show();
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
}
