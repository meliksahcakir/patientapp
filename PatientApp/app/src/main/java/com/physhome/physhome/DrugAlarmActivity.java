package com.physhome.physhome;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.provider.AlarmClock;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

public class DrugAlarmActivity extends BaseActivity implements View.OnClickListener{
    TextView monday_tv, tuesday_tv, wednesday_tv, thursday_tv, friday_tv, saturday_tv, sunday_tv;
    ArrayList<Boolean> is_day_selected;
    Button save_button;
    TimePicker time_picker;
    ArrayList<TreatmentModel> treatmentModels;
    DrugModel[] drugs;
    Spinner drug_spinner;
    ArrayAdapter<String> drug_adapter;
    ArrayList<DrugModel> drugModels;
    ArrayList<String> drugNames;
    String selected_name = "";
    String selected_comment = "";
    int selected_amount = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drug_alarm);
        is_day_selected = new ArrayList<>();
        for (int i=0;i<7;i++){
            is_day_selected.add(false);
        }
        monday_tv = (TextView) findViewById(R.id.monday_tv);
        tuesday_tv = (TextView) findViewById(R.id.tuesday_tv);
        wednesday_tv = (TextView) findViewById(R.id.wednesday_tv);
        thursday_tv = (TextView) findViewById(R.id.thursday_tv);
        friday_tv = (TextView) findViewById(R.id.friday_tv);
        saturday_tv = (TextView) findViewById(R.id.saturday_tv);
        sunday_tv = (TextView) findViewById(R.id.sunday_tv);
        save_button = (Button) findViewById(R.id.alarm_save_button);
        time_picker = (TimePicker) findViewById(R.id.time_picker);
        drug_spinner = (Spinner) findViewById(R.id.drug_alarm_spinner);

        monday_tv.setTag(0);
        tuesday_tv.setTag(1);
        wednesday_tv.setTag(2);
        thursday_tv.setTag(3);
        friday_tv.setTag(4);
        saturday_tv.setTag(5);
        sunday_tv.setTag(6);
        buttonEffect(save_button);
        monday_tv.setOnClickListener(this);
        tuesday_tv.setOnClickListener(this);
        wednesday_tv.setOnClickListener(this);
        thursday_tv.setOnClickListener(this);
        friday_tv.setOnClickListener(this);
        saturday_tv.setOnClickListener(this);
        sunday_tv.setOnClickListener(this);

        save_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int hour, minute;
                if (Build.VERSION.SDK_INT >= 23 ){
                    hour = time_picker.getHour();
                    minute = time_picker.getMinute();
                } else{
                    hour = time_picker.getCurrentHour();
                    minute = time_picker.getCurrentMinute();
                }
                ArrayList<Integer> days = new ArrayList<>();
                if(is_day_selected.get(0)) days.add(Calendar.MONDAY);
                if(is_day_selected.get(1)) days.add(Calendar.TUESDAY);
                if(is_day_selected.get(2)) days.add(Calendar.WEDNESDAY);
                if(is_day_selected.get(3)) days.add(Calendar.THURSDAY);
                if(is_day_selected.get(4)) days.add(Calendar.FRIDAY);
                if(is_day_selected.get(5)) days.add(Calendar.SATURDAY);
                if(is_day_selected.get(6)) days.add(Calendar.SUNDAY);

                Log.d("AlarmDebug",""+days.toString());
                Intent i = new Intent(AlarmClock.ACTION_SET_ALARM);
                i.putExtra(AlarmClock.EXTRA_SKIP_UI, true);
                i.putExtra(AlarmClock.EXTRA_HOUR, hour);
                i.putExtra(AlarmClock.EXTRA_MINUTES, minute);
                i.putExtra(AlarmClock.EXTRA_MESSAGE, selected_name+" "+selected_comment);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                    i.putExtra(AlarmClock.EXTRA_DAYS,days);
                }
                startActivity(i);
            }
        });
        drug_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                int hour;
                int min;
                if(position==0){
                    selected_name = "";
                    selected_comment = "";
                    selected_amount = 0;
                }else{
                    selected_name = drugModels.get(position-1).getName();
                    selected_comment = drugModels.get(position-1).getComment();
                    selected_amount = drugModels.get(position-1).getHow_many();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        getTreatmentData();
    }

    @Override
    public void onClick(View v) {
        int tag = (int) v.getTag();
        changeState(tag,(TextView) v);
    }

    void changeState(int tag, TextView v){
        if(is_day_selected.get(tag)){
            v.setTypeface(null, Typeface.NORMAL);
            v.setTextColor(Color.BLACK);
            is_day_selected.set(tag,false);
        }else{
            v.setTypeface(null, Typeface.BOLD);
            v.setTextColor(Color.GREEN);
            is_day_selected.set(tag,true);
        }
    }
    private void getTreatmentData() {
        treatmentModels = new ArrayList<>();
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
                            }
                            drugNames = new ArrayList<>();

                            if(!treatmentModels.isEmpty()){
                                sortForDates(treatmentModels);
                                drugs = treatmentModels.get(0).getDrugs();
                                drugModels = new ArrayList<>(Arrays.asList(drugs));
                                sortForNames(drugModels);
                                for(DrugModel drugModel:drugModels){
                                    String name = drugModel.getName();
                                    int times = drugModel.getHow_many();
                                    drugNames.add(name+"  ("+getString(R.string.amount)+" "+times+")");
                                }
                            }else{
                                drugs = new DrugModel[0];
                                drugModels = new ArrayList<>();
                            }
                            bindSpinnerAdapter(drugNames);
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

    void sortForDates(ArrayList<TreatmentModel> list){
        Collections.sort(list, new Comparator<TreatmentModel>(){
            public int compare(TreatmentModel obj1, TreatmentModel obj2) {
                String str2 = obj2.getCreated_at();
                String str1 = obj1.getCreated_at();
                return str2.compareToIgnoreCase(str1);
            }
        });
    }
    void sortForNames(ArrayList<DrugModel> list){
        Collections.sort(list, new Comparator<DrugModel>(){
            public int compare(DrugModel obj1, DrugModel obj2) {
                String str2 = obj2.getName();
                String str1 = obj1.getName();
                return str1.compareToIgnoreCase(str2);
            }
        });
    }

    void bindSpinnerAdapter(ArrayList<String> drug_names){
        ArrayList<String> list = new ArrayList<>();
        list.add(getString(R.string.select_a_drug));
        if(!drug_names.isEmpty()){
            list.addAll(drug_names);
        }
        drug_adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item,list);
        drug_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        drug_spinner.setAdapter(drug_adapter);
    }
}
