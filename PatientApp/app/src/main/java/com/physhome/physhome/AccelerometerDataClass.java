package com.physhome.physhome;

import java.util.ArrayList;
import java.util.Collections;

/**
 * Created by Meliksah on 7/9/2017.
 */

public class AccelerometerDataClass {
    private ArrayList<Integer> x_values;
    private ArrayList<Integer> y_values;
    private ArrayList<Integer> z_values;
    private ArrayList<Integer> inc_values;
    private int id;
    private int diff_x;
    private int diff_y;
    private int diff_z;

    public AccelerometerDataClass(int id) {
        x_values = new ArrayList<>();
        y_values = new ArrayList<>();
        z_values = new ArrayList<>();
        inc_values = new ArrayList<>();
        this.id = id;
        diff_x = 0;
        diff_y = 0;
        diff_z = 0;
    }

    public AccelerometerDataClass(ArrayList<Integer> x_values, ArrayList<Integer> y_values, ArrayList<Integer> z_values) {
        this.x_values = x_values;
        this.y_values = y_values;
        this.z_values = z_values;
        diff_x = 0;
        diff_y = 0;
        diff_z = 0;
    }

    public ArrayList<Integer> getX_values() {
        return x_values;
    }

    public void setX_values(ArrayList<Integer> x_values) {
        this.x_values = x_values;
    }

    public ArrayList<Integer> getY_values() {
        return y_values;
    }

    public void setY_values(ArrayList<Integer> y_values) {
        this.y_values = y_values;
    }

    public ArrayList<Integer> getZ_values() {
        return z_values;
    }

    public void setZ_values(ArrayList<Integer> z_values) {
        this.z_values = z_values;
    }
    public int getMax_X(){
        return Collections.max(x_values);
    }
    public int getMax_Y(){
        return Collections.max(y_values);
    }
    public int getMax_Z(){
        return Collections.max(z_values);
    }
    public int getMin_X(){
        return Collections.min(x_values);
    }
    public int getMin_Y(){
        return Collections.min(y_values);
    }
    public int getMin_Z(){
        return Collections.min(z_values);
    }
    public void clear(){
        if(!x_values.isEmpty()){
            x_values.clear();
        }
        if(!y_values.isEmpty()){
            y_values.clear();
        }
        if(!z_values.isEmpty()){
            z_values.clear();
        }
    }
    public void addXdata(int x){
        x_values.add(x);
    }
    public void addYdata(int y){
        y_values.add(y);
    }
    public void addZdata(int z){
        z_values.add(z);
    }
    public void addInc(int inc){
        inc_values.add(inc);
    }
    public void addAll(int x, int y, int z){
        addXdata(x);
        addYdata(y);
        addZdata(z);
    }
    public void addAll(int x, int y, int z, int inc){
        addXdata(x);
        addYdata(y);
        addZdata(z);
        addInc(inc);
    }
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getDiff_x() {
        return diff_x;
    }

    public void setDiff_x() {
        int size = x_values.size();
        if(size>1){
            this.diff_x = x_values.get(size-1)-x_values.get(size-2);
        }
    }

    public int getDiff_y() {
        return diff_y;
    }

    public void setDiff_y() {
        int size = y_values.size();
        if(size>1){
            this.diff_y = y_values.get(size-1)-y_values.get(size-2);
        }
    }

    public int getDiff_z() {
        return diff_z;
    }

    public void setDiff_z() {
        int size = z_values.size();
        if(size>1){
            this.diff_z = z_values.get(size-1)-z_values.get(size-2);
        }
    }

    public ArrayList<Integer> getInc_values() {
        return inc_values;
    }

    public void setInc_values(ArrayList<Integer> inc_values) {
        this.inc_values = inc_values;
    }
    public int[] getDataSet(int num){
        int[] data = new int[4];
        data[0] = x_values.get(num);
        data[1] = y_values.get(num);
        data[2] = z_values.get(num);
        data[3] = inc_values.get(num);
       return data;
    }
    public int[] getSensorDataSet(int num){
        int[] data = new int[3];
        data[0] = x_values.get(num);
        data[1] = y_values.get(num);
        data[2] = z_values.get(num);
        return data;
    }
}
