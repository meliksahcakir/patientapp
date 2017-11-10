package com.physhome.physhome;

/**
 * Created by Meliksah on 8/7/2017.
 */

public class TreatmentModel {
    private String _id;
    private String[] exercises;
    private String problem;
    private String comment;
    private String hospital_name;
    private String doctor_name;
    private String created_at;
    private DrugModel[] drugs;
    private String updated_at;
    String patient_id;
    String doctor_id;

    public TreatmentModel() {

    }

    public TreatmentModel(String problem, String comment, String hospital_name, String doctor_name, String created_at, DrugModel[] drugs) {
        this.problem = problem;
        this.comment = comment;
        this.hospital_name = hospital_name;
        this.doctor_name = doctor_name;
        this.created_at = created_at;
        this.drugs = drugs;
    }

    public String getProblem() {
        return problem;
    }

    public void setProblem(String problem) {
        this.problem = problem;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getHospital_name() {
        return hospital_name;
    }

    public void setHospital_name(String hospital_name) {
        this.hospital_name = hospital_name;
    }

    public String getDoctor_name() {
        return doctor_name;
    }

    public void setDoctor_name(String doctor_name) {
        this.doctor_name = doctor_name;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public DrugModel[] getDrugs() {
        return drugs;
    }

    public void setDrugs(DrugModel[] drugs) {
        this.drugs = drugs;
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String[] getExercises() {
        return exercises;
    }

    public void setExercises(String[] exercises) {
        this.exercises = exercises;
    }

    public String getUpdated_at() {
        return updated_at;
    }

    public void setUpdated_at(String updated_at) {
        this.updated_at = updated_at;
    }

    public String getPatient_id() {
        return patient_id;
    }

    public void setPatient_id(String patient_id) {
        this.patient_id = patient_id;
    }

    public String getDoctor_id() {
        return doctor_id;
    }

    public void setDoctor_id(String doctor_id) {
        this.doctor_id = doctor_id;
    }
}
