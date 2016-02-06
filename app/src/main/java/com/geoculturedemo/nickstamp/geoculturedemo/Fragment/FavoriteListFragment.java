package com.geoculturedemo.nickstamp.geoculturedemo.Fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.geoculturedemo.nickstamp.geoculturedemo.Adapter.FavoritesAdapter;
import com.geoculturedemo.nickstamp.geoculturedemo.Callback.OnMovieClicked;
import com.geoculturedemo.nickstamp.geoculturedemo.Callback.OnSongClicked;
import com.geoculturedemo.nickstamp.geoculturedemo.Database.Database;
import com.geoculturedemo.nickstamp.geoculturedemo.GeoCultureApplication;
import com.geoculturedemo.nickstamp.geoculturedemo.Model.Movie;
import com.geoculturedemo.nickstamp.geoculturedemo.Model.Song;
import com.geoculturedemo.nickstamp.geoculturedemo.R;

import java.util.ArrayList;

/**
 * Created by nickstamp on 1/31/2016.
 */
public class FavoriteListFragment extends Fragment implements OnSongClicked, OnMovieClicked {

    private View fragmentView;
    private RecyclerView recyclerView;
    private Database database;
    private ArrayList<Object> objects;
    private FavoritesAdapter favoritesAdapter;
    private GridLayoutManager gridLayoutManager;
    private Context context;
    private OnMovieClicked onMovieClicked;
    private OnSongClicked onSongClicked;

    public FavoriteListFragment() {

    }

    public static FavoriteListFragment newInstance() {
        FavoriteListFragment fragment = new FavoriteListFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        if (fragmentView == null) {

            fragmentView = inflater.inflate(R.layout.fragment_favorites, container, false);

            context = getContext();

            recyclerView = (RecyclerView) fragmentView.findViewById(R.id.recyclerList);
            database = ((GeoCultureApplication) getActivity().getApplication()).getDatabase();
            objects = database.getFavorites();
            favoritesAdapter = new FavoritesAdapter(context, objects, this, this);

            //get the width of the current screen in dp
            DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
            float dpWidth = displayMetrics.widthPixels / displayMetrics.density;
            dpWidth -= 50;

            //and calculate how many columns can fit according to the 120dp image size
            final int columns = (int) (dpWidth / 120);

            gridLayoutManager = new GridLayoutManager(context, columns, GridLayoutManager.VERTICAL, false);
            gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
                @Override
                public int getSpanSize(int position) {
                    switch (favoritesAdapter.getItemViewType(position)) {
                        case FavoritesAdapter.VIEW_TYPE_HEADER:
                            return columns;
                        case FavoritesAdapter.VIEW_TYPE_SONG:
                            if (columns > 2)
                                return columns / 2;
                            else if (columns == 2)
                                return 2;
                            return 1;
                        default:
                            return 1;
                    }
                }
            });
            recyclerView.setLayoutManager(gridLayoutManager);

            recyclerView.setAdapter(favoritesAdapter);

        }

        return fragmentView;
    }


    public void setOnMovieClickedListener(OnMovieClicked listener) {
        onMovieClicked = listener;
    }

    public void setOnSongClickedListener(OnSongClicked listener) {
        onSongClicked = listener;
    }


    @Override
    public void onMovie(Movie movie) {
//        Snackbar.make(recyclerView, movie.getTitle(), Snackbar.LENGTH_SHORT).show();

        onMovieClicked.onMovie(movie);
    }

    @Override
    public void onSong(Song song) {
//        Snackbar.make(recyclerView, song.getTitle(), Snackbar.LENGTH_SHORT).show();
        onSongClicked.onSong(song);
    }

    public void remove(Movie movie) {
        int pos = -1;
        for (int i = 0; i < objects.size(); i++) {
            Object o = objects.get(i);
            if (o instanceof Movie)
                if (((Movie) o).equals(movie)) {
                    pos = i;
                }
        }
        if (pos != -1) {
            objects.remove(pos);
        }
        int count = 0, headerPos = 1;
        for (int i = 0; i < objects.size(); i++) {
            Object o = objects.get(i);
            if (o instanceof Movie)
                count++;
            if (o instanceof String && i != 0)
                headerPos = i;
        }

        objects.set(headerPos, context.getString(R.string.text_songs) + " (" + count + ")");
        favoritesAdapter.notifyDataSetChanged();
    }

    public void remove(Song song) {
        int pos = -1;
        for (int i = 0; i < objects.size(); i++) {
            Object o = objects.get(i);
            if (o instanceof Song)
                if (((Song) o).equals(song)) {
                    pos = i;
                }
        }
        if (pos != -1) {
            objects.remove(pos);
        }
        int count = 0;
        for (Object o : objects) {
            if (o instanceof Song)
                count++;
        }

        objects.set(0, context.getString(R.string.text_songs) + " (" + count + ")");
        favoritesAdapter.notifyDataSetChanged();
    }
}
