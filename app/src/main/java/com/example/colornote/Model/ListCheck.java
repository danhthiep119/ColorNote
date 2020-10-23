package com.example.colornote.Model;

import java.io.Serializable;
import java.util.List;

public class ListCheck extends Title implements Serializable {
    private boolean isCheck;
    private List<Work> workList;

    public ListCheck(){

    }

    public ListCheck(String title, String reminer, boolean isSetReminder, String password,String date, String color ,boolean isCheck, List<Work> workList) {
        super(title, reminer, isSetReminder,password,date,color);
        this.isCheck = isCheck;
        this.workList = workList;
    }

    public List<Work> getWorkList() {
        return workList;
    }

    public void setWorkList(List<Work> workList) {
        this.workList = workList;
    }


    public boolean isCheck() {
        return isCheck;
    }

    public void setCheck(boolean check) {
        isCheck = check;
    }

}
