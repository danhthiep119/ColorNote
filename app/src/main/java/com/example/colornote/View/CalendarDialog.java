package com.example.colornote.View;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
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
import java.util.Calendar;
import java.util.List;

public class CalendarDialog extends Dialog {
    List<Title> list = new ArrayList<>();
    DataBinding dataBinding;
    RecyclerView rvList;
    Button btnAdd;
    TextView txtDate;
    Context mContext;
    Calendar mCalendar;
    int position;
    ContentAdapter contentAdapter;


    public CalendarDialog(@NonNull Context context, Calendar calendar) throws IOException, JSONException {
        super(context);
        this.mContext = context;
        this.mCalendar = calendar;
        this.dataBinding = new DataBinding(mContext);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_dialog);
        addControls();
        addEvents();
    }

    private void addEvents() {
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OptionMenu dialog = new OptionMenu(mContext, "create");
                dialog.show();
            }
        });
    }

    private void addControls() {
        txtDate = findViewById(R.id.txtDate);
        btnAdd = findViewById(R.id.btnAdd);
        rvList = findViewById(R.id.rvList);
        int date = mCalendar.get(Calendar.DAY_OF_MONTH);
        int month = mCalendar.get(Calendar.MONTH);
        int year = mCalendar.get(Calendar.YEAR);
        int monday = mCalendar.get(Calendar.DAY_OF_WEEK);
        String thu = "";
        switch (monday) {
            case 1:
                thu = "Sunday";
                break;
            case 2:
                thu = "Monday";
                break;
            case 3:
                thu = "Tuesday";
                break;
            case 4:
                thu = "Wednesday";
                break;
            case 5:
                thu = "Thursday";
                break;
            case 6:
                thu = "Friday";
                break;
            case 7:
                thu = "Saturday";
                break;
        }
        String dateString = "";
        if(date<10){
            dateString = "0"+date;
        }
        else dateString = ""+date;
        String timeReminder = dateString  + "/" + (month + 1) + "/" + year;
        txtDate.setText(thu + ", " + timeReminder);
        if (!dataBinding.getList().isEmpty()) {
            for (int i = 0; i < dataBinding.getList().size(); i++) {
                if (!dataBinding.getList().get(i).getReminer().equals("")) {
                    String reminder = dataBinding.getList().get(i).getReminer();
                    String[] getDate = reminder.split(", ");
                    position = i;
                    if (getDate[getDate.length - 1].equals(timeReminder)) {
                        list.add(dataBinding.getList().get(i));
                    }
                }
            }
        }
        if (!dataBinding.getArchives().isEmpty()) {
            for (int i = 0; i < dataBinding.getArchives().size(); i++) {
                if (!dataBinding.getList().get(i).getReminer().equals("")) {
                    String reminder = dataBinding.getArchives().get(i).getReminer();
                    String[] getDate = reminder.split(", ");
                    position = i;
                    if (getDate[getDate.length - 1].equals(timeReminder)) {
                        list.add(dataBinding.getArchives().get(i));
                    }
                }
            }
        }
        contentAdapter = new ContentAdapter(list, mContext);
        showList(contentAdapter);
    }

    private void showList(ContentAdapter contentAdapter) {
        rvList.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false));
        rvList.setHasFixedSize(true);
        rvList.setAdapter(contentAdapter);
        contentAdapter.notifyDataSetChanged();
    }
}
