package com.example.colornote.View;

import android.app.Dialog;
import android.content.Context;
import android.widget.Button;
import android.widget.CalendarView;

import androidx.annotation.NonNull;

import com.example.colornote.R;

public class DateDialog extends Dialog {
    Context mContext;
    CalendarView calendarView;
    Button btnDate;
    public DateDialog(@NonNull Context context, final Button btnDate) {
        super(context);
        this.mContext = context;
        this.btnDate = btnDate;
        setContentView(R.layout.calendar_dialog);
        calendarView = findViewById(R.id.calendarView);
        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
                String date = dayOfMonth+"/"+month+"/"+year;
                btnDate.setText(date);
            }
        });
    }
}
