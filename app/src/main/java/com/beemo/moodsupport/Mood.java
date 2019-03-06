package com.beemo.moodsupport;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.Exclude;

import java.sql.Time;
import java.util.Date;

public class Mood {
    int mood;
    Date timestamp;
    //String id;
    public Mood(){

    }
    public Mood(int mood, Date timestamp) {
        this.mood = mood;
        this.timestamp = timestamp;
    }
    public int getMood() {
        return mood;
    }

    public void setMood(int mood) {
        this.mood = mood;
    }


    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    /*@Exclude
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }*/


}
