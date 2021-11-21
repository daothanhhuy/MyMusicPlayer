package com.hfad.musicplayer;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import java.io.Serializable;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";

    private Button startServiceBtn,stopServiceBtn;

    private MediaPlayer player;
    private RecyclerView recyclerView;
    private ArrayList<Song> songs;
    private SongAdapter adapter;

    private int lastPlaySong = -1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        startServiceBtn = findViewById(R.id.start_service_btn);
        stopServiceBtn = findViewById(R.id.stop_service_et);
        recyclerView = findViewById(R.id.rcv);




        startServiceBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Song song = new Song("Enemy", "Imagine Dragon", R.drawable.enemy, R.raw.enemy_imagine_dragon);
                Intent intent = new Intent(MainActivity.this, MyService.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("song", song);
                intent.putExtras(bundle);
                startService(intent);
            }
        });

        stopServiceBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, MyService.class);
                stopService(intent);
            }
        });

        songs = new ArrayList<>();
        songs.add(new Song("Jolyne Theme", "Unknow", R.drawable.jolyne, R.raw.jolyne_theme));
        songs.add(new Song("Mienaikara ne", "Sora Amamiya", R.drawable.mieruko_chan, R.raw.mienaikara_ne_sora_amamiya));
        songs.add(new Song("Never gonna give you up", "Rick Astley", R.drawable.rick_astley, R.raw.never_gonna_give_you_up_rick_astley));
        songs.add(new Song("Enemy", "Imagine Dragon", R.drawable.enemy, R.raw.enemy_imagine_dragon));

        if(songs.isEmpty()){
            Log.d(TAG, "onCreate: song is empty");
        }
        adapter = new SongAdapter(songs);
        adapter.setItemListener(new SongAdapter.SongInterface() {
            @Override
            public void playStopSong(int position, ImageButton view) {
                Toast.makeText(MainActivity.this, "Button play", Toast.LENGTH_SHORT).show();
                Song song = songs.get(position);
                // Call only one time
                if(lastPlaySong == -1) lastPlaySong = position;

                if(lastPlaySong == position){
                    if(player == null){
                        player = MediaPlayer.create(MainActivity.this, song.getResource());
                        song.setPlaying(true);
                        player.start();
                    } else {
                        if (player.isPlaying()){
                            player.pause();
                            song.setPlaying(false);
                        } else {
                            player.start();
                            song.setPlaying(true);
                        }
                    }

                } else { // when lastPosition != position
                    player.release();
                    player = null;
                    Song lastSong = songs.get(lastPlaySong);
                    lastSong.setPlaying(false);

                    player = MediaPlayer.create(MainActivity.this, song.getResource());
                    player.start();
                    song.setPlaying(true);
                    lastPlaySong = position;
                }
                adapter.notifyDataSetChanged();

            }
        });
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);


    }

    @Override
    protected void onStop() {
        if(lastPlaySong != -1) {
            Song song = songs.get(lastPlaySong);
            player.pause();
            song.setPlaying(false);
            adapter.notifyDataSetChanged();

            Intent intent = new Intent(MainActivity.this, MyService.class);
            Bundle bundle = new Bundle();
            bundle.putSerializable("song", song);

            intent.putExtras(bundle);
            startService(intent);
        }
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        if(player != null){
            player.release();
            player = null;
        }
        super.onDestroy();
    }

    public static class NotificationBroadcastReceiver extends BroadcastReceiver{
        @Override
        public void onReceive(Context context, Intent intent) {
            Intent serviceIntent = new Intent(context, MyService.class);

        }
    }


    //    if(player == null){
//        player = MediaPlayer.create(MainActivity.this,R.raw.never_gonna_give_you_up_rick_astley);
//    }
//            player.start();
//
//    if(player != null){
//        player.release();
//        player = null;
//    }
//
//     if(player != null){
//        player.pause();
//    }





}