package com.example.colornote.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.colornote.Model.ColorList;
import com.example.colornote.Model.Content;
import com.example.colornote.Model.ListCheck;
import com.example.colornote.Model.Work;
import com.example.colornote.R;
import com.example.colornote.ViewModel.DataBinding;

import org.json.JSONException;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class CheckList extends AppCompatActivity {
    EditText txtInputTitle;
    TextView txtTitleInput;
    ImageButton btnSave;
    long time;
    Button btnAddCheck, btnSetReminder;
    ListView lvListCheck;
    ImageButton btnChangeColor;
    DataBinding dataBinding = new DataBinding(this);
    final int SET_PASSWORD = 1;
    String pass = "";
    LinearLayout llTitleZone, llListCheck;
    ConstraintLayout bottomZone;
    Toolbar toolbar;
    CheckListAdapter adapter;
    boolean isSetReminder;
    List<Work> list = new ArrayList<>();
    static String color = "3";
    static int position = -1;
    static int mode = 0;
    ListCheck listCheck;
    float fontsize = 12.0f;
    String font = "";
    String timeReminder = "";
    ColorList colorList = new ColorList();
    String view;
    boolean isArchives=false;
    public CheckList() throws IOException, JSONException {
    }

    @Override
    protected void onResume() {
        super.onResume();
        try {
            try {
                timeReminder = getIntent().getStringExtra("time");
                if (!timeReminder.equals("")) {
                    btnSetReminder.setText(timeReminder);
                    isSetReminder = true;
                }
            } catch (NullPointerException e) {
                Log.e("CheckList", "" + e);
            }

            position = getIntent().getIntExtra("position", -1);
            mode = getIntent().getIntExtra("mode", 0);
            if (mode == 1) {
                view = getIntent().getStringExtra("view");
                switch (view) {
                    case "Trash":
                        listCheck = (ListCheck) dataBinding.getTrash().get(position);
                        break;
                    case "Main":
                        listCheck = (ListCheck) dataBinding.getList().get(position);
                        break;
                    case "Archives":
                        isArchives=true;
                        listCheck = (ListCheck) dataBinding.getArchives().get(position);
                        break;
                    case "Calendar":
                    case "Search":
                        String type = getIntent().getStringExtra("type");
                        if (type.equals("Home")) {
                            listCheck = (ListCheck) dataBinding.getList().get(position);
                        } else if (type.equals("Archives")) {
                            listCheck = (ListCheck) dataBinding.getArchives().get(position);
                        }
                        break;
                }
                txtInputTitle.setText(listCheck.getTitle());
                list = listCheck.getWorkList();
                lvListCheck.setAdapter(new CheckListAdapter(list, this));
                color = listCheck.getColor();
                isSetReminder = listCheck.getIsSetReminder();
                llListCheck.setBackgroundColor(Color.parseColor(colorList.backgroundColor[Integer.parseInt(color)]));
                llTitleZone.setBackgroundColor(Color.parseColor(colorList.backgroundColor[Integer.parseInt(color)]));
                bottomZone.setBackgroundColor(Color.parseColor(colorList.titleColor[Integer.parseInt(color)]));
                toolbar.setBackgroundColor(Color.parseColor(colorList.titleColor[Integer.parseInt(color)]));
                btnChangeColor.setBackgroundColor(Color.parseColor(colorList.backgroundColor[Integer.parseInt(color)]));
            } else getShareReference();
        } catch (Exception e) {
            Log.e("TAG", "" + e);
        }
    }

    void getShareReference() {
        SharedPreferences sharedPreferences = getSharedPreferences("SaveSetting", Context.MODE_PRIVATE);
        fontsize = Float.parseFloat(sharedPreferences.getString("font-size", "12"));
        font = sharedPreferences.getString("font", "default");
        int colorPosition = sharedPreferences.getInt("colorPosition", 3);
        color = String.valueOf(colorPosition);
        llListCheck.setBackgroundColor(Color.parseColor(colorList.backgroundColor[colorPosition]));
        llTitleZone.setBackgroundColor(Color.parseColor(colorList.backgroundColor[colorPosition]));
        bottomZone.setBackgroundColor(Color.parseColor(colorList.titleColor[colorPosition]));
        toolbar.setBackgroundColor(Color.parseColor(colorList.titleColor[colorPosition]));
        btnChangeColor.setBackgroundColor(Color.parseColor(colorList.backgroundColor[colorPosition]));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_list);
        addControls();
        addEvents();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (isArchives){
            if(item.getItemId() == R.id.nav_Archive){
                item.setTitle("UnArchives");
            }
        }
        switch (item.getItemId()) {
            case R.id.nav_delete:
                androidx.appcompat.app.AlertDialog.Builder dialog = new androidx.appcompat.app.AlertDialog.Builder(this);
                dialog.setTitle("Confirm")
                        .setMessage("Are you sure want to move this check list to trash bin?")
                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        })
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                if (view.equals("Main")) {
                                    dataBinding.moveHomeToTrashbin(position);
                                } else if (view.equals("Archives")) {
                                    dataBinding.moveArchiveToTrashBin(position);
                                }
                                Toast.makeText(CheckList.this, "move check list successfully!", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(CheckList.this, TrashCanView.class);
                                intent.putExtra("view", 1);
                                startActivity(intent);
                                finish();
                            }
                        }).create();
                dialog.show();
                break;
            case R.id.nav_lock:
                Intent intent = new Intent(CheckList.this, SetPassword.class);
                startActivityForResult(intent, 1);
                break;
            case R.id.nav_Archive:
                showdialog(isArchives);
                break;
            case R.id.nav_back:
                rollBackItem();
                break;
            case R.id.nav_delete_penalty:
                deleteItem();
                break;
            default:
                super.onOptionsItemSelected(item);
        }
        return true;
    }

    private void showdialog(final boolean isArchives) {
        androidx.appcompat.app.AlertDialog.Builder dialog = new androidx.appcompat.app.AlertDialog.Builder(this);
        dialog.setTitle("Confirm")
                .setMessage("Are you sure want to restore this node?")
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = null;
                        if(isArchives) {
                            dataBinding.moveArchivesToHome(position);
                            Toast.makeText(CheckList.this, "Store notes successfully!", Toast.LENGTH_SHORT).show();
                            intent = new Intent(CheckList.this, MainActivity.class);
                        }
                        else {
                            dataBinding.moveToArchives(position);
                            Toast.makeText(CheckList.this, "Store notes successfully!", Toast.LENGTH_SHORT).show();
                            intent = new Intent(CheckList.this, ArchivesView.class);
                        }
                        startActivity(intent);
                        finish();
                    }
                }).create();
        dialog.show();
    }

    private void deleteItem() {
        androidx.appcompat.app.AlertDialog.Builder dialog = new androidx.appcompat.app.AlertDialog.Builder(this);
        dialog.setTitle("Delete")
                .setMessage("Are you sure want to delete this note")
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dataBinding.removeItem(position);
                        Toast.makeText(CheckList.this, "Delete note success!", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                })
                .create();
        dialog.show();
    }

    private void rollBackItem() {
        androidx.appcompat.app.AlertDialog.Builder dialog = new androidx.appcompat.app.AlertDialog.Builder(this);
        dialog.setTitle("Restore")
                .setMessage("Are you sure want to restore this note")
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dataBinding.rollBackItem(position);
                        Toast.makeText(CheckList.this, "Restore note success!", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(CheckList.this, MainActivity.class);
                        startActivity(intent);
                        finish();
                    }
                })
                .create();
        dialog.show();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
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
    public boolean onCreateOptionsMenu(Menu menu) {
        if (mode == 1) {
            if(view.equals("Trash")) {
                getMenuInflater().inflate(R.menu.menu_trash_item, menu);
                btnChangeColor.setVisibility(View.INVISIBLE);
            }
            else getMenuInflater().inflate(R.menu.menu_edit, menu);
        } else getMenuInflater().inflate(R.menu.menu_setting, menu);
        return super.onCreateOptionsMenu(menu);
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
        btnSetReminder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SetReminderDialog dialog = new SetReminderDialog(CheckList.this);
                dialog.show();
            }
        });
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mode == 0) {
                    dataBinding.addList(new ListCheck(txtInputTitle.getText().toString(),
                            !btnSetReminder.getText().toString().equals("REMINDER") ? btnSetReminder.getText().toString() : ""
                            , isSetReminder,
                            pass,
                            df.format(time),
                            color,
                            false,

                            list));
                    Toast.makeText(CheckList.this, "Save", Toast.LENGTH_SHORT).show();
                    mode = 1;
                    position = dataBinding.getList().size() - 1;
                }
                if (mode == 1) {
                    dataBinding.getList().set(position, new ListCheck(txtInputTitle.getText().toString(),
                            !btnSetReminder.getText().toString().equals("REMINDER") ? btnSetReminder.getText().toString() : ""
                            , isSetReminder,
                            pass,
                            df.format(time),
                            color,
                            false,
                            list));
                }
                dataBinding.saveJson();
                Toast.makeText(CheckList.this, "Save", Toast.LENGTH_SHORT).show();
            }
        });
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        btnAddCheck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final EditText myEdit = new EditText(CheckList.this);
                myEdit.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                AlertDialog.Builder dialog = new AlertDialog.Builder(CheckList.this);
                dialog.setTitle("Name Check Box")
                        .setView(myEdit)
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                if (myEdit.getText().toString() != "") {
                                    list.add(new Work(myEdit.getText().toString(), false));
                                    adapter.notifyDataSetChanged();
                                } else
                                    Toast.makeText(CheckList.this, "Bạn Chưa nhập tên check", Toast.LENGTH_SHORT).show();
                            }
                        })
                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        })
                        .create();
                dialog.show();
            }
        });

        btnChangeColor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ColorDialog dialog = new ColorDialog(CheckList.this, CheckList.this);
                dialog.show();
            }
        });

    }

    private void addControls() {
        txtInputTitle = findViewById(R.id.txtInputTitle);
        txtTitleInput = findViewById(R.id.txtTitleInput);
        btnSave = findViewById(R.id.btnSave);
        time = System.currentTimeMillis();
        btnSetReminder = findViewById(R.id.btnSetReminder);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        lvListCheck = findViewById(R.id.lvListCheck);
        btnAddCheck = findViewById(R.id.btnAddCheck);
        adapter = new CheckListAdapter(list, this);
        btnChangeColor = findViewById(R.id.btnChangeColor);
        lvListCheck.setAdapter(adapter);
        bottomZone = findViewById(R.id.bottom);
        llTitleZone = findViewById(R.id.llTitleZone);
        llListCheck = findViewById(R.id.llListCheck);
    }
}