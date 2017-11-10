package com.physhome.physhome;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.github.sundeepk.compactcalendarview.CompactCalendarView;
import com.github.sundeepk.compactcalendarview.domain.Event;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;


public class NotificationsActivity extends BaseActivity {
    ImageView left_iv, right_iv;
    TextView date_tv;
    CompactCalendarView calendarView;
    ArrayList<NotificationModel> notificationModelArrayList = new ArrayList<>();
    NotificationRecyclerViewAdapter adapter;
    RecyclerView recyclerView;
    private RecyclerView.LayoutManager mLayoutManager;
    String TAG = "AlarmDebug";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notifications);
        recyclerView = (RecyclerView) findViewById(R.id.notification_recycler_view);
        left_iv = (ImageView) findViewById(R.id.calendart_left_iv);
        right_iv = (ImageView) findViewById(R.id.calendar_right_iv);
        date_tv = (TextView) findViewById(R.id.calendar_date_tv);
        prepareListData();
        recyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(mLayoutManager);
        calendarView = (CompactCalendarView) findViewById(R.id.calendarView);
        calendarView.setFirstDayOfWeek(Calendar.MONDAY);
        Calendar c = Calendar.getInstance();
        calendarView.setCurrentDate(c.getTime());
        calendarView.setUseThreeLetterAbbreviation(true);
        SimpleDateFormat month_sdf = new SimpleDateFormat("MMMM");
        SimpleDateFormat year_sdf  = new SimpleDateFormat("yyyy");
        String month = month_sdf.format(c.getTime());
        String year  = year_sdf.format(c.getTime());
        date_tv.setText(month+" "+year);
        prepareCalendarData();
        ArrayList<NotificationModel> list = getListFromEvents(c.getTime());
        if(list.isEmpty()){
            Toast.makeText(NotificationsActivity.this, R.string.there_is_not_any_activity, Toast.LENGTH_SHORT).show();
        }
        bindAdapter(list);
        left_iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calendarView.showPreviousMonth();
            }
        });
        right_iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calendarView.showNextMonth();
            }
        });
        calendarView.setListener(new CompactCalendarView.CompactCalendarViewListener() {
            @Override
            public void onDayClick(Date dateClicked) {
                ArrayList<NotificationModel> list = getListFromEvents(dateClicked);
                if(list.isEmpty()){
                    Toast.makeText(NotificationsActivity.this, R.string.there_is_not_any_activity, Toast.LENGTH_SHORT).show();
                }
                bindAdapter(list);
            }

            @Override
            public void onMonthScroll(Date firstDayOfNewMonth) {
                Log.d(TAG, "Month was scrolled to: " + firstDayOfNewMonth);
                SimpleDateFormat month_sdf = new SimpleDateFormat("MMMM");
                SimpleDateFormat year_sdf  = new SimpleDateFormat("yyyy");
                String month = month_sdf.format(firstDayOfNewMonth);
                String year  = year_sdf.format(firstDayOfNewMonth);
                date_tv.setText(month+" "+year);
            }
        });
    }
    private void prepareListData() {
        String notifications = sharedpreferences.getString("notifications"+userID,"");
        if(!notifications.equals("")){
            Type type = new TypeToken<ArrayList<NotificationModel>>() {}.getType();
            notificationModelArrayList = gson.fromJson(notifications, type);
        }else{
            notificationModelArrayList = new ArrayList<>();
        }
    }
    private void prepareCalendarData(){
        try {
            for(NotificationModel model:notificationModelArrayList){
                String str_date = model.getDate();
                DateFormat format = new SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH);
                Date date = format.parse(str_date);
                calendarView.addEvent(new Event(Color.argb(255,14,190,1),date.getTime(),model));
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }
    private ArrayList<NotificationModel> getListFromEvents(Date date){
        ArrayList<NotificationModel> list = new ArrayList<>();
        List<Event> events = calendarView.getEvents(date);
        for(Event event:events){
            NotificationModel model = (NotificationModel)event.getData();
            list.add(model);
            Log.d(TAG,model.getDate()+" "+model.getInfo());
        }
        return list;
    }
    private void bindAdapter(ArrayList<NotificationModel> list){
        adapter = new NotificationRecyclerViewAdapter(this, list);
        adapter.notifyDataSetChanged();
        recyclerView.setAdapter(adapter);

    }
}
