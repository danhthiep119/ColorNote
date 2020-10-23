package com.example.colornote.Service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;

import androidx.annotation.RequiresApi;

public class AlarmReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d("Toi trong receiver", "xin chao!");
        String title = intent.getExtras().getString("title");
        String mess = intent.getExtras().getString("content");
        Intent intent1 = new Intent(context, AlarmNotification.class);
        intent1.putExtra("title", title);
        intent1.putExtra("content", mess);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            context.startForegroundService(intent1);
        }
        else context.startService(intent1);
    }
}
