package com.hfad.musicplayer;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.widget.RemoteViews;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

public class MyService extends Service {
    private static final String TAG = "MyService";

    private static final int ACTION_PAUSE = 1;
    private static final int ACTION_RESUME = 2;
    private static final int ACTION_CLEAR = 3;


    // Call only 1 time
    MediaPlayer player;
    private Song mSong;

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Bundle bundle = intent.getExtras();
        if (bundle != null) {
            Song song = (Song) bundle.get("song");
            if(song != null){
                mSong = song;
                startMusic(song);
                sendNotification(song);
            }
        }
        
        int action = intent.getIntExtra("service_action", 0);
        handleActionMusic(action);

        return START_NOT_STICKY; // service ko chạy lại khi bị dừng
    }

    private void startMusic(Song song) {
        if(player == null){
            player = MediaPlayer.create(getApplicationContext(), song.getResource());
        } else {
            player.release();
            player = null;
            player = MediaPlayer.create(getApplicationContext(), song.getResource());
        }
        if(song.isPlaying())  {

        }
    }

    private void handleActionMusic(int action){
        switch (action){
            case ACTION_PAUSE:
                pauseMusic();
                Log.d(TAG, "handleActionMusic: Pause Music");
                sendNotification(mSong);
                break;
            case ACTION_CLEAR:
                stopSelf();
                break;
            case ACTION_RESUME:
                resumeMusic();
                sendNotification(mSong);
                break;
        }
    }
    private void pauseMusic(){
        if(player != null && player.isPlaying()){
            player.pause();
        }
    }

    private void resumeMusic(){
        if(player != null && !player.isPlaying()){
            player.start();
        }
    }


    private void sendNotification(Song song) {
        Intent intent = new Intent(this, MainActivity.class);
        intent.setAction(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_LAUNCHER);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

      //  PendingIntent stop = PendingIntent.getService(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        RemoteViews remoteViews = new RemoteViews(getPackageName(), R.layout.music_player_notification);
        remoteViews.setTextViewText(R.id.title_tv, song.getTitle());
        remoteViews.setTextViewText(R.id.singer_tv, song.getSinger());
        remoteViews.setImageViewResource(R.id.song_iv, song.getImage());
        remoteViews.setImageViewResource(R.id.start_pause_iv, R.drawable.ic_baseline_pause_24);


        if(player.isPlaying()) {
            remoteViews.setOnClickPendingIntent(R.id.start_pause_iv, getPendingIntent(this, ACTION_PAUSE));
            remoteViews.setImageViewResource(R.id.start_pause_iv, R.drawable.ic_baseline_pause_24);
        } else {
            remoteViews.setOnClickPendingIntent(R.id.start_pause_iv, getPendingIntent(this, ACTION_RESUME));
            remoteViews.setImageViewResource(R.id.start_pause_iv, R.drawable.ic_baseline_play_arrow_24);
        }

        remoteViews.setOnClickPendingIntent(R.id.close_iv, getPendingIntent(this, ACTION_CLEAR));
        Notification notification = new NotificationCompat.Builder(this, App.CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_baseline_music_note_24)
                .setContentIntent(pendingIntent)
                .setCustomContentView(remoteViews)
                .build();
        startForeground(2, notification);


    }

    private PendingIntent getPendingIntent(Context context, int action) {
            Intent intent = new Intent(this, MyReceiver.class);
            intent.putExtra("action", action);
            return PendingIntent.getBroadcast(context.getApplicationContext(), action, intent, PendingIntent.FLAG_UPDATE_CURRENT);
    }


    // Call when destroy
    @Override
    public void onDestroy() {
        super.onDestroy();
        if (player != null) {
            player.release();
            player = null;
        }
    }

}
