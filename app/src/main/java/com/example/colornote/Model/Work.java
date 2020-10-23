package com.example.colornote.Model;

import java.io.Serializable;
import java.util.Map;

public class Work implements Serializable {
    private String titleWork;
    private boolean isChecked;
    public Work() {
    }

    public Work(String titleWork, boolean isChecked) {
        this.titleWork = titleWork;
        this.isChecked = isChecked;
    }

    public String getTitleWork() {
        return titleWork;
    }

    public void setTitleWork(String titleWork) {
        this.titleWork = titleWork;
    }

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }
}
