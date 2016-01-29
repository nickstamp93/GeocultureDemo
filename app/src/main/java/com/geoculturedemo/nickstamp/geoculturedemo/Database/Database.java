package com.geoculturedemo.nickstamp.geoculturedemo.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.geoculturedemo.nickstamp.geoculturedemo.Model.Movie;
import com.geoculturedemo.nickstamp.geoculturedemo.Model.Song;

import java.util.ArrayList;

/**
 * Created by nickstamp on 1/28/2016.
 */
public class Database extends SQLiteOpenHelper {

    private static Database sInstance;
    private Context context;

    public static synchronized Database getInstance(Context context) {

        // Use the application context, which will ensure that you
        // don't accidentally leak an Activity's context.
        // See this article for more information: http://bit.ly/6LRzfx
        if (sInstance == null) {
            sInstance = new Database(context.getApplicationContext());
            return sInstance;
        }
        return sInstance;
    }

    /**
     * Constructor should be private to prevent direct instantiation.
     * make call to static method "getInstance()" instead.
     */
    private Database(Context context) {
        super(context, Contract.DATABASE_NAME, null, Contract.DATABASE_VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL(Contract.SQL_CREATE_TABLE_MOVIES);
        db.execSQL(Contract.SQL_CREATE_TABLE_SONGS);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //Nothing so far
    }

    /**
     * @return a list with all the movies
     */
    public ArrayList<Movie> getListMovies() {

        Cursor cMovies = getReadableDatabase().rawQuery(
                "SELECT * FROM " + Contract.Movies.TABLE_NAME +
                        " ORDER BY " + Contract.Movies.COLUMN_TITLE, null);
        ArrayList<Movie> items = new ArrayList<>();

        for (cMovies.moveToFirst(); !cMovies.isAfterLast(); cMovies.moveToNext()) {

            items.add(new Movie(cMovies));

        }


        return items;
    }

    /**
     * @return a list with all the songs
     */
    public ArrayList<Song> getListSongs() {

        Cursor cSongs = getReadableDatabase().rawQuery(
                "SELECT * FROM " + Contract.Songs.TABLE_NAME +
                        " ORDER BY " + Contract.Songs.COLUMN_TITLE, null);
        ArrayList<Song> items = new ArrayList<>();

        for (cSongs.moveToFirst(); !cSongs.isAfterLast(); cSongs.moveToNext()) {

            items.add(new Song(cSongs));

        }


        return items;
    }

    /**
     * Insert a new movie in the database
     *
     * @param movie the movie to be inserted
     */
    public void insert(Movie movie) {
        //In order to insert a new movie , must do 2 things

        //1.Must insert the movie in the workouts table
        ContentValues contentValues = new ContentValues();
        contentValues.put(Contract.Movies.COLUMN_URL, movie.getUrl());
        contentValues.put(Contract.Movies.COLUMN_TITLE, movie.getTitle());
        contentValues.put(Contract.Movies.COLUMN_YEAR, movie.getYear());
        contentValues.put(Contract.Movies.COLUMN_GENRE, movie.getGenre());
        contentValues.put(Contract.Movies.COLUMN_RATING, movie.getRating());
        contentValues.put(Contract.Movies.COLUMN_RUNTIME, movie.getRuntime());
        contentValues.put(Contract.Movies.COLUMN_IMG_URL, movie.getImgUrl());
        contentValues.put(Contract.Movies.COLUMN_CAST, movie.getCast());
        contentValues.put(Contract.Movies.COLUMN_DIRECTOR, movie.getDirector());
        contentValues.put(Contract.Movies.COLUMN_WRITER, movie.getWriter());
        contentValues.put(Contract.Movies.COLUMN_SYNOPSIS, movie.getSynopsis());

        getWritableDatabase().insert(Contract.Movies.TABLE_NAME, "null", contentValues);

    }

    /**
     * Insert a new song in the database
     *
     * @param song the song to be inserted
     */
    public void insert(Song song) {
        //In order to insert a new song , must do 2 things

        //1.Must insert the song in the workouts table
        ContentValues contentValues = new ContentValues();
        contentValues.put(Contract.Songs.COLUMN_URL, song.getUrl());
        contentValues.put(Contract.Songs.COLUMN_TITLE, song.getTitle());
        contentValues.put(Contract.Songs.COLUMN_YEAR, song.getYear());
        contentValues.put(Contract.Songs.COLUMN_LYRICS_BY, song.getLyricsCreator());
        contentValues.put(Contract.Songs.COLUMN_MUSIC_BY, song.getMusicCreator());
        contentValues.put(Contract.Songs.COLUMN_ARTIST, song.getArtist());
        contentValues.put(Contract.Songs.COLUMN_LYRICS, song.getLyrics());
        contentValues.put(Contract.Songs.COLUMN_LINKS, song.getLinks());

        getWritableDatabase().insert(Contract.Songs.TABLE_NAME, "null", contentValues);

    }

    /**
     * Checks whether there is a local copy of the requested movie
     *
     * @param movie The movie to be checked if is saved
     * @return whether there is a local copy of the requested movie
     */
    public boolean isSaved(Movie movie) {
        //find the exercise
        Cursor c = getReadableDatabase().rawQuery(
                "SELECT * FROM " + Contract.Movies.TABLE_NAME +
                        " WHERE " + Contract.Movies.COLUMN_TITLE + "=\"" + movie.getTitle()
                        + "\" AND " + Contract.Movies.COLUMN_YEAR + "=\"" + movie.getYear() + "\"", null);

        if (c.moveToFirst()) {
            return true;
        }
        return false;
    }

    /**
     * Checks whether there is a local copy of the requested song
     *
     * @param song The song to be checked if is saved
     * @return whether there is a local copy of the requested song
     */
    public boolean isSaved(Song song) {
        //find the exercise
        Cursor c = getReadableDatabase().rawQuery(
                "SELECT * FROM " + Contract.Songs.TABLE_NAME +
                        " WHERE " + Contract.Songs.COLUMN_TITLE + "=\"" + song.getTitle()
                        + "\" AND " + Contract.Songs.COLUMN_ARTIST + "=\"" + song.getArtist()
                        + "\" AND " + Contract.Songs.COLUMN_YEAR + "=\"" + song.getYear() + "\"", null);

        if (c.moveToFirst()) {
            return true;
        }
        return false;
    }

    /**
     * Delete a movie from the local database
     *
     * @param movie the movie to be deleted
     */
    public void delete(Movie movie) {

        String selection = Contract.Movies.COLUMN_TITLE + " = ?";

        String[] selectionArgs = {movie.getTitle()};

        getWritableDatabase().delete(Contract.Movies.TABLE_NAME, selection, selectionArgs);

    }

    /**
     * Delete a song from the local database
     *
     * @param song the song to be deleted
     */
    public void delete(Song song) {

        /*String selection = Contract.Songs.COLUMN_TITLE + " = ? AND " +
                Contract.Songs.COLUMN_ARTIST + " = ? AND " +
                Contract.Songs.COLUMN_YEAR + " = ?";

        String[] selectionArgs = {song.getTitle(), song.getArtist(), song.getYear()};*/

        String selection = Contract.Songs.COLUMN_LYRICS + " = ?";

        String[] selectionArgs = {song.getLyrics()};

        getWritableDatabase().delete(Contract.Songs.TABLE_NAME, selection, selectionArgs);

    }

}