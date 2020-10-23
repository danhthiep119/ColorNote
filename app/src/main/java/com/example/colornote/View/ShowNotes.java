package com.example.colornote.View;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.renderscript.ScriptGroup;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.example.colornote.Model.ColorList;
import com.example.colornote.Model.Content;
import com.example.colornote.R;
import com.example.colornote.ViewModel.DataBinding;

import org.json.JSONException;

import java.io.IOException;

public class ShowNotes extends AppCompatActivity {
    EditText txtContent;
    TextView txtTimeCreated, txtTitle;
    Toolbar toolbar;
    ImageButton btnEdit, btnSave;
    LinearLayout llTitleZone;
    ConstraintLayout llbottomZone;
    DataBinding dataBinding = new DataBinding(this);
    ColorList colorList = new ColorList();
    int positionColor;
    static int position = -1;
    boolean isCheck = false;
    Content content;
    static String pass = "";
    static String reminder = "";
    static boolean isSetReminder;
    final int SET_PASSWORD = 1;
    String view;
    String type;
    boolean isArchives = false;

    public ShowNotes() throws IOException, JSONException {

    }

    @Override
    protected void onResume() {
        super.onResume();
        try {
            position = getIntent().getIntExtra("position", -1);
            view = getIntent().getStringExtra("view");
            switch (view) {
                case "Main":
                    content = (Content) dataBinding.getList().get(position);
                    break;
                case "Archives":
                    content = (Content) dataBinding.getArchives().get(position);
                    isArchives=true;
                    break;
                case "Trash":
                    content = (Content) dataBinding.getTrash().get(position);
                    break;
                case "Calendar":
                case "Search":
                    type = getIntent().getStringExtra("type");
                    if (type.equals("Home")) {
                        content = (Content) dataBinding.getList().get(position);
                    } else if (type.equals("Archives")) {
                        content = (Content) dataBinding.getArchives().get(position);
                    }
                    break;
            }
            try {
                String notification = getIntent().getStringExtra("notification");
                if (notification.equals("yes")) {
                    txtContent.setText("");
                    final EditText myEdit = new EditText(this);
                    myEdit.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                    android.app.AlertDialog.Builder dialog = new android.app.AlertDialog.Builder(this);
                    dialog.setTitle("Enter password:")
                            .setView(myEdit)
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    if (myEdit.getText().toString() != "") {
                                        if (myEdit.getText().toString().equals(content.getPassword())) {
                                            dialog.dismiss();
                                        } else
                                            Toast.makeText(ShowNotes.this, "Bạn nhập  sai mật khẩu! Xin vui lòng thử lại.", Toast.LENGTH_SHORT).show();
                                    } else
                                        Toast.makeText(ShowNotes.this, "Bạn Chưa nhập mật khẩu", Toast.LENGTH_SHORT).show();
                                }
                            })
                            .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    finish();
                                }
                            })
                            .create();
                    dialog.show();
                }
            } catch (Exception e) {

            }
            reminder = content.getReminer();
            txtContent.setText(content.getNote());
            isSetReminder = content.getIsSetReminder();
            txtTimeCreated.setText(content.getDate());
            txtTitle.setText(content.getTitle());
            positionColor = Integer.parseInt(content.getColor());
            toolbar.setBackgroundColor(Color.parseColor(colorList.titleColor[positionColor]));
            txtContent.setBackgroundColor(Color.parseColor(colorList.backgroundColor[positionColor]));
            llTitleZone.setBackgroundColor(Color.parseColor(colorList.backgroundColor[positionColor]));
            llbottomZone.setBackgroundColor(Color.parseColor(colorList.titleColor[positionColor]));
        } catch (Exception e) {

        }
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (isArchives){
            if(item.getItemId() == R.id.nav_Archive){
                item.setTitle("UnArchives");
            }
        }
        switch (item.getItemId()) {
            case R.id.nav_Check:
                if (isCheck) {
                    isCheck = false;
                } else isCheck = true;
                if (isCheck) {
                    item.setTitle("UnCkeck");
                    txtTitle.setPaintFlags(txtTitle.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                } else {
                    item.setTitle("Check");
                    txtTitle.setPaintFlags(txtTitle.getPaintFlags() & (~Paint.STRIKE_THRU_TEXT_FLAG));
                }
                break;
            case R.id.nav_Share:
                break;
            case R.id.nav_lock:
                showLock();
                break;
            case R.id.nav_reminder:
                showReminder();
                break;
            case R.id.nav_Archive:
                showdialog(isArchives);
                break;
            case R.id.nav_delete:
                deleteContent();
                break;
            case R.id.nav_back:
                rollBackItem();
                break;
            case R.id.nav_delete_penalty:
                deleteItem();
                break;
        }
        return true;
    }

    private void deleteItem() {
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
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
                        Toast.makeText(ShowNotes.this, "Delete note success!", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                })
                .create();
        dialog.show();
    }

    private void rollBackItem() {
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
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
                        Toast.makeText(ShowNotes.this, "Restore note success!", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(ShowNotes.this, MainActivity.class);
                        startActivity(intent);
                        finish();
                    }
                })
                .create();
        dialog.show();
    }

    private void showLock() {
        Intent intent = new Intent(ShowNotes.this, SetPassword.class);
        startActivityForResult(intent, 1);
        changeSave();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == SET_PASSWORD) {
            if (resultCode == Activity.RESULT_OK) {
                pass = data.getStringExtra("pass");
            } else if (resultCode == Activity.RESULT_CANCELED) {
                pass = content.getPassword();
            }
        }
    }

    private void showReminder() {
        SetReminderDialog dialog = new SetReminderDialog(ShowNotes.this);
        dialog.show();
        changeSave();
    }

    private void deleteContent() {
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setTitle("Confirm")
                .setMessage("Are you sure want to move this note to trash bin?")
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String view = getIntent().getStringExtra("view");
                        if (view.equals("Main")) {
                            dataBinding.moveHomeToTrashbin(position);
                        } else if (view.equals("Archives")) {
                            dataBinding.moveArchiveToTrashBin(position);
                        }
                        Toast.makeText(ShowNotes.this, "move notes successfully!", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(ShowNotes.this, TrashCanView.class);
                        startActivity(intent);
                        finish();
                    }
                }).create();
        dialog.show();
    }

    private void showdialog(final boolean isArchives) {
            AlertDialog.Builder dialog = new AlertDialog.Builder(this);
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
                                Toast.makeText(ShowNotes.this, "Store notes successfully!", Toast.LENGTH_SHORT).show();
                                intent = new Intent(ShowNotes.this, MainActivity.class);
                            }
                            else {
                                dataBinding.moveToArchives(position);
                                Toast.makeText(ShowNotes.this, "Store notes successfully!", Toast.LENGTH_SHORT).show();
                                intent = new Intent(ShowNotes.this, ArchivesView.class);
                            }
                            startActivity(intent);
                            finish();
                        }
                    }).create();
            dialog.show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (view.equals("Trash")) {
            getMenuInflater().inflate(R.menu.menu_trash_item, menu);
            btnEdit.setVisibility(View.INVISIBLE);
        } else getMenuInflater().inflate(R.menu.menu_edit, menu);
        return true;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.show_notes);
        txtContent = findViewById(R.id.txtContent);
        txtContent.setTextColor(Color.parseColor("#000000"));
        txtTimeCreated = findViewById(R.id.txtTimeCreated);
        txtTitle = findViewById(R.id.txtTitle);
        txtTitle.setTextColor(Color.parseColor("#000000"));
        toolbar = findViewById(R.id.toolbar);
        btnEdit = findViewById(R.id.btnEdit);
        llbottomZone = findViewById(R.id.bottomZone);
        setSupportActionBar(toolbar);
        llTitleZone = findViewById(R.id.llTitleZone);
        btnSave = findViewById(R.id.btnSave);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeSave();
            }
        });
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (view) {
                    case "Main":
                        dataBinding.getList().set(position, new Content(txtTitle.getText().toString()
                                , reminder
                                , isSetReminder
                                , pass
                                , content.getDate()
                                , String.valueOf(positionColor)
                                , txtContent.getText().toString()));
                        break;
                    case "Archives":
                        dataBinding.getArchives().set(position, new Content(txtTitle.getText().toString()
                                , reminder
                                , isSetReminder
                                , pass
                                , content.getDate()
                                , String.valueOf(positionColor)
                                , txtContent.getText().toString()));
                        break;
                    case "Calendar":
                    case "Search":
                        switch (type) {
                            case "Home":
                                dataBinding.getList().set(position, new Content(txtTitle.getText().toString()
                                        , reminder
                                        , isSetReminder
                                        , pass
                                        , content.getDate()
                                        , String.valueOf(positionColor)
                                        , txtContent.getText().toString()));
                                break;
                            case "Archives":
                                dataBinding.getArchives().set(position, new Content(txtTitle.getText().toString()
                                        , reminder
                                        , isSetReminder
                                        , pass
                                        , content.getDate()
                                        , String.valueOf(positionColor)
                                        , txtContent.getText().toString()));
                                break;
                        }
                        break;
                }
                dataBinding.saveJson();
                Toast.makeText(ShowNotes.this, "Save", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void changeSave() {
        txtContent.setEnabled(true);
        txtTitle.setEnabled(true);
        llbottomZone.setVisibility(View.VISIBLE);
    }
}
