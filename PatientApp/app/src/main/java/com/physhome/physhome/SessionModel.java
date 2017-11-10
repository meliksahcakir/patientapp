package com.physhome.physhome;

/**
 * Created by Meliksah on 8/30/2017.
 */

public class SessionModel {
    private int time;
    private String date_time;
    private String session;
    private CorrectModel correct;
    private WrongModel[] wrong;
    private int total;

    public SessionModel() {
    }

    public int getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }

    public String getDate_time() {
        return date_time;
    }

    public void setDate_time(String date_time) {
        this.date_time = date_time;
    }

    public String getSession() {
        return session;
    }

    public void setSession(String session) {
        this.session = session;
    }

    public CorrectModel getCorrect() {
        return correct;
    }

    public void setCorrect(CorrectModel correct) {
        this.correct = correct;
    }

    public WrongModel[] getWrong() {
        return wrong;
    }

    public void setWrong(WrongModel[] wrong) {
        this.wrong = wrong;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }
}


class CorrectModel {
    private int fast;
    private int slow;
    private int expected;
    private int total;

    public CorrectModel() {
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

    public int getExpected() {
        return expected;
    }

    public void setExpected(int expected) {
        this.expected = expected;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }
}
class  WrongModel {
    private String key;
    private int value;

    public WrongModel() {
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }
}

