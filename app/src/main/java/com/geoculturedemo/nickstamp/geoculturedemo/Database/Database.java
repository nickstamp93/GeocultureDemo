package com.geoculturedemo.nickstamp.geoculturedemo.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Environment;
import android.util.Log;

import com.geoculturedemo.nickstamp.geoculturedemo.Model.Movie;
import com.geoculturedemo.nickstamp.geoculturedemo.Model.Song;
import com.geoculturedemo.nickstamp.geoculturedemo.R;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.io.File;
import java.io.FileOutputStream;
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
    public ArrayList<Object> getListMovies() {

        Cursor cMovies = getReadableDatabase().rawQuery(
                "SELECT * FROM " + Contract.Movies.TABLE_NAME +
                        " ORDER BY " + Contract.Movies.COLUMN_TITLE, null);
        ArrayList<Object> items = new ArrayList<>();

        for (cMovies.moveToFirst(); !cMovies.isAfterLast(); cMovies.moveToNext()) {

            items.add(new Movie(cMovies));

        }


        return items;
    }

    /**
     * @return a list with all the songs
     */
    public ArrayList<Object> getListSongs() {

        Cursor cSongs = getReadableDatabase().rawQuery(
                "SELECT * FROM " + Contract.Songs.TABLE_NAME +
                        " ORDER BY " + Contract.Songs.COLUMN_TITLE, null);
        ArrayList<Object> items = new ArrayList<>();

        for (cSongs.moveToFirst(); !cSongs.isAfterLast(); cSongs.moveToNext()) {

            items.add(new Song(cSongs));

        }

        return items;
    }

    /**
     * Get all items saved to the favorites
     *
     * @return the list with the saved items
     */
    public ArrayList<Object> getFavorites() {
        ArrayList<Object> objects = new ArrayList<>();
        ArrayList songs = getListSongs();
        objects.addAll(songs);
        objects.add(0, context.getString(R.string.text_songs) + " (" + songs.size() + ")");
        int movieHeaderPos = songs.size() + 1;
        ArrayList movies = getListMovies();
        objects.addAll(movies);
        objects.add(movieHeaderPos, context.getString(R.string.text_movies) + " (" + movies.size() + ")");
        return objects;
    }

    /**
     * Insert a new movie in the database
     *
     * @param movie the movie to be inserted
     */
    public void insert(final Movie movie) {
        //In order to insert a new movie , must do 2 things

        //1.Must insert the movie in the workouts table
        ContentValues contentValues = new ContentValues();
        contentValues.put(Contract.Movies.COLUMN_URL, movie.getUrl());
        contentValues.put(Contract.Movies.COLUMN_TITLE, movie.getTitle());
        contentValues.put(Contract.Movies.COLUMN_YEAR, movie.getYear());
        contentValues.put(Contract.Movies.COLUMN_GENRE, movie.getGenre());
        contentValues.put(Contract.Movies.COLUMN_RATING, movie.getRating());
        contentValues.put(Contract.Movies.COLUMN_RUNTIME, movie.getRuntime());
        File folder = new File(Environment.getExternalStorageDirectory() +
                File.separator + "GeoCulture");
        if (!folder.exists()) {
            Log.i("nikos","not exists");
            folder.mkdir();
        }
        if(Environment.isExternalStorageEmulated())
            Log.i("nikos","is emulated");
        final String path = Environment.getExternalStorageDirectory().getPath()
                + File.separator + "GeoCulture" + File.separator +
                movie.getTitle() + ".jpg";
        contentValues.put(Contract.Movies.COLUMN_IMG_URL, path);
        contentValues.put(Contract.Movies.COLUMN_CAST, movie.getCast());
        contentValues.put(Contract.Movies.COLUMN_DIRECTOR, movie.getDirector());
        contentValues.put(Contract.Movies.COLUMN_WRITER, movie.getWriter());
        contentValues.put(Contract.Movies.COLUMN_SYNOPSIS, movie.getSynopsis());


        Target target = new Target() {

            @Override
            public void onBitmapLoaded(final Bitmap bitmap, Picasso.LoadedFrom from) {
                new Thread(new Runnable() {

                    @Override
                    public void run() {

                        File file = new File(path);
                        try {
                            file.createNewFile();
                            FileOutputStream ostream = new FileOutputStream(file);
                            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, ostream);
                            ostream.close();
                        } catch (Exception e) {
                            Log.i("nikos","exception: " + path);
                            e.printStackTrace();
                        }

                    }
                }).start();
            }

            @Override
            public void onBitmapFailed(Drawable errorDrawable) {
            }

            @Override
            public void onPrepareLoad(Drawable placeHolderDrawable) {
                if (placeHolderDrawable != null) {
                }
            }
        };

        Picasso.with(context)
                .load(movie.getImgUrl())
                .into(target);

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
        //find the movie
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
        //find the song
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
     * @return true if the movie image was deleted successfully
     */
    public boolean delete(Movie movie) {

        String selection = Contract.Movies.COLUMN_TITLE + " = ?";

        String[] selectionArgs = {movie.getTitle()};

        getWritableDatabase().delete(Contract.Movies.TABLE_NAME, selection, selectionArgs);

        File file = new File(movie.getImgUrl());
        return file.delete();


    }

    /**
     * Delete a song from the local database
     *
     * @param song the song to be deleted
     */
    public void delete(Song song) {

        String selection = Contract.Songs.COLUMN_LYRICS + " = ?";

        String[] selectionArgs = {song.getLyrics()};

        getWritableDatabase().delete(Contract.Songs.TABLE_NAME, selection, selectionArgs);

    }

    public Movie get(Movie movie) {
        //find the movie
        Cursor c = getReadableDatabase().rawQuery(
                "SELECT * FROM " + Contract.Movies.TABLE_NAME +
                        " WHERE " + Contract.Movies.COLUMN_TITLE + "=\"" + movie.getTitle()
                        + "\" AND " + Contract.Movies.COLUMN_YEAR + "=\"" + movie.getYear() + "\"", null);

        if (c.moveToFirst()) {
            return new Movie(c);
        } else return null;
    }

}