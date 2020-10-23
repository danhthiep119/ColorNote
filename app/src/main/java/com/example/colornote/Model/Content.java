package com.example.colornote.Model;

import java.io.Serializable;

public class Content extends Title implements Serializable {
    private String note;

    public Content() {
    }

    public Content(String title, String reminer, boolean isSetReminer , String password ,  String date, String color,String note) {
        super(title, reminer, isSetReminer,password,date,color);
        this.note = note;
    }


    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }
}
