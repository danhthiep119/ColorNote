package com.example.colornote.View;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import com.example.colornote.Model.Content;
import com.example.colornote.Model.ListCheck;
import com.example.colornote.Model.Title;
import com.example.colornote.R;
import com.example.colornote.Service.AlarmNotification;
import com.example.colornote.Service.AlarmReceiver;
import com.example.colornote.ViewModel.DataBinding;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Environment;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.style.ImageSpan;
import android.util.Log;
import android.view.View;

import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import org.json.JSONException;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    DrawerLayout drawerLayout;
    TextView txtNote;
    NavigationView navView;
    Toolbar toolbar;
    RecyclerView rvListNote;
    ContentAdapter adapter;
    DataBinding dataBinding = new DataBinding(this);
    AlarmManager alarmManager;
    final String TAG = "Main Activity";
    PendingIntent pendingIntent;
    List<String> reminder = new ArrayList<>();
    String mode = "Dark";
    NotificationManager manager;
    final int REQUEST_CODE = 1;

    public MainActivity() throws IOException, JSONException {
    }

    @Override
    protected void onResume() {
        super.onResume();
        try {
            if (!dataBinding.getList().isEmpty()) {
                txtNote.setVisibility(View.GONE);
                adapter.notifyDataSetChanged();
                SharedPreferences sharedPreferences = getSharedPreferences("SaveSetting", Context.MODE_PRIVATE);
                mode = sharedPreferences.getString("theme", "Dark");
                String pin = sharedPreferences.getString("pin", "off");
                if (mode.equals("Dark")) {
                    toolbar.setBackgroundColor(Color.parseColor("#696C69"));
                } else if (mode.equals("Soft")) {
                    toolbar.setBackgroundColor(Color.parseColor("#B0F6EDED"));
                }
                openAlarm();
                if (pin.equals("on")) {
                    NotificationCompat.Builder notification = new NotificationCompat.Builder(this)
                            .setContentTitle(dataBinding.getList().get(dataBinding.getList().size() - 1).getTitle())
                            .setSmallIcon(R.drawable.ic_baseline_bookmark_24)
                            .setAutoCancel(true);
                    Intent intent = null;
                    if (dataBinding.getList().get(dataBinding.getList().size() - 1) instanceof Content) {
                        intent = new Intent(MainActivity.this, ShowNotes.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        intent.putExtra("position", dataBinding.getList().size() - 1);
                        intent.putExtra("view", "Main");
                        if (!dataBinding.getList().get(dataBinding.getList().size() - 1).getPassword().equals("")) {
                            intent.putExtra("notification", "yes");
                        }
                    } else if (dataBinding.getList().get(dataBinding.getList().size() - 1) instanceof ListCheck) {
                        intent = new Intent(MainActivity.this, CheckList.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        intent.putExtra("position", dataBinding.getList().size() - 1);
                        intent.putExtra("mode", 1);
                    }
                    PendingIntent pendingIntent = PendingIntent.getActivity(this, 1, intent, 0);
                    notification.setContentIntent(pendingIntent);
                    manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                    manager.notify(123, notification.build());
                } else if (pin.equals("off")) {
                    manager.cancel(123);
                }
            }
        } catch (Exception e) {
            Log.w(TAG, "" + e);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        drawerLayout = findViewById(R.id.drawerLayout);
        navView = findViewById(R.id.nav_view);
        rvListNote = findViewById(R.id.rvListNote);
        txtNote = findViewById(R.id.txtNote);
        adapter = new ContentAdapter(dataBinding.getList(), this);
        alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        requestPermissions();
        if (!dataBinding.getList().isEmpty()) {
            txtNote.setVisibility(View.GONE);
        }
        if (dataBinding.getList().isEmpty()) {
            txtNote.setVisibility(View.VISIBLE);
            String text = "Please add a new note by clicking the Icon @ below";
            SpannableString str = new SpannableString(text);
            int index = text.indexOf("@");
            @SuppressLint("UseCompatLoadingForDrawables") Object obj = (Object) new ImageSpan(getResources().getDrawable(R.drawable.ic_baseline_add_circle_24));
            str.setSpan(obj,index,index+1,ImageSpan.ALIGN_BASELINE);
            txtNote.setText(str);
        }
        showView(adapter);
        NavigationDrawer();
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                OptionMenu optionMenu = new OptionMenu(MainActivity.this, "create");
                optionMenu.show();
            }
        });
    }


    private void openAlarm() {
        String stringDate = "";
        reminder.clear();
        for (Title time : dataBinding.getList()) {
            if (!time.getReminer().equals("") && time.getIsSetReminder()) {
                reminder.add(time.getReminer());
            }
        }
        for (Title time : dataBinding.getArchives()) {
            if (!time.getReminer().equals("") && time.getIsSetReminder()) {
                reminder.add(time.getReminer());
            }
        }
        Collections.sort(reminder, new Comparator<String>() {
            @Override
            public int compare(String o1, String o2) {
                return o2.compareTo(o1);
            }
        });
        SimpleDateFormat dateFormat = new SimpleDateFormat("HH : mm  , dd/MM/yyyy");
        if (!reminder.isEmpty()) {
            stringDate = reminder.get(0);
            System.out.println(reminder.size());
            System.out.println(stringDate);
        }

        try {
            String title = "";
            String mess = "";
            Date date = null;
            Intent intent = new Intent(MainActivity.this, AlarmReceiver.class);
            if (!stringDate.equals("")) {
                date = dateFormat.parse(stringDate);
                for (int i = 0; i < dataBinding.getList().size(); i++) {
                    if (reminder.get(0).equals(dataBinding.getList().get(i).getReminer())) {
                        if (dataBinding.getList().get(i) instanceof Content) {
                            Content content = (Content) dataBinding.getList().get(i);
                            title = content.getTitle();
                            mess = content.getNote();
                        } else if (dataBinding.getList().get(i) instanceof ListCheck) {
                            ListCheck listCheck = (ListCheck) dataBinding.getList().get(i);
                            title = listCheck.getTitle();
                            mess = "";
                        }
                    }
                }
                for (int i = 0; i < dataBinding.getArchives().size(); i++) {
                    if (reminder.get(0).equals(dataBinding.getArchives().get(i).getReminer())) {
                        if (dataBinding.getArchives().get(i) instanceof Content) {
                            Content content = (Content) dataBinding.getArchives().get(i);
                            title = content.getTitle();
                            mess = content.getNote();
                        } else if (dataBinding.getArchives().get(i) instanceof ListCheck) {
                            ListCheck listCheck = (ListCheck) dataBinding.getArchives().get(i);
                            title = listCheck.getTitle();
                            mess = "";
                        }
                    }
                }
                if (System.currentTimeMillis() - date.getTime() > 10000) {
                    for (int j = 0; j < dataBinding.getList().size(); j++) {
                        if (reminder.get(0).equals(dataBinding.getList().get(j).getReminer())) {
                            dataBinding.getList().get(j).setReminer("");
                            dataBinding.getList().get(j).setSetReminder(false);
                            dataBinding.saveJson();
                            reminder.remove(0);
//                            alarmManager.cancel(pendingIntent);
                            break;
                        }
                    }
                    for (int j = 0; j < dataBinding.getArchives().size(); j++) {
                        if (reminder.get(0).equals(dataBinding.getArchives().get(j).getReminer())) {
                            dataBinding.getArchives().get(j).setReminer("");
                            dataBinding.getArchives().get(j).setSetReminder(false);
                            dataBinding.saveJson();
                            reminder.remove(0);
//                            alarmManager.cancel(pendingIntent);
                            break;
                        }
                    }
                }
                intent.putExtra("title", title);
                intent.putExtra("content", mess);
                pendingIntent = PendingIntent.getBroadcast(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
                alarmManager.setExact(AlarmManager.RTC_WAKEUP, date.getTime(), pendingIntent);

            }
        } catch (ParseException e) {
            e.printStackTrace();

        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        for (int grantResult : grantResults) {
            if (grantResult == PackageManager.PERMISSION_GRANTED) {
                dataBinding.saveJson();
            }
        }
    }

    private void requestPermissions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (shouldShowRequestPermissionRationale(Manifest.permission.READ_EXTERNAL_STORAGE)) {

            } else
                requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, REQUEST_CODE);
            if (shouldShowRequestPermissionRationale(Manifest.permission.WRITE_EXTERNAL_STORAGE)) {

            } else
                requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_CODE);
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            if (shouldShowRequestPermissionRationale(Manifest.permission.READ_EXTERNAL_STORAGE)) {

            }
        }
    }

    public void showView(ContentAdapter adapter) {
        rvListNote.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        rvListNote.setHasFixedSize(true);
        rvListNote.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }


    private void NavigationDrawer() {
        setSupportActionBar(toolbar);
        navView.bringToFront();
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        navView.setNavigationItemSelectedListener(this);
    }

    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        }
        else {
            AlertDialog.Builder dialog = new AlertDialog.Builder(MainActivity.this);
            dialog.setTitle("Exit!")
                    .setMessage("Are you sure want to exit?")
                    .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    })
                    .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            finish();
                        }
                    }).create();
            dialog.show();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.nav_sort:
                OptionMenu optionMenu = new OptionMenu(MainActivity.this, "sort");
                optionMenu.show();
                break;
            case R.id.nav_setting:
                Intent intent = new Intent(this, Setting.class);
                startActivity(intent);
                break;
            case R.id.nav_view:
                OptionMenu optionMenu1 = new OptionMenu(MainActivity.this, "view");
                optionMenu1.show();
                break;
            case R.id.app_bar_search:
            case R.id.nav_search:
                Intent intent2 = new Intent(this, SearchView.class);
                startActivity(intent2);
                break;
        }
        return true;
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.nav_Calendar:
                Intent intent3 = new Intent(this, CalendarView.class);
                startActivity(intent3);
                break;
            case R.id.nav_Note:
                if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
                    drawerLayout.closeDrawer(GravityCompat.START);
                }
                break;
            case R.id.nav_Setting:
                Intent intent = new Intent(this, Setting.class);
                startActivity(intent);
                break;
            case R.id.nav_Archive:
                drawerLayout.closeDrawer(GravityCompat.START);
                Intent intent1 = new Intent(this, ArchivesView.class);
                startActivity(intent1);
                break;

            case R.id.nav_Trash:
                drawerLayout.closeDrawer(GravityCompat.START);
                Intent intent2 = new Intent(this, TrashCanView.class);
                startActivity(intent2);
                break;
            case R.id.nav_Rate:
                String url = "https://play.google.com/store/apps/details?id=com.socialnmobile.dictapps.notepad.color.note";
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(url)));
                break;
            case R.id.nav_Share:
                Intent shareIntent = new Intent(Intent.ACTION_SEND);
                shareIntent.setType("text/plain");
                String shareBody = "- Title: Color Note\n- Nội Dung : https://play.google.com/store/apps/details?id=com.socialnmobile.dictapps.notepad.color.note";
                String shareSubject = "Color Note";
                shareIntent.putExtra(Intent.EXTRA_SUBJECT, shareSubject);
                shareIntent.putExtra(Intent.EXTRA_TEXT, shareBody);
                startActivity(Intent.createChooser(shareIntent, "choose one"));
                break;
            case R.id.nav_Infomation:
                InfomationDialog dialog = new InfomationDialog(this);
                dialog.show();
                break;
        }
        return true;
    }
}