package com.physhome.physhome;

/**
 * Created by Monster on 16.5.2017.
 */

public class ExerciseModel {
    private String _id;
    private String exercise_id;
    private String created_at;
    private int repeat;
    private int total_day;
    private int current_day;
    private String treatment_id;
    private AccelerometerReferenceModel reference;
    private SessionModel[] exercise_history;
    public ExerciseModel() {
    }

    public ExerciseModel(String _id, String exercise_id, String created_at, int repeat, int total_day, int current_day, AccelerometerReferenceModel reference, SessionModel[] exercise_history) {
        this._id = _id;
        this.exercise_id = exercise_id;
        this.created_at = created_at;
        this.repeat = repeat;
        this.total_day = total_day;
        this.current_day = current_day;
        this.reference = reference;
        this.exercise_history = exercise_history;
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getExercise_id() {
        return exercise_id;
    }

    public void setExercise_id(String exercise_id) {
        this.exercise_id = exercise_id;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public int getRepeat() {
        return repeat;
    }

    public void setRepeat(int repeat) {
        this.repeat = repeat;
    }

    public int getTotal_day() {
        return total_day;
    }

    public void setTotal_day(int total_day) {
        this.total_day = total_day;
    }

    public int getCurrent_day() {
        return current_day;
    }

    public void setCurrent_day(int current_day) {
        this.current_day = current_day;
    }

    public AccelerometerReferenceModel getReference() {
        return reference;
    }

    public void setReference(AccelerometerReferenceModel reference) {
        this.reference = reference;
    }

    public SessionModel[] getExercise_history() {
        return exercise_history;
    }

    public void setExercise_history(SessionModel[] exercise_history) {
        this.exercise_history = exercise_history;
    }

    public String getTreatment_id() {
        return treatment_id;
    }

    public void setTreatment_id(String treatment_id) {
        this.treatment_id = treatment_id;
    }
}
