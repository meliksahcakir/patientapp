package com.physhome.physhome;

/**
 * Created by Meliksah on 8/31/2017.
 */

public class DrugAlarmModel {
    private boolean checked;
    private int id;
    private String name;
    private String message;
    private int day;
    private int hour;
    private int minute;

    public DrugAlarmModel() {
    }

    public DrugAlarmModel(boolean checked, int id, String name, String message, int day, int hour, int minute) {
        this.checked = checked;
        this.id = id;
        this.name = name;
        this.message = message;
        this.day = day;
        this.hour = hour;
        this.minute = minute;
    }

    public boolean isChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day = day;
    }

    public int getHour() {
        return hour;
    }

    public void setHour(int hour) {
        this.hour = hour;
    }

    public int getMinute() {
        return minute;
    }

    public void setMinute(int minute) {
        this.minute = minute;
    }
}
