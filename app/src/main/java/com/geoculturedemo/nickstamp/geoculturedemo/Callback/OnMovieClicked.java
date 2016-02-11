package com.geoculturedemo.nickstamp.geoculturedemo.Callback;

import android.widget.ImageView;

import com.geoculturedemo.nickstamp.geoculturedemo.Model.Movie;

/**
 * Created by nickstamp on 1/6/2016.
 */
public interface OnMovieClicked {

    void onMovie(Movie movie, ImageView movieImage);
}