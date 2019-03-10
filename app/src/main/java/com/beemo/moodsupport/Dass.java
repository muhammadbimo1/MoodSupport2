package com.beemo.moodsupport;

import java.util.Date;

public class Dass {
    int anxiety;
    int depression;
    int stress;
    Date timestamp;

    public Dass(){

    }

    public Dass(int anxiety, int depression, int stress, Date timestamp) {
        this.anxiety = anxiety;
        this.depression = depression;
        this.stress = stress;
        this.timestamp = timestamp;
    }

    public int getAnxiety() {
        return anxiety;
    }

    public void setAnxiety(int anxiety) {
        this.anxiety = anxiety;
    }

    public int getDepression() {
        return depression;
    }

    public void setDepression(int depression) {
        this.depression = depression;
    }

    public int getStress() {
        return stress;
    }

    public void setStress(int stress) {
        this.stress = stress;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }
}
