package com.example.colornote.View;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.example.colornote.R;

import java.util.List;

public class OptionDialog extends Dialog {
    String mode;
    Activity mContext;
    TextView txtOption;
    ListView lvListOption;
    Setting setting;
    public OptionDialog(@NonNull Activity context, String mode,Setting setting) {
        super(context);
        this.mode = mode;
        this.mContext = context;
        this.setting = setting;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_option_adapter);
        addControls();
        addEvents();
    }

    private void addEvents() {
        switch (mode){
            case "screen":
                screenOption();
                break;
            case "font-size":
                fontSize();
                break;
            case "item-height":
                itemHeight();
                break;
            case "theme":
                theme();
                break;
            case "default-font-size":
                defaultFontSize();
                break;
            case "sound-reminder":
                soundReminder();
                break;
            case "font":
                font();
                break;
            case "create":
                showCreate();
                break;
        }
    }

    private void showCreate() {
        String[] list = {"Text","Checklist"};
        showOption(list);
    }

    private void fontSize() {
        txtOption.setText("Font size");
        String[] list = {"8","10","12","14","16"};
        showOption(list);
    }
    private void itemHeight() {
        txtOption.setText("List item height");
        String[] list = {"Normal","Small"};
        showOption(list);
    }
    private void theme() {
        txtOption.setText("Theme");
        String[] list = {"Soft","Dark"};
        showOption(list);
    }
    private void defaultFontSize() {
        txtOption.setText("Default font size");
        String[] list = {"Small","Medium","Large"};
        showOption(list);
    }
    private void soundReminder() {
        txtOption.setText("Sound reminder");
        String[] list = {"Default sound","Sound 1","Sound 2","Sound 3","Sound 4"};
        showOption(list);
    }
    private void font() {
        txtOption.setText("Font");
        String[] list = {"Default","Font 1","Font 2","Font 3","Font 4","Font 5"};
        showOption(list);
    }

    private void screenOption() {
        txtOption.setText("Default Screen");
        String[] list = {"Note","Calendar"};
        showOption(list);
    }

    void showOption(String[] list){
        lvListOption.setAdapter(new AdapterDialog(list,mContext,setting));
    }

    private void addControls() {
        txtOption = findViewById(R.id.txtOption);
        lvListOption = findViewById(R.id.lvListOption);
    }
}
