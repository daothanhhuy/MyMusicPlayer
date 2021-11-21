package com.hfad.musicplayer;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class SongAdapter extends RecyclerView.Adapter<SongAdapter.ViewHolder> {

    private static final String TAG = "SongAdapter";
    private ArrayList<Song> songs;
    private SongInterface listener;

    public void setItemListener(SongInterface listener){
        this.listener = listener;
    }

    public SongAdapter(ArrayList<Song> songs) {
        this.songs = songs;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.song_item_layout, parent, false);
        return new ViewHolder(view, listener);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Song song = songs.get(position);
        Log.d(TAG, "onBindViewHolder: " + song.toString());

        holder.singerTv.setText(song.getSinger());
        holder.titleTv.setText(song.getTitle());
        holder.imageIv.setImageResource(song.getImage());

        if(song.isPlaying()){
            holder.playStopBtn.setImageResource(R.drawable.ic_baseline_pause_24);
        } else {
            holder.playStopBtn.setImageResource(R.drawable.ic_baseline_play_arrow_24);
        }
    }

    @Override
    public int getItemCount() {
        return songs.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        private ImageView imageIv;
        private TextView titleTv, singerTv;
        private ImageButton playStopBtn;
        private SongInterface listener;

        public ViewHolder(@NonNull View view, SongInterface listener) {
            super(view);
            imageIv = view.findViewById(R.id.song_iv);
            titleTv = view.findViewById(R.id.song_name_tv);
            singerTv = view.findViewById(R.id.song_singer_tv);
            playStopBtn = view.findViewById(R.id.play_stop);
            this.listener = listener;

            playStopBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    listener.playStopSong(getAdapterPosition(), (ImageButton) view);
                }
            });
        }
    }

    public interface SongInterface{
        void playStopSong(int position, ImageButton view);
    }
}
