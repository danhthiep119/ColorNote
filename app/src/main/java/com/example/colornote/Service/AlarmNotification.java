package com.example.colornote.Service;

import android.app.AlertDialog;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.IBinder;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.example.colornote.R;
import com.example.colornote.View.MainActivity;
import com.example.colornote.View.ShowNotes;
import com.example.colornote.ViewModel.DataBinding;


public class AlarmNotification extends Service {
    private static final String CHANNEL_ID = "colornotechannel";

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        try {
            SharedPreferences sharedPreferences = getSharedPreferences("SaveSetting", MODE_PRIVATE);
            String sound = sharedPreferences.getString("sound-reminder", "Default sound");
            Uri uri;
            switch (sound) {
                case "Default sound":
                    uri = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.analog_watch);
                    break;
                case "Sound 1":
                    uri = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.bleep_alarm);
                    break;
                case "Sound 2":
                    uri = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.missile_alert);
                    break;
                case "Sound 3":
                    uri = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.old_fashion);
                    break;
                case "Sound 4":
                    uri = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.old_school_bell);
                    break;
                default:
                    uri = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.analog_watch);
                    break;
            }
            Intent notificationIntent = new Intent(this, ShowNotes.class);
            notificationIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            DataBinding dataBinding = new DataBinding(getApplicationContext());
            notificationIntent.putExtra("position",dataBinding.getList().size()-1);
            notificationIntent.putExtra("view","Main");
            if(!dataBinding.getList().get(dataBinding.getList().size()-1).getPassword().equals("")) {
                notificationIntent.putExtra("notification", "yes");
            }
            PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);
            String title = intent.getExtras().getString("title");
            String mess = intent.getExtras().getString("content");
                Notification notification = new NotificationCompat.Builder(this, CHANNEL_ID)
                        .setContentTitle(title)
                        .setContentText(mess)
                        .setSmallIcon(R.drawable.ic_baseline_bookmark_24)
                        .setSound(uri)
                        .setContentIntent(pendingIntent)
                        .build();
                startForeground(123, notification);
        } catch (Exception e) {

        }
        return START_NOT_STICKY;
    }

}
