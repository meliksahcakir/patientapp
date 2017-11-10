package com.physhome.physhome;
/**
 * Created by meliksah on 6/6/2017.
 */

public class AlarmModel {
    private String name;
    private String message;
    private int repeat;
    private int[] alarm_hours;
    public AlarmModel() {
    }

    public AlarmModel(String name, String message, int repeat) {
        this.name = name;
        this.message = message;
        this.repeat = repeat;
        alarm_hours = new int[repeat];
        switch (repeat){
            case 1: alarm_hours[0]=12;
                    break;
            case 2: alarm_hours[0]=10; alarm_hours[1]=22;
                    break;
            case 3: alarm_hours[0]= 9; alarm_hours[1]=14; alarm_hours[2]=19;
                    break;
            case 4: alarm_hours[0]= 8; alarm_hours[1]=12; alarm_hours[2]=16; alarm_hours[3]=20;
                    break;
            case 5: alarm_hours[0]= 7; alarm_hours[1]=11; alarm_hours[2]=15; alarm_hours[3]=19; alarm_hours[4]=23;
                    break;
            case 6: alarm_hours[0]= 7; alarm_hours[1]=10; alarm_hours[2]=13; alarm_hours[3]=16; alarm_hours[4]=19; alarm_hours[5]=22;
                    break;
        }
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

    public int getRepeat() {
        return repeat;
    }

    public void setRepeat(int repeat) {
        this.repeat = repeat;
    }

    public int[] getAlarm_hours() {
        return alarm_hours;
    }

    public void setAlarm_hours(int[] alarm_hours) {
        this.alarm_hours = alarm_hours;
    }
}
