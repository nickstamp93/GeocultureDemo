package com.geoculturedemo.nickstamp.geoculturedemo.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.geoculturedemo.nickstamp.geoculturedemo.Model.Movie;

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
     * @return a list with all the workouts
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


}