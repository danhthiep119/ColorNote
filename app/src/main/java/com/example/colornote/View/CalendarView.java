package com.example.colornote.View;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.applandeo.materialcalendarview.EventDay;
import com.applandeo.materialcalendarview.listeners.OnDayClickListener;
import com.example.colornote.R;
import com.example.colornote.ViewModel.DataBinding;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class CalendarView extends AppCompatActivity {
    com.applandeo.materialcalendarview.CalendarView calendarView;
    Toolbar toolbar;
    DataBinding dataBinding = new DataBinding(this);
    String timeReminder = "";
    public CalendarView() throws IOException, JSONException {
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);
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
        List<String> date = new ArrayList<>();
        List<Date> date1 = new ArrayList<>();
        List<EventDay> eventDays = new ArrayList<>();
        if (!dataBinding.getList().isEmpty()) {
            for (int i = 0; i < dataBinding.getList().size(); i++) {
                if (!dataBinding.getList().get(i).getReminer().equals("") && dataBinding.getList().get(i).getIsSetReminder()) {
                    date.add(dataBinding.getList().get(i).getReminer());
                }
            }
        }
        if(!dataBinding.getArchives().isEmpty()){
            for (int i = 0; i < dataBinding.getArchives().size(); i++) {
                if (!dataBinding.getArchives().get(i).getReminer().equals("") && dataBinding.getArchives().get(i).getIsSetReminder()) {
                    date.add(dataBinding.getArchives().get(i).getReminer());
                }
            }
        }
        for (String formatDate : date) {
            try {
                date1.add(new SimpleDateFormat("HH : mm  , dd/MM/yyyy").parse(formatDate));
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        for (int i=0;i<date1.size();i++) {
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(date1.get(i).getTime());
            calendar.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DATE));
            eventDays.add(new EventDay(calendar, R.drawable.ic_baseline_star_24));
        }
        calendarView.setEvents(eventDays);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_calendar,menu);
        Calendar calendar = Calendar.getInstance();
        menu.getItem(0).setTitle(String.valueOf(calendar.get(Calendar.DATE)));
        return super.onCreateOptionsMenu(menu);
    }

    private void addEvents() {
        calendarView.setOnDayClickListener(new OnDayClickListener() {
            @Override
            public void onDayClick(@NotNull EventDay eventDay) {
                Calendar clickedDay = eventDay.getCalendar();
                String dateString="";
                if(clickedDay.get(Calendar.DATE)<10){
                    dateString = "0"+clickedDay.get(Calendar.DATE);
                }
                else dateString = ""+clickedDay.get(Calendar.DATE);
                timeReminder = "00 : 00  , "+dateString+"/"+(clickedDay.get(Calendar.MONTH)+1)+"/"+clickedDay.get(Calendar.YEAR);
                CalendarDialog dialog = null;
                try {
                    dialog = new CalendarDialog(CalendarView.this,clickedDay);
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
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


    private void addControls() {
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle("Calendar");
        calendarView = findViewById(R.id.calendar_view);
    }
}
