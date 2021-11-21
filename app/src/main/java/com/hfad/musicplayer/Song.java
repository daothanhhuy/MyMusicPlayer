package com.hfad.musicplayer;

import android.media.MediaPlayer;

import java.io.Serializable;

public class Song implements Serializable {
    private String title;
    private String singer;
    private int image;
    private int resource;
    private boolean isPlaying;


    public Song(String title, String singer, int image, int resource) {
        this.title = title;
        this.singer = singer;
        this.image = image;
        this.resource = resource;
        isPlaying = false;
    }

    public String getTitle() {
        return title;
    }

    public String getSinger() {
        return singer;
    }

    public int getImage() {
        return image;
    }

    public int getResource() {
        return resource;
    }


    public boolean isPlaying() {
        return isPlaying;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setSinger(String singer) {
        this.singer = singer;
    }

    public void setImage(int image) {
        this.image = image;
    }

    public void setResource(int resource) {
        this.resource = resource;
    }

    public void setPlaying(boolean playing) {
        isPlaying = playing;
    }

    @Override
    public String toString() {
        return "Song{" +
                "title='" + title + '\'' +
                ", singer='" + singer + '\'' +
                ", image=" + image +
                ", resource=" + resource +
                '}';
    }
}
