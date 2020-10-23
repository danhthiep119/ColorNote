package com.example.colornote.View;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageButton;

import androidx.appcompat.widget.Toolbar;

import com.example.colornote.Model.ColorList;
import com.example.colornote.R;

class ColorAdapter extends BaseAdapter {
    String[] listColor;
    Context mContext;
    ColorList colorList = new ColorList();
    Activity activity;
    ColorDialog dialog;
    public ColorAdapter(String[] listColor, Context mContext, Activity activity,ColorDialog colorDialog) {
        this.listColor = listColor;
        this.mContext = mContext;
        this.activity = activity;
        this.dialog = colorDialog;
    }

    @Override
    public int getCount() {
        return listColor.length;
    }

    @Override
    public Object getItem(int position) {
        return listColor[position];
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        convertView = LayoutInflater.from(mContext).inflate(R.layout.color_item_adapter,parent,false);
        ImageButton ChangeColor = convertView.findViewById(R.id.ChangeColor);
        ChangeColor.setBackgroundColor(Color.parseColor(listColor[position]));
        ChangeColor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(activity instanceof InputContent) {
                    InputContent content = (InputContent) activity;
                    content.toolbar.setBackgroundColor(Color.parseColor(colorList.titleColor[position]));
                    content.btnChangeColor.setBackgroundColor(Color.parseColor(colorList.backgroundColor[position]));
                    content.llTitleZone.setBackgroundColor(Color.parseColor(colorList.backgroundColor[position]));
                    content.txtInputContent.setBackgroundColor(Color.parseColor(colorList.backgroundColor[position]));
                    content.bottomZone.setBackgroundColor(Color.parseColor(colorList.titleColor[position]));
                    content.color = String.valueOf(position);
                }
                else if(activity instanceof CheckList){
                    CheckList checkList = (CheckList) activity;
                    checkList.toolbar.setBackgroundColor(Color.parseColor(colorList.titleColor[position]));
                    checkList.btnChangeColor.setBackgroundColor(Color.parseColor(colorList.backgroundColor[position]));
                    checkList.llTitleZone.setBackgroundColor(Color.parseColor(colorList.backgroundColor[position]));
                    checkList.bottomZone.setBackgroundColor(Color.parseColor(colorList.titleColor[position]));
                    checkList.llListCheck.setBackgroundColor(Color.parseColor(colorList.backgroundColor[position]));
                    checkList.color = String.valueOf(position);
                }
                else if(activity instanceof Setting){
                    Setting setting =  (Setting) activity;
                    setting.btnChangeColor.setBackgroundColor(Color.parseColor(colorList.backgroundColor[position]));
                    SharedPreferences sharedPreferences = mContext.getSharedPreferences("SaveSetting",Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putInt("colorPosition",position);
                    editor.commit();
                }
                else if(activity instanceof SearchView){
                    SearchView searchView = (SearchView) activity;
                    searchView.color = String.valueOf(position);
                    searchView.btnChangeColor.setBackgroundColor(Color.parseColor(colorList.backgroundColor[position]));
                }
                dialog.dismiss();
            }
        });
        return convertView;
    }
}
