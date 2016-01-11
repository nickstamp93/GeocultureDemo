package com.geoculturedemo.nickstamp.geoculturedemo.Fragment;


import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.geoculturedemo.nickstamp.geoculturedemo.Adapter.MoviesAdapter;
import com.geoculturedemo.nickstamp.geoculturedemo.Adapter.SongsAdapter;
import com.geoculturedemo.nickstamp.geoculturedemo.Callback.OnMovieClicked;
import com.geoculturedemo.nickstamp.geoculturedemo.Callback.OnSongClicked;
import com.geoculturedemo.nickstamp.geoculturedemo.Model.Location;
import com.geoculturedemo.nickstamp.geoculturedemo.Model.Song;
import com.geoculturedemo.nickstamp.geoculturedemo.Parser.SongParser;
import com.geoculturedemo.nickstamp.geoculturedemo.R;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class SongListFragment extends Fragment {

    private static final String ARG_LOCATION = "ARG_LOCATION";

    private ArrayList<Song> songs;

    private Location location;

    private ProgressBar pbList;

    private RecyclerView recyclerView;
    private View fragmentView;
    private Context context;
    private SongParser songParser;
    private SongsAdapter songsAdapter;
    private OnSongClicked onSongClicked;
    private LinearLayoutManager linearLayoutManager;
    private String urlCity;


    public SongListFragment() {
        songs = new ArrayList<>();
    }

    public static SongListFragment newInstance(Location location) {
        SongListFragment fragment = new SongListFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_LOCATION, location);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            location = (Location) getArguments().getSerializable(ARG_LOCATION);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        //TODO somehow give the user the option to load more results(distinct for area-city???)


        if (fragmentView == null) {

            context = getContext();

            fragmentView = inflater.inflate(R.layout.fragment_list, container, false);

            pbList = (ProgressBar) fragmentView.findViewById(R.id.pbList);

            recyclerView = (RecyclerView) fragmentView.findViewById(R.id.recyclerList);
            linearLayoutManager = new LinearLayoutManager(context);
            recyclerView.setLayoutManager(linearLayoutManager);

            parseSongs();


        }


        return fragmentView;
    }

    private void parseSongs() {

        songParser = new SongParser();
        songParser.execute();
    }

    public void setOnSongClicked(OnSongClicked listener) {
        onSongClicked = listener;
    }

    public class SongParser extends AsyncTask<Void, Void, Void> {

        public SongParser() {

        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected Void doInBackground(Void... params) {

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

        }
    }

}
