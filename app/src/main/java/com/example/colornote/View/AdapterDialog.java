package com.example.colornote.View;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.media.MediaPlayer;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;


import com.example.colornote.R;

import java.io.IOException;
import java.util.List;

public class AdapterDialog extends BaseAdapter {
    String[] list;
    Activity mContext;
    Setting setting;

    public AdapterDialog(String[] list, Activity mContext, Setting setting) {
        this.list = list;
        this.mContext = mContext;
        this.setting = setting;
    }

    @Override
    public int getCount() {
        return list.length;
    }

    @Override
    public Object getItem(int position) {
        return list[position];
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        convertView = LayoutInflater.from(mContext).inflate(R.layout.option_adapter, parent, false);
        LinearLayout llRadioGroup = convertView.findViewById(R.id.llRadioGroup);
        RadioGroup group = new RadioGroup(mContext);
        RadioButton[] rdbutton = new RadioButton[list.length];
        group.setOrientation(RadioGroup.VERTICAL);
        if (position == 0) {
            for (int i = 0; i < list.length; i++) {
                rdbutton[i] = new RadioButton(mContext);
                rdbutton[i].setId(i);
                rdbutton[i].setText(list[i]);
                group.addView(rdbutton[i]);
                rdbutton[i].setOnCheckedChangeListener(listener);
            }
        }

//        Typeface font = mContext.getResources().getFont(listfont[position]);
//        rdbutton.setTypeface(font);
//        for(int i=0;i<list.length-1;i++) {
//            rdbutton.setId(i);
//            rdbutton.setText(list[i]);
//        if(setting.mode.equals("font")){
//            int[] listfont = {
//                    R.font.be_honest,
//                    R.font.authentic_script_rough,
//                    R.font.beforetherain_personal_use_demo,
//                    R.font.california_sun,
//                    R.font.casking_cream_script,
//                    R.font.lovingyou
//                    };
//            Typeface font = mContext.getResources().getFont(listfont[i]);
//            rdbutton.setTypeface(font);
//        }
//        }
        llRadioGroup.addView(group);
        return convertView;
    }

    CompoundButton.OnCheckedChangeListener listener = new CompoundButton.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            if (isChecked) {
                buttonView.setChecked(true);
                SharedPreferences sharedPreferences = mContext.getSharedPreferences("SaveSetting", mContext.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                System.out.println(buttonView.getText().toString());
                switch (setting.mode) {
                    case "font":
                        setting.btnFont.setText(buttonView.getText().toString());
                        editor.putString("font", buttonView.getText().toString());
                        break;
                    case "default-font-size":
                        setting.btnFontSize.setText(buttonView.getText().toString());
                        editor.putString("default-font-size", buttonView.getText().toString());
                        break;
                    case "theme":
                        setting.btnTheme.setText(buttonView.getText().toString());
                        editor.putString("theme", buttonView.getText().toString());
                        break;
                    case "item-height":
                        setting.btnItemHeight.setText(buttonView.getText().toString());
                        editor.putString("item-height", buttonView.getText().toString());
                        break;
                    case "sound-reminder":
                        setting.btnSound.setText(buttonView.getText().toString());
                        editor.putString("sound-reminder", buttonView.getText().toString());
                        try {
                            openMusic(buttonView.getId());
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        break;
                    case "font-size":
                        setting.btnSize.setText(buttonView.getText().toString());
                        editor.putString("font-size", buttonView.getText().toString());
                        break;
                    case "screen":
                        setting.btnScreen.setText(buttonView.getText().toString());
                        editor.putString("screen", buttonView.getText().toString());
                        break;
                }
                editor.commit();
            } else buttonView.setChecked(false);
        }
    };

    private void openMusic(int position) throws IOException {
        int[] listAlarm = {
                R.raw.analog_watch,
                R.raw.bleep_alarm,
                R.raw.missile_alert,
                R.raw.old_fashion,
                R.raw.old_school_bell,
                R.raw.analog_watch
        };
        MediaPlayer mp = new MediaPlayer();
        if (mp.isPlaying()) {
            mp.stop();
        }
        mp.reset();
        mp = MediaPlayer.create(mContext, listAlarm[position]);
        mp.start();
    }

}
