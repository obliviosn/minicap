package com.android.recoder.kikt;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Handler;
import android.os.IBinder;
import android.os.Vibrator;
import android.widget.Toast;

import java.io.IOException;

import io.devicefarmer.minicap.R;

public class RecordService extends Service {
    static final String TAG = "Check";
    boolean isForceStop = false;

    /* renamed from: n */
    Notification f4n;

    /* renamed from: nm */
    NotificationManager f5nm;
    String path;
    int pid;
    Process process;

    /* renamed from: r */
    public Runnable f6r = new Runnable() {

        /* renamed from: t */
        long f7t = 0;

        public void run() {
            this.f7t++;
            if (this.f7t <= ((long) RecordService.this.time)) {
//                RecordService.this.f4n.setLatestEventInfo(RecordService.this.getApplicationContext(), "Recording...", String.valueOf(this.f7t) + " sec / "
//                + RecordService.this.time + " sec", (PendingIntent) null);
                RecordService.this.f5nm.notify(0, RecordService.this.f4n);
                new Handler().postDelayed(RecordService.this.f6r, 1000);
            }
            if (this.f7t == ((long) RecordService.this.time)) {
                new Handler().postDelayed(new Runnable() {
                    @SuppressLint("WrongConstant")
                    public void run() {
                        ((Vibrator) RecordService.this.getSystemService("vibrator")).vibrate(300);
                        MediaPlayer mpEffect = MediaPlayer.create(RecordService.this.getApplicationContext(), R.raw.sound_finish);
                        mpEffect.start();
                        mpEffect.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                            public void onCompletion(MediaPlayer mp) {
                                mp.release();
                            }
                        });
                        Toast.makeText(RecordService.this.getBaseContext(), "Saved to " + RecordService.this.path, 0).show();
                        RecordService.this.f5nm.cancel(0);
                        RecordService.this.stopSelf();
                    }
                }, 500);
            }
        }
    };
    int time;

    public void onRebind(Intent intent) {
        super.onRebind(intent);
    }

    public boolean onUnbind(Intent intent) {
        return super.onUnbind(intent);
    }

    public void onCreate() {
        super.onCreate();
    }

    public void onDestroy() {
        super.onDestroy();
    }

    @SuppressLint("WrongConstant")
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (intent == null) {
            return 1;
        }
        String action = intent.getAction();
        if (action == null) {
            this.f5nm = (NotificationManager) getSystemService("notification");
            String[] cmd_set = intent.getStringArrayExtra("Command");
            this.time = intent.getIntExtra("Time", 180);
            this.path = intent.getStringExtra("Path");
            try {
                this.process = Runtime.getRuntime().exec(cmd_set);
                String sPid = this.process.toString();
                this.pid = Integer.parseInt(sPid.substring(12, sPid.length() - 1));
                createNotification();
                ((Vibrator) getSystemService("vibrator")).vibrate(50);
                MediaPlayer mpEffect = MediaPlayer.create(getApplicationContext(), R.raw.sound_start);
                mpEffect.start();
                mpEffect.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                    public void onCompletion(MediaPlayer mp) {
                        mp.release();
                    }
                });
                return 1;
            } catch (IOException e) {
                e.printStackTrace();
                return 1;
            }
        } else {
            action.equals("stop");
            return 1;
        }
    }

    public void createNotification() {
        this.f4n = new Notification(R.drawable.ic_launcher_foreground, "Recording...", System.currentTimeMillis());
//        this.f4n.setLatestEventInfo(this, "Recording...", "0  sec / " + this.time + " sec", (PendingIntent) null);
        this.f4n.flags |= 2;
        this.f5nm.notify(0, this.f4n);
        new Handler().postDelayed(this.f6r, 1000);
    }

    public IBinder onBind(Intent intent) {
        return null;
    }
}
