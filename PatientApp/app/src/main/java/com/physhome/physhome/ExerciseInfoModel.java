package com.physhome.physhome;

/**
 * Created by Meliksah on 8/23/2017.
 */

public class ExerciseInfoModel {
    private String _id;
    private String name;
    private String gif_id;
    private String image_id;
    private String[] error_messages;

    public ExerciseInfoModel() {
    }

    public ExerciseInfoModel(String _id, String name, String gif_id, String[] error_messages) {
        this._id = _id;
        this.name = name;
        this.gif_id = gif_id;
        this.error_messages = error_messages;
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getGif_id() {
        return gif_id;
    }

    public void setGif_id(String gif_id) {
        this.gif_id = gif_id;
    }

    public String[] getError_messages() {
        return error_messages;
    }

    public void setError_messages(String[] error_messages) {
        this.error_messages = error_messages;
    }

    public String getImage_id() {
        return image_id;
    }

    public void setImage_id(String image_id) {
        this.image_id = image_id;
    }
}
