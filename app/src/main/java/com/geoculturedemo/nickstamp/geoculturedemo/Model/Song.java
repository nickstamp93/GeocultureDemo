package com.geoculturedemo.nickstamp.geoculturedemo.Model;

/**
 * Created by nickstamp on 1/6/2016.
 */
public class Song {


    private String title;
    private String year;
    private String lyricist;
    private String composer;
    private String artist;
    private String url;


    public Song(String title, String year, String lyricist, String musician, String artist, String url) {

    }

    public String getComposer() {
        return composer;
    }

    public void setComposer(String composer) {
        this.composer = composer;
    }

    public String getLyricist() {
        return lyricist;
    }

    public void setLyricist(String lyricist) {
        this.lyricist = lyricist;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getArtist() {

        if (artist.trim().equals(""))
            return "Άγνωστος";
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
