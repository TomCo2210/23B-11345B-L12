package com.example.a23b_11345b_l12;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.IBinder;
import android.provider.Settings;
import android.util.Log;

public class DownloadService extends Service {
    public static final String ACTION_START = "ACTION_START";
    public static final String ACTION_STOP = "ACTION_STOP";
    public static final String DOWNLOAD = "DOWNLOAD";
    public static final String DOWNLOAD_PROGRESS = "DOWNLOAD_PROGRESS";
    private MediaPlayer mediaPlayer;

    private boolean isPlaying = false;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);
        String action = intent.getAction();
        Log.d("onStartCommand", "action is: " + action);
        if (action.equals(ACTION_START)) {
            if (!isPlaying) {
                new Thread(() -> {
                    Log.d("Start Downloading", "run downloadAndPlay()");
                    downloadAndPlay();
                }).start();
            }
        } else if (action.equals(ACTION_STOP)) {
            if (isPlaying) {
                Log.d("Stopping", "run stopMusic()");
                stopMusic();
            }
        }

        return START_STICKY;
    }

    private void downloadAndPlay() {
        isPlaying = true;
        for (int i = 0; i <= 100; i += 10) {
            Log.d("Downloading", "downloadAndPlay: i " + i);
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            Intent intent = new Intent(DOWNLOAD);
            intent.putExtra(DOWNLOAD_PROGRESS, i);
            sendBroadcast(intent);
        }
        startMusic();
    }


    private void startMusic() {
        mediaPlayer = MediaPlayer.create(this, Settings.System.DEFAULT_RINGTONE_URI);
        mediaPlayer.setLooping(true);
        mediaPlayer.start();
    }

    private void stopMusic() {
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer.release();
            mediaPlayer = null;
            isPlaying = false;
        }
    }


    @Override
    public IBinder onBind(Intent intent) {
        throw new UnsupportedOperationException("Not yet implemented");
    }
}