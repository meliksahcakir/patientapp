package com.physhome.physhome;

/**
 * Created by Meliksah on 11/7/2017.
 */

public class UserModel {
    private String phone;
    private String name_surname;
    private String _id;
    private String blood_type;
    private int age;
    private int height;
    private String doctor_id;
    private String password;
    public UserModel() {
    }

    public UserModel(String phone, String name_surname, String _id, String blood_type, int age, int height, String doctor_id, String password) {
        this.phone = phone;
        this.name_surname = name_surname;
        this._id = _id;
        this.blood_type = blood_type;
        this.age = age;
        this.height = height;
        this.doctor_id = doctor_id;
        this.password = password;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getName_surname() {
        return name_surname;
    }

    public void setName_surname(String name_surname) {
        this.name_surname = name_surname;
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getBlood_type() {
        return blood_type;
    }

    public void setBlood_type(String blood_type) {
        this.blood_type = blood_type;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public String getDoctor_id() {
        return doctor_id;
    }

    public void setDoctor_id(String doctor_id) {
        this.doctor_id = doctor_id;
    }

    @Override
    public String toString() {
        return name_surname;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
