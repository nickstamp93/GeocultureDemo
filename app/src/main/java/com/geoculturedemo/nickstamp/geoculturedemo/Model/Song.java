package com.geoculturedemo.nickstamp.geoculturedemo.Model;

/**
 * Created by nickstamp on 1/6/2016.
 */
public class Song {


    private String id;
    private String title;
    private String year;
    private String lyricsWriter;
    private final String musicCreator;
    private String composer;
    private String artist;
    private String url;


    public Song(String id, String title, String year, String lyricsWriter, String musicCreator, String artist, String url) {

        this.id = id;
        this.title = title;
        this.year = year;
        this.lyricsWriter = lyricsWriter;
        this.musicCreator = musicCreator;
        this.artist = artist;
        this.url = url;
    }

    public String getComposer() {
        return composer;
    }

    public void setComposer(String composer) {
        this.composer = composer;
    }

    public String getLyricsWriter() {
        return lyricsWriter;
    }

    public void setLyricsWriter(String lyricsWriter) {
        this.lyricsWriter = lyricsWriter;
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

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
