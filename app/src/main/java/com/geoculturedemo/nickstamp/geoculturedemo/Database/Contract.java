package com.geoculturedemo.nickstamp.geoculturedemo.Database;

import android.provider.BaseColumns;

/**
 * Created by nickstamp on 1/28/2016.
 */
public class Contract {

    public static final String DATABASE_NAME = "GEOCULTURE.db";
    public static final int DATABASE_VERSION = 1;

    public static final String SQL_CREATE_TABLE_MOVIES =
            "CREATE TABLE IF NOT EXISTS " + Movies.TABLE_NAME + " (" +
                    Movies._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                    Movies.COLUMN_URL + " TEXT NOT NULL," +
                    Movies.COLUMN_TITLE + " TEXT NOT NULL," +
                    Movies.COLUMN_YEAR + " TEXT NOT NULL," +
                    Movies.COLUMN_GENRE + " TEXT NOT NULL," +
                    Movies.COLUMN_RATING + " TEXT NOT NULL," +
                    Movies.COLUMN_RUNTIME + " TEXT NOT NULL," +
                    Movies.COLUMN_IMG_URL + " TEXT NOT NULL," +
                    Movies.COLUMN_CAST + " TEXT NOT NULL," +
                    Movies.COLUMN_DIRECTOR + " TEXT NOT NULL," +
                    Movies.COLUMN_WRITER + " TEXT NOT NULL," +
                    Movies.COLUMN_SYNOPSIS + " TEXT NOT NULL," +
                    ")";

    public static final String SQL_CREATE_TABLE_SONGS =
            "CREATE TABLE IF NOT EXISTS " + Songs.TABLE_NAME + " (" +
                    Songs._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                    Songs.COLUMN_URL + " TEXT NOT NULL," +
                    Songs.COLUMN_TITLE + " TEXT NOT NULL," +
                    Songs.COLUMN_YEAR + " TEXT NOT NULL," +
                    Songs.COLUMN_LYRICS_BY + " TEXT NOT NULL," +
                    Songs.COLUMN_MUSIC_BY + " TEXT NOT NULL," +
                    Songs.COLUMN_ARTIST + " TEXT NOT NULL," +
                    Songs.COLUMN_LYRICS + " TEXT NOT NULL," +
                    ")";

    public abstract class Movies implements BaseColumns {
        public static final String TABLE_NAME = "Movies";
        public static final String COLUMN_URL = "url";
        public static final String COLUMN_TITLE = "title";
        public static final String COLUMN_YEAR = "year";
        public static final String COLUMN_GENRE = "genre";
        public static final String COLUMN_RATING = "rating";
        public static final String COLUMN_RUNTIME = "runtime";
        public static final String COLUMN_IMG_URL = "img_url";
        public static final String COLUMN_CAST = "cast";
        public static final String COLUMN_DIRECTOR = "director";
        public static final String COLUMN_WRITER = "writer";
        public static final String COLUMN_SYNOPSIS = "synopsis";
    }

    public abstract class Songs implements BaseColumns {
        public static final String TABLE_NAME = "Songs";
        public static final String COLUMN_URL = "url";
        public static final String COLUMN_TITLE = "title";
        public static final String COLUMN_YEAR = "year";
        public static final String COLUMN_LYRICS_BY = "lyrics_by";
        public static final String COLUMN_MUSIC_BY = "music_by";
        public static final String COLUMN_ARTIST = "artist";
        public static final String COLUMN_LYRICS = "lyrics";
    }

}
