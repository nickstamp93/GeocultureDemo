package com.geoculturedemo.nickstamp.geoculturedemo.Callback;

import com.geoculturedemo.nickstamp.geoculturedemo.Model.Movie;
import com.geoculturedemo.nickstamp.geoculturedemo.Model.Song;

/**
 * Created by nickstamp on 1/31/2016.
 */
public interface OnFavoriteDelete {

    public void onDelete(Movie movie);

    public void onDelete(Song song);
}
