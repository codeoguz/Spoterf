package com.example.freespotify;

import android.media.Image;

public class music {
    public String musicName;
    public String artistName;
    public Image photo;

    public music(String musicName, String artistName) {
        this.musicName = musicName;
        this.artistName = artistName;
        this.photo = photo;
    }

    public String getMusicName() {
        return musicName;
    }

    public void setMusicName(String musicName) {
        this.musicName = musicName;
    }

    public String getArtistName() {
        return artistName;
    }

    public void setArtistName(String artistName) {
        this.artistName = artistName;
    }

    public Image getPhoto() {
        return photo;
    }

    public void setPhoto(Image photo) {
        this.photo = photo;
    }
}
