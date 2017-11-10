package com.physhome.physhome;

import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.reflect.TypeToken;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

public class SelectExerciseActivity extends BaseActivity implements OnRecyclerViewItemClickListener{
    RecyclerView recyclerView;
    Button select_button;
    Spinner exercise_spinner;
    SelectExerciseRecyclerViewAdapter adapter;
    //ArrayList<ExerciseModel> exerciseList;
    int selected_model=-1;
    //ArrayList<ExerciseModel> serverExerciseList;
    //ArrayList<ExerciseInfoModel> serverExerciseInfoList;
    AtomicInteger delay = new AtomicInteger(100);
    ArrayList<String> dates;
    ArrayAdapter<String> exercise_adapter;

    ArrayList<ExerciseInfoModel> filteredInfoList;
    ArrayList<ExerciseModel> filteredList;
    ProgressDialog pd;
    private BluetoothAdapter BA;
    private Set<BluetoothDevice> pairedDevices;
    boolean both_connected =false;
    boolean need_update = false;
    HashMap<String,ArrayList<ExerciseModel>> exerciseMap;
    HashMap<String,HashMap<String,ExerciseInfoModel>> exerciseInfoMap;
    ArrayList<TreatmentModel> treatmentModels = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_exercise);
        recyclerView = (RecyclerView) findViewById(R.id.exercise_recyclerview);
        select_button = (Button) findViewById(R.id.select_exercise_button);
        exercise_spinner = (Spinner) findViewById(R.id.exercise_spinner);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        BA = BluetoothAdapter.getDefaultAdapter();
        select_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(both_connected){
                    if(selected_model!=-1){
                        ExerciseModel model = filteredList.get(selected_model);
                        ExerciseInfoModel infoModel = filteredInfoList.get(selected_model);
                        String json = gson.toJson(model);
                        String info_json = gson.toJson(infoModel);
                        Bundle bundle = new Bundle();
                        bundle.putString("model", json);
                        bundle.putString("info_model", info_json);
                        bundle.putInt("exercise_id",selected_model);
                        Intent intent = new Intent(SelectExerciseActivity.this, BluetoothActivity.class);
                        intent.putExtras(bundle);
                        startActivity(intent);
                    }else{
                        Toast.makeText(SelectExerciseActivity.this, R.string.select_an_exercise, Toast.LENGTH_SHORT).show();
                    }
                }else{
                    showAlertDialog();
                }
            }
        });
        exercise_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String date = dates.get(position);
                filterForTreatment(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        String str_exercise = getFromPref(Constants.PREF_EXERCISE_LIST);
        String str_exercise_info = getFromPref(Constants.PREF_EXERCISE_INFO_LIST);
        String str_treatment = getFromPref(Constants.PREF_TREATMENT_LIST);

        if(!str_exercise.equals("")){
            Type type = new TypeToken<HashMap<String,ArrayList<ExerciseModel>>>() {}.getType();
            exerciseMap = gson.fromJson(str_exercise, type);
        }else{
            exerciseMap = new HashMap<>();
        }
        if(!str_exercise_info.equals("")){
            Type type = new TypeToken<HashMap<String,HashMap<String,ExerciseInfoModel>>>() {}.getType();
            exerciseInfoMap = gson.fromJson(str_exercise_info, type);
        }else{
            exerciseInfoMap = new HashMap<>();
        }
        if(!str_treatment.equals("")){
            Type type = new TypeToken<ArrayList<TreatmentModel>>() {}.getType();
            treatmentModels = gson.fromJson(str_treatment, type);
        }else{
            need_update = true;
            treatmentModels = new ArrayList<>();
        }
        dates = new ArrayList<>();
        for(TreatmentModel treatmentModel:treatmentModels){
            String createdAt = treatmentModel.getCreated_at();
            String problem = treatmentModel.getProblem();
            createdAt = createdAt.substring(0,createdAt.indexOf('T'));
            String treatment = problem+" "+createdAt;
            if(!dates.contains(treatment)){
                dates.add(treatment);
            }
        }
        if(!dates.isEmpty()){
            bindSpinnerAdapter();
            filterForTreatment(0);
            checkBTState();
        }
        if(checkConnection()) {
            if(need_update){
                pd = new ProgressDialog(SelectExerciseActivity.this);
                pd.setMessage(getString(R.string.loading));
                pd.show();
            }
            getTreatmentData();
        }
    }

    void filterForTreatment(int position){
        if(!exerciseMap.isEmpty()){
            String treatment_id = treatmentModels.get(position).get_id();
            filteredInfoList = new ArrayList<>();
            filteredList = new ArrayList<>();
            filteredList.addAll(exerciseMap.get(treatment_id));
            HashMap<String,ExerciseInfoModel> hashMap = exerciseInfoMap.get(treatment_id);
            for(ExerciseModel exerciseModel:filteredList){

                filteredInfoList.add(hashMap.get(exerciseModel.getExercise_id()));
            }
            if(!filteredList.isEmpty()){
                updateRecyclerView(filteredList,filteredInfoList);
            }

        }
    }
    /*private void prepareListData() {
        exerciseList = new ArrayList<>();

        exerciseList.add(new ExerciseModel("1","22 August",getString(R.string.leg_lift),"bacakegzersizi","",20,30,null));
        exerciseList.add(new ExerciseModel("2","22 August",getString(R.string.triceps),"arka_kol","",20,30,null));
        exerciseList.add(new ExerciseModel("3","22 August",getString(R.string.knee_extension),"diz_ekstansiyon","",20,30,null));
        exerciseList.add(new ExerciseModel("4","22 August",getString(R.string.shoulder_flexion),"omuz_fleksiyon","",20,30,null));
        exerciseList.add(new ExerciseModel("5","22 August",getString(R.string.back_skapula_adduction),"skapula_adduksiyon_sirt","",20,30,null));
        exerciseList.add(new ExerciseModel("5","22 August",getString(R.string.arms_stretcing_while_lying),"yatarak_kol_acma","",20,30,null));
        exerciseList.add(new ExerciseModel("5","22 August",getString(R.string.shoulder_elevation),"shoulder_elevation","",20,30,null));
        exerciseList.add(new ExerciseModel("5","22 August",getString(R.string.shoulder_horizontal_abduction),"shoulder_horizontal_abduction","",20,30,null));
    }*/
    void updateRecyclerView(ArrayList<ExerciseModel> list,ArrayList<ExerciseInfoModel> infoList){
        adapter = new SelectExerciseRecyclerViewAdapter(this, list, infoList,this);
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();  // data set changed
    }
    @Override
    protected void onResume() {
        super.onResume();
        //prepareListData();
        //updateRecyclerView(exerciseList);

    }
    @Override
    public void onRecyclerViewItemClicked(int position) {
        selected_model = position;
    }
    void bindSpinnerAdapter(){
        Log.d("PhyshomeDebug",dates.toString());
        exercise_adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item,dates);
        exercise_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        exercise_spinner.setAdapter(exercise_adapter);
    }

    private void getExerciseData() {
        //dates = new ArrayList<>();
        //exerciseList = new HashMap<>();
        //exerciseInfoList = new HashMap<>();
        Log.d("VOLLEYTAG","exercise_start");
        RequestQueue mQueue = Volley.newRequestQueue(getApplicationContext());
        JsonArrayRequest jsArrayRequest = new JsonArrayRequest
                (Request.Method.GET,Constants.URL_USERS+"/"+userID+"/exercises", null, new Response.Listener<JSONArray>() {

                    @Override
                    public void onResponse(JSONArray response) {
                        exerciseInfoMap = new HashMap<>();
                        Log.d("VOLLEYTAG","exercise_finish");
                        Log.d("VOLLEYTAG",response.toString());
                        try {
                            delay.set(2*response.length());
                            if(delay.get()==0){
                                saveToPref(Constants.PREF_EXERCISE_INFO_LIST,gson.toJson(exerciseInfoMap));
                                Util.dismissProgressDialog(pd);
                                if(!dates.isEmpty()){
                                    bindSpinnerAdapter();
                                    filterForTreatment(0);
                                }
                            }
                            for(int i=0;i<response.length();i++){
                                JSONObject object = response.getJSONObject(i);
                                ExerciseModel model = gson.fromJson(object.toString(),ExerciseModel.class);
                                Log.d("PhyshomeDebug","Exercise id: "+model.getTreatment_id());
                                exerciseMap.get(model.getTreatment_id()).add(model);
                                //exerciseList.put(model.getExercise_id(),model);

                            }

                            if(!exerciseMap.isEmpty()){
                                for(String treatment_id:exerciseMap.keySet()){
                                    ArrayList<ExerciseModel> list = exerciseMap.get(treatment_id);
                                    for(ExerciseModel model:list){
                                        exerciseInfoMap.put(treatment_id,new HashMap<String, ExerciseInfoModel>());
                                        getExerciseInfoData(treatment_id,model.getExercise_id());
                                    }
                                }
                            }
                            saveToPref(Constants.PREF_EXERCISE_LIST,gson.toJson(exerciseMap));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Util.dismissProgressDialog(pd);
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

    private void getExerciseInfoData(final String treatment_id, final String exercise_id) {
        Log.d("VOLLEYTAG","info_start");
        RequestQueue req = Volley.newRequestQueue(getApplicationContext());
        JsonObjectRequest jsObjectRequest = new JsonObjectRequest
                (Request.Method.GET,Constants.URL_EXERCISES+"/"+exercise_id, null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("VOLLEYTAG","info_finish");
                        Log.d("VOLLEYTAG","info:"+response.toString());
                        ExerciseInfoModel model = gson.fromJson(response.toString(),ExerciseInfoModel.class);
                        exerciseInfoMap.get(treatment_id).put(exercise_id,model);
                        //exerciseInfoList.put(model.get_id(),model);
                        downloadGif(model.getGif_id());
                        downloadImage(model.getImage_id());
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Util.dismissProgressDialog(pd);
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

    void downloadGif(String image_id){
        final String path = getFilesDir()+"/"+image_id+".gif";
        File file = new File(path);
        if(!file.exists()){
            Ion.with(this)
                    .load(Constants.URL_IMAGES+"/"+image_id)
                    .write(new File(path))
                    .setCallback(new FutureCallback<File>() {
                        @Override
                        public void onCompleted(Exception e, File result) {
                            Log.d("DownloadGif","completed. Path: "+path);
                            delay.getAndDecrement();
                            Log.d("ExerciseDebug","file downloaded. Delay:"+delay.get());
                            if(delay.get()==0){
                                Util.dismissProgressDialog(pd);
                                saveToPref(Constants.PREF_EXERCISE_INFO_LIST,gson.toJson(exerciseInfoMap));
                                Log.d("ExerciseDebug",dates.toString());
                                if(!dates.isEmpty()){
                                    bindSpinnerAdapter();
                                    filterForTreatment(0);
                                    checkBTState();
                                }
                                delay.getAndDecrement();
                            }
                        }
                    });
        }else{
            delay.getAndDecrement();
            Log.d("ExerciseDebug","file found. Delay:"+delay.get());
            if(delay.get()==0){
                saveToPref(Constants.PREF_EXERCISE_INFO_LIST,gson.toJson(exerciseInfoMap));
                Util.dismissProgressDialog(pd);
                Log.d("ExerciseDebug",dates.toString());
                if(!dates.isEmpty()){
                    bindSpinnerAdapter();
                    filterForTreatment(0);
                    checkBTState();
                }
                delay.getAndDecrement();
            }
        }
    }
    void downloadImage(String image_id){
        if(!image_id.equals("000000000000000000000000")){
            final String path = getFilesDir()+"/"+image_id+".png";
            File file = new File(path);
            if(!file.exists()){
                Ion.with(this)
                        .load(Constants.URL_IMAGES+"/"+image_id)
                        .write(new File(path))
                        .setCallback(new FutureCallback<File>() {
                            @Override
                            public void onCompleted(Exception e, File result) {
                                Log.d("DownloadImage","completed. Path: "+path);
                                delay.getAndDecrement();
                                Log.d("ExerciseDebug","file downloaded. Delay:"+delay.get());
                                if(delay.get()==0){
                                    Util.dismissProgressDialog(pd);
                                    saveToPref(Constants.PREF_EXERCISE_INFO_LIST,gson.toJson(exerciseInfoMap));
                                    Log.d("ExerciseDebug",dates.toString());
                                    if(!dates.isEmpty()){
                                        bindSpinnerAdapter();
                                        filterForTreatment(0);
                                        checkBTState();
                                    }
                                    delay.getAndDecrement();
                                }
                            }
                        });
            }else{
                delay.getAndDecrement();
                Log.d("ExerciseDebug","file found. Delay:"+delay.get());
                if(delay.get()==0){
                    saveToPref(Constants.PREF_EXERCISE_INFO_LIST,gson.toJson(exerciseInfoMap));
                    Util.dismissProgressDialog(pd);
                    Log.d("ExerciseDebug",dates.toString());
                    if(!dates.isEmpty()){
                        bindSpinnerAdapter();
                        filterForTreatment(0);
                        checkBTState();
                    }
                    delay.getAndDecrement();
                }
            }
        }else{
            delay.getAndDecrement();
            Log.d("ExerciseDebug","file found. Delay:"+delay.get());
            if(delay.get()==0){
                saveToPref(Constants.PREF_EXERCISE_INFO_LIST,gson.toJson(exerciseInfoMap));
                Util.dismissProgressDialog(pd);
                Log.d("ExerciseDebug",dates.toString());
                if(!dates.isEmpty()){
                    bindSpinnerAdapter();
                    filterForTreatment(0);
                    checkBTState();
                }
                delay.getAndDecrement();
            }
        }
    }
    void checkBTState(){
        if(BA==null) {
            Toast.makeText(SelectExerciseActivity.this, R.string.bluetooth_not_support, Toast.LENGTH_SHORT).show();
        }else{
            if (BA.isEnabled()) {
                pairedDevices = BA.getBondedDevices();
                for(PhyshomePatientDeviceModel model: userDevices){
                    Log.d("BtDebug",model.getMac_address() +" "+model.getChe_limb());
                }

                int count = 0;
                if (pairedDevices.size() > 0){
                    for(BluetoothDevice bt : pairedDevices){
                        for(PhyshomePatientDeviceModel model: userDevices){
                            if(bt.getAddress().equals(model.getMac_address())){
                                count++;
                            }
                        }
                    }
                }
                if(count!=2){
                    both_connected = false;
                    showAlertDialog();
                }else{
                    both_connected = true;
                }
            } else {
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
    void showAlertDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.directing_to_bt_settings)
                .setCancelable(false)
                .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                        Intent intent = new Intent(SelectExerciseActivity.this, BluetoothSettingsActivity.class);
                        intent.putExtra(Constants.BT_RETURN_ACTIVITY,Constants.BT_SELECT_EXERCISE_ACTIVITY);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
    }
    private void getTreatmentData() {
        Log.d("VOLLEYTAG","treatment_start");
        RequestQueue mQueue = Volley.newRequestQueue(getApplicationContext());
        JsonArrayRequest jsArrayRequest = new JsonArrayRequest
                (Request.Method.GET,Constants.URL_USERS+"/"+userID+"/treatments", null, new Response.Listener<JSONArray>() {

                    @Override
                    public void onResponse(JSONArray response) {
                        Log.d("VOLLEYTAG","treatment_finish");
                        Log.d("VOLLEYTAG",response.toString());
                        treatmentModels = new ArrayList<>();
                        dates = new ArrayList<>();
                        exerciseMap = new HashMap<>();
                        try {
                            for(int i=0;i<response.length();i++){
                                JSONObject object = response.getJSONObject(i);
                                TreatmentModel treatmentModel = gson.fromJson(object.toString(),TreatmentModel.class);
                                treatmentModels.add(treatmentModel);
                                String createdAt = treatmentModel.getCreated_at();
                                String problem = treatmentModel.getProblem();
                                createdAt = createdAt.substring(0,createdAt.indexOf('T'));
                                String treatment = problem+" "+createdAt;
                                if(!dates.contains(treatment)){
                                    dates.add(treatment);
                                }
                            }
                            for(int i=0;i<treatmentModels.size();i++){
                                ArrayList<ExerciseModel> arrayList = new ArrayList<>();
                                exerciseMap.put(treatmentModels.get(i).get_id(),arrayList);
                                Log.d("PhyshomeDebug","Treatment id: "+treatmentModels.get(i).get_id());
                            }
                            saveToPref(Constants.PREF_TREATMENT_LIST,gson.toJson(treatmentModels));
                            if(!treatmentModels.isEmpty()){
                                getExerciseData();
                            }else{
                                Util.dismissProgressDialog(pd);
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

}
