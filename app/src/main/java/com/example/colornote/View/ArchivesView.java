package com.example.colornote.View;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.PostProcessor;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.colornote.R;
import com.example.colornote.ViewModel.DataBinding;
import com.google.android.material.navigation.NavigationView;

import org.json.JSONException;

import java.io.IOException;

public class ArchivesView extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    DrawerLayout drawerLayout;
    NavigationView navView;
    Toolbar toolbar;
    RecyclerView rvListNote;
    ContentAdapter adapter;
    DataBinding dataBinding = new DataBinding(this);

    public ArchivesView() throws IOException, JSONException {
    }

    @Override
    protected void onResume() {
        super.onResume();
        SharedPreferences sharedPreferences = getSharedPreferences("SaveSetting", Context.MODE_PRIVATE);
        String mode = sharedPreferences.getString("theme", "Dark");
        if (mode.equals("Dark")) {
            toolbar.setBackgroundColor(Color.parseColor("#696C69"));
        }
        if (mode.equals("Soft")) {
            toolbar.setBackgroundColor(Color.parseColor("#B0F6EDED"));
        }
        if (!dataBinding.getArchives().isEmpty()) {
            adapter.notifyDataSetChanged();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_archives_view);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle("Archive");
        adapter = new ContentAdapter(dataBinding.getArchives(), this);
        drawerLayout = findViewById(R.id.drawerLayout);
        navView = findViewById(R.id.nav_view);
        rvListNote = findViewById(R.id.rvListNote);
        showView(adapter);
        NavigationDrawer();
    }

    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        }
    }

    private void NavigationDrawer() {
        setSupportActionBar(toolbar);
        navView.bringToFront();
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        navView.setNavigationItemSelectedListener(this);
    }

    public void showView(ContentAdapter adapter) {
        rvListNote.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        rvListNote.setHasFixedSize(true);
        rvListNote.setAdapter(adapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_archives, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.nav_sort:
                OptionMenu optionMenu = new OptionMenu(ArchivesView.this, "sort");
                optionMenu.show();
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
                Intent intent1 = new Intent(this, MainActivity.class);
                startActivity(intent1);
                break;
            case R.id.nav_Setting:
                Intent intent = new Intent(this, Setting.class);
                startActivity(intent);
                break;
            case R.id.nav_Archive:
                if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
                    drawerLayout.closeDrawer(GravityCompat.START);
                }
                break;
            case R.id.nav_Trash:
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
        }
        return true;
    }
}