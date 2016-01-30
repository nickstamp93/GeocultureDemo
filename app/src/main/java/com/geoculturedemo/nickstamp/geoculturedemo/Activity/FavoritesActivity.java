package com.geoculturedemo.nickstamp.geoculturedemo.Activity;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.view.MenuItem;
import android.view.View;

import com.geoculturedemo.nickstamp.geoculturedemo.Adapter.FavoritesAdapter;
import com.geoculturedemo.nickstamp.geoculturedemo.Callback.OnMovieClicked;
import com.geoculturedemo.nickstamp.geoculturedemo.Callback.OnSongClicked;
import com.geoculturedemo.nickstamp.geoculturedemo.Database.Database;
import com.geoculturedemo.nickstamp.geoculturedemo.GeoCultureApp;
import com.geoculturedemo.nickstamp.geoculturedemo.Model.Movie;
import com.geoculturedemo.nickstamp.geoculturedemo.Model.Song;
import com.geoculturedemo.nickstamp.geoculturedemo.R;

import java.util.ArrayList;

public class FavoritesActivity extends AppCompatActivity implements OnMovieClicked, OnSongClicked {

    private RecyclerView recyclerView;
    private StaggeredGridLayoutManager staggeredGridLayoutManager;
    private Database database;
    private FavoritesAdapter favoritesAdapter;
    private ArrayList<Object> objects;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorites);

        recyclerView = (RecyclerView) findViewById(R.id.recyclerList);
        database = ((GeoCultureApp) getApplication()).getDatabase();
        objects = database.getAllFavorites();
        favoritesAdapter = new FavoritesAdapter(this, objects, this, this);

//        recyclerView.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false));
//        recyclerView.setLayoutManager(staggeredGridLayoutManager);
        DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
        float dpHeight = displayMetrics.heightPixels / displayMetrics.density;
        float dpWidth = displayMetrics.widthPixels / displayMetrics.density;
        dpWidth -= 50;

        final int columns = (int) (dpWidth / 120);

        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, columns, GridLayoutManager.VERTICAL, false);
        gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                switch (favoritesAdapter.getItemViewType(position)) {
                    case FavoritesAdapter.VIEW_TYPE_HEADER:
                        return columns;
                    case FavoritesAdapter.VIEW_TYPE_SONG:
                        if(columns > 2)
                            return columns/2;
                        else if(columns == 2)
                            return 2;
                        return 1;
                    default:
                        return 1;
                }
            }
        });
        recyclerView.setLayoutManager(gridLayoutManager);

//        staggeredGridLayoutManager = new StaggeredGridLayoutManager(columns, StaggeredGridLayoutManager.VERTICAL);
//        recyclerView.setLayoutManager(staggeredGridLayoutManager);

        recyclerView.setAdapter(favoritesAdapter);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
        }
        return true;
    }

    @Override
    public void onMovie(Movie movie) {
        Snackbar.make(recyclerView, movie.getTitle(), Snackbar.LENGTH_SHORT).show();
    }

    @Override
    public void onSong(Song song) {
        Snackbar.make(recyclerView, song.getTitle(), Snackbar.LENGTH_SHORT).show();
    }
}
