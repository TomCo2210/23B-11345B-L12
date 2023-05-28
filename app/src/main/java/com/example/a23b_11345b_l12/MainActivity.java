package com.example.a23b_11345b_l12;

import androidx.appcompat.app.AppCompatActivity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.widget.ProgressBar;

import com.google.android.material.button.MaterialButton;

public class MainActivity extends AppCompatActivity {

    private ProgressBar main_PRG_download;
    private MaterialButton main_BTN_stop;
    private MaterialButton main_BTN_start;

    private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent == null) return;

            runOnUiThread(() -> {
                int progress = intent.getIntExtra(DownloadService.DOWNLOAD_PROGRESS,0);
                updateProgress(progress);
            });
        }
    };

    @Override
    protected void onStart() {
        super.onStart();
        registerReceiver(broadcastReceiver,new IntentFilter(DownloadService.DOWNLOAD));
    }

    @Override
    protected void onStop() {
        super.onStop();
        unregisterReceiver(broadcastReceiver);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViews();
        initViews();

    }

    private void initViews() {
        main_BTN_stop.setOnClickListener(v -> stop());
        main_BTN_start.setOnClickListener(v -> start());
    }

    private void start() {
        main_PRG_download.setProgress(0);
        main_PRG_download.setMax(100);
        //startService()
        Intent intent = new Intent(this, DownloadService.class);
        intent.setAction(DownloadService.ACTION_START);
        startService(intent);
    }

    private void stop() {
        //stopService()
        Intent intent = new Intent(this, DownloadService.class);
        intent.setAction(DownloadService.ACTION_STOP);
        startService(intent);
    }

    private void findViews() {
        main_PRG_download = findViewById(R.id.main_PRG_download);
        main_BTN_stop = findViewById(R.id.main_BTN_stop);
        main_BTN_start = findViewById(R.id.main_BTN_start);
    }

    private void updateProgress(int progress) {
        main_PRG_download.setProgress(progress + 1);
    }
}