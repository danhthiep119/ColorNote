package com.example.colornote.Model;

import java.io.Serializable;

public class Title implements Serializable {
    private String title;
    private String reminer;
    private boolean isSetReminder;
    private String password;
    private String date;
    private String color;
    public Title() {
    }

    public Title(String title, String reminer,boolean isSetReminder,String password,String date,String color) {
        this.title = title;
        this.reminer = reminer;
        this.isSetReminder = isSetReminder;
        this.password = password;
        this.date = date;
        this.color = color;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public boolean isSetReminder() {
        return isSetReminder;
    }

    public void setSetReminder(boolean setReminder) {
        isSetReminder = setReminder;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getReminer() {
        return reminer;
    }

    public void setReminer(String reminer) {

        this.reminer = reminer;
    }

    public boolean getIsSetReminder() {
        return isSetReminder;
    }

    public void setIsSetReminder(boolean isSetReminder) {
        this.isSetReminder = isSetReminder;
    }
}
