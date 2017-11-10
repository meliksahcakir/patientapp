package com.physhome.physhome;

/**
 * Created by Meliksah on 8/8/2017.
 */

public class ExerciseWarningModel {
    private int exercise_id;
    private int dominant_axis_arm;
    private int dominant_axis_chest;
    private String msg_limb_1;
    private String msg_limb_2;
    private String msg_limb_3;
    private String msg_chest_1;
    private String msg_chest_2;
    private String msg_chest_3;
    private String error_msg_1;
    private String error_msg_2;
    private String msg_peak_1;
    private String msg_peak_2;
    private int tolarenca_x_limb;
    private int tolarenca_y_limb;
    private int tolarenca_z_limb;
    private int tolarenca_x_chest;
    private int tolarenca_y_chest;
    private int tolarenca_z_chest;

    public ExerciseWarningModel(int exercise_id) {
        this.exercise_id = exercise_id;
        switch (exercise_id){
            case 1: //TODO: First exercise has zero(0) index.
                dominant_axis_arm = 0;
                dominant_axis_chest = 0;
                msg_limb_1  = "AYAGINIZI FAZLA YANA ACTINIZ";
                msg_peak_1  = "AYAGINIZI YETERINCE YUKARI KALDIRMADINIZ";
                msg_peak_2  = "AYAGINIZI FAZLA YUKARI KALDIRDINIZ";
                msg_chest_1 = "DIK DURUN";
                error_msg_1 = "HATALI HAREKET";
                error_msg_2 = "SADECE 1 CHECKPOINT HATALI";
                tolarenca_x_limb = 3;
                tolarenca_y_limb = 5;
                tolarenca_z_limb = 5;
                break;
            case 2:
                dominant_axis_arm = 0;
                dominant_axis_chest = 0;
                msg_limb_1  = "KOLUNUZU FAZLA YANA ACTINIZ";
                msg_peak_1  = "KOLUNUZU YETERINCE YUKARI KALDIRMADINIZ";
                msg_peak_2  = "KOLUNUZU FAZLA YUKARI KALDIRDINIZ";
                msg_chest_1 = "DIK DURUN";
                error_msg_1 = "HATALI HAREKET";
                error_msg_2 = "SADECE 1 CHECKPOINT HATALI";
                break;
            case 3:
                dominant_axis_arm = 0;
                dominant_axis_chest = 0;
                msg_limb_1  = "DIZLERINIZI FAZLA YANA ACTINIZ";
                msg_peak_1  = "DIZLERINIZI YETERINCE KIRMADINIZ";
                msg_peak_2  = "DIZLERINIZI FAZLA KIRDINIZ";
                msg_chest_1 = "DIK DURUN";
                error_msg_1 = "HATALI HAREKET";
                error_msg_2 = "SADECE 1 CHECKPOINT HATALI";
                break;
            case 4:
                dominant_axis_arm = 0;
                dominant_axis_chest = 0;
                msg_limb_1 = "KOLUNUZU FAZLA YANA ACTINIZ";
                msg_peak_1 = "KOLUNUZU YETERINCE KALDIRMADINIZ";
                msg_peak_2 = "KOLUNUZU FAZLA KALDIRDINIZ";
                msg_chest_1 = "DIK DURUN";
                error_msg_1 = "HATALI HAREKET";
                error_msg_2 = "SADECE 1 CHECKPOINT HATALI";
                break;
            case 5:
                dominant_axis_arm = 0;
                dominant_axis_chest = 0;
                msg_limb_1 = "KOLUNUZU FAZLA YANA ACTINIZ";
                msg_peak_1 = "KOLUNUZU YETERINCE KALDIRMADINIZ";
                msg_peak_2 = "KOLUNUZU FAZLA KALDIRDINIZ";
                msg_chest_1 = "DIK DURUN";
                error_msg_1 = "HATALI HAREKET";
                error_msg_2 = "SADECE 1 CHECKPOINT HATALI";
                break;
            case 6:
                dominant_axis_arm = 2;
                dominant_axis_chest = 2;
                msg_limb_1 = "KOLUNUZU FAZLA YANA ACTINIZ";
                msg_peak_1 = "KOLUNUZU YETERINCE ARKAYA GERDIRMEDINIZ";
                msg_peak_2 = "KOLUNUZU FAZLA ARKAYA GERDIRDINIZ";
                msg_chest_1 = "DIK DURUN";
                error_msg_1 = "HATALI HAREKET";
                error_msg_2 = "SADECE 1 CHECKPOINT HATALI";
                break;
            case 7:
                dominant_axis_arm = 0;
                dominant_axis_chest = 0;
                msg_limb_1 = "KOLUNUZU GOVDENIZE DIK BIR SEKILDE YUKARI KALDIRINIZ";
                msg_peak_1 = "KOLUNUZU YETERINCE YUKARI KALDIRMADINIZ";
                msg_peak_2 = "KOLUNUZU FAZLA YUKARI KALDIRDINIZ";
                msg_chest_1 = "DIK DURUN";
                error_msg_1 = "HATALI HAREKET";
                error_msg_2 = "SADECE 1 CHECKPOINT HATALI";
                break;
            case 8:
                dominant_axis_arm = 0;
                dominant_axis_chest = 0;
                msg_limb_1 = "KOLUNUZU FAZLA ONE/ARKAYA ACTINIZ";
                msg_peak_1 = "KOLUNUZU YETERINCE YUKARI KALDIRMADINIZ";
                msg_peak_2 = "KOLUNUZU FAZLA YUKARI KALDIRDINIZ";
                msg_chest_1 = "DIK DURUN";
                error_msg_1 = "HATALI HAREKET";
                error_msg_2 = "SADECE 1 CHECKPOINT HATALI";
                break;
            case 9:
                dominant_axis_arm = 0; //TODO: case 9 will completely change
                dominant_axis_chest = 0;
                msg_limb_1 = "YETERINCE YUKARI KALDIRMADIN";
                msg_limb_2 = "KOLLARIN COK ACIK";
                msg_limb_3 = "KOLLARIN COK KAPALI";
                msg_chest_1 = "YETERINCE ÖNE EGILMEDIN";
                msg_chest_2 = "COK FAZLA EGILDIN";
                msg_chest_3 = "DIK DURUN";
                break;
        }
    }

    public int getExercise_id() {
        return exercise_id;
    }

    public int getDominant_axis_arm() {
        return dominant_axis_arm;
    }

    public int getDominant_axis_chest() {
        return dominant_axis_chest;
    }

    public String getMsg_limb_1() {
        return msg_limb_1;
    }

    public String getMsg_limb_2() {
        return msg_limb_2;
    }

    public String getMsg_limb_3() {
        return msg_limb_3;
    }

    public String getMsg_chest_1() {
        return msg_chest_1;
    }

    public String getMsg_chest_2() {
        return msg_chest_2;
    }

    public String getMsg_chest_3() {
        return msg_chest_3;
    }

    public String getMsg_peak_1() { return msg_peak_1; }

    public String getMsg_peak_2() { return msg_peak_2; }

    public String getError_msg_1() {
        return error_msg_1;
    }

    public String getError_msg_2() {
        return error_msg_2;
    }

    public int getTolerance_x_limb() { return tolarenca_x_limb; }

    public int getTolerance_y_limb() { return tolarenca_y_limb; }

    public int getTolerance_z_limb() { return tolarenca_z_limb; }

    public int getTolerance_x_chest() { return tolarenca_x_chest; }

    public int getTolerance_y_chest() { return tolarenca_y_chest; }

    public int getTolerance_z_chest() { return tolarenca_z_chest; }

}
