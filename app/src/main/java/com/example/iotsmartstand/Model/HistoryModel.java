package com.example.iotsmartstand.Model;

public class HistoryModel {
    String date,time;
    int amount;

    public HistoryModel(){}
    public HistoryModel(String date, String time, int amount) {
        this.date = date;
        this.time = time;
        this.amount = amount;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

   public void setTime(String time) {
      this.time = time;
  }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }
}
