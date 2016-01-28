package com.geoculturedemo.nickstamp.geoculturedemo.Model;

import android.database.Cursor;

import java.io.Serializable;

/**
 * Created by nickstamp on 1/6/2016.
 */
public class Movie implements Serializable {

    private String imgUrl;
    private String title;
    private String cast;
    private String director;
    private String writer;
    private String genre;
    private String rating;
    private String runtime;
    private String synopsis;
    private String year;

    private String url;


    public Movie(String url, String title, String rating, String imgUrl, String runtime, String genre, String director, String cast, String year) {

        this.url = url;
        this.title = title;
        this.rating = rating;
        this.imgUrl = imgUrl;
        this.runtime = runtime;
        this.genre = genre;
        this.director = director;
        this.cast = cast;
        this.year = year;
    }

    public Movie(Cursor cMovie) {
        this.url = cMovie.getString(1);
        this.title = cMovie.getString(2);
        this.year = cMovie.getString(3);
        this.genre = cMovie.getString(4);
        this.rating = cMovie.getString(5);
        this.runtime = cMovie.getString(6);
        this.imgUrl = cMovie.getString(7);
        this.cast = cMovie.getString(8);
        this.director = cMovie.getString(9);
        this.writer = cMovie.getString(10);
        this.synopsis = cMovie.getString(11);
    }

    @Override
    public boolean equals(Object object) {
        boolean isIdentical = false;

        if (object != null && object instanceof Movie) {
            isIdentical = this.url.equals(((Movie) object).url);
        }

        return isIdentical;
    }


    public String getUrl() {
        return "http://www.imdb.com/" + url;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getRating() {

        if (rating.trim().length() > 0)
            return rating;
        else
            return "-";
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public String getGenre() {
        if (genre.trim().length() > 0)
            return genre;
        else
            return "-";
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getRuntime() {
        if (runtime.trim().length() > 0)
            return runtime;
        else
            return "-";
    }

    public void setRuntime(String runtime) {
        this.runtime = runtime;
    }

    public String getSynopsis() {
        return synopsis;
    }

    public void setSynopsis(String synopsis) {
        this.synopsis = synopsis;
    }

    public String getCast() {
        return cast;
    }

    public void setCast(String cast) {
        this.cast = cast;
    }

    public String getDirector() {
        return director;
    }

    public void setDirector(String director) {
        this.director = director;
    }

    public String getWriter() {
        return writer;
    }

    public void setWriter(String writer) {
        this.writer = writer;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
