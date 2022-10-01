package com.example.iotsmartstand.Model;

public class TimeLineModel {
    String date,tag,time;
    public TimeLineModel(){}
    public TimeLineModel(String date, String tag, String time ) {
        this.date = date;
        this.tag = tag;
        this.time = time;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
