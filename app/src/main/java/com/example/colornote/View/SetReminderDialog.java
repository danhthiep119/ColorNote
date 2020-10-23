package com.example.colornote.View;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.Switch;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.colornote.R;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;

public class SetReminderDialog extends Dialog implements DatePickerDialog.OnDateSetListener {
    Activity mContext;
    Button btnTime, btnDate, btnCancel, btnSave;
    SimpleDateFormat dateFormat, timeFormat;
    long time = System.currentTimeMillis();
    String timePicker = "";
    String datePicker = "";
    Switch swReminder;

    public SetReminderDialog(Activity context) {
        super(context);
        this.mContext = context;
        setContentView(R.layout.set_reminder_dialog);
        dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        timeFormat = new SimpleDateFormat("HH : mm ");
        timePicker = timeFormat.format(time);
        datePicker = dateFormat.format(time);
        addControls();
        addEvents();
    }

    private void addEvents() {
        btnSave.setOnClickListener(listener);
        btnDate.setOnClickListener(listener);
        btnCancel.setOnClickListener(listener);
        btnTime.setOnClickListener(listener);
        swReminder.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    buttonView.setChecked(true);
                    if (mContext instanceof InputContent) {
                        ((InputContent) mContext).isSetReminder = true;
                    } else if (mContext instanceof CheckList) {
                        ((CheckList) mContext).isSetReminder = true;
                    } else if (mContext instanceof ShowNotes) {
                        ((ShowNotes) mContext).isSetReminder = true;
                    }
                } else {
                    buttonView.setChecked(false);
                    if (mContext instanceof InputContent) {
                        ((InputContent) mContext).isSetReminder = false;
                    } else if (mContext instanceof CheckList) {
                        ((CheckList) mContext).isSetReminder = false;
                    } else if (mContext instanceof ShowNotes) {
                        ((ShowNotes) mContext).isSetReminder = false;
                    }
                }
            }
        });
    }

    CompoundButton.OnClickListener listener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.btnCancel:
                    dismiss();
                    break;
                case R.id.btnSave:
                    try {
                        save();
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    break;
                case R.id.btnSetDate:
                    setDate();
                    break;
                case R.id.btnSetTime:
                    setTime();
                    break;
            }
        }
    };

    private void setTime() {
        Calendar c = Calendar.getInstance();
        int hour = c.get(Calendar.HOUR_OF_DAY);
        int minute = c.get(Calendar.MINUTE);
        TimePickerDialog dialog = new TimePickerDialog(mContext, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                String minuteString="";
                String hourString="";
                if(minute<10){
                    minuteString = "0"+minute;
                }
                else minuteString = String.valueOf(minute);
                if(hourOfDay<10){
                    hourString = "0"+hourOfDay;
                }
                else hourString = String.valueOf(hourOfDay);
                timePicker = hourString + " : " + minuteString + " ";
                btnTime.setText(timePicker);
            }
        }, hour, minute, false);
        dialog.show();

    }

    private void setDate() {
        DatePickerDialog dialog = new DatePickerDialog(mContext, this,
                Calendar.getInstance().get(Calendar.YEAR),
                Calendar.getInstance().get(Calendar.MONTH),
                Calendar.getInstance().get(Calendar.DAY_OF_MONTH));
        dialog.show();
    }

    private void save() throws ParseException {
        String reminder = timePicker + " , " + datePicker;
        SimpleDateFormat dateFormat = new SimpleDateFormat("HH : mm  , dd/MM/yyyy");
        Date date = dateFormat.parse(reminder);
        if (System.currentTimeMillis() > date.getTime()) {
            Toast.makeText(mContext, "You cannot set time in the past!", Toast.LENGTH_SHORT).show();
        } else {
            if (mContext instanceof InputContent) {
                ((InputContent) mContext).btnSetReminder.setText(timePicker + " , " + datePicker);
            } else if (mContext instanceof CheckList) {
                ((CheckList) mContext).btnSetReminder.setText(timePicker + " , " + datePicker);
            } else if (mContext instanceof ShowNotes) {
                ((ShowNotes) mContext).reminder = timePicker + " , " + datePicker;
            }
            dismiss();
        }
    }

    private void addControls() {
        btnTime = findViewById(R.id.btnSetTime);
        btnDate = findViewById(R.id.btnSetDate);
        btnCancel = findViewById(R.id.btnCancel);
        btnSave = findViewById(R.id.btnSave);
        btnTime.setText(timeFormat.format(time));
        btnDate.setText(dateFormat.format(time));
        swReminder = findViewById(R.id.swReminder);
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        String dateString="";
        if(dayOfMonth<10){
            dateString = "0"+dayOfMonth;
        }
        else dateString = ""+dayOfMonth;
        datePicker = dateString + "/" + (month + 1) + "/" + year;
        btnDate.setText(datePicker);
    }
}
