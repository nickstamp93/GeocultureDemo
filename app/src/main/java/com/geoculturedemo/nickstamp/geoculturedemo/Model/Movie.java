package com.geoculturedemo.nickstamp.geoculturedemo.Model;

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


    public Movie(String url, String title, String rating, String imgUrl, String runtime, String genre) {

        this.url = url;
        this.title = title;
        this.rating = rating;
        this.imgUrl = imgUrl;
        this.runtime = runtime;
        this.genre = genre;
    }


    public String getUrl() {
        return url;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getRating() {

        if(rating.trim().length()>0)
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
        if(genre.trim().length()>0)
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
        if(runtime.trim().length()>0)
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
