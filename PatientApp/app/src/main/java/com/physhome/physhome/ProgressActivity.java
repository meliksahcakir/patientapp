package com.physhome.physhome;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by Meliksah on 7/16/2017.
 */

public class ProgressActivity extends BaseActivity {
    Toolbar toolbar;
    TabLayout tabLayout;
    ViewPager viewPager;
    TextView date_tv;
    Calendar cal_begin, cal_end;
    int elapsedDays = 0;
    ArrayList<ProgressModel> progressModels;
    ProgressPagerAdapter adapter;
    final static int DAY = 1;
    final static int WEEK = 7;
    final static int MONTH = 30;
    int selected_span = 1;
    ArrayList<String> dates, exerciseNames;
    AtomicInteger delay = new AtomicInteger(100);
    Spinner date_spinner, exercise_spinner;
    ArrayAdapter<String> date_adapter, exercise_adapter;
    ArrayList<ExerciseInfoModel> filteredInfoList;
    ArrayList<ExerciseModel> filteredList;
    ProgressDialog pd;
    ExerciseModel selected_model;
    ExerciseInfoModel selected_info_model;
    String selected_exercise_date;
    SeekBar seekBar;
    TextView current_tv, remain_tv;

    HashMap<String,ArrayList<ExerciseModel>> exerciseMap;
    HashMap<String,HashMap<String,ExerciseInfoModel>> exerciseInfoMap;
    ArrayList<TreatmentModel> treatmentModels = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_progress);
        cal_end = Calendar.getInstance();
        tabLayout = (TabLayout) findViewById(R.id.progress_tablayout);
        toolbar = (Toolbar) findViewById(R.id.progress_toolbar);
        date_spinner = (Spinner) toolbar.findViewById(R.id.progress_date_spinner);
        exercise_spinner = (Spinner) toolbar.findViewById(R.id.progress_exercise_spinner);
        date_tv = (TextView) findViewById(R.id.progress_date_tv);
        seekBar = (SeekBar) findViewById(R.id.progress_seekbar);
        current_tv = (TextView) findViewById(R.id.progress_current_tv);
        remain_tv = (TextView) findViewById(R.id.progress_remain_tv);
        setSupportActionBar(toolbar);
        viewPager = (ViewPager) findViewById(R.id.progress_viewpager);
        /*Calendar c = Calendar.getInstance();
        SimpleDateFormat dateformat = new SimpleDateFormat("dd-MMM-yyyy");
        String datetime = dateformat.format(c.getTime());
        Generate a date for Jan. 9, 2013, 10:11:12 AM
        Calendar cal = Calendar.getInstance();
        cal.set(2013, Calendar.JANUARY, 9, 10, 11, 12); //Year, month, day of month, hours, minutes and seconds
        Date date = cal.getTime();
        */

        tabLayout.addTab(tabLayout.newTab().setText(R.string.day));
        tabLayout.addTab(tabLayout.newTab().setText(R.string.week));
        tabLayout.addTab(tabLayout.newTab().setText(R.string.month));
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                int selectedTab = tabLayout.getSelectedTabPosition();
                if(selectedTab==0){
                    selected_span = DAY;
                }else if(selectedTab==1){
                    selected_span = WEEK;
                }else if(selectedTab==2){
                    selected_span = MONTH;
                }
                setScreenView();
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
        tabLayout.setScrollPosition(0,0,true);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                //ProgressModel model =  progressModels.get(position);
                //Calendar c =model.getCalendar();
                date_tv.setText(adapter.getPageTitle(position));
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        date_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                filterForTreatment(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        seekBar.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return true;
            }
        });

        exercise_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selected_model = filteredList.get(position);
                selected_info_model = filteredInfoList.get(position);
                selected_exercise_date = selected_model.getCreated_at();
                selected_exercise_date = selected_exercise_date.substring(0,selected_exercise_date.indexOf('T'));
                selected_exercise_date = Util.ChangeDateFormat(selected_exercise_date,"yyyy-MM-dd","dd/MM/yyyy");
                int current = selected_model.getCurrent_day();
                int total = selected_model.getTotal_day();
                int remain = total-current;
                if(remain<0) remain = 0;
                seekBar.setProgress(current*100/total);
                String day_current, day_left;
                if(current==1){
                   day_current = getResources().getString(R.string.day);
                }else{
                    day_current = getResources().getString(R.string.days);
                }
                if(remain==1){
                    day_left = getResources().getString(R.string.day);
                }else{
                    day_left = getResources().getString(R.string.days);
                }
                String str_current = ""+current+" "+day_current+" "+getResources().getString(R.string.done);
                String str_remain = ""+remain+" "+day_left+" "+getResources().getString(R.string.left);
                current_tv.setText(str_current);
                remain_tv.setText(str_remain);
                prepareCalendar();
                prepareList();
                for(int i=0;i<progressModels.size();i++){
                    ProgressModel model = progressModels.get(i);
                }
                setScreenView();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        if(checkConnection()) {
            pd = new ProgressDialog(ProgressActivity.this);
            pd.setMessage(getString(R.string.loading));
            pd.show();
            getTreatmentData();
        }else{
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
                createdAt = Util.ChangeDateFormat(createdAt,"yyyy-MM-dd","dd/MM/yyyy");
                String treatment = problem+" "+createdAt;
                dates.add(treatment);
            }
            if(!dates.isEmpty()){
                bindSpinnerAdapter(date_spinner,date_adapter,dates);
                filterForTreatment(0);
            }
        }
    }

    void setScreenView(){
        adapter = new ProgressPagerAdapter(ProgressActivity.this,progressModels,selected_span);
        viewPager.setAdapter(adapter);
        int lastPosition = adapter.getCount()-1;
        viewPager.setCurrentItem(lastPosition);
        date_tv.setText(adapter.getPageTitle(lastPosition));
    }

    void filterForTreatment(int position){
        TreatmentModel treatmentModel = treatmentModels.get(position);
        String treatment_id = treatmentModel.get_id();
        if(!exerciseMap.isEmpty()){
            filteredInfoList = new ArrayList<>();
            filteredList = new ArrayList<>();
            filteredList.addAll(exerciseMap.get(treatment_id));
            exerciseNames = new ArrayList<>();
            HashMap<String,ExerciseInfoModel> hashMap = exerciseInfoMap.get(treatment_id);
            for(ExerciseModel exerciseModel:filteredList){
                ExerciseInfoModel infoModel = hashMap.get(exerciseModel.getExercise_id());
                filteredInfoList.add(infoModel);
                exerciseNames.add(infoModel.getName());
            }
            bindSpinnerAdapter(exercise_spinner,exercise_adapter,exerciseNames);
        }
    }

    void prepareCalendar(){
        cal_begin = Calendar.getInstance();
        Log.d("PhyshomeDebug", "date: "+selected_exercise_date);
        cal_begin.setTime(Util.StringToDate(selected_exercise_date,"dd/MM/yyyy"));
        elapsedDays = getElapsedDays(cal_begin,cal_end);
    }
    void prepareList(){
        progressModels = new ArrayList<>();
        for(int i=0;i<=elapsedDays;i++){
            Calendar cal = Calendar.getInstance();
            cal.set(cal_begin.get(Calendar.YEAR),cal_begin.get(Calendar.MONTH),cal_begin.get(Calendar.DAY_OF_MONTH));
            cal.add(Calendar.DATE,i);
            progressModels.add(new ProgressModel(cal));
        }
        SessionModel[] history = selected_model.getExercise_history();
        for(int i=0;i<history.length;i++){
            SessionModel sessionModel = history[i];
            //2017-09-30T19:28:54.000Z
            String date_time = sessionModel.getDate_time();
            date_time = date_time.substring(0,date_time.indexOf('T'));
            Calendar cal_model = Calendar.getInstance();
            date_time = Util.ChangeDateFormat(date_time,"yyyy-MM-dd","dd/MM/yyyy");
            cal_model.setTime(Util.StringToDate(date_time,"dd/MM/yyyy"));
            int elapse = getElapsedDays(cal_begin,cal_model);
            Log.d("ElapseDay",""+elapse);

            CorrectModel correctModel = sessionModel.getCorrect();
            WrongModel[] wrongModel = sessionModel.getWrong();
            int correct = correctModel.getTotal();
            int fast = correctModel.getFast();
            int normal = correctModel.getExpected();
            int slow = correctModel.getSlow();
            int total = sessionModel.getTotal();
            int time = sessionModel.getTime();
            int[] error_amount = new int[wrongModel.length];
            for(int j=0;j<wrongModel.length;j++){
                error_amount[j] = wrongModel[j].getValue();
            }
            int[] speed_amount = {fast, normal, slow};

            if(progressModels.get(elapse).isAvailable()){
                ProgressModel model = progressModels.get(elapse);
                model.setCorrect_movement(model.getCorrect_movement()+correct);
                model.setTotal_movement(model.getTotal_movement()+total);
                model.setExercise_time(model.getExercise_time()+time);
                int[] model_speed = model.getSpeed();
                int[] model_error = model.getError();
                for(int j=0;j<3;j++){
                    model_speed[j]+=speed_amount[j];
                    model_error[j]+=error_amount[j];
                }
                model.setSpeed(model_speed);
                model.setError(model_error);
            }else{
                ProgressModel progressModel = new ProgressModel(true,cal_model,total,correct,time,speed_amount,error_amount);
                progressModels.set(elapse,progressModel);
            }
        }
    }
    public int getElapsedDays(Calendar c1, Calendar c2) {
        long milliSeconds1 = c1.getTimeInMillis();
        long milliSeconds2 = c2.getTimeInMillis();
        long periodSeconds = (milliSeconds2 - milliSeconds1) / 1000;
        return (int) (periodSeconds / 60 / 60 / 24);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.progress_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.progress_menu_date:
                final Calendar myCalendar = Calendar.getInstance();
                DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        myCalendar.set(Calendar.YEAR, year);
                        myCalendar.set(Calendar.MONTH, monthOfYear);
                        myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                        if(myCalendar.after(cal_end)||myCalendar.before(cal_begin)){
                            String start  = Util.dateToString(cal_begin,"dd/MM/yyyy");
                            String finish = Util.dateToString(cal_end,"dd/MM/yyyy");
                            Toast.makeText(ProgressActivity.this,getString(R.string.date_should_be_in_this_interval)+start+" - "+finish, Toast.LENGTH_LONG).show();
                        }else{
                            int position = (getElapsedDays(cal_begin,myCalendar))/selected_span;
                            viewPager.setCurrentItem(position);
                        }
                    }

                };
                new DatePickerDialog(this, date, cal_end.get(Calendar.YEAR), cal_end.get(Calendar.MONTH), cal_end.get(Calendar.DAY_OF_MONTH)).show();


                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    void bindSpinnerAdapter(Spinner spinner, ArrayAdapter adapter, ArrayList<String> list){
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item,list);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
    }

    private void getExerciseData() {
        exerciseInfoMap = new HashMap<>();
        exerciseNames = new ArrayList<>();

        Log.d("VOLLEYTAG","exercise_start");
        RequestQueue mQueue = Volley.newRequestQueue(getApplicationContext());
        JsonArrayRequest jsArrayRequest = new JsonArrayRequest
                (Request.Method.GET,Constants.URL_USERS+"/"+userID+"/exercises", null, new Response.Listener<JSONArray>() {

                    @Override
                    public void onResponse(JSONArray response) {
                        Log.d("VOLLEYTAG","exercise_finish");
                        Log.d("VOLLEYTAG",response.toString());
                        try {
                            delay.set(response.length());
                            if(delay.get()==0){
                                saveToPref(Constants.PREF_EXERCISE_INFO_LIST,gson.toJson(exerciseInfoMap));
                                pd.dismiss();
                                if(!dates.isEmpty()){
                                    bindSpinnerAdapter(date_spinner,date_adapter,dates);
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
                        delay.getAndDecrement();
                        if(delay.get()==0){
                            pd.dismiss();
                            saveToPref(Constants.PREF_EXERCISE_INFO_LIST,gson.toJson(exerciseInfoMap));
                            if(!dates.isEmpty()){
                                bindSpinnerAdapter(date_spinner,date_adapter,dates);
                                filterForTreatment(0);
                            }
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
        req.add(jsObjectRequest);
    }

    private void getTreatmentData() {
        treatmentModels = new ArrayList<>();
        dates = new ArrayList<>();
        exerciseMap = new HashMap<>();
        Log.d("VOLLEYTAG","treatment_start");
        RequestQueue mQueue = Volley.newRequestQueue(getApplicationContext());
        JsonArrayRequest jsArrayRequest = new JsonArrayRequest
                (Request.Method.GET,Constants.URL_USERS+"/"+userID+"/treatments", null, new Response.Listener<JSONArray>() {

                    @Override
                    public void onResponse(JSONArray response) {
                        Log.d("VOLLEYTAG","treatment_finish");
                        Log.d("VOLLEYTAG",response.toString());
                        try {
                            for(int i=0;i<response.length();i++){
                                JSONObject object = response.getJSONObject(i);
                                TreatmentModel treatmentModel = gson.fromJson(object.toString(),TreatmentModel.class);
                                treatmentModels.add(treatmentModel);
                                String createdAt = treatmentModel.getCreated_at();
                                String problem = treatmentModel.getProblem();
                                createdAt = createdAt.substring(0,createdAt.indexOf('T'));
                                createdAt = Util.ChangeDateFormat(createdAt,"yyyy-MM-dd","dd/MM/yyyy");
                                String treatment = problem+" "+createdAt;
                                dates.add(treatment);
                            }
                            for(int i=0;i<treatmentModels.size();i++){
                                exerciseMap.put(treatmentModels.get(i).get_id(),new ArrayList<ExerciseModel>());
                            }
                            saveToPref(Constants.PREF_TREATMENT_LIST,gson.toJson(treatmentModels));
                            if(!treatmentModels.isEmpty()){
                                getExerciseData();
                            }else{
                                pd.dismiss();
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
