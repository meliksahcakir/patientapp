package com.physhome.physhome;

/**
 * Created by Monster on 16.5.2017.
 */

public class NotificationModel {
    private String imageID;
    private String info;
    private String date;
    private ExerciseSummary summary;
    public NotificationModel() {
    }

    public NotificationModel(String date, String imageID, String info, ExerciseSummary summary) {
        this.date =date;
        this.imageID = imageID;
        this.info = info;
        this.summary = summary;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getImageID() {
        return imageID;
    }

    public void setImageID(String imageID) {
        this.imageID = imageID;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public ExerciseSummary getSummary() {
        return summary;
    }

    public void setSummary(ExerciseSummary summary) {
        this.summary = summary;
    }
}
