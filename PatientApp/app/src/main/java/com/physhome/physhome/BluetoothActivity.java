package com.physhome.physhome;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.graphics.drawable.ClipDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.ParcelUuid;
import android.speech.tts.TextToSpeech;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.reflect.TypeToken;
import com.jackandphantom.circularprogressbar.CircleProgressbar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicBoolean;

import pl.droidsonroids.gif.GifDrawable;
import pl.droidsonroids.gif.GifImageView;

public class BluetoothActivity extends BaseActivity {
    ImageView action_button;
    int action_state = 0;
    boolean exercise_break = false;
    GifImageView imageView;
    private BluetoothAdapter BA;
    private Set<BluetoothDevice> pairedDevices;
    private static final UUID MY_UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
    boolean bt_open=false;
    BluetoothCustomAdapter bluetoothCustomAdapter;
    final String TAG = "BluetoothDebug";
    int pause=0;
    int bt_repeat=0;
    TextView exercise_start_message_tv;
    ArrayList<BluetoothDevice> mBluetoothDeviceList;
    ArrayList<BluetoothSocket> mBluetoothSocketList;
    ArrayList<InputStream> mInputStreamList;
    ArrayList<UUID> mUUIDList;
    ArrayList<String> mBluetoothNameList;
    ArrayList<String> mBluetoothMACList;
    ArrayList<Boolean> mBluetoothConnectionStateList;
    ArrayList<Integer> mSelectedBTList;
    AccelerometerDataClass sensor1, sensor2, checkPointsArm, checkPointsChest, patient1, patient2, temp1, temp2;
    ArrayList<Integer> checkPointsMotionArm = new ArrayList<>();
    ArrayList<Integer> checkPointsMotionChest = new ArrayList<>();
    ArrayList<Integer> checkPointsMotionArm_X = new ArrayList<>();
    ArrayList<Integer> checkPointsMotionArm_Y = new ArrayList<>();
    ArrayList<Integer> checkPointsMotionArm_Z = new ArrayList<>();
    ArrayList<Integer> checkPointsMotionChest_X = new ArrayList<>();
    ArrayList<Integer> checkPointsMotionChest_Y = new ArrayList<>();
    ArrayList<Integer> checkPointsMotionChest_Z = new ArrayList<>();
    ArrayList<ArrayList<Integer> > radiusArm = new ArrayList<>();
    ArrayList<ArrayList<Integer> > radiusChest = new ArrayList<>();
    ArrayList<Integer> peaks_Arm = new ArrayList<>();
    ArrayList<Integer> peaks_Chest = new ArrayList<>();
    //int init_x=0, init_y=0, init_z=0, x_pp=0, y_pp=0, z_pp=0, checkPointCounter = 0;
    int dominant_axis_arm=0, dominant_axis_chest=0;
    final static int X_AXIS = 0;
    final static int Y_AXIS = 1;
    final static int Z_AXIS = 2;
    final static int THRESHOLD = 17;
    final static int CHECKPOINTSTEP = 7;
    int checkPointsNumArm =0, checkPointsNumChest=0;
    int whichCpArm = 1, whichCpChest=1;
    int current_checkpoint = 1, last_passed_checkpoint = 0;
    int time2nextCPArm=0, time2nextCPChest=0;
    int fastCountArm = 0, fastCountChest = 0;
    int slowCountArm = 0, slowCountChest = 0;
    int waitForChest = 0;
    int globalRecordCounter = 0;
    int globalTestCounter = 0;
    int testPeak = 0;
    int motionDirection = 0;
    int limbMotionSign = 0;
    int chestMotionSign = 0;
    int referenceAngle=0;
    int firstCP_z_chest=0;
    ArrayList<Integer> x_limb_error = new ArrayList<>();
    ArrayList<Integer> y_limb_error = new ArrayList<>();
    ArrayList<Integer> z_limb_error = new ArrayList<>();
    ArrayList<Integer> x_chest_error = new ArrayList<>();
    ArrayList<Integer> y_chest_error = new ArrayList<>();
    ArrayList<Integer> z_chest_error = new ArrayList<>();
    AtomicBoolean correctPosition = new AtomicBoolean();
    AtomicBoolean record_completed = new AtomicBoolean(true);
    AtomicBoolean test_pressed = new AtomicBoolean(false);
    AtomicBoolean condition1 = new AtomicBoolean(false);
    AtomicBoolean condition2 = new AtomicBoolean(false);
    boolean state_x=false, state_y=false, state_z=false;

    private int[] x_fifo_arm = new int[]{0, 0, 0, 0, 0};
    private int[] y_fifo_arm = new int[]{0, 0, 0, 0, 0};
    private int[] z_fifo_arm = new int[]{0, 0, 0, 0, 0};
    private int[] x_fifo_chest = new int[]{0, 0, 0, 0, 0};
    private int[] y_fifo_chest = new int[]{0, 0, 0, 0, 0};
    private int[] z_fifo_chest = new int[]{0, 0, 0, 0, 0};
    private int[] dom_data_arm = new int[5];
    private int[] dom_data_chest = new int[5];
    int reset_counter = 0;
    int startDelayCounter = 41;
    boolean start_command = true;
    ExerciseModel selected_model = new ExerciseModel();
    ExerciseInfoModel selected_info_model = new ExerciseInfoModel();
    CountDownTimer mStartTimer, mTrueTimer, mFastTimer, mWarningTimer;
    String start_state = "", fast_state = "", warning_message= "";
    int bg_start_state = 0, speed_state =0;
    int limb_state = 0; //1 false, 2 true
    int chest_state = 0;
    Runnable updateRunnableStart, updateRunnableTrue, updateRunnableFast, updateRunnableWarning;
    private final Handler myHandler = new Handler();
    TextView time_tv;
    Runnable updateTime;
    Runnable updateSuccessful;
    boolean bt_connected = false;
    int exercise_id = 0;
    Runnable updateSeekbar;
    int sb_progress = 0;
    int sb_threshold = 0;
    int sb_start = 33;
    double sb_ratio=0;
    TextToSpeech textToSpeech;
    private ClipDrawable mImageDrawable;
    private ImageView progressbar_iv;
    Dialog dialog_warning;
    ProgressDialog pd;
    /*----------------SESSIONS----------------*/
    int session_fast;
    int session_slow;
    int session_expected;
    int session_succesful=0;
    int session_time=0;
    HashMap<String,Integer> session_wrong;
    /*--------------NOTIFICATIONS-------------*/
    CircleProgressbar limb_pv, chest_pv, speed_pv;
    TextView completed_ratio_tv;
    int accuracy_limb = 4;
    int accuracy_chest = 4;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG,"onCreateBeforeLayout");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exercise);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        initializeViews();
        dialog_warning = new Dialog(BluetoothActivity.this);
        sensor1 = new AccelerometerDataClass(1);
        sensor2 = new AccelerometerDataClass(2);
        checkPointsArm = new AccelerometerDataClass(3);
        checkPointsChest = new AccelerometerDataClass(4);
        patient1 = new AccelerometerDataClass(5);
        patient2 = new AccelerometerDataClass(6);
        temp1 = new AccelerometerDataClass(7);
        temp2 = new AccelerometerDataClass(8);
        Log.d(TAG,"onCreateAfterLayout");
        mStartTimer = new CountDownTimer(1000, 100) {
            @Override
            public void onTick(long millisUntilFinished) {

            }

            @Override
            public void onFinish() {
                start_state = "";
                bg_start_state = 0;
                myHandler.post(updateRunnableStart);
            }
        };
        mTrueTimer = new CountDownTimer(1800, 100) {
            @Override
            public void onTick(long millisUntilFinished) {

            }

            @Override
            public void onFinish() {
                limb_state = 0;
                chest_state = 0;
                myHandler.post(updateRunnableTrue);
            }
        };
        mFastTimer = new CountDownTimer(1800, 100) {
            @Override
            public void onTick(long millisUntilFinished) {

            }

            @Override
            public void onFinish() {
                fast_state = "";
                speed_state = 0;
                myHandler.post(updateRunnableFast);
            }
        };
        mWarningTimer = new CountDownTimer(1000, 100) {
            @Override
            public void onTick(long millisUntilFinished) {

            }

            @Override
            public void onFinish() {
                warning_message = "";
                dialog_warning.dismiss();
                //myHandler.post(updateRunnableWarning);
            }
        };
        initializeLists(2);
        Log.d(TAG,"BeforeAdapter");
        BA = BluetoothAdapter.getDefaultAdapter();
        Log.d(TAG,"AfterAdapter");
        Intent intent = getIntent();
        if (null != intent) {
            Bundle bundle = intent.getExtras();
            String json = bundle.getString("model","");
            String info_json = bundle.getString("info_model","");
            exercise_id = bundle.getInt("exercise_id",1);

            if(!json.equals("")){
                Type type = new TypeToken<ExerciseModel>() {}.getType();
                selected_model = gson.fromJson(json, type);
                String str = "0/"+String.valueOf(selected_model.getRepeat());
                completed_ratio_tv.setText(str);
                //getReferenceData();
            }
            if(!info_json.equals("")){
                Type type = new TypeToken<ExerciseInfoModel>() {}.getType();
                selected_info_model = gson.fromJson(info_json, type);
                try {
                    String gif_str = selected_info_model.getGif_id()+".gif";
                    File gifFile = new File(getFilesDir(),gif_str);
                    GifDrawable gifFromFile = new GifDrawable(gifFile);
                    //GifDrawable gifFromResource = new GifDrawable( getResources(), R.drawable.gif_name);
                    imageView.setImageDrawable(gifFromFile);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            initializeSessionVariables();
            String unsaved_id = getFromPref(Constants.PREF_UNSAVED_EXERCISE_ID,"");
            if(unsaved_id.equals(selected_model.get_id())){
                showUnsavedExerciseAlertDialog();
            }
        }

        /*record_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(record_button.isChecked()){
                    startRecord();
                }else{
                    stopRecord();
                }
            }
        }); */

        action_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch(action_state){
                    case 0:
                        action_state=1;
                        action_button.setImageResource(R.drawable.ic_action_pause);
                        startRecord();
                        break;
                    case 1:
                        action_state=2;
                        action_button.setImageResource(R.drawable.ic_action_play);
                        stopRecord();
                        break;
                    case 2:
                        action_state=3;
                        action_button.setImageResource(R.drawable.ic_action_pause);
                        String str = String.valueOf(session_succesful)+"/"+String.valueOf(selected_model.getRepeat());
                        completed_ratio_tv.setText(str);
                        test_pressed.set(true);
                        myHandler.postDelayed(updateTime,1000);
                        break;
                    case 3:
                        exercise_break = true;
                        showBreakDialog();
                        break;
                }
            }
        });

        updateRunnableStart = new Runnable() {
            public void run() {
                //call the activity method that updates the UI
                updateStartUI();
            }
        };
        updateRunnableTrue = new Runnable() {
            public void run() {
                //call the activity method that updates the UI
                updateTrueUI();
            }
        };
        updateRunnableFast = new Runnable() {
            public void run() {
                //call the activity method that updates the UI
                updateFastUI();
            }
        };
        updateTime = new Runnable() {
            public void run() {
                //call the activity method that updates the UI
                if(!exercise_break){
                    session_time++;
                }
                String time = countToTime(session_time);
                time_tv.setText(time);
                myHandler.postDelayed(this,1000);
                int e_limb = session_wrong.get("limb");
                int e_chest = session_wrong.get("chest");
                int e_both = session_wrong.get("both");
                int total_wrong = e_limb+e_chest+e_both;
                Log.d("SessionDebug","Correct:"+session_succesful+" "+"Fast:"+session_fast+" "+"Expected:"+session_expected+" "+"Slow:"+session_slow);
                Log.d("SessionDebug","Wrong:"+total_wrong+" "+"Limb:"+e_limb+" "+"Chest:"+e_chest+" "+"Both:"+e_both);
            }
        };

        updateSuccessful = new Runnable() {
            @Override
            public void run() {
                String str = String.valueOf(session_succesful)+"/"+selected_model.getRepeat();
                completed_ratio_tv.setText(str);
                if(session_succesful==selected_model.getRepeat()){
                    imageView.performClick();
                }
            }
        };

        updateRunnableWarning = new Runnable() {
            @Override
            public void run() {
                //exercise_warning_message_tv.setText(warning_message);
                dialog_warning.setContentView(R.layout.exercise_warning_dialog);
                TextView textView = (TextView)dialog_warning.findViewById(R.id.warning_tv);
                textView.setText(warning_message);
                dialog_warning.show();
            }
        };

        updateSeekbar = new Runnable() {
            @Override
            public void run() {
                mImageDrawable.setLevel((sb_start+sb_progress)*10000/180);
            }
        };

        textToSpeech =new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if (status == TextToSpeech.SUCCESS) {

                    Locale locale = new Locale("tr", "TR");
                    int	result = textToSpeech.setLanguage(locale);



                    if (result == TextToSpeech.LANG_MISSING_DATA
                            || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                        Log.e("TTS", "This Language is not supported");
                    }

                } else {
                    Log.e("TTS", "Initilization Failed!");
                }
            }
        });
    }

    void showBreakDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.continue_to_exercise)
                .setCancelable(false)
                .setNegativeButton(R.string.no_finish_exercise, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                        action_state=4;
                        action_button.setImageResource(R.drawable.ic_action_play);
                        test_pressed.set(false);
                        myHandler.removeCallbacks(updateTime);
                        addSessionToExercise(selected_model.get_id());
                    }
                })
                .setPositiveButton(R.string.yes_continue, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                        exercise_break = false;
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();


    }

    String countToTime(int count){
        int min, sec;
        min = count/60;
        sec = count%60;
        String str = String.format("%02d:%02d", min, sec);
        return str;
    }

    void initializeSessionVariables(){
        session_fast = 0;
        session_slow = 0;
        session_expected = 0;
        session_succesful = 0;
        session_wrong = new HashMap<>();
        session_wrong.put("limb",0);
        session_wrong.put("both",0);
        session_wrong.put("chest",0);
    }
    void initializeViews(){
        imageView = (GifImageView) findViewById(R.id.gif_imageview);
        exercise_start_message_tv = (TextView) findViewById(R.id.exercise_start_message_tv);
        //exercise_fast_message_tv = (TextView) findViewById(R.id.exercise_fast_message_tv);
        action_button = (ImageView) findViewById(R.id.exercise_action_button);
        time_tv = (TextView) findViewById(R.id.exercise_time_tv);
        progressbar_iv = (ImageView) findViewById(R.id.bt_progressbar);
        mImageDrawable = (ClipDrawable) progressbar_iv.getDrawable();
        mImageDrawable.setLevel(0);
        limb_pv  = (CircleProgressbar) findViewById(R.id.exercise_limb_pv);
        chest_pv = (CircleProgressbar) findViewById(R.id.exercise_chest_pv);
        speed_pv = (CircleProgressbar) findViewById(R.id.exercise_speed_pv);
        completed_ratio_tv = (TextView) findViewById(R.id.exercise_completed_ratio_tv);
    }
    void addLastof5(int[] fifo, int x){
        fifo[0] = fifo[1];
        fifo[1] = fifo[2];
        fifo[2] = fifo[3];
        fifo[3] = fifo[4];
        fifo[4] = x;
    }
    void setDominantDataArm(){
        if(dominant_axis_arm == X_AXIS){
            dom_data_arm = x_fifo_arm;
            referenceAngle = checkPointsArm.getDataSet(0)[0];
        }else if(dominant_axis_arm == Y_AXIS){
            dom_data_arm = y_fifo_arm;
            referenceAngle = checkPointsArm.getDataSet(0)[1];
        }else if(dominant_axis_arm == Z_AXIS){
            dom_data_arm = z_fifo_arm;
            referenceAngle = checkPointsArm.getDataSet(0)[2];
        }
    }

    void setDominantDataChest(){
        if(dominant_axis_chest == X_AXIS){
            dom_data_chest = x_fifo_chest;
        }else if(dominant_axis_chest == Y_AXIS){
            dom_data_chest = y_fifo_chest;
        }else if(dominant_axis_chest == Z_AXIS){
            dom_data_chest = z_fifo_chest;
        }
    }

    void updateStartUI(){
        exercise_start_message_tv.setText(start_state);
        exercise_start_message_tv.setBackgroundResource(bg_start_state);
    }
    void updateTrueUI(){
        if(limb_state==2){
            //limb_layout.setBackgroundResource(R.drawable.selection_green_bg);
            changeProgressView(limb_pv, getResources().getColor(R.color.colorGreenTransparent), 25*accuracy_limb,1000);
        }else if ( limb_state ==1){
            //limb_layout.setBackgroundResource(R.drawable.selection_red_bg);
            changeProgressView(limb_pv, Color.RED,100,1000);
        }else {
            //limb_layout.setBackgroundResource(R.drawable.selection_bg);
            changeProgressView(limb_pv, getResources().getColor(R.color.colorOrange),0,200);
        }
        if(chest_state == 2){
            //chest_layout.setBackgroundResource(R.drawable.selection_green_bg);
            changeProgressView(chest_pv, getResources().getColor(R.color.colorGreenTransparent),  25*accuracy_chest,1000);
        }else if(chest_state == 1) {
            //chest_layout.setBackgroundResource(R.drawable.selection_red_bg);
            changeProgressView(chest_pv, Color.RED,100,1000);
        }else {
            //chest_layout.setBackgroundResource(R.drawable.selection_bg);
            changeProgressView(chest_pv, getResources().getColor(R.color.colorOrange),0,200);
        }

        if(limb_state==2 && chest_state==2) {
            textToSpeech.speak(getResources().getString(R.string.info_true), TextToSpeech.QUEUE_FLUSH, null);
        }

        if(limb_state==1 && chest_state==1) {
            textToSpeech.speak(getResources().getString(R.string.info_false), TextToSpeech.QUEUE_FLUSH, null);
        }

        if(limb_state==1 && chest_state==2) {
            textToSpeech.speak(getResources().getString(R.string.info_false_limb), TextToSpeech.QUEUE_FLUSH, null);
        }

        if(limb_state==2 && chest_state==1) {
            textToSpeech.speak(getResources().getString(R.string.info_false_chest), TextToSpeech.QUEUE_FLUSH, null);
        }
    }
    void updateFastUI(){
        //speed_tv.setText(fast_state);
        if(speed_state==1){
            changeProgressView(speed_pv,getResources().getColor(R.color.colorPhyshome),100,1000);
        }else if(speed_state==2){
            changeProgressView(speed_pv,Color.YELLOW,100,1000);
        }else if(speed_state==3){
            changeProgressView(speed_pv,Color.RED,100,1000);
        }else if(speed_state==4){
            changeProgressView(speed_pv,getResources().getColor(R.color.colorGreenTransparent),100,1000);
        }else{
            changeProgressView(speed_pv,getResources().getColor(R.color.colorOrange),0,200);
        }
        if(!fast_state.equals(""))
        {
            textToSpeech.speak(fast_state.toLowerCase(), TextToSpeech.QUEUE_FLUSH, null);
        }
    }

    int defineCPs(AccelerometerDataClass sensor_record,int step, AccelerometerDataClass CP, int axis){
        Log.d("ControlDebug",sensor_record.getId()+"  "+sensor_record.getX_values().toString());
        Log.d("ControlDebug",sensor_record.getId()+"  "+sensor_record.getY_values().toString());
        Log.d("ControlDebug",sensor_record.getId()+"  "+sensor_record.getZ_values().toString());
        int length_x_rec = sensor_record.getX_values().size();
        int length_y_rec = sensor_record.getY_values().size();
        int length_z_rec = sensor_record.getZ_values().size();
        int inc = -1;
        Log.d("ControlDebug","\t\t\t\t\t "+"length_of_x_rec:  "+ length_x_rec);
        for(int i=0; i<30;i++){
            if(step*i >= length_x_rec){
                int extra = Math.abs(step*(i-1) - length_x_rec);

                if(extra > 3)
                {
                    if(axis == X_AXIS)
                        CP.addAll(sensor_record.getX_values().get(length_x_rec-1),sensor_record.getY_values().get(length_y_rec-1),sensor_record.getZ_values().get(length_z_rec-1),isIncreasing(sensor_record.getX_values().subList(length_x_rec-5,length_x_rec-1)));
                    else if(axis == Y_AXIS)
                        CP.addAll(sensor_record.getX_values().get(length_x_rec-1),sensor_record.getY_values().get(length_y_rec-1),sensor_record.getZ_values().get(length_z_rec-1),isIncreasing(sensor_record.getY_values().subList(length_y_rec-5,length_y_rec-1)));
                    else if(axis == Z_AXIS)
                        CP.addAll(sensor_record.getX_values().get(length_x_rec-1),sensor_record.getY_values().get(length_y_rec-1),sensor_record.getZ_values().get(length_z_rec-1),isIncreasing(sensor_record.getZ_values().subList(length_z_rec-5,length_z_rec-1)));

                    return i+1;
                }
                else
                    return i;
            }
            else if(i==0){
                CP.addAll(sensor_record.getX_values().get(i*step),sensor_record.getY_values().get(i*step),sensor_record.getZ_values().get(i*step),isIncreasing(sensor_record.getX_values().subList(0,3))); //TODO: dominant_axis e gÃ¶re inc i hesapla
            }
            else{
                if(axis == X_AXIS){
                    if(i*step+2 < length_x_rec ){
                        //son eleman dahil mi?
                        inc = isIncreasing(sensor_record.getX_values().subList(i*step-2,i*step+3));
                    }else if(i*step+1 < length_x_rec ){
                        //son eleman dahil mi?
                        inc = isIncreasing(sensor_record.getX_values().subList(i*step-3,i*step+2));
                    }else if(i*step < length_x_rec ){
                        //son eleman dahil mi?
                        inc = isIncreasing(sensor_record.getX_values().subList(i*step-4,i*step+1));
                    }
                }else if(axis == Y_AXIS){
                    if(i*step+2 < length_y_rec ){
                        //son eleman dahil mi?
                        inc = isIncreasing(sensor_record.getY_values().subList(i*step-2,i*step+3));
                    }else if(i*step+1 < length_y_rec ){
                        //son eleman dahil mi?
                        inc = isIncreasing(sensor_record.getY_values().subList(i*step-3,i*step+2));
                    }else if(i*step < length_y_rec ){
                        //son eleman dahil mi?
                        inc = isIncreasing(sensor_record.getY_values().subList(i*step-4,i*step+1));
                    }
                }else if(axis == Z_AXIS){
                    if(i*step+2 < length_z_rec ){
                        //son eleman dahil mi?
                        inc = isIncreasing(sensor_record.getZ_values().subList(i*step-2,i*step+3));
                    }else if(i*step+1 < length_z_rec ){
                        //son eleman dahil mi?
                        inc = isIncreasing(sensor_record.getZ_values().subList(i*step-3,i*step+2));
                    }else if(i*step < length_z_rec ){
                        //son eleman dahil mi?
                        inc = isIncreasing(sensor_record.getZ_values().subList(i*step-4,i*step+1));
                    }
                }
                CP.addAll(sensor_record.getX_values().get(i*step),sensor_record.getY_values().get(i*step),sensor_record.getZ_values().get(i*step),inc);
            }
        }
        return 0;
    }

    int isIncreasing(List<Integer> list){
        int length = list.size();
        int count = 0;
        int stationaryCounter=0;
        for(int i=0;i<length-1;i++){
            if(list.get(i)<list.get(i+1)){
                count++;
            }
            if (Math.abs(list.get(i+1) - list.get(i)) <= 1)
            {
                stationaryCounter++;
            }
        }
        if (stationaryCounter > length-2)
            return 0;
        else if( count >= length -2){
            return 1;
        }else{
            return -1;
        }
    }
    int isIncreasing(int[] array){
        int length = array.length;
        int count = 0;
        int stationaryCounter=0;
        for(int i=0;i<length-1;i++){
            if(array[i]<array[i+1]){
                count++;
            }
            if (Math.abs(array[i+1] - array[i]) <= 1)
            {
                stationaryCounter++;
            }
        }
        if (stationaryCounter > length-2)
            return 0;
        else if( count >= length -2){
            return 1;
        }else{
            return -1;
        }
    }

    void checkCorrectPositionArm(){
        ArrayList<Integer> cp_control = new ArrayList<>();
        if (dominant_axis_arm == 0)
            cp_control = checkPointsMotionArm_X;
        else if (dominant_axis_arm == 1)
            cp_control = checkPointsMotionArm_Y;
        else if (dominant_axis_arm == 2)
            cp_control = checkPointsMotionArm_Z;

        if(checkPointsArm.getDataSet(0)[0]-radiusArm.get(0).get(0) < x_fifo_arm[4] && x_fifo_arm[4] < checkPointsArm.getDataSet(0)[0]+radiusArm.get(0).get(0) && !(cp_control.get(1)==1 || cp_control.get(2)==1 || cp_control.get(3)==1)){
            if(checkPointsArm.getDataSet(0)[1]-radiusArm.get(0).get(1) < y_fifo_arm[4] && y_fifo_arm[4] < checkPointsArm.getDataSet(0)[1]+radiusArm.get(0).get(1)){
//                if(checkPointsArm.getDataSet(0)[2]-radiusArm.get(0).get(2) < z_fifo_arm[4] && z_fifo_arm[4] < checkPointsArm.getDataSet(0)[2]+radiusArm.get(0).get(2)){
                int initial_ref_Z_diff = Math.abs(exceeded_180_correction(checkPointsChest.getDataSet(0)[2] - checkPointsArm.getDataSet(0)[2]));
                int current_test_Z_diff = Math.abs(exceeded_180_correction(z_fifo_chest[4] - z_fifo_arm[4]));
                int difference = Math.abs(exceeded_180_correction(initial_ref_Z_diff - current_test_Z_diff));

                if(difference <= 40){
                    Log.d("ControlDebug","\tCORRRRRRECT ARMMMM");
                    initializeAll();
                    checkPointsMotionArm.set(0,1);
                    checkPointsMotionArm_X.set(0,1);
                    checkPointsMotionArm_Y.set(0,1);
                    checkPointsMotionArm_Z.set(0,1);
                }
                else{
                    checkPointsMotionArm.set(0,0);
                    checkPointsMotionChest.set(0,0);
                }


//                }
            }
        }
    }
    void checkCorrectPositionChest(){
        ArrayList<Integer> cp_control = new ArrayList<>();
        if (dominant_axis_arm == 0)
            cp_control = checkPointsMotionArm_X;
        else if (dominant_axis_arm == 1)
            cp_control = checkPointsMotionArm_Y;
        else if (dominant_axis_arm == 2)
            cp_control = checkPointsMotionArm_Z;

        Log.d("CONTROLDEBUG: ", cp_control.toString());

        if(checkPointsChest.getDataSet(0)[0]-radiusChest.get(0).get(0) < x_fifo_chest[4] && x_fifo_chest[4] < checkPointsChest.getDataSet(0)[0]+radiusChest.get(0).get(0) && !(cp_control.get(1)==1 || cp_control.get(2)==1 || cp_control.get(3)==1)){
            if(checkPointsChest.getDataSet(0)[1]-radiusChest.get(0).get(1) < y_fifo_chest[4] && y_fifo_chest[4] < checkPointsChest.getDataSet(0)[1]+radiusChest.get(0).get(1)){
//                if(checkPointsChest.getDataSet(0)[2]-radiusChest.get(0).get(2) < z_fifo_chest[4] && z_fifo_chest[4] < checkPointsChest.getDataSet(0)[2]+radiusChest.get(0).get(2)){
                    Log.d("ControlDebug","\tCORRRRRRECT CHESTTT");
                    checkPointsMotionChest.set(0,1);
                    checkPointsMotionChest_X.set(0,1);
                    checkPointsMotionChest_Y.set(0,1);
                    checkPointsMotionChest_Z.set(0,1);
//                }
            }
        }
    }

    int sign(int num){
        if(num==0){
            return 0;
        }else if(num>0){
            return 1;
        }else{
            return -1;
        }
    }

    boolean is_motion_completed(){
        int c = 0;
        if (dominant_axis_arm == 0){
            for (int n=0;n<checkPointsNumArm;n++){
                if (checkPointsMotionArm_X.get(n) == 1){
                    c++;
                }
            }
        }
        if (dominant_axis_arm == 1){
            for (int n=0;n<checkPointsNumArm;n++){
                if (checkPointsMotionArm_Y.get(n) == 1){
                    c++;
                }
            }
        }
        if (dominant_axis_arm == 2){
            for (int n=0;n<checkPointsNumArm;n++){
                if (checkPointsMotionArm_Z.get(n) == 1){
                    c++;
                }
            }
        }

        return c >= 4; // return true if c>= 4 else false

    }

    int get_region(int angle){
        if (0 <= angle && angle <= 180)
            return 1;
        else if (-180 <= angle && angle <= 0)
            return 2;
        else
            return -1;
    }

    int exceeded_180_correction(int data){
        if (data > 180)
            data -=360;
        else if (data < -180)
            data +=360;
        return data;
    }

    boolean circular_interval_control(int data, ArrayList<Integer> bounds){
        int region_lower = get_region(bounds.get(0));
        int region_higher = get_region(bounds.get(1));
        int region_data = get_region(data);

        if (region_lower == 1 && region_higher == 1){
            if (region_data == 1){
                return bounds.get(0) <= data && data <= bounds.get(1);
            }
            else if (region_data == 2)
                return false;
        }
        else if (region_lower == 2 && region_higher == 2){
            if (region_data == 1)
                return false;
            else if (region_data == 2)
                return bounds.get(0) <= data && data <= bounds.get(1);
        }
        else if (region_lower == 1 && region_higher == 2){
            if (region_data == 1)
                return data >= bounds.get(0);
            else if (region_data == 2)
                return data <= bounds.get(1);
        }
        else if (region_lower == 2 && region_higher == 1){
            if (region_data == 1)
                return data <= bounds.get(1);
            else if (region_data == 2)
                return data >= bounds.get(0);
        }
        else
            return false;

        return false;
    }

    boolean isInInterval(int curr, AccelerometerDataClass CP, ArrayList<ArrayList<Integer>> r, int wCP, int ax){
//        Log.d("Control-Debug:\t", "wCP: " + whichCpArm + " | " + "current: " +  curr + " | " + "cP: " + CP.getDataSet(wCP)[ax] + " | " +"radius: " + r);
        ArrayList<Integer> bounds = new ArrayList<>();
        bounds.add(CP.getDataSet(wCP)[ax]-r.get(wCP-1).get(ax));
        bounds.add(CP.getDataSet(wCP)[ax]+r.get(wCP).get(ax));

        bounds.set(0,exceeded_180_correction(bounds.get(0)));
        bounds.set(1,exceeded_180_correction(bounds.get(1)));

        return circular_interval_control(curr, bounds);
    }

    void isInInterval_Loop_limb(AccelerometerDataClass CP, ArrayList<Integer> relatedAxisArray, int curr, ArrayList<ArrayList<Integer>> rad, int axis, int[] dom_data)
    {
        for(int i=current_checkpoint;i<checkPointsNumArm;i++){
            if (isInInterval(curr,CP,rad,i,axis)){
                if (CP.getDataSet(i)[3] == isIncreasing(dom_data)){
                    relatedAxisArray.set(i,1);
                }
            }

        }
    }

    void isInInterval_Loop_chest(AccelerometerDataClass CP, ArrayList<Integer> relatedAxisArray, int curr, ArrayList<ArrayList<Integer>> rad, int axis, int[] dom_data)
    {
        for(int i=last_passed_checkpoint;i<checkPointsNumArm;i++)
        {
            if (relatedAxisArray.get(i) == 0)
            {
                if (i == 0)
                    i = 1;
                if (CP.getDataSet(i)[axis] - rad.get(i-1).get(axis) < curr && curr < CP.getDataSet(i)[axis] + rad.get(i).get(axis))
                {
                    if (CP.getDataSet(i)[3] == isIncreasing(dom_data))
                    {
                        relatedAxisArray.set(i,1);
                    }
                }
            }

        }
    }

    void calculateTestPeak(int[] domData)
    {
        int temp = domData[4] - referenceAngle;

        if (temp > 180)
            temp -= 360;
        else if (temp < -180)
            temp += 360;

        if (limbMotionSign == -1)
        {
            if (domData[4] > referenceAngle && sign(domData[4]) == sign(referenceAngle))
                temp = 0;
        }
        else if (limbMotionSign == 1)
        {
            if (domData[4] < referenceAngle && sign(domData[4]) == sign(referenceAngle))
                temp = 0;
        }

        temp = Math.abs(temp);

        if (temp > testPeak)
            testPeak = temp;

        Log.d("CONTROLDEBUG:","\t\t\t\t\t\t\t\tTESTPEAK:    " + testPeak);

    }

    void z_difference_control_loop(){

        for(int i=current_checkpoint;i<checkPointsNumArm;i++){

            int current_ref_z_diff = Math.abs(exceeded_180_correction(checkPointsChest.getZ_values().get(i) - checkPointsArm.getZ_values().get(i)));
            int current_test_z_diff = Math.abs(exceeded_180_correction(z_fifo_chest[4] - z_fifo_arm[4]));

//            Log.d("CONTROLDEBUG: ", "CHECKPOINT:\t" + i);

            if (-25 <= current_ref_z_diff - current_test_z_diff && current_ref_z_diff - current_test_z_diff <= 25){
                checkPointsMotionArm_Z.set(i,1);
                checkPointsMotionChest_Z.set(i,1);
                z_limb_error.set(i,1000);
                z_chest_error.set(i,1000);
//                Log.d("CONTROLDEBUG:", "TRUE Z " + i);
            }
            else{
                if (Math.abs(exceeded_180_correction(firstCP_z_chest - z_fifo_chest[4])) <= 30){
                    z_limb_error.set(i,Math.abs(current_ref_z_diff - current_test_z_diff));
                    checkPointsMotionArm_Z.set(i,0);
                    checkPointsMotionChest_Z.set(i,1);
//                z_chest_error.set(current_checkpoint,1000);
//                    Log.d("CONTROLDEBUG:", "Z ERROR ARM " + current_checkpoint);

                }
                else{
                    z_chest_error.set(i,Math.abs(current_ref_z_diff - current_test_z_diff));
                    checkPointsMotionChest_Z.set(i,0);
                    checkPointsMotionArm_Z.set(i,1);
//                z_limb_error.set(current_checkpoint,1000);
//                    Log.d("CONTROLDEBUG:", "Z ERROR CHEST " + i);
                }
            }

        }


    }

    void limb()
    {
        for (int i=current_checkpoint; i < checkPointsNumArm; i++)
        {
            checkPointsMotionArm_X.set(i,0);
            checkPointsMotionArm_Y.set(i,0);
            checkPointsMotionArm_Z.set(i,0);
            checkPointsMotionChest_Z.set(i,0);
        }

        isInInterval_Loop_limb(checkPointsArm,checkPointsMotionArm_X,x_fifo_arm[4],radiusArm,0,dom_data_arm);
        isInInterval_Loop_limb(checkPointsArm,checkPointsMotionArm_Y,y_fifo_arm[4],radiusArm,1,dom_data_arm);
//        isInInterval_Loop_limb(checkPointsArm,checkPointsMotionArm_Z,z_fifo_arm[4],radiusArm,2,dom_data_arm);



        if (checkPointsMotionArm_X.get(1) == 1 && current_checkpoint == 1)
            firstCP_z_chest = z_fifo_chest[4];

        z_difference_control_loop();

        boolean cp_passed = false;

        for (int n=current_checkpoint; n < checkPointsNumArm; n++){
            if (checkPointsMotionArm_X.get(n) == 1 && !((n == checkPointsNumArm-1 || n == checkPointsNumArm-2) && checkPointsMotionArm_X.get(1) == 0)){ //TODO: check this if condition again
                x_limb_error.set(n,1000);
                last_passed_checkpoint = current_checkpoint;
                current_checkpoint = n+1;
                globalTestCounter += time2nextCPArm;
                time2nextCPArm = 0;
                cp_passed = true;
                if (checkPointsMotionArm_Y.get(n) == 1 && checkPointsMotionArm_Z.get(n) == 1) {
                    checkPointsMotionArm.set(n, 1);
                    y_limb_error.set(n, 1000);
                }
                else{
                    if (checkPointsMotionArm_Y.get(n) == 0)
                        y_limb_error.set(n,y_fifo_arm[4]);
                }
            }
            else
                x_limb_error.set(n,x_fifo_arm[4]);
        }

        if (!cp_passed)
        {
            time2nextCPArm++;
        }

    }

    void chest()
    {
        for (int i=last_passed_checkpoint; i<checkPointsNumArm; i++)
        {
            checkPointsMotionChest_X.set(i,0);
            checkPointsMotionChest_Y.set(i,0);
//            checkPointsMotionChest_Z.set(i,0);
        }

        isInInterval_Loop_chest(checkPointsChest,checkPointsMotionChest_X,x_fifo_chest[4],radiusChest,0,dom_data_chest);
        isInInterval_Loop_chest(checkPointsChest,checkPointsMotionChest_Y,y_fifo_chest[4],radiusChest,1,dom_data_chest);
//        isInInterval_Loop_chest(checkPointsChest,checkPointsMotionChest_Z,z_fifo_chest[4],radiusChest,2,dom_data_chest);

        for (int n=current_checkpoint; n<checkPointsNumArm; n++)
        {
            if (checkPointsMotionChest_X.get(n) == 1){
                x_chest_error.set(n,1000);
                if (checkPointsMotionArm_Y.get(n) == 1 && checkPointsMotionArm_Z.get(n) == 1)
                {
                    checkPointsMotionChest.set(n,1);
                    y_chest_error.set(n,1000);
                }
                else{
                    if (checkPointsMotionChest_Y.get(n) == 0)
                        y_chest_error.set(n,y_fifo_chest[4]);
                }
            }
            else
                x_chest_error.set(n,x_fifo_chest[4]);

        }

    }

    void isInCheckpoint_Any()
    {
        limb();
        chest();

        Log.d("CONTROLDEBUG", checkPointsMotionArm_X.toString());
        Log.d("CONTROLDEBUG", checkPointsMotionArm_Y.toString());
        Log.d("CONTROLDEBUG", checkPointsMotionArm_Z.toString());
        Log.d("CONTROLDEBUG", checkPointsMotionChest_X.toString());
        Log.d("CONTROLDEBUG", checkPointsMotionChest_Y.toString());
        Log.d("CONTROLDEBUG", checkPointsMotionChest_Z.toString());
    }
    int checkError()
    {
        ArrayList<Integer> limb_error_count = new ArrayList<>();
        ArrayList<Integer> limb_correct_count = new ArrayList<>();
        ArrayList<Integer> chest_error_count = new ArrayList<>();
        ArrayList<Integer> chest_correct_count = new ArrayList<>();
        for (int n=0;n<3;n++){
            limb_error_count.add(0);
            limb_correct_count.add(0);
            chest_error_count.add(0);
            chest_correct_count.add(0);
        }
        boolean limb_error = false;
        boolean chest_error = false;
        boolean limb_error_x = false;
        boolean limb_error_y = false;
        boolean limb_error_z = false;
        boolean chest_error_x = false;
        boolean chest_error_y = false;
        boolean chest_error_z = false;
        int diff = 0;
        Log.d("CONTROLDEBUG", x_limb_error.toString());
        Log.d("CONTROLDEBUG", y_limb_error.toString());
        Log.d("CONTROLDEBUG", z_limb_error.toString());
        Log.d("CONTROLDEBUG", x_chest_error.toString());
        Log.d("CONTROLDEBUG", y_chest_error.toString());
        Log.d("CONTROLDEBUG", z_chest_error.toString());

        accuracy_limb = 4;
        accuracy_chest = 4;
        int error_limb_counter = 0;
        int error_chest_counter = 0;

        for(int i=0;i<checkPointsNumArm;i++){
            if((checkPointsMotionArm_X.get(i) == 1) && (checkPointsMotionArm_Y.get(i) == 1) && (checkPointsMotionArm_Z.get(i) == 1)){
                checkPointsMotionArm.set(i,1);
            }
            else{
                checkPointsMotionArm.set(i,0);
                error_limb_counter++;
            }
            if((checkPointsMotionChest_X.get(i) == 1) && (checkPointsMotionChest_Y.get(i) == 1) && (checkPointsMotionChest_Z.get(i) == 1)){
                checkPointsMotionChest.set(i,1);
            }
            else{
                checkPointsMotionChest.set(i,0);
                error_chest_counter++;
            }
        }

        double accuracy_percentage_limb = ((double)(checkPointsNumArm-error_limb_counter)/(double)(checkPointsNumArm))*100;
        double accuracy_percentage_chest = ((double)(checkPointsNumArm-error_chest_counter)/(double)(checkPointsNumArm))*100;

        if (accuracy_percentage_limb == 100)
            accuracy_limb = 4;
        else if(75 < accuracy_percentage_limb && accuracy_percentage_limb < 100)
            accuracy_limb = 3;
        else if (50<= accuracy_percentage_limb && accuracy_percentage_limb <= 75)
            accuracy_limb = 2;
		else
            accuracy_limb = 1;

        if (accuracy_percentage_chest == 100)
            accuracy_chest = 4;
        else if(75 < accuracy_percentage_chest && accuracy_percentage_chest < 100)
            accuracy_chest = 3;
        else if (50<= accuracy_percentage_chest && accuracy_percentage_chest <= 75)
            accuracy_chest = 2;
        else
            accuracy_chest = 1;







        for (int n=0;n<x_limb_error.size();n++){
            if (x_limb_error.get(n) != 1000){
//                diff = checkPointsArm.getDataSet(n)[0] - x_limb_error.get(n);
//                diff = exceeded_180_correction(diff);
//                if (Math.abs(diff) - radiusArm.get(n).get(0) >= warningModel.getTolerance_x_limb()){
                    limb_error_count.set(0,limb_error_count.get(0)+1);
//                }
//                else
//                    limb_correct_count.set(0,limb_correct_count.get(0)+1);
            }
            else
                limb_correct_count.set(0,limb_correct_count.get(0)+1);
        }
        for (int n=0;n<y_limb_error.size();n++){
            if (y_limb_error.get(n) != 1000){
//                diff = checkPointsArm.getDataSet(n)[1] - y_limb_error.get(n);
//                diff = exceeded_180_correction(diff);
//                if (Math.abs(diff) - radiusArm.get(n).get(1) >= warningModel.getTolerance_y_limb()){
                    limb_error_count.set(1,limb_error_count.get(1)+1);
//                }
//                else
//                    limb_correct_count.set(1,limb_correct_count.get(1)+1);
            }
            else
                limb_correct_count.set(1,limb_correct_count.get(1)+1);
        }
        for (int n=0;n<z_limb_error.size();n++){
            if (z_limb_error.get(n) != 1000){
//                diff = checkPointsArm.getDataSet(n)[2] - z_limb_error.get(n);
//                diff = exceeded_180_correction(diff);
//                if (Math.abs(diff) - radiusArm.get(n).get(2) >= warningModel.getTolerance_z_limb()){
                    limb_error_count.set(2,limb_error_count.get(2)+1);
//                }
//                else
//                    limb_correct_count.set(2,limb_correct_count.get(2)+1);
            }
            else
                limb_correct_count.set(2,limb_correct_count.get(2)+1);
        }
        //-----------------------------------------chest------------------------------------
        for (int n=0;n<x_chest_error.size();n++){
            if (x_chest_error.get(n) != 1000){
//                diff = checkPointsChest.getDataSet(n)[0] - x_chest_error.get(n);
//                diff = exceeded_180_correction(diff);
//                if (Math.abs(diff) - radiusChest.get(n).get(0) >= warningModel.getTolerance_x_chest()){
                    chest_error_count.set(0,chest_error_count.get(0)+1);
//                }
//                else
//                    chest_correct_count.set(0,chest_correct_count.get(0)+1);
            }
            else
                chest_correct_count.set(0,chest_correct_count.get(0)+1);
        }
        for (int n=0;n<y_chest_error.size();n++){
            if (y_chest_error.get(n) != 1000){
//                diff = checkPointsChest.getDataSet(n)[1] - y_chest_error.get(n);
//                diff = exceeded_180_correction(diff);
//                if (Math.abs(diff) - radiusChest.get(n).get(1) >= warningModel.getTolerance_y_chest()){
                    chest_error_count.set(1,chest_error_count.get(1)+1);
//                }
//                else
//                    chest_correct_count.set(1,chest_correct_count.get(1)+1);
            }
            else
                chest_correct_count.set(1,chest_correct_count.get(1)+1);
        }
        for (int n=0;n<z_chest_error.size();n++){
            if (z_chest_error.get(n) != 1000){
//                diff = checkPointsChest.getDataSet(n)[2] - z_chest_error.get(n);
//                diff = exceeded_180_correction(diff);
//                if (Math.abs(diff) - radiusChest.get(n).get(2) >= warningModel.getTolerance_z_chest()){
                    chest_error_count.set(2,chest_error_count.get(2)+1);
//                }
//                else
//                    chest_correct_count.set(2,chest_correct_count.get(2)+1);
            }
            else
                chest_correct_count.set(2,chest_correct_count.get(2)+1);
        }
        Log.d("CONTROLDEBUG","ERROR COUNTERS:");
        Log.d("CONTROLDEBUG", limb_error_count.toString());
        Log.d("CONTROLDEBUG", limb_correct_count.toString());
        Log.d("CONTROLDEBUG", chest_error_count.toString());
        Log.d("CONTROLDEBUG", chest_correct_count.toString());



        if (limb_error_count.get(0) - limb_correct_count.get(0) > -checkPointsNumArm/2)
            limb_error_x = true;
        if (limb_error_count.get(1) - limb_correct_count.get(1) > -checkPointsNumArm/2)
            limb_error_y = true;
        if (limb_error_count.get(2) - limb_correct_count.get(2) > -checkPointsNumArm/2)
            limb_error_z = true;

        if (chest_error_count.get(0) - chest_correct_count.get(0) > -checkPointsNumArm/2)
            chest_error_x = true;
        if (chest_error_count.get(1) - chest_correct_count.get(1) > -checkPointsNumArm/2)
            chest_error_y = true;
        if (chest_error_count.get(2) - chest_correct_count.get(2) > -checkPointsNumArm/2)
            chest_error_z = true;



        if ((limb_error_x || limb_error_y) || limb_error_z)
            limb_error = true;
        if ((chest_error_x || chest_error_y) || chest_error_z)
            chest_error = true;

        if (limb_error && chest_error){
            Log.d("CONTROLDEBUG","\t\t\t\"▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀\tUZUV YANLIŞ\t\"");
            Log.d("CONTROLDEBUG","\t\t\t\"▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀\tGÖĞÜS YANLIŞ\t\"");
            mTrueTimer.cancel();
            limb_state = 1;
            chest_state = 1;
            session_wrong.put("both",session_wrong.get("both")+1);
            myHandler.post(updateRunnableTrue);
            mTrueTimer.start();

            mFastTimer.cancel();
            speed_state = 3;
            myHandler.post(updateRunnableFast);
            mFastTimer.start();

            if (limb_error_y || limb_error_z)
                checkSide();
            else if (limb_error_x)
                checkPeak();
        }
        if (!limb_error && chest_error){
            Log.d("CONTROLDEBUG","\t\t\t\"▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀\tUZUV DOĞRU\t\"");
            Log.d("CONTROLDEBUG","\t\t\t\"▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀\tGÖĞÜS YANLIŞ\t\"");

            Log.d("CONTROLDEBUG","ACCURACY LIMB:\t" + accuracy_limb);

            mTrueTimer.cancel();
            limb_state = checkPeak();
            chest_state = 1;
            session_wrong.put("chest",session_wrong.get("chest")+1);
            myHandler.post(updateRunnableTrue);
            mTrueTimer.start();

            if(selected_info_model.getError_messages()!=null){
                warning_message = selected_info_model.getError_messages()[3];
            }else{
                warning_message = "Error";
            }
            Log.d("CONTROLDEBUG", "Message:  "+ warning_message);
            mWarningTimer.cancel();
            myHandler.post(updateRunnableWarning);
            mWarningTimer.start();

            if (limb_state == 2)
                checkSpeed(false);
            else {
                mFastTimer.cancel();
                speed_state = 3;
                myHandler.post(updateRunnableFast);
                mFastTimer.start();
            }
        }
        if (limb_error && !chest_error){
            Log.d("CONTROLDEBUG","\t\t\t\"▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀\tUZUV YANLIŞ\t\"");
            Log.d("CONTROLDEBUG","\t\t\t\"▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀\tGÖĞÜS DOĞRU\t\"");

            Log.d("CONTROLDEBUG","ACCURACY CHEST:\t" + accuracy_chest);


            mTrueTimer.cancel();
            limb_state = 1;
            chest_state = 2;
            session_wrong.put("limb",session_wrong.get("limb")+1);
            myHandler.post(updateRunnableTrue);
            mTrueTimer.start();
            mFastTimer.cancel();
            speed_state = 3;
            myHandler.post(updateRunnableFast);
            mFastTimer.start();

            if (limb_error_y || limb_error_z)
                checkSide();
            else if (limb_error_x)
                checkPeak();
        }
        if (!limb_error && !chest_error){
            Log.d("CONTROLDEBUG","\t\t\t\"▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀\tUZUV DOĞRU\t\"");
            Log.d("CONTROLDEBUG","\t\t\t\"▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀\tGÖĞÜS DOĞRU\t\"");

            Log.d("CONTROLDEBUG","ACCURACY LIMB:\t" + accuracy_limb);
            Log.d("CONTROLDEBUG","ACCURACY CHEST:\t" + accuracy_chest);

            mTrueTimer.cancel();
            limb_state = checkPeak();
            chest_state = 2;
            myHandler.post(updateRunnableTrue);
            mTrueTimer.start();
            if (limb_state == 2){
                session_succesful++;
                myHandler.post(updateSuccessful);
                checkSpeed(true);
            }
            else {
                mFastTimer.cancel();
                speed_state = 3;
                myHandler.post(updateRunnableFast);
                mFastTimer.start();
            }
        }

        return 1;


    }

    int checkPeak()
    {
        if ((double)Math.abs(testPeak) < (Math.abs(peaks_Arm.get(dominant_axis_arm)) * 0.9))    //TODO: add <0.9 and 1.1> check | Check 0 to 90 and 90 to 0 case
        {
//            Log.d("CONTROLDEBUG ", "ERROR: " + warningModel.getMsg_peak_1());
            if(selected_info_model.getError_messages()!=null){
                warning_message = selected_info_model.getError_messages()[1];
            }else{
                warning_message = "Error";
            }
            Log.d("CONTROLDEBUG", "Message:  "+ warning_message);
            mWarningTimer.cancel();
            myHandler.post(updateRunnableWarning);
            mWarningTimer.start();
            return 1;
        }
        else if ((double)Math.abs(testPeak) > (Math.abs(peaks_Arm.get(dominant_axis_arm)) * 1.1))
        {
//            Log.d("CONTROLDEBUG ", "ERROR: " + warningModel.getMsg_peak_2());
            if(selected_info_model.getError_messages()!=null){
                warning_message = selected_info_model.getError_messages()[2];
            }else{
                warning_message = "Error";
            }
            Log.d("CONTROLDEBUG", "Message:  "+ warning_message);
            mWarningTimer.cancel();
            myHandler.post(updateRunnableWarning);
            mWarningTimer.start();
            return 1;
        }
        else
        {
            Log.d("CONTROLDEBUG ", "ERROR PEAK OUTER");
            return 2;
        }

    }

    void checkSpeed(boolean state)
    {
        if (globalTestCounter > (int)(globalRecordCounter*1.1))
        {
            Log.d("CONTROLDEBUG ", "ERROR: " + "YAVAŞ");
            mFastTimer.cancel();
            if(state) {
                session_slow++;
            }
            fast_state = getString(R.string.info_slow);
            speed_state = 2;
            myHandler.post(updateRunnableFast);
            mFastTimer.start();
        }
        else if (globalTestCounter < (int)(globalRecordCounter*0.7))
        {
            Log.d("CONTROLDEBUG ", "ERROR: " + "HIZLI");
            mFastTimer.cancel();
            if(state) {
                session_fast++;
            }
            fast_state = getString(R.string.info_fast);
            speed_state = 1;
            myHandler.post(updateRunnableFast);
            mFastTimer.start();
        }
        else
        {
            Log.d("CONTROLDEBUG ", "ERROR: " + "NORMAL");
            mFastTimer.cancel();
            if(state) {
                session_expected++;
            }
            speed_state = 4;
            myHandler.post(updateRunnableFast);
            mFastTimer.start();
        }
    }

    void checkSide()
    {
        //warning_message = warningModel.getMsg_limb_1();
        if(selected_info_model.getError_messages()!=null){
            warning_message = selected_info_model.getError_messages()[0];
        }else{
            warning_message = "Error";
        }
        Log.d("CONTROLDEBUG", "Message:  "+ warning_message);
        mWarningTimer.cancel();
        myHandler.post(updateRunnableWarning);
        mWarningTimer.start();
    }

    void initializeAll(){
        for(int i=0;i<checkPointsNumArm;i++){
            checkPointsMotionArm.set(i,0);
            checkPointsMotionArm_X.set(i,0);
            checkPointsMotionArm_Y.set(i,0);
            checkPointsMotionArm_Z.set(i,0);

            checkPointsMotionChest.set(i,0);
            checkPointsMotionChest_X.set(i,0);
            checkPointsMotionChest_Y.set(i,0);
            checkPointsMotionChest_Z.set(i,0);

            x_limb_error.set(i,1000);
            y_limb_error.set(i,1000);
            z_limb_error.set(i,1000);
            x_chest_error.set(i,1000);
            y_chest_error.set(i,1000);
            z_chest_error.set(i,1000);
        }
        time2nextCPArm = 0;
        globalTestCounter = 0;

        start_command = true;
        whichCpArm = 1;
        current_checkpoint = 1;
        last_passed_checkpoint = 0;
        testPeak = 0;
    }

    void removeStationaryData()
    {
        int pos = 0;
        while(true)
        {
            if(sensor1.getSensorDataSet(pos+1)[dominant_axis_arm] - sensor1.getSensorDataSet(pos)[dominant_axis_arm] == 0|| sensor1.getSensorDataSet(pos+1)[dominant_axis_arm] - sensor1.getSensorDataSet(pos)[dominant_axis_arm] == 1|| sensor1.getSensorDataSet(pos+1)[dominant_axis_arm] - sensor1.getSensorDataSet(pos)[dominant_axis_arm] == -1)
                pos++;
//                if (pos == sensor1.getX_values().size()){
//                    break;
//                }
            else
                break;
        }

        for(int j=0;j<pos;j++)
        {
            sensor1.getX_values().remove(0);
            sensor1.getY_values().remove(0);
            sensor1.getZ_values().remove(0);
            sensor2.getX_values().remove(0);
            sensor2.getY_values().remove(0);
            sensor2.getZ_values().remove(0);

        }


        int size = sensor1.getX_values().size();
        pos = size-1;
        while(true)
        {
            if(sensor1.getSensorDataSet(pos)[dominant_axis_arm] - sensor1.getSensorDataSet(pos-1)[dominant_axis_arm] == 0
                    || sensor1.getSensorDataSet(pos)[dominant_axis_arm] - sensor1.getSensorDataSet(pos-1)[dominant_axis_arm] == 1
                    || sensor1.getSensorDataSet(pos)[dominant_axis_arm] - sensor1.getSensorDataSet(pos-1)[dominant_axis_arm] == -1)
                pos--;
            else
                break;
        }

        for(int j=pos;j<size-1;j++)
        {
            sensor1.getX_values().remove(pos+1);
            sensor1.getY_values().remove(pos+1);
            sensor1.getZ_values().remove(pos+1);
            sensor2.getX_values().remove(pos+1);
            sensor2.getY_values().remove(pos+1);
            sensor2.getZ_values().remove(pos+1);

        }
    }

    ArrayList<Integer> calculatePeaks(AccelerometerDataClass sensor)
    {
        ArrayList<Integer> diff_X = new ArrayList<>();
        ArrayList<Integer> diff_Y = new ArrayList<>();
        ArrayList<Integer> diff_Z = new ArrayList<>();
        ArrayList<Integer> maxArray = new ArrayList<>();

        int length_x_rec = sensor.getX_values().size();
        int length_y_rec = sensor.getY_values().size();
        int length_z_rec = sensor.getZ_values().size();

        for(int i=0;i<length_x_rec-1;i++)
        {
            int temp = sensor.getX_values().get(i+1) - sensor.getX_values().get(0);
            if(temp > 180)
                temp -= 360;
            else if(temp < -180)
                temp += 360;

            diff_X.add(Math.abs(temp));
        }

        int max_X = 0;

        for(int val : diff_X){
            if(val>max_X){
                max_X = val;
            }
        }


        for(int i=0;i<length_y_rec-1;i++)
        {
            int temp = sensor.getY_values().get(i+1) - sensor.getY_values().get(0);
            if(temp > 180)
                temp -= 360;
            else if(temp < -180)
                temp += 360;

            diff_Y.add(Math.abs(temp));
        }

        int max_Y = 0;

        for(int val : diff_Y){
            if(val>max_Y){
                max_Y = val;
            }
        }

        for(int i=0;i<length_z_rec-1;i++)
        {
            int temp = sensor.getZ_values().get(i+1) - sensor.getZ_values().get(0);
            if(temp > 180)
                temp -= 360;
            else if(temp < -180)
                temp += 360;

            diff_Z.add(Math.abs(temp));
        }



        int max_Z = 0;

        for(int val : diff_Z){
            if(val>max_Z){
                max_Z = val;
            }
        }

        maxArray.add(max_X);
        maxArray.add(max_Y);
        maxArray.add(max_Z);

        return maxArray;


    }

    void determineMotionSign()
    {
        limbMotionSign = checkPointsArm.getInc_values().get(1);
        chestMotionSign = checkPointsChest.getInc_values().get(1);

    }
    void startRecord(){ //sorun burda
        sensor1.clear();
        sensor2.clear();
        record_completed.set(false);

    }
    void stopRecord(){
        record_completed.set(true);
        dominant_axis_arm = 0; /*determineDominantAxis(sensor1);*/ //TODO: Change with Exercise specific values
        dominant_axis_chest = 0; /*determineDominantAxis(sensor2);*/

        int size1 = sensor1.getX_values().size();
        int size2 = sensor2.getX_values().size();

        if(size1 > size2)
        {
            for(int i=0;i<size1-size2;i++) {
                sensor1.getX_values().remove(size2);
                sensor1.getY_values().remove(size2);
                sensor1.getZ_values().remove(size2);
            }
        }
        else if(size1 < size2)
        {
            for(int i=0;i<size2-size1;i++){
                sensor2.getX_values().remove(size1);
                sensor2.getY_values().remove(size1);
                sensor2.getZ_values().remove(size1);
            }
        }

        Log.d("ControlDebug",sensor1.getId()+"  "+sensor1.getX_values().toString());
        Log.d("ControlDebug",sensor1.getId()+"  "+sensor1.getY_values().toString());
        Log.d("ControlDebug",sensor1.getId()+"  "+sensor1.getZ_values().toString());

        removeStationaryData();

        globalRecordCounter = sensor1.getX_values().size() - CHECKPOINTSTEP;

        peaks_Arm = calculatePeaks(sensor1);
        peaks_Chest = calculatePeaks(sensor2);



        Log.d("ControlDebug","PEAK-ARM"+"  "+peaks_Arm.get(0));
        Log.d("ControlDebug","PEAK-ARM"+"  "+peaks_Arm.get(1));
        Log.d("ControlDebug","PEAK-ARM"+"  "+peaks_Arm.get(2));

        Log.d("ControlDebug","PEAK-CHEST"+"  "+peaks_Chest.get(0));
        Log.d("ControlDebug","PEAK-CHEST"+"  "+peaks_Chest.get(1));
        Log.d("ControlDebug","PEAK-CHEST"+"  "+peaks_Chest.get(2));

        sb_threshold = peaks_Arm.get(dominant_axis_arm);
        sb_ratio = 100.0/sb_threshold;
        checkPointsNumArm = defineCPs(sensor1,CHECKPOINTSTEP,checkPointsArm,dominant_axis_arm);
        checkPointsNumChest = defineCPs(sensor2,CHECKPOINTSTEP,checkPointsChest,dominant_axis_chest);

        if(peaks_Arm.get(dominant_axis_arm) - checkPointsArm.getDataSet(0)[dominant_axis_arm] < 0) //TODO: will be removed. Get motion sign with first checkpoint (1)
        {
            motionDirection = -1;
        }
        else
        {
            motionDirection = 1;
        }

        determineMotionSign();

        Log.d("ControlDebug","SIGN LIMB"+"  "+limbMotionSign);
        Log.d("ControlDebug","SIGN CHEST"+"  "+chestMotionSign);

        int min = Math.min(checkPointsNumArm,checkPointsNumChest);
        checkPointsNumArm = min;
        checkPointsNumChest = min;
        for(int i=0;i<checkPointsNumArm;i++){
            checkPointsMotionArm.add(0);
            checkPointsMotionChest.add(0);

            checkPointsMotionArm_X.add(0);
            checkPointsMotionArm_Y.add(0);
            checkPointsMotionArm_Z.add(0);

            checkPointsMotionChest_X.add(0);
            checkPointsMotionChest_Y.add(0);
            checkPointsMotionChest_Z.add(0);

            x_limb_error.add(0);
            y_limb_error.add(0);
            z_limb_error.add(0);
            x_chest_error.add(0);
            y_chest_error.add(0);
            z_chest_error.add(0);
        }

        radiusArm = calculateRadius(checkPointsArm, checkPointsNumArm);
        radiusChest = calculateRadius(checkPointsChest,  checkPointsNumChest);
//        radiusChest.set(0,15); //TODO: Check if this value is problematic (?) |   check if it is necessary
        Log.d("ControlDebug", "dom_arm: "+dominant_axis_arm+" dom_chest: "+dominant_axis_chest);
        Log.d("ControlDebug", "CP num: "+checkPointsNumArm+" CP num: "+ checkPointsNumChest);
        Log.d("ControlDebug", "radius: "+radiusArm.toString()+" radius: "+ radiusChest.toString());
        Log.d("ControlDebug","CP Arm: "+ checkPointsArm.getX_values().toString());
        Log.d("ControlDebug","CP Arm: "+ checkPointsArm.getY_values().toString());
        Log.d("ControlDebug","CP Arm: "+ checkPointsArm.getZ_values().toString());
        Log.d("ControlDebug","CP Arm: "+ checkPointsArm.getInc_values().toString());
        Log.d("ControlDebug","CP Chest: "+ checkPointsChest.getX_values().toString());
        Log.d("ControlDebug","CP Chest: "+ checkPointsChest.getY_values().toString());
        Log.d("ControlDebug","CP Chest: "+ checkPointsChest.getZ_values().toString());
        Log.d("ControlDebug","CP Chest: "+ checkPointsChest.getInc_values().toString());
    }

    ArrayList<ArrayList<Integer>> calculateRadius(AccelerometerDataClass CP_arr, int num){
        ArrayList<ArrayList<Integer>> rad = new ArrayList<>();
        int cp_diff2_x = 0;
        int cp_diff2_y = 0;
        int cp_diff2_z = 0;
        for(int i=0; i < num - 1; i++) {
            ArrayList<Integer> temp = new ArrayList<>();
            cp_diff2_x = CP_arr.getDataSet(i+1)[0] - CP_arr.getDataSet(i)[0];
            if ( cp_diff2_x > 180) {
                cp_diff2_x -= 360;
            } else if (cp_diff2_x < -180) {
                cp_diff2_x += 360;
            }

            cp_diff2_y = CP_arr.getDataSet(i+1)[1] - CP_arr.getDataSet(i)[1];
            if ( cp_diff2_y > 180) {
                cp_diff2_y -= 360;
            } else if (cp_diff2_y < -180) {
                cp_diff2_y += 360;
            }

            cp_diff2_z = CP_arr.getDataSet(i+1)[2] - CP_arr.getDataSet(i)[2];
            if ( cp_diff2_z > 180) {
                cp_diff2_z -= 360;
            } else if (cp_diff2_z < -180) {
                cp_diff2_z += 360;
            }


            if (Math.abs(cp_diff2_x) > 20) {
                cp_diff2_x = Math.abs(cp_diff2_x) / 2;
            } else {
                cp_diff2_x = 10;
            }

            if (Math.abs(cp_diff2_y) > 30) {
                cp_diff2_y = Math.abs(cp_diff2_y) / 2;
            } else {
                cp_diff2_y = 25; //TODO: change to default value:15 if it creates problem
            }

            if (Math.abs(cp_diff2_z) > 30) {
                cp_diff2_z = Math.abs(cp_diff2_z) / 2;
            } else {
                cp_diff2_z = 15;
            }

            temp.add(cp_diff2_x);
            temp.add(cp_diff2_y);
            temp.add(cp_diff2_z);

            rad.add(temp);
        }
        ArrayList<Integer> last = rad.get(rad.size()-1);
        rad.add(last);

        return rad;
    }

    private void initializeLists(int n){
        mBluetoothConnectionStateList = new ArrayList<>();
        mBluetoothDeviceList = new ArrayList<>();
        mBluetoothMACList = new ArrayList<>();
        mBluetoothSocketList  = new ArrayList<>();
        mSelectedBTList = new ArrayList<>();
        mInputStreamList = new ArrayList<>();
        mUUIDList = new ArrayList<>();
        mBluetoothNameList = new ArrayList<>();
        mBluetoothMACList = new ArrayList<>();
        for(int i=0;i<n;i++){
            int a=i+1;
            mBluetoothConnectionStateList.add(false);
            mBluetoothDeviceList.add(null);
            mBluetoothSocketList.add(null);
            mSelectedBTList.add(0);
            //mOutputStreamList.add(null);
            mInputStreamList.add(null);
            mUUIDList.add(null);
        }
        mBluetoothNameList.add(limb_device.getDevice_name());
        mBluetoothNameList.add(chest_device.getDevice_name());
        mBluetoothMACList.add(limb_device.getMac_address());
        mBluetoothMACList.add(chest_device.getMac_address());
    }
    private void checkBTState() {
        Log.d(TAG,"entryCheckBTState");
        // Check for Bluetooth support and then check to make sure it is turned on
        // Emulator doesn't support Bluetooth and will return null
        if(BA==null) {
            Toast.makeText(BluetoothActivity.this, R.string.bluetooth_not_support, Toast.LENGTH_SHORT).show();
            bt_open=false;
            for(int i=0;i<mBluetoothConnectionStateList.size();i++){
                mBluetoothConnectionStateList.set(i,false);
            }
        } else {
            if (BA.isEnabled()) {
                Log.d(TAG, getString(R.string.bluetooth_on));
                bt_open=true;
                int count=0;
                for(String name:mBluetoothNameList){
                    if(name.equals("")){
                        count++;
                    }
                }
                if(count!=0){
                    alertBT();
                }else{
                    setBTDevice(mBluetoothMACList);  //changeeeeeeeeeeeeeeee
                    new ConnectionTask().execute((Void) null);
                }
            } else {
                //Prompt user to turn on Bluetooth
                bt_open=false;
                Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                startActivityForResult(enableBtIntent, 1);
            }
        }
        Log.d(TAG,"exitCheckBTState");
    }
    private void errorExit(String title, String message){
        Log.d(title,message);
    }
    public void setBTDevice(ArrayList<String> macList){
        Log.d(TAG,"entrySetBTDevice");
        if(bt_open){
            pairedDevices = BA.getBondedDevices();
            if (pairedDevices.size() > 0){
                for(BluetoothDevice bt : pairedDevices){
                    for(int i=0;i<macList.size();i++){
                        if(macList.get(i).equals(bt.getAddress())){
                            mBluetoothDeviceList.set(i,bt);
                        }
                    }
                }
            }
        }
        Log.d(TAG,"exitSetBTDevice");
    }
    public void alertBT(){
        Log.d(TAG,"entryBTAlert");
        if(bt_open){
            final Dialog dialog = new Dialog(BluetoothActivity.this);
            dialog.setContentView(R.layout.bt_dialog_new);
            dialog.setCancelable(false);
            dialog.setTitle(getString(R.string.select_device));
            Spinner spinner1 =(Spinner) dialog.findViewById(R.id.spinner_bt1_new);
            Spinner spinner2 =(Spinner) dialog.findViewById(R.id.spinner_bt2_new);
            Button saveButton = (Button) dialog.findViewById(R.id.button_save_bt_new);
            final TextView selected1 = (TextView) dialog.findViewById(R.id.selected_tv_new);
            final TextView selected2 = (TextView) dialog.findViewById(R.id.selected2_tv_new);
            pairedDevices = BA.getBondedDevices();
            final ArrayList<BluetoothDevice> list = new ArrayList<>();
            if (pairedDevices.size() > 0){
                list.addAll(pairedDevices);
            }
            bluetoothCustomAdapter = new BluetoothCustomAdapter(BluetoothActivity.this,list);
            spinner1.setAdapter(bluetoothCustomAdapter);
            spinner2.setAdapter(bluetoothCustomAdapter);
            spinner2.setSelection(1);
            spinner1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    String name = list.get(position).getName();
                    selected1.setText(name);
                    mSelectedBTList.set(0,position);
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
            spinner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    String name = list.get(position).getName();
                    selected2.setText(name);
                    mSelectedBTList.set(1,position);
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
            saveButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String name1 = selected1.getText().toString();
                    String name2 = selected2.getText().toString();
                    String address1 = list.get(mSelectedBTList.get(0)).getAddress();
                    String address2 = list.get(mSelectedBTList.get(1)).getAddress();

                    //if(!name1.equals(name2)&&!name2.equals(name3)&&!name1.equals(name3))
                    if(!address1.equals(address2)){
                        mBluetoothNameList.set(0,name1);
                        mBluetoothNameList.set(1,name2);
                        mBluetoothMACList.set(0,address1);
                        mBluetoothMACList.set(1,address2);
                        //editor.putString("bluetoothMAC1", address1);
                        //editor.putString("bluetoothMAC2", address2);
                        //editor.apply();
                        setBTDevice(mBluetoothMACList);
                        new ConnectionTask().execute((Void) null);
                        dialog.dismiss();
                    }
                }
            });
            dialog.show();
        }
        Log.d(TAG,"exitBTAlert");
    }
    private BluetoothSocket createBluetoothSocket(BluetoothDevice device, int which) throws IOException {
        Log.d(TAG,"entryBTSocket");
        UUID uuid = null;
        if(Build.VERSION.SDK_INT >= 10){
            try {
                ParcelUuid[] uuids = device.getUuids();
                for(ParcelUuid p : uuids) {
                    Log.d(TAG, p.getUuid().toString() + "\n");
                }
                uuid = device.getUuids()[0].getUuid();
                if(which==1){
                    //MY_UUID1 = uuid;
                    mUUIDList.set(0,uuid);
                }else if(which==2){
                    if(uuid.equals(mUUIDList.get(0))){
                        if(uuids.length>1){
                            uuid = device.getUuids()[1].getUuid();
                        }
                        mUUIDList.set(1,uuid);
                    }else{
                        mUUIDList.set(1,uuid);
                    }
                }
                else if(which==3){
                    if(uuid.equals(mUUIDList.get(0))){
                        if(uuids.length>1){
                            uuid = device.getUuids()[1].getUuid();
                        }
                        //MY_UUID3 = uuid;
                    }else{
                        //MY_UUID3 = uuid;
                    }
                }
                final Method m = device.getClass().getMethod("createInsecureRfcommSocketToServiceRecord", new Class[] { UUID.class });
                return (BluetoothSocket) m.invoke(device, uuid);
            } catch (Exception e) {
                Log.e(TAG, "Could not create Insecure RFComm Connection",e);
            }
        }
        Log.d(TAG,"exitBTSocket");
        return  device.createRfcommSocketToServiceRecord(uuid);
    }

    @Override
    public void onSaveInstanceState(Bundle saveInstanceState) {
        //saveInstanceState.putInt("PAUSE", pause);
        Log.d(TAG,"onSaveInstanceState");
        super.onSaveInstanceState(saveInstanceState);
    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        // pause = savedInstanceState.getInt("PAUSE");
        //if(pause==1){
        //   connectBT();
        //  pause=0;
        // savedInstanceState.putInt("PAUSE", pause);
        //}
        Log.d(TAG,"onRestoreInstanceState");
    }

    @Override
    public void onResume() {
        super.onResume();
        bt_repeat = 0;
        Log.d(TAG,"onResume");
        checkBTState();
        IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_ACL_DISCONNECTED);
        this.registerReceiver(mConnectionReceiver, filter);
        /*if(pause==1){
            new ConnectionTask().execute((Void) null);
        }*/
    }

    @Override
    public void onPause() {
        if(textToSpeech !=null){
            textToSpeech.stop();
            textToSpeech.shutdown();
        }
        super.onPause();
        Log.d(TAG,"entryOnPause");
        String str = (bt_open) ? "true":"false";
        Log.d(TAG,str);
        int count=mBluetoothSocketList.size();
        int count2=count;
        for(int i=0;i<count;i++){
            if(mBluetoothSocketList.get(i)!=null){
                str = "true";
                count2--;
            }else{
                str = "false";
            }
            Log.d(TAG,str);
        }

        if(bt_open&&count2!=count){  //&&btSocket3!=null -----------------------------------
            new CloseTask().execute((Void) null);

        }
        unregisterReceiver(mConnectionReceiver);
        Log.d(TAG,"exitOnPause");
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
    private void sendData(String message, OutputStream outStream) {
        byte[] msgBuffer = message.getBytes();
        Log.d(TAG, "...Send data: " + message + "...");

        try {
            Log.d(TAG,"------40------");
            outStream.write(msgBuffer);
            Log.d(TAG,"------41------");
        } catch (IOException e) {
            String msg = e.getMessage();
            //if (MAC.equals("00:00:00:00:00:00"))
            //    msg = msg +  ".\n\nCheck that the SPP UUID: " + uuid.toString() + " exists on server.\n\n";
            Log.d(TAG,"------42------");
            errorExit("Fatal Error","In sendData-42: " + msg);
        }
    }

    private int[] readData_chest(InputStream inputStream, int which) {
        int [] data = new int[4];
        int[] buffer = new int[11];  // buffer store for the stream
        try {

            buffer[0] = inputStream.read();
            String str=""+which+" "+ buffer[0]+ " ";
            for(int i=1;i<11 && buffer[0]==85 ;i++){
                buffer[i]=inputStream.read();
                str += buffer[i]+" ";
            }
            data[3]= buffer[1];
            Log.d("btvalues-b", str);
            if(buffer[1]==83){
                int value_x = (buffer[2]>=0) ? buffer[2]:256+buffer[2];
                value_x+=(buffer[3]<<8);
                value_x = (int) (value_x/32768.0*180);
                if(value_x>180) value_x-=360;
                int value_y = (buffer[4]>=0) ? buffer[4]:256+buffer[4];
                value_y+=(buffer[5]<<8);
                value_y = (int) (value_y/32768.0*180);
                if(value_y>180) value_y-=360;
                int value_z = (buffer[6]>=0) ? buffer[6]:256+buffer[6];
                value_z+=(buffer[7]<<8);
                value_z = (int) (value_z/32768.0*180);
                if(value_z>180) value_z-=360;
                data[0] = value_x;
                data[1] = value_y;
                data[2] = value_z;
                Log.d("btvalues-v"+which+"-"+which,"x: " + value_x + " y: "+value_y + " z: "+value_z);
                Log.d("btvalues-s"+which+"-"+which,"x: " + data[0] + " y: "+data[1] + " z: "+data[2]);
            }
//            else if (buffer[1]==82){
//                double value_x = (buffer[2]>=0) ? buffer[2]:256+buffer[2];
//                value_x+=(buffer[3]<<8);
//                value_x = value_x/32768.0*2000;
////                if(value_x>180) value_x-=360;
//                double value_y = (buffer[4]>=0) ? buffer[4]:256+buffer[4];
//                value_y+=(buffer[5]<<8);
//                value_y = value_y/32768.0*2000;
////                if(value_y>180) value_y-=360;
//                double value_z = (buffer[6]>=0) ? buffer[6]:256+buffer[6];
//                value_z+=(buffer[7]<<8);
//                value_z = value_z/32768.0*2000;
////                if(value_z>180) value_z-=360;
//                data[0] = (int)value_x;
//                data[1] = (int)value_y;
//                data[2] = (int)value_z;
//                Log.d("btvalues-v-gyro"+which+"-"+which,"x: " + value_x + " y: "+value_y + " z: "+value_z);
//                Log.d("btvalues-s-gyro"+which+"-"+which,"x: " + data[0] + " y: "+data[1] + " z: "+data[2]);
//            }
            return data;
        } catch (IOException e) {
            String msg = e.getMessage();
            Log.i(TAG,"------42------");
            errorExit("Fatal Error","In readData-42: " + msg);
        }
        Log.d("btvalues-d"+which+"-"+which,"x: " + data[0] + " y: "+data[1] + " z: "+data[2]);
        return new int[4];
    }
    private int[] readData_arm(InputStream inputStream, int which) {
        int [] data = new int[4];
        int[] buffer = new int[11];  // buffer store for the stream
        try {
            buffer[0] = inputStream.read();
            String str=""+which+" "+ buffer[0]+ " ";
            for(int i=1;i<11&&buffer[0]==85;i++){
                buffer[i]=inputStream.read();
                str += buffer[i]+" ";
            }
            data[3]= buffer[1];
            Log.d("btvalues-b", str);
            if(buffer[1]==83){
                int value_x = (buffer[2]>=0) ? buffer[2]:256+buffer[2];
                value_x+=(buffer[3]<<8);
                value_x = (int) (value_x/32768.0*180);
                if(value_x>180) value_x-=360;
                int value_y = (buffer[4]>=0) ? buffer[4]:256+buffer[4];
                value_y+=(buffer[5]<<8);
                value_y = (int) (value_y/32768.0*180);
                if(value_y>180) value_y-=360;
                int value_z = (buffer[6]>=0) ? buffer[6]:256+buffer[6];
                value_z+=(buffer[7]<<8);
                value_z = (int) (value_z/32768.0*180);
                if(value_z>180) value_z-=360;
                data[0] = value_x;
                data[1] = value_y;
                data[2] = value_z;
                Log.d("btvalues-v"+which+"-"+which,"x: " + value_x + " y: "+value_y + " z: "+value_z);
                Log.d("btvalues-s"+which+"-"+which,"x: " + data[0] + " y: "+data[1] + " z: "+data[2]);
            }
            return data;
        } catch (IOException e) {
            String msg = e.getMessage();
            Log.i(TAG,"------42------");
            errorExit("Fatal Error","In readData-42: " + msg);
        }
        Log.d("btvalues-d"+which+"-"+which,"x: " + data[0] + " y: "+data[1] + " z: "+data[2]);
        return new int[4];
    }
    private class ConnectionTask extends AsyncTask<Void, Boolean, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pd = new ProgressDialog(BluetoothActivity.this);
            pd.setCancelable(false);
            pd.setMessage(getString(R.string.connecting));
            if (!BluetoothActivity.this.isFinishing()){
                pd.show();
            }
        }

        @Override
        protected Void doInBackground(Void... params) {
            Log.d(TAG,"entryConnectBT");
            int count=0;
            int size = mBluetoothNameList.size();
            for(int i=0;i<size;i++){
                if(mBluetoothNameList.get(i).equals("")||mBluetoothMACList.get(i).equals("")){
                    count++;
                    break;
                }
            }
            if(bt_open&&count==0){
                Log.d(TAG,"------1------");
                ArrayList<BluetoothDevice> deviceList = new ArrayList<>();
                for(String mac: mBluetoothMACList){
                    deviceList.add(BA.getRemoteDevice(mac));
                }
                Log.d(TAG,"------2------");
                // Two things are needed to make a connection:
                //   A MAC address, which we got above.
                //   A Service ID or UUID.  In this case we are using the
                //     UUID for SPP.
                //for(int i=0;i<mBluetoothConnectionStateList.size();i++){
                //    mBluetoothConnectionStateList.set(i,false);
                //}
                for(int i=0;i<mBluetoothSocketList.size();i++){
                    try {
                        Log.d(TAG,"------3------");
                        mBluetoothSocketList.set(i,createBluetoothSocket(deviceList.get(i),1));
                        Log.d(TAG,"------4------");
                    } catch (IOException e1) {
                        Log.d(TAG,"------5------catch1");
                        errorExit("Fatal Error", "In connectBT-5: " + e1.getMessage() + ".");
                    }
                }
                // Discovery is resource intensive.  Make sure it isn't going on
                // when you attempt to connect and pass your message.
                Log.d(TAG,"------12------");
                BA.cancelDiscovery();
                Log.d(TAG,"------13------");
                // Establish the connection.  This will block until it connects.
                Log.d(TAG,"------14------");

                for(int i=0;i<mBluetoothSocketList.size();i++){
                    try {
                        Log.d(TAG,"------15------");
                        if ( !mBluetoothSocketList.get(i).isConnected()){
                            mBluetoothSocketList.get(i).connect();
                            mBluetoothConnectionStateList.set(i,true);
                            Log.d(TAG,"------16------");
                        }
                    } catch (IOException e) {
                        try {
                            Log.d(TAG,"------17------");
                            mBluetoothSocketList.get(i).close();
                            Log.d(TAG,"------18------");
                        } catch (IOException e2) {
                            Log.d(TAG,"------19------catch1");
                            errorExit("Fatal Error", "In connectBT-19: " + e2.getMessage() + ".");
                        }
                    }
                    publishProgress(mBluetoothConnectionStateList.get(0),mBluetoothConnectionStateList.get(1),false);
                }
                /*for(int i=0;i<mBluetoothSocketList.size();i++){
                    try {
                        Log.d(TAG,"------30------");
                        mOutputStreamList.set(i,mBluetoothSocketList.get(i).getOutputStream());
                        Log.d(TAG,"------31------");
                    } catch (IOException e) {
                        Log.d(TAG,"------32------catch1");
                        errorExit("Fatal Error", "In connectBT-24: " + e.getMessage() + ".");
                    }
                }*/
                for(int i=0;i<mBluetoothSocketList.size();i++){
                    try {
                        Log.d(TAG,"------30------");
                        mInputStreamList.set(i,mBluetoothSocketList.get(i).getInputStream());
                        Log.d(TAG,"------31------");
                    } catch (IOException e) {
                        Log.d(TAG,"------32------catch1");
                        errorExit("Fatal Error", "In connectBT-24: " + e.getMessage() + ".");
                    }
                }
            }
            Log.d(TAG,"exitConnectBT");
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            Util.dismissProgressDialog(pd);
            if(test_pressed.get()){
                myHandler.postDelayed(updateTime,1000);
            }
            pause=0;
            int count = 0;
            for(boolean state:mBluetoothConnectionStateList){
                Log.d(TAG,"open: "+ String.valueOf(state));
                if(!state){
                    count++;
                }
            }
            if(count!=0){
                if(bt_repeat==0){
                    bt_repeat=1;
                    Toast.makeText(BluetoothActivity.this, R.string.connection_problem_trying_to_connect, Toast.LENGTH_SHORT).show();
                    new ConnectionTask().execute((Void) null);
                }else if(bt_repeat==1){
                    bt_repeat=0;
                    Toast.makeText(BluetoothActivity.this, R.string.bt_connection_problem, Toast.LENGTH_SHORT).show();
                    if (!(mBluetoothSocketList.get(0).isConnected() && mBluetoothSocketList.get(1).isConnected()))
                        showBtConnectionLostAlertDialog();
                }
            }else {
                mStartTimer.start();
                mTrueTimer.start();
                mFastTimer.start();
                bt_connected = true;
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        while (bt_connected) {
                            try {
                                if (mInputStreamList.get(0).available() > 0) {
                                    //String b = (record_completed.get()) ? "true":"false";
                                    //Log.d("recordDebug",b);
                                    int[] data = readData_arm(mInputStreamList.get(0), 1);
                                    if (data[3] == 83) {
                                        addLastof5(x_fifo_arm, data[0]);
                                        addLastof5(y_fifo_arm, data[1]);
                                        addLastof5(z_fifo_arm, data[2]);
                                        if (!record_completed.get()) {
                                            sensor1.addAll(data[0], data[1], data[2]);
                                            Log.d("btvalues-arm", data[0] + " " + data[1] + " " + data[2]);
//                                            globalRecordCounter++;

                                        } else if (test_pressed.get()) {
                                            //temp1.addAll(data[0],data[1],data[2]);
                                            //Log.d("ThreadDebug-1",data[0]+" "+data[1]+" "+data[2]);
                                            checkCorrectPositionArm();
                                            checkCorrectPositionChest();
                                            setDominantDataArm();
                                            setDominantDataChest();

                                            Log.d("ControlDebug", "\t\t\t\t\t\t\t\t" + checkPointsMotionArm.get(0) + "   " + checkPointsMotionChest.get(0));
                                            if (checkPointsMotionArm.get(0) == 1 && checkPointsMotionChest.get(0) == 1) {
                                                if(start_state.trim().equals("")&&!(checkPointsMotionArm.get(1)==1 || checkPointsMotionArm.get(2)==1 || checkPointsMotionArm.get(3)==1)){   //TODO: change with dominant axis control array
                                                    mStartTimer.cancel();
                                                    start_state = getString(R.string.info_start);
                                                    bg_start_state = 0;
                                                    myHandler.post(updateRunnableStart);
                                                    mStartTimer.start();
                                                }

                                                calculateTestPeak(dom_data_arm);

                                                int curr_diff = dom_data_arm[4]-checkPointsArm.getDataSet(0)[dominant_axis_arm]; //TODO: move the peak bar calculations into a func.
                                                boolean jump = false;
                                                if(curr_diff>180){
                                                    jump=true;
                                                    curr_diff-=360;
                                                }else if(curr_diff<-180){
                                                    jump=true;
                                                    curr_diff+=360;
                                                }

                                                Log.d("ControlDebug:", "\tReference: " + referenceAngle);
                                                Log.d("ControlDebug:", "\tPEAK: " + peaks_Arm.get(dominant_axis_arm));
                                                Log.d("ControlDebug:", "\tDOMDATA: " + dom_data_arm[4]);
                                                Log.d("ControlDebug:", "\tSIGN: " + checkPointsArm.getDataSet(1)[3]);


                                                if(checkPointsArm.getDataSet(1)[3] == -1){
                                                    if(jump){
                                                        if (dom_data_arm[4] < referenceAngle){
                                                            curr_diff = 0;
                                                        }
                                                    }
                                                    else{
                                                        if (dom_data_arm[4] > referenceAngle){
                                                            curr_diff = 0;
                                                        }
                                                    }
                                                }
                                                else if(checkPointsArm.getDataSet(1)[3] == 1){
                                                    if(jump){
                                                        if(dom_data_arm[4] > referenceAngle){
                                                            curr_diff = 0;
                                                        }
                                                    }
                                                    else{
                                                        if(dom_data_arm[4] < referenceAngle){
                                                            curr_diff = 0;
                                                        }
                                                    }
                                                }

                                                curr_diff = Math.abs(curr_diff);

//                                                Log.d("ControlDebug", "\t\t\t\tPEAK BAR:\t" + curr_diff);


                                                sb_progress = (int) (curr_diff * sb_ratio);
                                                myHandler.post(updateSeekbar);
                                                /*if (startDelayCounter > 40) {
                                                    motion_state = "BASLA";
                                                    background_state = R.drawable.oval_blue;
                                                    myHandler.post(updateRunnable);
//                                                    startDelayCounter = 0;
                                                    start_command = false;
                                                    reset_counter = 0;
                                                }*/

                                                if (time2nextCPArm > CHECKPOINTSTEP*4){
                                                    boolean motionCompleted = is_motion_completed();
                                                    if (motionCompleted){
                                                        Log.d("ControlDebug", "TIME OUT !:\t");
                                                        Log.d("ControlDebug", "CHECKPOINT-X-ARM:\t" + checkPointsMotionArm_X);
                                                        Log.d("ControlDebug", "CHECKPOINT-Y-ARM:\t" + checkPointsMotionArm_Y);
                                                        Log.d("ControlDebug", "CHECKPOINT-Z-ARM:\t" + checkPointsMotionArm_Z +"\n");
                                                        Log.d("ControlDebug", "CHECKPOINT-X-CHEST:\t" + checkPointsMotionChest_X);
                                                        Log.d("ControlDebug", "CHECKPOINT-Y-CHEST:\t" + checkPointsMotionChest_Y);
                                                        Log.d("ControlDebug", "CHECKPOINT-Z-CHEST:\t" + checkPointsMotionChest_Z +"\n");
                                                        checkError();
                                                        initializeAll();
                                                    }
                                                    else{
                                                        initializeAll();
                                                    }
                                                }
                                                else
                                                {
                                                    if (current_checkpoint < checkPointsNumArm)
                                                    {
                                                        isInCheckpoint_Any();
                                                    }
                                                    else if (current_checkpoint == checkPointsNumArm)
                                                    {
                                                        boolean motionCompleted = is_motion_completed();
                                                        if (motionCompleted){
                                                            Log.d("ControlDebug", "TIME OUT !:\t");
                                                            Log.d("ControlDebug", "CHECKPOINT-X-ARM:\t" + checkPointsMotionArm_X);
                                                            Log.d("ControlDebug", "CHECKPOINT-Y-ARM:\t" + checkPointsMotionArm_Y);
                                                            Log.d("ControlDebug", "CHECKPOINT-Z-ARM:\t" + checkPointsMotionArm_Z +"\n");
                                                            Log.d("ControlDebug", "CHECKPOINT-X-CHEST:\t" + checkPointsMotionChest_X);
                                                            Log.d("ControlDebug", "CHECKPOINT-Y-CHEST:\t" + checkPointsMotionChest_Y);
                                                            Log.d("ControlDebug", "CHECKPOINT-Z-CHEST:\t" + checkPointsMotionChest_Z +"\n");
                                                            checkError();
                                                            initializeAll();
                                                        }
                                                        else{
                                                            initializeAll();
                                                        }
//
                                                    }
                                                }
                                            } else if (checkPointsMotionChest.get(0) == 0) {    //TODO:Review

                                                if(dom_data_chest[4] < checkPointsChest.getDataSet(0)[dominant_axis_chest] - radiusChest.get(0).get(0)) //TODO: MESSAGES ARE NOT VALID WHEN DOM_AX IS Y and Z
                                                {
                                                    Log.d("ControlDebug", "GOGSUNU ASAGI INDIR");
                                                    mStartTimer.cancel();
                                                    start_state = getString(R.string.info_lower_chest);
                                                    bg_start_state = R.drawable.oval_yellow;
                                                    myHandler.post(updateRunnableStart);
                                                    mStartTimer.start();
                                                }
                                                else if(dom_data_chest[4] > checkPointsChest.getDataSet(0)[dominant_axis_chest] + radiusChest.get(0).get(0))
                                                {
                                                    Log.d("ControlDebug", "GOGSUNU YUKARI KALDIR");
                                                    mStartTimer.cancel();
                                                    start_state = getString(R.string.info_upper_chest);
                                                    bg_start_state = R.drawable.oval_yellow;
                                                    myHandler.post(updateRunnableStart);
                                                    mStartTimer.start();
                                                }
                                                time2nextCPArm++;
                                                if (time2nextCPArm > 2*CHECKPOINTSTEP)
                                                {
                                                    initializeAll();
                                                }
//                                                else if(z_fifo_chest[4] < checkPointsChest.getDataSet(0)[2] - 20) //TODO: Need to be arrange according to <-180 and >+180 cases
//                                                {
//                                                    Log.d("ControlDebug", "GOGSUNU SAGA CEVIR");
//                                                    mStartTimer.cancel();
//                                                    start_state = "GOGSUNU SAGA CEVIR";
//                                                    bg_start_state = R.drawable.oval_red;
//                                                    myHandler.post(updateRunnableStart);
//                                                    mStartTimer.start();
//                                                }
//                                                else if(z_fifo_chest[4] > checkPointsChest.getDataSet(0)[2] - 20)
//                                                {
//                                                    Log.d("ControlDebug", "GOGSUNU SOLA CEVIR");
//                                                    mStartTimer.cancel();
//                                                    start_state = "GOGSUNU SOLA CEVIR";
//                                                    bg_start_state = R.drawable.oval_red;
//                                                    myHandler.post(updateRunnableStart);
//                                                    mStartTimer.start();
//                                                }

//                                                Log.d("ControlDebug", "DIK DUR");
//                                                mStartTimer.cancel();
//                                                start_state = "DIK DUR";
//                                                bg_start_state = R.drawable.oval_red;
//                                                myHandler.post(updateRunnableStart);
//                                                mStartTimer.start();
                                                //resetHandler.postDelayed(resetRunnable,1000);
                                                sb_progress = -sb_start;
                                                myHandler.post(updateSeekbar);
                                            }
                                            else
                                            {
                                                time2nextCPArm++;
                                                if (time2nextCPArm > 2*CHECKPOINTSTEP)
                                                {
                                                    initializeAll();
                                                }
                                                sb_progress = -sb_start;
                                                myHandler.post(updateSeekbar);
                                            }
                                            Log.d("ControlDebug", "ANGLES: " + data[0] + " " + data[1] + " " + data[2]);
                                        }
                                        //if(!motion_state.equals("DIK DUR"))
                                        /*if (start_command)
                                            startDelayCounter++;

//                                        Log.d("ControlDebug:", "RESET COUNTER:\t" + reset_counter);
                                        reset_counter++;

                                        if (reset_counter == 20) {
                                            motion_state = " ";
                                            background_state = 0;
                                            reset_counter = 0;
                                            myHandler.post(updateRunnable);
                                        }*/
                                    }
                                }
                            } catch (IOException e) {
                                Log.d("connectionProblem",getString(R.string.bt_device_is_disconnected));
                                e.printStackTrace();
                            }
                        }
                    }
                }).start();
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        while (bt_connected) {
                            try {
                                if (mInputStreamList.get(1).available() > 0) {
                                    int[] data = readData_chest(mInputStreamList.get(1), 2);
                                    if (data[3] == 83) {
                                        addLastof5(x_fifo_chest, data[0]);
                                        addLastof5(y_fifo_chest, data[1]);
                                        addLastof5(z_fifo_chest, data[2]);
                                        if (!record_completed.get()) {
                                            sensor2.addAll(data[0], data[1], data[2]);
                                            Log.d("btvalues-chest", data[0] + " " + data[1] + " " + data[2]);
                                        } else if (test_pressed.get()) {
                                            //temp2.addAll(data[0],data[1],data[2]);
                                            //Log.d("ThreadDebug-2",data[0]+" "+data[1]+" "+data[2]);
                                        }
                                    }
                                }
                            } catch (IOException e) {
                                Log.d("connectionProblem",getString(R.string.bt_device_is_disconnected));
                                e.printStackTrace();
                            }
                        }
                    }
                }).start();
            }

        }

        @Override
        protected void onProgressUpdate(Boolean... values) {
            super.onProgressUpdate(values);
            String str1 = (values[0]) ? "true":"false";
            String str2 = (values[1]) ? "true":"false";
            String str3 = (values[2]) ? "true":"false";
        }

        @Override
        protected void onCancelled(Void result) {
            super.onCancelled(result);
            Util.dismissProgressDialog(pd);
            finish();
        }
    }
    private class CloseTask extends AsyncTask<Void, Boolean, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            bt_connected = false;
            myHandler.removeCallbacks(updateTime);
        }

        @Override
        protected Void doInBackground(Void... params) {
            /*for(int i=0;i<mOutputStreamList.size();i++){
                if (mOutputStreamList.get(i) != null) {
                    try {
                        Log.d(TAG,"------39------");
                        mOutputStreamList.get(i).flush();
                        Log.d(TAG,"------40------");
                    } catch (IOException e) {
                        Log.d(TAG,"------41------catch1");
                        errorExit("Fatal Error", "In onFlush-41: " + e.getMessage() + ".");
                    }
                }
            }*/
            for(int i=0;i<mInputStreamList.size();i++){
                if (mInputStreamList.get(i) != null) {
                    try {
                        Log.d(TAG,"------39------");
                        mInputStreamList.get(i).close();
                        Log.d(TAG,"------40------");
                    } catch (IOException e) {
                        Log.d(TAG,"------41------catch1");
                        errorExit("Fatal Error", "In onClose-41: " + e.getMessage() + ".");
                    }
                }
            }

            for(int i=0;i<mBluetoothSocketList.size();i++){
                try     {
                    Log.d(TAG,"------48------");
                    mBluetoothSocketList.get(i).close();
                    mBluetoothConnectionStateList.set(i,false);
                    Log.d(TAG,"------49------");
                } catch (IOException e2) {
                    Log.d(TAG,"------50------catch1");
                    errorExit("Fatal Error", "In onPause-50: " + e2.getMessage() + ".");
                }
                publishProgress(mBluetoothConnectionStateList.get(0),mBluetoothConnectionStateList.get(1),false);
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            for(boolean state:mBluetoothConnectionStateList){
                Log.d(TAG, "close: "+String.valueOf(state));
            }
            Log.d("PauseDebug","CloseBtFunction: "+System.currentTimeMillis());
            pause = 1;
        }

        @Override
        protected void onProgressUpdate(Boolean... values) {
            super.onProgressUpdate(values);
            String str1 = (values[0]) ? "true":"false";
            String str2 = (values[1]) ? "true":"false";
            String str3 = (values[2]) ? "true":"false";
        }

        @Override
        protected void onCancelled(Void result) {
            super.onCancelled(result);
            finish();
        }
    }
    void addSessionToExercise(final String exercise_id){
        final int time = session_time;
        final Date current_date = new Date();
        final SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ");
        String date = df.format(current_date);
        Calendar c = Calendar.getInstance();
        c.setTime(current_date);
        String session;
        int hours = c.get(Calendar.HOUR_OF_DAY);
        if(hours>=5 && hours<=17){
            session = "morning";
        }else{
            session = "evening";
        }
        JSONObject correct = new JSONObject();
        JSONArray wrong = new JSONArray();
        JSONObject jsonObject = new JSONObject();
        int total=0;
        try {
            correct.put("fast",session_fast);
            correct.put("slow",session_slow);
            correct.put("expected",session_expected);
            correct.put("total",session_succesful);
            total+=session_succesful;
            for(String key: session_wrong.keySet()){
                JSONObject object = new JSONObject();
                object.put("key",key);
                int value = session_wrong.get(key);
                object.put("value",value);
                total+=value;
                wrong.put(object);
            }
            jsonObject.put("time",time);
            jsonObject.put("date_time",date);
            jsonObject.put("session",session);
            jsonObject.put("correct",correct);
            jsonObject.put("wrong",wrong);
            jsonObject.put("total",total);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.d("VOLLEYTAG","session_start");
        final ExerciseSummary summary = new ExerciseSummary();
        final String str_json = jsonObject.toString();
        Log.d("VOLLEYTAG",str_json);
        if(checkConnection()){
            RequestQueue mQueue = Volley.newRequestQueue(getApplicationContext());
            final int finalTotal = total;
            JsonObjectRequest jsObjRequest = new JsonObjectRequest
                    (Request.Method.POST,Constants.URL_USERS+"/"+userID+"/exercises/"+exercise_id, jsonObject, new Response.Listener<JSONObject>() {

                        @Override
                        public void onResponse(JSONObject response) {
                            Log.d("VOLLEYTAG","session_finish");
                            Log.d("VOLLEYTAG",response.toString());
                            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
                            String notification_date = sdf.format(current_date);
                            String notification_name = selected_info_model.getName();
                            summary.setDate(notification_date);
                            summary.setName(notification_name);
                            summary.setExercise_time(time);
                            summary.setTotal(finalTotal);
                            summary.setNormal(session_expected);
                            summary.setFast(session_fast);
                            summary.setSlow(session_slow);
                            summary.setWrong(finalTotal-session_succesful);
                            summary.setLimb(session_wrong.get("limb"));
                            summary.setChest(session_wrong.get("chest"));
                            summary.setBoth(session_wrong.get("both"));
                            showSummary(summary);                    }
                    }, new Response.ErrorListener() {

                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Log.d("VOLLEYTAG", error.getMessage(), error);
                            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
                            String notification_date = sdf.format(current_date);
                            String notification_name = selected_info_model.getName();
                            summary.setDate(notification_date);
                            summary.setName(notification_name);
                            summary.setExercise_time(time);
                            summary.setTotal(finalTotal);
                            summary.setNormal(session_expected);
                            summary.setFast(session_fast);
                            summary.setSlow(session_slow);
                            summary.setWrong(finalTotal-session_succesful);
                            summary.setLimb(session_wrong.get("limb"));
                            summary.setChest(session_wrong.get("chest"));
                            summary.setBoth(session_wrong.get("both"));
                            showSummary(summary);
                            String str_array = getFromPref(Constants.PREF_SUMMARY);
                            String str_array_id = getFromPref(Constants.PREF_SUMMARY);
                            JSONArray jsonArray, jsonArray_id;
                            try {
                                jsonArray = new JSONArray(str_array);
                                jsonArray_id = new JSONArray(str_array_id);
                            } catch (JSONException e) {
                                jsonArray = new JSONArray();
                                jsonArray_id = new JSONArray();
                                e.printStackTrace();
                            }
                            jsonArray.put(str_json);
                            jsonArray_id.put(exercise_id);

                            saveToPref(Constants.PREF_SUMMARY,jsonArray.toString());
                            saveToPref(Constants.PREF_SUMMARY_ID,jsonArray_id.toString());
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
        }else{
            showSummary(summary);
            String str_array = getFromPref(Constants.PREF_SUMMARY);
            String str_array_id = getFromPref(Constants.PREF_SUMMARY_ID);
            JSONArray jsonArray, jsonArray_id;
            try {
                jsonArray = new JSONArray(str_array);
                jsonArray_id = new JSONArray(str_array_id);
            } catch (JSONException e) {
                jsonArray = new JSONArray();
                jsonArray_id = new JSONArray();
                e.printStackTrace();
            }
            jsonArray.put(str_json);
            jsonArray_id.put(exercise_id);

            saveToPref(Constants.PREF_SUMMARY,jsonArray.toString());
            saveToPref(Constants.PREF_SUMMARY_ID,jsonArray_id.toString());
        }

    }
    private void sendNotification(ExerciseSummary summary) {
        String notifications = sharedpreferences.getString("notifications"+userID,"");
        ArrayList<NotificationModel> notificationModels;
        if(!notifications.equals("")){
            Type type = new TypeToken<ArrayList<NotificationModel>>() {}.getType();
            notificationModels = gson.fromJson(notifications, type);
        }else{
            notificationModels = new ArrayList<>();
        }
        int correct = summary.getNormal()+summary.getFast()+summary.getSlow();
        String info = summary.getName()+"\n"+correct+"/"+summary.getTotal()+" "+getString(R.string.success_rate)+"\n"+getString(R.string.passing_time)+":   "+countToTime(summary.getExercise_time());
        NotificationModel model = new NotificationModel(summary.getDate(),"physhome2",info, summary);
        notificationModels.add(model);

        notifications = gson.toJson(notificationModels);
        editor.putString("notifications"+userID,notifications);
        editor.apply();
    }

    private void showSummary(final ExerciseSummary summary){
        final Dialog dialog = new Dialog(BluetoothActivity.this,R.style.Theme_AppCompat_Light_Dialog_Alert);
        dialog.setContentView(R.layout.exercise_summary);
        dialog.setCancelable(false);
        dialog.setTitle(getString(R.string.exercise_summary));
        final TextView rating_tv = (TextView) dialog.findViewById(R.id.exercise_rating_tv);
        TextView time_tv        = (TextView) dialog.findViewById(R.id.exercise_passing_time_tv);
        TextView total_tv       = (TextView) dialog.findViewById(R.id.exercise_total_movement_tv);
        TextView correct_tv     = (TextView) dialog.findViewById(R.id.exercise_correct_pb_tv);
        TextView normal_tv      = (TextView) dialog.findViewById(R.id.exercise_normal_pb_tv);
        TextView fast_tv        = (TextView) dialog.findViewById(R.id.exercise_fast_pb_tv);
        TextView slow_tv        = (TextView) dialog.findViewById(R.id.exercise_slow_pb_tv);
        TextView wrong_tv       = (TextView) dialog.findViewById(R.id.exercise_wrong_pb_tv);
        TextView limb_error_tv  = (TextView) dialog.findViewById(R.id.exercise_limb_error_pb_tv);
        TextView chest_error_tv = (TextView) dialog.findViewById(R.id.exercise_chest_error_pb_tv);
        TextView both_error_tv  = (TextView) dialog.findViewById(R.id.exercise_limb_and_chest_error_pb_tv);
        ProgressBar correct_pb  = (ProgressBar) dialog.findViewById(R.id.exercise_correct_pb);
        ProgressBar normal_pb   = (ProgressBar) dialog.findViewById(R.id.exercise_normal_pb);
        ProgressBar fast_pb     = (ProgressBar) dialog.findViewById(R.id.exercise_fast_pb);
        ProgressBar slow_pb     = (ProgressBar) dialog.findViewById(R.id.exercise_slow_pb);
        ProgressBar wrong_pb    = (ProgressBar) dialog.findViewById(R.id.exercise_wrong_pb);
        ProgressBar limb_pb     = (ProgressBar) dialog.findViewById(R.id.exercise_limb_error_pb);
        ProgressBar chest_pb    = (ProgressBar) dialog.findViewById(R.id.exercise_chest_error_pb);
        ProgressBar both_pb     = (ProgressBar) dialog.findViewById(R.id.exercise_limb_and_chest_error_pb);
        RatingBar ratingBar     = (RatingBar) dialog.findViewById(R.id.exercise_ratingbar);
        Button ok_button        = (Button) dialog.findViewById(R.id.exercise_ok_button);
        final ImageView correct_detail_iv = (ImageView) dialog.findViewById(R.id.summary_correct_details_iv);
        final ImageView wrong_detail_iv = (ImageView) dialog.findViewById(R.id.summary_wrong_details_iv);
        final CardView correct_detail_cardView = (CardView) dialog.findViewById(R.id.summary_detailed_correct_cv);
        final CardView wrong_detail_cardView = (CardView) dialog.findViewById(R.id.summary_detailed_wrong_cv);
        Drawable correct_draw   = new ProgressDrawable(0xdd00ff00, 0x4400ff00);
        Drawable normal_draw    = new ProgressDrawable(0xdd00ffff, 0x4400ffff);
        Drawable fast_draw      = new ProgressDrawable(0xddffff00, 0x44ffff00);
        Drawable slow_draw      = new ProgressDrawable(0xddff00ff, 0x44ff00ff);
        Drawable wrong_draw     = new ProgressDrawable(0xddff0000, 0x44ff0000);
        Drawable limb_draw    = new ProgressDrawable(0xdd00ffff, 0x4400ffff);
        Drawable chest_draw      = new ProgressDrawable(0xddffff00, 0x44ffff00);
        Drawable both_draw      = new ProgressDrawable(0xddff00ff, 0x44ff00ff);
        final boolean[] correct_detail_state = {false};
        final boolean[] wrong_detail_state = {false};
        final int[] exercise_rating = {7};
        ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                exercise_rating[0] = (int) (rating*2);
                rating_tv.setText(String.valueOf(exercise_rating[0]));
            }
        });
        ok_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                summary.setDifficulty(exercise_rating[0]);
                sendNotification(summary);
                Intent intent;
                intent = new Intent(BluetoothActivity.this, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                dialog.dismiss();
                startActivity(intent);
                finish();

            }
        });
        int normal = summary.getNormal();
        int fast = summary.getFast();
        int slow = summary.getSlow();
        int total = summary.getTotal();
        int wrong = summary.getWrong();
        int limb = summary.getLimb();
        int chest = summary.getChest();
        int both = summary.getBoth();
        int exercise_time = summary.getExercise_time();
        double percentage_correct = (normal+fast+slow)*100.0/total;
        double percentage_normal  = normal*100.0/total;
        double percentage_fast    = fast*100.0/total;
        double percentage_slow    = slow*100.0/total;
        double percentage_wrong   = wrong*100.0/total;
        double percentage_limb    = limb*100.0/total;
        double percentage_chest   = chest*100.0/total;
        double percentage_both    = both*100.0/total;
        String correct_str        = "% "+String.format("%.1f",percentage_correct);
        String normal_str         = "% "+String.format("%.1f",percentage_normal);
        String fast_str           = "% "+String.format("%.1f",percentage_fast);
        String slow_str           = "% "+String.format("%.1f",percentage_slow);
        String wrong_str          = "% "+String.format("%.1f",percentage_wrong);
        String limb_str           = "% "+String.format("%.1f",percentage_limb);
        String chest_str          = "% "+String.format("%.1f",percentage_chest);
        String both_str           = "% "+String.format("%.1f",percentage_both);
        String time_str           = countToTime(exercise_time);
        time_tv.setText(time_str);
        total_tv.setText(String.valueOf(total));
        correct_tv.setText(correct_str);
        normal_tv.setText(normal_str);
        fast_tv.setText(fast_str);
        slow_tv.setText(slow_str);
        wrong_tv.setText(wrong_str);
        limb_error_tv.setText(limb_str);
        chest_error_tv.setText(chest_str);
        both_error_tv.setText(both_str);
        correct_pb.setProgressDrawable(correct_draw);
        normal_pb.setProgressDrawable(normal_draw);
        fast_pb.setProgressDrawable(fast_draw);
        slow_pb.setProgressDrawable(slow_draw);
        wrong_pb.setProgressDrawable(wrong_draw);
        limb_pb.setProgressDrawable(limb_draw);
        chest_pb.setProgressDrawable(chest_draw);
        both_pb.setProgressDrawable(both_draw);
        correct_pb.setProgress((int) percentage_correct);
        normal_pb.setProgress((int) percentage_normal);
        fast_pb.setProgress((int) percentage_fast);
        slow_pb.setProgress((int) percentage_slow);
        wrong_pb.setProgress((int) percentage_wrong);
        limb_pb.setProgress((int) percentage_limb);
        chest_pb.setProgress((int) percentage_chest);
        both_pb.setProgress((int) percentage_both);
        correct_detail_iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(correct_detail_state[0]){
                    correct_detail_iv.setImageResource(R.drawable.ic_action_down);
                    correct_detail_cardView.setVisibility(View.GONE);
                    correct_detail_state[0] = false;
                }else{
                    correct_detail_iv.setImageResource(R.drawable.ic_action_up);
                    correct_detail_cardView.setVisibility(View.VISIBLE);
                    correct_detail_state[0] = true;
                }
            }
        });
        wrong_detail_iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(wrong_detail_state[0]){
                    wrong_detail_iv.setImageResource(R.drawable.ic_action_down);
                    wrong_detail_cardView.setVisibility(View.GONE);
                    wrong_detail_state[0] = false;
                }else{
                    wrong_detail_iv.setImageResource(R.drawable.ic_action_up);
                    wrong_detail_cardView.setVisibility(View.VISIBLE);
                    wrong_detail_state[0] = true;
                }
            }
        });
        dialog.show();
    }

    void getReferenceData(){
        AccelerometerReferenceModel ref = selected_model.getReference();
        for(int i=0;i<ref.getRecorded_arm().getX().length;i++){
            sensor1.addAll(ref.getRecorded_arm().getX()[i],ref.getRecorded_arm().getY()[i],ref.getRecorded_arm().getZ()[i]);
        }
        for(int i=0;i<ref.getRecorded_chest().getX().length;i++){
            sensor2.addAll(ref.getRecorded_chest().getX()[i],ref.getRecorded_chest().getY()[i],ref.getRecorded_chest().getZ()[i]);
        }

        Log.d("ControlDebugSensor",sensor1.getId()+"  "+sensor1.getX_values().toString());
        Log.d("ControlDebugSensor",sensor1.getId()+"  "+sensor1.getY_values().toString());
        Log.d("ControlDebugSensor",sensor1.getId()+"  "+sensor1.getZ_values().toString());
        Log.d("ControlDebugSensor",sensor2.getId()+"  "+sensor2.getX_values().toString());
        Log.d("ControlDebugSensor",sensor2.getId()+"  "+sensor2.getY_values().toString());
        Log.d("ControlDebugSensor",sensor2.getId()+"  "+sensor2.getZ_values().toString());

        globalRecordCounter = sensor1.getX_values().size() - CHECKPOINTSTEP;
        dominant_axis_arm = 0;
        dominant_axis_chest = 0;

        for(int i=0;i<ref.getCp_arm().getX().length;i++){
            checkPointsArm.getX_values().add(ref.getCp_arm().getX()[i]);
            checkPointsArm.getY_values().add(ref.getCp_arm().getY()[i]);
            checkPointsArm.getZ_values().add(ref.getCp_arm().getZ()[i]);
            checkPointsArm.getInc_values().add(ref.getCp_arm().getInc()[i]);
        }
        for(int i=0;i<ref.getCp_chest().getX().length;i++){
            checkPointsChest.getX_values().add(ref.getCp_chest().getX()[i]);
            checkPointsChest.getY_values().add(ref.getCp_chest().getY()[i]);
            checkPointsChest.getZ_values().add(ref.getCp_chest().getZ()[i]);
            checkPointsChest.getInc_values().add(ref.getCp_chest().getInc()[i]);
        }

        Log.d("ControlDebugSensor",checkPointsArm.getId()+"  "+checkPointsArm.getX_values().toString());
        Log.d("ControlDebugSensor",checkPointsArm.getId()+"  "+checkPointsArm.getY_values().toString());
        Log.d("ControlDebugSensor",checkPointsArm.getId()+"  "+checkPointsArm.getZ_values().toString());
        Log.d("ControlDebugSensor",checkPointsArm.getId()+"  "+checkPointsArm.getInc_values().toString());
        Log.d("ControlDebugSensor",checkPointsChest.getId()+"  "+checkPointsChest.getX_values().toString());
        Log.d("ControlDebugSensor",checkPointsChest.getId()+"  "+checkPointsChest.getY_values().toString());
        Log.d("ControlDebugSensor",checkPointsChest.getId()+"  "+checkPointsChest.getZ_values().toString());
        Log.d("ControlDebugSensor",checkPointsChest.getId()+"  "+checkPointsChest.getInc_values().toString());

        checkPointsNumArm = ref.getCp_num_arm();
        checkPointsNumChest = ref.getCp_num_chest();
        Log.d("ControlDebugSensor","cp num arm: "+checkPointsNumArm+" cp num chest: "+checkPointsNumChest);
        peaks_Arm.add(ref.getPeak_arm().getX());
        peaks_Arm.add(ref.getPeak_arm().getY());
        peaks_Arm.add(ref.getPeak_arm().getZ());
        peaks_Chest.add(ref.getPeak_chest().getX());
        peaks_Chest.add(ref.getPeak_chest().getY());
        peaks_Chest.add(ref.getPeak_chest().getZ());
        Log.d("ControlDebugSensor","peaks_Arm: "+peaks_Arm.toString()+" peaks_Chest: "+peaks_Chest.toString());
        sb_threshold = peaks_Arm.get(dominant_axis_arm);
        sb_ratio = 100.0/sb_threshold;

        if(peaks_Arm.get(dominant_axis_arm) - checkPointsArm.getDataSet(0)[dominant_axis_arm] < 0) //TODO: will be removed. Get motion sign with first checkpoint (1)
        {
            motionDirection = -1;
        }
        else
        {
            motionDirection = 1;
        }

        determineMotionSign();

        Log.d("ControlDebugSensor","SIGN LIMB"+"  "+limbMotionSign);
        Log.d("ControlDebugSensor","SIGN CHEST"+"  "+chestMotionSign);
        for(int i=0;i<checkPointsNumArm;i++){
            checkPointsMotionArm.add(0);
            checkPointsMotionChest.add(0);

            checkPointsMotionArm_X.add(0);
            checkPointsMotionArm_Y.add(0);
            checkPointsMotionArm_Z.add(0);

            checkPointsMotionChest_X.add(0);
            checkPointsMotionChest_Y.add(0);
            checkPointsMotionChest_Z.add(0);

            x_limb_error.add(0);
            y_limb_error.add(0);
            z_limb_error.add(0);
            x_chest_error.add(0);
            y_chest_error.add(0);
            z_chest_error.add(0);
        }

        radiusArm = calculateRadius(checkPointsArm, checkPointsNumArm);
        radiusChest = calculateRadius(checkPointsChest,  checkPointsNumChest);
//        radiusChest.set(0,15); //TODO: Check if this value is problematic (?) |   check if it is necessary
        Log.d("ControlDebugSensor", "dom_arm: "+dominant_axis_arm+" dom_chest: "+dominant_axis_chest);
        Log.d("ControlDebugSensor", "CP num: "+checkPointsNumArm+" CP num: "+ checkPointsNumChest);
        Log.d("ControlDebugSensor", "radius: "+radiusArm.toString()+" radius: "+ radiusChest.toString());
        Log.d("ControlDebugSensor","CP Arm: "+ checkPointsArm.getX_values().toString());
        Log.d("ControlDebugSensor","CP Arm: "+ checkPointsArm.getY_values().toString());
        Log.d("ControlDebugSensor","CP Arm: "+ checkPointsArm.getZ_values().toString());
        Log.d("ControlDebugSensor","CP Arm: "+ checkPointsArm.getInc_values().toString());
        Log.d("ControlDebugSensor","CP Chest: "+ checkPointsChest.getX_values().toString());
        Log.d("ControlDebugSensor","CP Chest: "+ checkPointsChest.getY_values().toString());
        Log.d("ControlDebugSensor","CP Chest: "+ checkPointsChest.getZ_values().toString());
        Log.d("ControlDebugSensor","CP Chest: "+ checkPointsChest.getInc_values().toString());
    }

    private final BroadcastReceiver mConnectionReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (BluetoothDevice.ACTION_ACL_DISCONNECTED.equals(action)) {
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                showBtConnectionLostAlertDialog();
                Log.d("PauseDebug","ReceiveFunction: "+System.currentTimeMillis());
            }
        }
    };
    void showBtConnectionLostAlertDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.bt_need_to_charge)
                .setCancelable(false)
                .setNegativeButton(R.string.ignore, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                })
                .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        JSONObject jsonObject = new JSONObject();
                        try {
                            jsonObject.put("time",session_time);
                            jsonObject.put("successful",session_succesful);
                            jsonObject.put("expected",session_expected);
                            jsonObject.put("fast",session_fast);
                            jsonObject.put("slow",session_slow);
                            jsonObject.put("both",session_wrong.get("both"));
                            jsonObject.put("chest",session_wrong.get("chest"));
                            jsonObject.put("limb",session_wrong.get("limb"));
                            saveToPref(Constants.PREF_UNSAVED_EXERCISE,jsonObject.toString());
                            saveToPref(Constants.PREF_UNSAVED_EXERCISE_ID,selected_model.get_id());

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        dialog.cancel();
                        finish();
                    }
                });

        AlertDialog alert = builder.create();
        alert.show();
    }
    void showUnsavedExerciseAlertDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.continue_to_unsaved)
                .setCancelable(false)
                .setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                        initializeSessionVariables();
                        saveToPref(Constants.PREF_UNSAVED_EXERCISE,"");
                        saveToPref(Constants.PREF_UNSAVED_EXERCISE_ID,"");
                    }
                })
                .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        String json = getFromPref(Constants.PREF_UNSAVED_EXERCISE,"");
                        try {
                            if(!json.equals("")){
                                JSONObject jsonObject = new JSONObject(json);
                                session_time = jsonObject.getInt("time");
                                session_succesful = jsonObject.getInt("successful");
                                session_expected = jsonObject.getInt("expected");
                                session_fast = jsonObject.getInt("fast");
                                session_slow = jsonObject.getInt("slow");
                                session_wrong = new HashMap<>();
                                session_wrong.put("both",jsonObject.getInt("both"));
                                session_wrong.put("chest",jsonObject.getInt("chest"));
                                session_wrong.put("limb",jsonObject.getInt("limb"));
                                time_tv.setText(countToTime(session_time));
                                String str = String.valueOf(session_succesful)+"/"+selected_model.getRepeat();
                                completed_ratio_tv.setText(str);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        dialog.cancel();
                        saveToPref(Constants.PREF_UNSAVED_EXERCISE,"");
                        saveToPref(Constants.PREF_UNSAVED_EXERCISE_ID,"");
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
    }

    @Override
    protected void onDestroy() {
        Util.dismissProgressDialog(pd);
        super.onDestroy();
    }

    void changeProgressView(CircleProgressbar pv, int color, float percent, int duration){
        pv.setForegroundProgressColor(color);
        pv.setProgressWithAnimation(percent,duration);
    }

}
