package com.physhome.physhome;

/**
 * Created by Meliksah on 8/8/2017.
 */

public class DrugModel {
    private String name;
    private String comment;
    private int how_many;

    public DrugModel() {

    }

    public DrugModel(String name, String comment, int how_many) {
        this.name = name;
        this.comment = comment;
        this.how_many = how_many;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public int getHow_many() {
        return how_many;
    }

    public void setHow_many(int how_many) {
        this.how_many = how_many;
    }
}
