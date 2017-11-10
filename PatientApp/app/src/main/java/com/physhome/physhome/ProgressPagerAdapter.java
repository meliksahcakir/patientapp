package com.physhome.physhome;

import android.content.Context;
import android.graphics.Color;
import android.support.v4.view.PagerAdapter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.jackandphantom.circularprogressbar.CircleProgressbar;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;

/**
 * Created by Meliksah on 7/16/2017.
 */

public class ProgressPagerAdapter extends PagerAdapter {
    private Context context;
    private ArrayList<ProgressModel> models;
    int span;
    public ProgressPagerAdapter(Context context, ArrayList<ProgressModel> models, int span) {
        this.context = context;
        this.models = models;
        this.span = span;
    }

    @Override
    public int getCount() {
        int count = (int) Math.ceil(((1.0*models.size()/span)));
        Log.d("ProgressDebug","Count:"+count);
        return count;

    }

    @Override
    public Object instantiateItem(ViewGroup collection, int position) {
        int correct=0, total=0, time=0;
        int[] speed = {0,0,0};
        int[] error = {0,0,0};
        String[] str_speed = {"Fast","Normal","Slow"};
        String[] str_error = {"Limb","Chest","Both"};
        boolean available = false;
        for(int i=0;i<span&& i+position*span <models.size();i++){
            ProgressModel model = models.get(position*span+i);
            total+=model.getTotal_movement();
            correct+=model.getCorrect_movement();
            time+=model.getExercise_time();
            for(int j=0;j<3;j++){
                speed[j]+=model.getSpeed()[j];
                error[j]+=model.getError()[j];
            }
            if(!available){
                available = model.isAvailable();
            }
        }
        LayoutInflater inflater = LayoutInflater.from(context);
        ViewGroup layout = (ViewGroup) inflater.inflate(R.layout.progress_layout, collection, false);
        float percentage;
        String str_percentage;
        if(total==0){
            percentage=0;
            str_percentage = "--";
        }else{
            percentage = (float) (correct*100.0/total);
            str_percentage = String.format(Locale.US, "%.1f",percentage)+ " %";
        }
        String str_time = String.format(Locale.US, "%.1f",time/60.0);
        TextView no_activity = (TextView) layout.findViewById(R.id.progress_no_activity_tv);
        if(!available){
            no_activity.setVisibility(View.VISIBLE);
        }
        TextView percentage_tv = (TextView) layout.findViewById(R.id.progress_correct_ratio_tv);
        TextView time_tv = (TextView) layout.findViewById(R.id.progress_time_tv);
        CircleProgressbar correct_pv = (CircleProgressbar) layout.findViewById(R.id.progress_correct_ratio_pv);
        CircleProgressbar time_pv = (CircleProgressbar) layout.findViewById(R.id.progress_time_pv);
        PieChart speed_chart = (PieChart) layout.findViewById(R.id.progress_speed_piechart);
        PieChart error_chart = (PieChart) layout.findViewById(R.id.progress_error_piechart);
        percentage_tv.setText(str_percentage);
        time_tv.setText(str_time);
        correct_pv.setProgressWithAnimation(percentage,2000);
        time_pv.setProgressWithAnimation(100,2000);
        setPieChart(speed_chart,speed,str_speed,"");
        setPieChart(error_chart,error,str_error,"");
        collection.addView(layout);
        return layout;
    }

    void setPieChart(PieChart pieChart, int[] data, String[] str_data, String legend){
        final int[] piecolors = new int[]{
                context.getResources().getColor(R.color.colorRedTransparent),
                context.getResources().getColor(R.color.colorLightGreen),
                context.getResources().getColor(R.color.colorPhyshome)};
        ArrayList<PieEntry> entries = new ArrayList<>();
        int size = data.length;
        for(int i=0;i<size;i++){
            entries.add(new PieEntry(data[i]*1.0f,str_data[i]));
        }
        PieDataSet dataSet = new PieDataSet(entries,legend);
        dataSet.setColors(piecolors,150);
        PieData pieData = new PieData(dataSet);
        pieData.setValueTextColor(Color.WHITE);
        pieData.setValueTextSize(14f);
        pieChart.setData(pieData);

        Legend l = pieChart.getLegend();
        l.setTextSize(14f);
        l.setVerticalAlignment(Legend.LegendVerticalAlignment.BOTTOM);
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.CENTER);
        l.setOrientation(Legend.LegendOrientation.HORIZONTAL);
        l.setDrawInside(false);
        l.setTextColor(Color.WHITE);
        l.setEnabled(false);
        pieChart.getDescription().setEnabled(false);
        pieChart.setCenterTextColor(Color.WHITE);
        pieChart.setDrawHoleEnabled(true);
        pieChart.setHoleColor(Color.argb(180,255,255,255));

        pieChart.setTransparentCircleColor(Color.WHITE);
        pieChart.setTransparentCircleAlpha(200);

        pieChart.setHoleRadius(38f);
        pieChart.setTransparentCircleRadius(41f);

        pieChart.setDrawCenterText(true);
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }
    @Override
    public void destroyItem(ViewGroup collection, int position, Object view) {
        collection.removeView((View) view);
    }

    @Override
    public CharSequence getPageTitle(int position) {
        SimpleDateFormat dateformat = new SimpleDateFormat("dd-MM-yyyy");
        ProgressModel model = models.get(position*span);
        String datetime = dateformat.format(model.getCalendar().getTime());
        if(span>1){
            ProgressModel model2;
            if(models.size()<span*position+span){
                model2 = models.get(models.size()-1);
            }else{
                model2 = models.get(span*position+span-1);
            }
            datetime += " - "+dateformat.format(model2.getCalendar().getTime());
        }
        return datetime;
    }
}
