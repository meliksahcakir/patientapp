package com.physhome.physhome;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LoginActivity extends BaseActivity {
    EditText phone_et, password_et;
    Button login_button;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        phone_et = (EditText) findViewById(R.id.et_login_phone);
        password_et = (EditText) findViewById(R.id.et_login_password);
        login_button = (Button) findViewById(R.id.login_button);
        buttonEffect(login_button);
        login_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String phone = phone_et.getText().toString().trim();
                String password = password_et.getText().toString();
                if(!phone.equals("")&&!password.equals("")){
                    if(checkConnection()){
                        login(phone, password);
                    }
                }
            }
        });
        Log.d("LoginDebug","id:"+userID);
        if(!userID.equals("")){
            Intent intent= new Intent(LoginActivity.this,MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            finish();
        }
        checkPermissions();
    }

    void login(final String phone, String password){
        final ProgressDialog pd;
        userModel = new UserModel();
        pd = new ProgressDialog(LoginActivity.this);
        pd.setMessage(getString(R.string.loading));
        pd.show();
        RequestQueue mQueue = Volley.newRequestQueue(getApplicationContext());
        HashMap<String, String> params = new HashMap<>();
        params.put("phone", phone);
        params.put("password", password);
        Log.d("VOLLEYTAG","login_start");
        JsonObjectRequest jsObjRequest = new JsonObjectRequest
                (Request.Method.POST,Constants.URL_SIGNIN, new JSONObject(params), new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        pd.dismiss();
                        Log.d("VOLLEYTAG","login_finish");
                        userModel = gson.fromJson(response.toString(),UserModel.class);
                        String errmsg="";
                        Log.d("VOLLEYTAG",response.toString());
                        try {
                            String doctor_id = response.getString("doctor_id");
                            editor.putString("userID",userModel.get_id());
                            editor.putString("user"+userModel.get_id(),gson.toJson(userModel));
                            editor.apply();
                            AlarmReceiver alarmReceiver = new AlarmReceiver();
                            alarmReceiver.setExerciseAlarm(LoginActivity.this);
                            Intent intent= new Intent(LoginActivity.this,MainActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(intent);
                            finish();
                        } catch (JSONException e) {
                            try {
                                errmsg = response.getString("errmsg");
                                Log.d("VOLLEYTAG","error:"+errmsg);
                                Toast.makeText(LoginActivity.this, errmsg, Toast.LENGTH_SHORT).show();
                            } catch (JSONException e1) {
                                e1.printStackTrace();
                                Log.d("VOLLEYTAG","error:"+e1.toString());
                            }
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        pd.dismiss();
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
    private boolean checkPermissions(){
        PermissionsUtil.askPermissions(this);
        try {
            if(PermissionsUtil.checkPermissions(this,PermissionsUtil.getPermissions(this))){
                return true;
            }
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return false;
    }
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case PermissionsUtil.PERMISSION_ALL: {

                if (grantResults.length > 0) {

                    List<Integer> indexesOfPermissionsNeededToShow = new ArrayList<>();

                    for(int i = 0; i < permissions.length; ++i) {
                        if(ActivityCompat.shouldShowRequestPermissionRationale(this, permissions[i])) {
                            indexesOfPermissionsNeededToShow.add(i);
                        }
                    }

                    int size = indexesOfPermissionsNeededToShow.size();
                    if(size != 0) {
                        int i = 0;
                        boolean isPermissionGranted = true;

                        while(i < size && isPermissionGranted) {
                            isPermissionGranted = grantResults[indexesOfPermissionsNeededToShow.get(i)]
                                    == PackageManager.PERMISSION_GRANTED;
                            i++;
                        }

                        if(!isPermissionGranted) {

                            showDialogNotCancelable("Permissions mandatory",
                                    "All the permissions are required for this app",
                                    new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {
                                            checkPermissions();
                                        }
                                    });
                        }
                    }
                }
            }
        }
    }

    private void showDialogNotCancelable(String title, String message,
                                         DialogInterface.OnClickListener okListener) {
        new AlertDialog.Builder(this)
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton("OK", okListener)
                .setCancelable(false)
                .create()
                .show();
    }
    @Override
    public void onBackPressed() {
        new android.support.v7.app.AlertDialog.Builder(this)
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
}
