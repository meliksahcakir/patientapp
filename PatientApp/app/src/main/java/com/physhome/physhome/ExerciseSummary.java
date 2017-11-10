package com.physhome.physhome;

/**
 * Created by Meliksah on 9/26/2017.
 */

public class ExerciseSummary {
    private String date;
    private String name;
    private int exercise_time;
    private int total;
    private int normal;
    private int fast;
    private int slow;
    private int wrong;
    private int limb;
    private int chest;
    private int both;
    private int difficulty;

    public ExerciseSummary() {
    }

    public ExerciseSummary(String date, String name, int exercise_time, int total, int normal, int fast, int slow, int wrong, int limb, int chest, int both, int difficulty) {
        this.date = date;
        this.name = name;
        this.exercise_time = exercise_time;
        this.total = total;
        this.normal = normal;
        this.fast = fast;
        this.slow = slow;
        this.wrong = wrong;
        this.limb = limb;
        this.chest = chest;
        this.both = both;
        this.difficulty = difficulty;
    }

    public int getExercise_time() {
        return exercise_time;
    }

    public void setExercise_time(int exercise_time) {
        this.exercise_time = exercise_time;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public int getNormal() {
        return normal;
    }

    public void setNormal(int normal) {
        this.normal = normal;
    }

    public int getFast() {
        return fast;
    }

    public void setFast(int fast) {
        this.fast = fast;
    }

    public int getSlow() {
        return slow;
    }

    public void setSlow(int slow) {
        this.slow = slow;
    }

    public int getWrong() {
        return wrong;
    }

    public void setWrong(int wrong) {
        this.wrong = wrong;
    }

    public int getLimb() {
        return limb;
    }

    public void setLimb(int limb) {
        this.limb = limb;
    }

    public int getChest() {
        return chest;
    }

    public void setChest(int chest) {
        this.chest = chest;
    }

    public int getBoth() {
        return both;
    }

    public void setBoth(int both) {
        this.both = both;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(int difficulty) {
        this.difficulty = difficulty;
    }
}
