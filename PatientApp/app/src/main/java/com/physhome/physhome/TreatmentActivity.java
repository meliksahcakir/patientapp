package com.physhome.physhome;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

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
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

public class TreatmentActivity extends BaseActivity implements OnRecyclerViewItemClickListener {
    RecyclerView exercise_rv, drug_rv;
    TextView disorder_tv, comment_tv, doctor_name_tv, hospital_name_tv;
    ExerciseRecyclerViewAdapter exerciseAdapter;
    DrugRecyclerViewAdapter drugAdapter;
    ArrayList<TreatmentModel> treatmentModels = new ArrayList<>();
    ArrayList<ExerciseInfoModel> filteredInfoList;
    ArrayList<ExerciseModel> filteredList;
    Spinner date_spinner;
    ArrayAdapter<String> date_adapter;
    ArrayList<String> dates;
    AtomicInteger delay = new AtomicInteger(100);
    ProgressDialog pd;
    boolean need_update = false;
    HashMap<String,ArrayList<ExerciseModel>> exerciseMap;
    HashMap<String,HashMap<String,ExerciseInfoModel>> exerciseInfoMap;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_treatment);
        exercise_rv = (RecyclerView) findViewById(R.id.treatment_recyclerview_exercise);
        drug_rv = (RecyclerView) findViewById(R.id.treatment_recyclerview_drug);
        disorder_tv = (TextView) findViewById(R.id.treatment_problem_tv);
        comment_tv = (TextView) findViewById(R.id.treatment_comment_tv);
        doctor_name_tv = (TextView) findViewById(R.id.treatment_doctor_name_tv);
        hospital_name_tv = (TextView) findViewById(R.id.treatment_hospital_name_tv);
        date_spinner = (Spinner) findViewById(R.id.treatment_spinner);
        drug_rv.setHasFixedSize(true);
        LinearLayoutManager drug_layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        drug_rv.setLayoutManager(drug_layoutManager);
        exercise_rv.setHasFixedSize(true);
        LinearLayoutManager exercise_layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        exercise_rv.setLayoutManager(exercise_layoutManager);
        date_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                filterForTreatment(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        getOfflineData();
        if(checkConnection()){
            if (need_update){
                pd = new ProgressDialog(TreatmentActivity.this);
                pd.setMessage(getString(R.string.loading));
                pd.show();
            }
            getTreatmentData();
        }
    }
    void getOfflineData(){
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
        }
    }
    void filterForTreatment(int position){
        TreatmentModel treatmentModel = treatmentModels.get(position);
        String treatment_id = treatmentModel.get_id();
        bindDrugAdapter(treatmentModel.getDrugs());
        setTreatmentInfo(treatmentModel);
        if(!exerciseMap.isEmpty()){
            filteredInfoList = new ArrayList<>();
            filteredList = new ArrayList<>();
            filteredList.addAll(exerciseMap.get(treatment_id));
            HashMap<String,ExerciseInfoModel> hashMap = exerciseInfoMap.get(treatment_id);
            for(ExerciseModel exerciseModel:filteredList){
                filteredInfoList.add(hashMap.get(exerciseModel.getExercise_id()));
            }
            bindExerciseAdapter(filteredList,filteredInfoList);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        /*prepareExerciseList();
        prepareDrugList();
        prepareTreatmentInfo(disorder, comment, name, hospital);
        exerciseAdapter = new ExerciseRecyclerViewAdapter(this,exerciseModels, this);
        drugAdapter = new DrugRecyclerViewAdapter(this,drugModels);
        exercise_rv.setHasFixedSize(true);
        LinearLayoutManager exercise_layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        exercise_rv.setLayoutManager(exercise_layoutManager);
        exercise_rv.setAdapter(exerciseAdapter);
        drug_rv.setHasFixedSize(true);
        LinearLayoutManager drug_layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        drug_rv.setLayoutManager(drug_layoutManager);
        drug_rv.setAdapter(drugAdapter);  */
        //getTreatmentData();
        //getExerciseData();
    }

    /*

    void prepareExerciseList(){
        if(!exerciseModels.isEmpty()){
            exerciseModels.clear();
        }
        exerciseModels.add(new ExerciseModel("1","22 August",getString(R.string.triceps),"arka_kol","",20,30,null));
        exerciseModels.add(new ExerciseModel("1","22 August",getString(R.string.knee_extension),"diz_ekstansiyon","",20,30,null));
        exerciseModels.add(new ExerciseModel("1","22 August",getString(R.string.shoulder_flexion),"omuz_fleksiyon","",20,30,null));
        exerciseModels.add(new ExerciseModel("1","22 August",getString(R.string.back_skapula_adduction),"skapula_adduksiyon_sirt","",20,30,null));
        exerciseModels.add(new ExerciseModel("1","22 August",getString(R.string.arms_stretcing_while_lying),"yatarak_kol_acma","",20,30,null));
    }

    void prepareDrugList(){
        if(!drugModels.isEmpty()){
            drugModels.clear();
        }
        drugModels.add(new DrugModel("B12 vitamin", "Ac karnina",1));
        drugModels.add(new DrugModel("Majezik", "Tok karnina",2));
        drugModels.add(new DrugModel("Parol", "Tok karnina",3));
    }

    */

    void bindDrugAdapter(DrugModel[] models){
        ArrayList<DrugModel> drugs = new ArrayList<>(Arrays.asList(models));
        drugAdapter = new DrugRecyclerViewAdapter(this,drugs);
        drug_rv.setAdapter(drugAdapter);
    }
    void bindExerciseAdapter(ArrayList<ExerciseModel> models, ArrayList<ExerciseInfoModel> infoModels){
        exerciseAdapter = new ExerciseRecyclerViewAdapter(this,models,infoModels,this);
        exercise_rv.setAdapter(exerciseAdapter);
    }

    @Override
    public void onRecyclerViewItemClicked(int position) {
        ExerciseInfoModel infoModel = filteredInfoList.get(position);
        ExerciseModel model = filteredList.get(position);
        String info_json = gson.toJson(infoModel);
        String json = gson.toJson(model);
        Bundle bundle = new Bundle();
        bundle.putString("model", json);
        bundle.putString("info_model", info_json);
        bundle.putString("deneme","12345");
        Log.d("BundleDebug",""+bundle.toString());
        Intent intent = new Intent(TreatmentActivity.this, ExerciseInfoActivity.class);
        intent.putExtras(bundle);
        startActivity(intent);
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
                                exerciseMap.put(treatmentModels.get(i).get_id(),new ArrayList<ExerciseModel>());
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
    void bindSpinnerAdapter(){
        date_adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item,dates);
        date_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        date_spinner.setAdapter(date_adapter);
    }

    private void getExerciseData() {
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
        RequestQueue mQueue = Volley.newRequestQueue(getApplicationContext());
        JsonObjectRequest jsObjectRequest = new JsonObjectRequest
                (Request.Method.GET,Constants.URL_EXERCISES+"/"+exercise_id, null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("VOLLEYTAG","info_finish");
                        Log.d("VOLLEYTAG","info:"+response.toString());
                        ExerciseInfoModel model = gson.fromJson(response.toString(),ExerciseInfoModel.class);
                        exerciseInfoMap.get(treatment_id).put(exercise_id,model);
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
        mQueue.add(jsObjectRequest);
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
                            if(delay.get()==0){
                                saveToPref(Constants.PREF_EXERCISE_INFO_LIST,gson.toJson(exerciseInfoMap));
                                Util.dismissProgressDialog(pd);
                                if(!dates.isEmpty()){
                                    bindSpinnerAdapter();
                                    filterForTreatment(0);
                                }
                            }
                        }
                    });
        }else{
            delay.getAndDecrement();
            if(delay.get()==0){
                saveToPref(Constants.PREF_EXERCISE_INFO_LIST,gson.toJson(exerciseInfoMap));
                Util.dismissProgressDialog(pd);
                if(!dates.isEmpty()){
                    bindSpinnerAdapter();
                    filterForTreatment(0);
                }
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
                                if(delay.get()==0){
                                    saveToPref(Constants.PREF_EXERCISE_INFO_LIST,gson.toJson(exerciseInfoMap));
                                    Util.dismissProgressDialog(pd);
                                    if(!dates.isEmpty()){
                                        bindSpinnerAdapter();
                                        filterForTreatment(0);
                                    }
                                }
                            }
                        });
            }else{
                delay.getAndDecrement();
                if(delay.get()==0){
                    saveToPref(Constants.PREF_EXERCISE_INFO_LIST,gson.toJson(exerciseInfoMap));
                    Util.dismissProgressDialog(pd);
                    if(!dates.isEmpty()){
                        bindSpinnerAdapter();
                        filterForTreatment(0);
                    }
                }
            }
        }else{
            delay.getAndDecrement();
            if(delay.get()==0){
                saveToPref(Constants.PREF_EXERCISE_INFO_LIST,gson.toJson(exerciseInfoMap));
                Util.dismissProgressDialog(pd);
                if(!dates.isEmpty()){
                    bindSpinnerAdapter();
                    filterForTreatment(0);
                }
            }
        }
    }
    void setTreatmentInfo(TreatmentModel treatmentModel){
        String problem_str = getString(R.string.problem)+": "+"<b>" + treatmentModel.getProblem()+ "</b>";
        String comment_str = getString(R.string.doctor_comment)+": "+"<b>" + treatmentModel.getComment()+ "</b>";
        disorder_tv.setText(Util.fromHtml(problem_str));
        comment_tv.setText(Util.fromHtml(comment_str));
        doctor_name_tv.setText(treatmentModel.getDoctor_name());
        hospital_name_tv.setText(treatmentModel.getHospital_name());
    }
}
