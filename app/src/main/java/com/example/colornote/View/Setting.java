package com.example.colornote.View;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.Toast;

import com.example.colornote.Model.ColorList;
import com.example.colornote.R;
import com.example.colornote.ViewModel.DataBinding;

import org.json.JSONException;

import java.io.IOException;

public class Setting extends AppCompatActivity {
    Button btnFont, btnFontSize, btnTheme, btnItemHeight, btnSound, btnSize, btnScreen;
    int colorPosition = 3;
    ImageButton btnChangeColor;
    Button btnRemovePass;
    Toolbar toolbar;
    String mode = "";
    ColorList colorList = new ColorList();
    Switch swPinTaskBar;
    DataBinding dataBinding = new DataBinding(this);

    public Setting() throws IOException, JSONException {
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        addControls();
        addEvents();
    }

    private void addEvents() {
        btnFont.setOnClickListener(listener);
        btnFontSize.setOnClickListener(listener);
        btnTheme.setOnClickListener(listener);
        btnItemHeight.setOnClickListener(listener);
        btnSound.setOnClickListener(listener);
        btnSize.setOnClickListener(listener);
        btnScreen.setOnClickListener(listener);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        swPinTaskBar.setOnCheckedChangeListener(listener1);
        btnChangeColor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ChangeColor();
            }
        });
        btnRemovePass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder dialog = new AlertDialog.Builder(Setting.this);
                dialog.setTitle("Confirm")
                        .setMessage("Are you sure you want to remove the password for all notes?")
                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        })
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                for (int i = 0; i <dataBinding.getList().size();i++){
                                    if(!dataBinding.getList().get(i).getPassword().equals("")){
                                        dataBinding.getList().get(i).setPassword("");
                                    }
                                }
                                for (int i = 0; i <dataBinding.getArchives().size();i++){
                                    if(!dataBinding.getArchives().get(i).getPassword().equals("")){
                                        dataBinding.getArchives().get(i).setPassword("");
                                    }
                                }
                                dataBinding.saveJson();
                                Toast.makeText(Setting.this,"Remove all password protection successfully",Toast.LENGTH_SHORT).show();
                            }
                        }).create();
                dialog.show();
            }
        });
    }

    CompoundButton.OnCheckedChangeListener listener1 = new CompoundButton.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            SharedPreferences sharedPreferences = getSharedPreferences("SaveSetting",MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            if (isChecked) {
                editor.putString("pin","on");
                buttonView.setChecked(true);
            }
            else {
                editor.putString("pin","off");
                buttonView.setChecked(false);
            }
            editor.commit();
        }
    };


    CompoundButton.OnClickListener listener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.btnFont:
                    mode = "font";
                    break;
                case R.id.btnFontSize:
                    mode = "default-font-size";
                    break;
                case R.id.btnTheme:
                    mode = "theme";
                    break;
                case R.id.btnItemHeight:
                    mode = "item-height";
                    break;
                case R.id.btnSound:
                    mode = "sound-reminder";
                    break;
                case R.id.btnSize:
                    mode = "font-size";
                    break;
                case R.id.btnScreen:
                    mode = "screen";
                    break;
            }
            showDialog(mode);
        }
    };

    void ChangeColor() {
        ColorDialog dialog = new ColorDialog(this, Setting.this);
        dialog.show();
    }

    void showDialog(String mode) {
        OptionDialog dialog = new OptionDialog(this, mode, this);
        dialog.show();
    }

    private void addControls() {
        btnFont = findViewById(R.id.btnFont);
        btnFontSize = findViewById(R.id.btnFontSize);
        btnTheme = findViewById(R.id.btnTheme);
        btnItemHeight = findViewById(R.id.btnItemHeight);
        btnSound = findViewById(R.id.btnSound);
        btnSize = findViewById(R.id.btnSize);
        btnScreen = findViewById(R.id.btnScreen);
        toolbar = findViewById(R.id.toolbar);
        btnChangeColor = findViewById(R.id.btnChangeColor);
        swPinTaskBar = findViewById(R.id.pinTaskBar);
        btnRemovePass = findViewById(R.id.btnRemovePass);
        setSupportActionBar(toolbar);
        getDataShareReference();
    }

    private void getDataShareReference() {
        SharedPreferences sharedPreferences = getSharedPreferences("SaveSetting", Context.MODE_PRIVATE);
        String screen = sharedPreferences.getString("screen", "Note");
        String fontSize = sharedPreferences.getString("default-font-size", "Medium");
        String itemheight = sharedPreferences.getString("item-height", "Normal");
        String font = sharedPreferences.getString("font", "default");
        String fontsize = sharedPreferences.getString("font-size", "12");
        String theme = sharedPreferences.getString("theme", "Soft");
        String soundreminder = sharedPreferences.getString("sound-reminder", "Default sound");
        String pintoTaskBar =sharedPreferences.getString("pin","off");
        btnFont.setText(font);
        btnFontSize.setText(fontSize);
        btnTheme.setText(theme);
        btnItemHeight.setText(itemheight);
        btnSound.setText(soundreminder);
        btnSize.setText(fontsize);
        btnScreen.setText(screen);
        btnChangeColor.setBackgroundColor(Color.parseColor(colorList.backgroundColor[sharedPreferences.getInt("colorPosition", 3)]));
        if(pintoTaskBar.equals("on")){
            swPinTaskBar.setChecked(true);
        }
        else if(pintoTaskBar.equals("off")){
            swPinTaskBar.setChecked(false);
        }
    }
}