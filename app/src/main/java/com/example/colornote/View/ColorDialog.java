package com.example.colornote.View;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.widget.EditText;
import android.widget.GridView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;

import com.example.colornote.Model.ColorList;
import com.example.colornote.R;

class ColorDialog extends Dialog {
    Activity mContext;
    ColorList colorList = new ColorList();
    Activity activity;
    public ColorDialog(@NonNull Activity context,Activity activity) {
        super(context);
        this.mContext = context;
        this.activity = activity;
        setContentView(R.layout.color_dialog);
        GridView gvColor = findViewById(R.id.gvColor);
        gvColor.setAdapter(new ColorAdapter(colorList.backgroundColor,mContext,activity,this));
    }
}
