package com.example.colornote.View;

import android.app.SearchManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.colornote.Model.Content;
import com.example.colornote.Model.ListCheck;
import com.example.colornote.Model.Title;
import com.example.colornote.R;
import com.example.colornote.ViewModel.DataBinding;

import org.json.JSONException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class SearchView extends AppCompatActivity {
    TextView txtRessult;
    Toolbar toolbar;
    DataBinding dataBinding = new DataBinding(this);
    RecyclerView rvSearch;
    ContentAdapter adapter;
    List<Title> result = new ArrayList<>();
    List<Title> list = new ArrayList<>();
    LinearLayout llPickZone;
    ImageButton btnNote, btnCheck, btnReminder, btnChangeColor;
    boolean isReminder = false;
    boolean isNote = false;
    boolean isCheck = false;
    boolean isColor = false;
    List<Content> contents = new ArrayList<>();
    List<ListCheck> listChecks = new ArrayList<>();
    String color = "";
    final boolean[] check = {false};

    public SearchView() throws IOException, JSONException {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_view);
        addControls();
        addEvents();
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
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_search, menu);
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        android.widget.SearchView searchView = (android.widget.SearchView) menu.findItem(R.id.nav_search).getActionView();
        if (searchView != null) {
            assert searchManager != null;
            searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
            searchView.setIconifiedByDefault(false);
        }
        android.widget.SearchView.OnQueryTextListener queryTextListener = new android.widget.SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public boolean onQueryTextChange(String newText) {
                System.out.println(newText);
                result.clear();
                if (!newText.equals("")) {
                    if (isNote) {
                        for (int i = 0; i < list.size(); i++) {
                            if (list.get(i).getTitle().contains(newText) && list.get(i) instanceof Content) {
                                result.add(list.get(i));
                            }
                        }
                    }
                    if (isCheck) {
                        for (int i = 0; i < list.size(); i++) {
                            if (list.get(i).getTitle().contains(newText) && list.get(i) instanceof ListCheck) {
                                result.add(list.get(i));
                            }
                        }
                    }
                    if (isReminder) {
                        result.clear();
                        for (int i = 0; i < list.size(); i++) {
                            if (list.get(i).getTitle().contains(newText) && list.get(i).getIsSetReminder()) {
                                result.add(list.get(i));
                            }
                        }
                    }
                    if (isColor) {
                        result.clear();
                        System.out.println(color);
                        if (isReminder) {
                            for (int i = 0; i < list.size(); i++) {
                                if (list.get(i).getTitle().contains(newText) && list.get(i).getColor().equals(color) && list.get(i).getIsSetReminder()) {
                                    result.add(list.get(i));
                                }
                            }
                        } else {
                            for (int i = 0; i < list.size(); i++) {
                                if (list.get(i).getTitle().contains(newText) && list.get(i).getColor().equals(color)) {
                                    result.add(list.get(i));
                                }
                            }
                        }
                    }
                    if (!isNote && !isCheck && !isReminder) {
                        List<Title> allList = new ArrayList<>();
                        allList.addAll(dataBinding.getList());
                        allList.addAll(dataBinding.getArchives());
                        for(int i = 0;i<allList.size();i++){
                            if(allList.get(i).getTitle().contains(newText)){
                                result.add(allList.get(i));
                            }
                        }
                    }
                    adapter = new ContentAdapter(result, SearchView.this);
                    if (result.isEmpty()) {
                        txtRessult.setVisibility(View.VISIBLE);
                    } else txtRessult.setVisibility(View.INVISIBLE);
                    showView(adapter);
                }
                return true;
            }
        };
        assert searchView != null;
        searchView.setOnQueryTextListener(queryTextListener);

        return true;
    }

    void showView(ContentAdapter adapter) {
        rvSearch.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        rvSearch.setHasFixedSize(true);
        rvSearch.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();

    }

    private void addEvents() {
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        btnNote.setOnClickListener(listener);
        btnCheck.setOnClickListener(listener);
        btnReminder.setOnClickListener(listener);
        btnChangeColor.setOnClickListener(listener);
    }

    CompoundButton.OnClickListener listener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.btnNote:
                    if (!check[0]) {
                        check[0] = true;
                        isNote = true;
                    } else {
                        check[0] = false;
                        isNote = false;
                    }
                    setColorButton(isNote, btnNote);
                    break;
                case R.id.btnCheck:
                    if (!check[0]) {
                        check[0] = true;
                        isCheck = true;
                    } else {
                        check[0] = false;
                        isCheck = false;
                    }
                    setColorButton(isCheck, btnCheck);
                    break;
                case R.id.btnReminder:
                    if (!check[0]) {
                        check[0] = true;
                        isReminder = true;
                    } else {
                        check[0] = false;
                        isReminder = false;
                    }
                    setColorButton(isReminder, btnReminder);
                    break;
                case R.id.btnChangeColor:
                    if (!check[0]) {
                        check[0] = true;
                        isColor = true;
                    } else {
                        check[0] = false;
                        isColor = false;
                    }
                    ColorDialog dialog = new ColorDialog(SearchView.this, SearchView.this);
                    dialog.show();
                    break;
            }
        }
    };

    void setColorButton(boolean b, ImageButton btn) {
        if (b) {
            btn.setBackgroundColor(Color.parseColor("#FFFFFF"));
        } else btn.setBackgroundColor(Color.parseColor("#696C69"));
    }

    private void addControls() {
        rvSearch = findViewById(R.id.rvSearch);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle("Search");
        txtRessult = findViewById(R.id.txtResult);
        llPickZone = findViewById(R.id.llPickZone);
        btnNote = findViewById(R.id.btnNote);
        btnCheck = findViewById(R.id.btnCheck);
        btnReminder = findViewById(R.id.btnReminder);
        btnChangeColor = findViewById(R.id.btnChangeColor);
        list.addAll(dataBinding.getList());
        list.addAll(dataBinding.getArchives());
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i) instanceof Content) {
                contents.add((Content) list.get(i));
            } else if (list.get(i) instanceof ListCheck) {
                listChecks.add((ListCheck) list.get(i));
            }
        }
        setColorButton(false, btnNote);
        setColorButton(false, btnCheck);
        setColorButton(false, btnReminder);
        setColorButton(false, btnChangeColor);
    }
}