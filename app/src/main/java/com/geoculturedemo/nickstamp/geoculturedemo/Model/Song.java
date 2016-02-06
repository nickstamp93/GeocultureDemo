package com.geoculturedemo.nickstamp.geoculturedemo.Model;

import android.database.Cursor;

import java.io.Serializable;

/**
 * Created by nickstamp on 1/6/2016.
 */
public class Song implements Serializable {


    private String id;
    private String url;
    private String title;
    private String year;
    private String lyricsCreator;
    private String musicCreator;
    private String artist;
    private String lyrics;
    private String links;


    public Song(String id, String title, String year, String lyricsCreator, String musicCreator, String artist, String url) {

        this.id = id;
        this.title = title;
        this.year = year;
        this.lyricsCreator = lyricsCreator;
        this.musicCreator = musicCreator;
        this.artist = artist;
        this.url = url;
        this.links = "";
        this.lyrics = "";
    }

    public Song(Cursor cSongs) {
        this.id = cSongs.getString(0);
        this.url = cSongs.getString(1);
        this.title = cSongs.getString(2);
        this.year = cSongs.getString(3);
        this.lyricsCreator = cSongs.getString(4);
        this.musicCreator = cSongs.getString(5);
        this.artist = cSongs.getString(6);
        this.lyrics = cSongs.getString(7);
        this.links = cSongs.getString(8);

    }

    @Override
    public boolean equals(Object object) {
        boolean isIdentical = false;

        if (object != null && object instanceof Song) {
            isIdentical = this.title.equals(((Song) object).title)
                    && this.year.equals(((Song) object).year)
                    && this.artist.equals(((Song) object).artist);
        }

        return isIdentical;
    }

    public String getLyricsCreator() {

        if (lyricsCreator.trim().equals(""))
            return "Άγνωστος";
        return lyricsCreator;
    }

    public void setLyricsCreator(String lyricsCreator) {
        this.lyricsCreator = lyricsCreator;
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
        return "http://www.stixoi.info/" + url;
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

    public String getMusicCreator() {

        if (musicCreator.trim().equals(""))
            return "Άγνωστος";
        return musicCreator;
    }

    public String getLyrics() {
        return lyrics;
    }

    public void setLyrics(String lyrics) {
        this.lyrics = lyrics;
    }

    public void setLinks(String links) {
        this.links = links;
    }

    public String getLinks() {
        return links;
    }
}
