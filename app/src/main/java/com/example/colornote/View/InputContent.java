package com.example.colornote.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
//import androidx.annotation.RequiresApi;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;

import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import android.widget.TextView;
import android.widget.Toast;

import com.example.colornote.Model.ColorList;
import com.example.colornote.Model.Content;
import com.example.colornote.R;
import com.example.colornote.ViewModel.DataBinding;

import org.json.JSONException;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

public class InputContent extends AppCompatActivity {
    EditText txtInputContent, txtInputTitle;
    TextView txtTitleInput;
    ImageButton btnSave, btnChangeColor;
    Button btnSetReminder;
    long time = 0;
    DataBinding dataBinding = new DataBinding(this);
    final int SET_PASSWORD = 1;
    static String pass = "";
    Toolbar toolbar;
    LinearLayout llTitleZone;
    int position = -1;
    boolean isSetReminder;
    static String color = "3";
    final String TAG = "Input Content";
    ConstraintLayout bottomZone;
    ColorList colorList = new ColorList();
    float fontsize = 12f;
    String font = "";
    int mode =0;

    public InputContent() throws IOException, JSONException {
    }

    @Override
    protected void onResume() {
        super.onResume();
        try {
            try {
                String timeReminder = getIntent().getStringExtra("time");
                if (!timeReminder.equals("")) {
                    btnSetReminder.setText(timeReminder);
                    isSetReminder = true;
                }
            }
            catch (NullPointerException e){
                Log.e("Time reminder",""+e);
            }
            if(mode == 0) {
                getShareReference();
            }
        } catch (Exception e) {

        }
    }

    void getShareReference(){
        try {
            SharedPreferences sharedPreferences = getSharedPreferences("SaveSetting", Context.MODE_PRIVATE);
            fontsize = Float.parseFloat(sharedPreferences.getString("font-size", "12"));
            font = sharedPreferences.getString("font", "default");
            txtInputContent.setTextSize(fontsize);
            int colorPosition = sharedPreferences.getInt("colorPosition",3);
            color = String.valueOf(colorPosition);
            txtInputContent.setBackgroundColor(Color.parseColor(colorList.backgroundColor[colorPosition]));
            llTitleZone.setBackgroundColor(Color.parseColor(colorList.backgroundColor[colorPosition]));
            bottomZone.setBackgroundColor(Color.parseColor(colorList.titleColor[colorPosition]));
            toolbar.setBackgroundColor(Color.parseColor(colorList.titleColor[colorPosition]));
            btnChangeColor.setBackgroundColor(Color.parseColor(colorList.backgroundColor[colorPosition]));
//            Typeface typeface = null;
//        switch (font){
//            case "default":
//                typeface = getResources().getFont(R.font.be_honest);
//                break;
//            case "Font 1":
//                typeface = getResources().getFont(R.font.authentic_script_rough);
//                break;
//            case "Font 2":
//                typeface = getResources().getFont(R.font.beforetherain_personal_use_demo);
//                break;
//            case "Font 3":
//                typeface = getResources().getFont(R.font.california_sun);
//                break;
//            case "Font 4":
//                typeface = getResources().getFont(R.font.casking_cream_script);
//                break;
//            case "Font 5":
//                typeface = getResources().getFont(R.font.lovingyou);
//                break;
//            default:
//                throw new IllegalStateException("Unexpected value: " + font);
//        }
//        txtInputContent.setTypeface(typeface);
        }
        catch (NullPointerException e){
            Log.e("Input Content", ""+e);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_input_content);
        addControls();
        addEvents();
    }

    private void addEvents() {
        final DateFormat df = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        txtInputTitle.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                txtTitleInput.setText(txtInputTitle.getText().toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mode==0) {
                    dataBinding.addList(new Content(txtInputTitle.getText().toString()
                            , !btnSetReminder.getText().toString().equals("REMINDER") ? btnSetReminder.getText().toString() : ""
                            , isSetReminder
                            , pass
                            , df.format(time)
                            , color
                            , txtInputContent.getText().toString()
                    ));
                    position = dataBinding.getList().size() - 1;
                    mode = 1;
                }
                else if(mode==1){
                    dataBinding.getList().set(position, new Content(txtInputTitle.getText().toString()
                            , !btnSetReminder.getText().toString().equals("REMINDER") ? btnSetReminder.getText().toString() : ""
                            , isSetReminder
                            , pass
                            , df.format(time)
                            , color
                            , txtInputContent.getText().toString()));
                }
                dataBinding.saveJson();

                Toast.makeText(InputContent.this, "Save", Toast.LENGTH_SHORT).show();
            }
        });
        btnSetReminder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SetReminderDialog dialog = new SetReminderDialog(InputContent.this);
                dialog.show();
            }
        });
        btnChangeColor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ColorDialog dialog = new ColorDialog(InputContent.this, InputContent.this);
                dialog.show();
            }
        });
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_setting, menu);
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == SET_PASSWORD) {
            if (resultCode == Activity.RESULT_OK) {
                pass = data.getStringExtra("pass");
            } else if (resultCode == Activity.RESULT_CANCELED) {
                pass = "";
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.nav_lock:
                Intent intent = new Intent(InputContent.this, SetPassword.class);
                startActivityForResult(intent, 1);
                break;
            case R.id.nav_delete:
                AlertDialog.Builder dialog = new AlertDialog.Builder(this);
                dialog.setTitle("Confirm")
                        .setMessage("Are your sure want to delete this note?")
                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        })
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                finish();
                            }
                        }).create();
                dialog.show();
                break;
            case R.id.nav_Share:
                try {
                    Intent shareIntent = new Intent(Intent.ACTION_SEND);
                    shareIntent.setType("text/plain");
                    String shareBody = "- Title : Color Note\n- Content: " + txtInputContent.getText().toString();
                    String shareSubject = "- Color Note";
                    shareIntent.putExtra(Intent.EXTRA_SUBJECT, shareSubject);
                    shareIntent.putExtra(Intent.EXTRA_TEXT, shareBody);
                    startActivity(Intent.createChooser(shareIntent, "choose one"));
                } catch (Exception e) {
                }
                break;
            case R.id.nav_copy:
                ClipboardManager clipboardManager = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clip = ClipData.newPlainText("CopyContent", txtInputContent.getText().toString());
                clipboardManager.setPrimaryClip(clip);
                Toast.makeText(InputContent.this, "Copy success!", Toast.LENGTH_SHORT).show();
            default:
                super.onOptionsItemSelected(item);
        }
        return true;
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    private void addControls() {
        txtInputContent = findViewById(R.id.txtInputContent);
        txtInputTitle = findViewById(R.id.txtInputTitle);
        txtTitleInput = findViewById(R.id.txtTitleInput);
        btnSave = findViewById(R.id.btnSave);
        time = System.currentTimeMillis();
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        llTitleZone = findViewById(R.id.llTitleZone);
        btnSetReminder = findViewById(R.id.btnSetReminder);
        btnChangeColor = findViewById(R.id.btnChangeColor);
        bottomZone = findViewById(R.id.bottomZone);
    }
}