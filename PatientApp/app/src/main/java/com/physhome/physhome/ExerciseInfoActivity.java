package com.physhome.physhome;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Type;

import pl.droidsonroids.gif.GifDrawable;
import pl.droidsonroids.gif.GifImageView;

/**
 * Created by Meliksah on 7/19/2017.
 */

public class ExerciseInfoActivity extends BaseActivity {
    GifImageView imageView;
    TextView title_tv;
    ExerciseInfoModel info_model;
    ExerciseModel model;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exercise_info);
        imageView = (GifImageView) findViewById(R.id.exercise_info_imageview);
        title_tv = (TextView) findViewById(R.id.exercise_info_title_tv);
        Log.d("InfoDebug","OK1");
        Intent intent = getIntent();
        Log.d("InfoDebug","OK2");
        if (null != intent) {
            Log.d("InfoDebug","OK3");
            Bundle bundle = intent.getExtras();
            String json = bundle.getString("model","");
            String info_json = bundle.getString("info_model","");
            String deneme = bundle.getString("deneme","");
            String imageID = bundle.getString("imageID","");
            String title = bundle.getString("title","");
            Log.d("InfoDebug",json);
            if(!info_json.equals("")){
                Type type = new TypeToken<ExerciseInfoModel>() {}.getType();
                info_model = gson.fromJson(info_json, type);
                try {
                    if(!info_model.getGif_id().equals("")){
                        imageID = info_model.getGif_id();
                    }
                    if(!info_model.getName().equals("")){
                        title = info_model.getName();
                    }
                    if(!imageID.equals("")){
                        String gif_str = info_model.getGif_id()+".gif";
                        File gifFile = new File(getFilesDir(),gif_str);
                        GifDrawable gifFromFile = new GifDrawable(gifFile);
                        imageView.setImageDrawable(gifFromFile);
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                }
                title_tv.setText(title);
            }
        }
    }
}
