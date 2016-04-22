package com.geoculturedemo.nickstamp.geoculturedemo;

import android.app.Application;
import android.os.Environment;
import android.util.Log;

import com.geoculturedemo.nickstamp.geoculturedemo.Database.Database;

import java.io.File;

/**
 * Created by nickstamp on 1/28/2016.
 */
public class GeoCultureApplication extends Application {

    private Database database;

    @Override
    public void onCreate() {
        super.onCreate();

        database = Database.getInstance(getApplicationContext());

    }

    public Database getDatabase() {
        return database;
    }
}