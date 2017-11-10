package com.physhome.physhome;

import java.util.Calendar;

/**
 * Created by Meliksah on 7/16/2017.
 */

public class ProgressModel {
    private boolean available;
    private Calendar calendar;
    private int total_movement;
    private int correct_movement;
    private int exercise_time;
    private int[] speed;
    private int[] error;

    public ProgressModel(Calendar calendar) {
        available = false;
        this.calendar = calendar;
        total_movement = 0;
        correct_movement = 0;
        exercise_time = 0;
        speed = new int[3];
        error = new int[3];
    }

    public ProgressModel(boolean available, Calendar calendar, int total_movement, int correct_movement, int exercise_time, int[] speed, int[] error) {
        this.available = available;
        this.calendar = calendar;
        this.total_movement = total_movement;
        this.correct_movement = correct_movement;
        this.exercise_time = exercise_time;
        this.speed = speed;
        this.error = error;
    }

    public boolean isAvailable() {
        return available;
    }

    public void setAvailable(boolean available) {
        this.available = available;
    }

    public Calendar getCalendar() {
        return calendar;
    }

    public void setCalendar(Calendar calendar) {
        this.calendar = calendar;
    }

    public int getTotal_movement() {
        return total_movement;
    }

    public void setTotal_movement(int total_movement) {
        this.total_movement = total_movement;
    }

    public int getCorrect_movement() {
        return correct_movement;
    }

    public void setCorrect_movement(int correct_movement) {
        this.correct_movement = correct_movement;
    }

    public int getExercise_time() {
        return exercise_time;
    }

    public void setExercise_time(int exercise_time) {
        this.exercise_time = exercise_time;
    }

    public int[] getSpeed() {
        return speed;
    }

    public void setSpeed(int[] speed) {
        this.speed = speed;
    }

    public int[] getError() {
        return error;
    }

    public void setError(int[] error) {
        this.error = error;
    }
}
