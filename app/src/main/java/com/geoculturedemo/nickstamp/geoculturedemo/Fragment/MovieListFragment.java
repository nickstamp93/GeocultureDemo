package com.geoculturedemo.nickstamp.geoculturedemo.Fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.geoculturedemo.nickstamp.geoculturedemo.Model.Location;
import com.geoculturedemo.nickstamp.geoculturedemo.Model.Movie;
import com.geoculturedemo.nickstamp.geoculturedemo.R;

import java.util.ArrayList;

public class MovieListFragment extends Fragment {

    private static final String ARG_LOCATION = "ARG_LOCATION";
    private final ArrayList<Movie> movies;

    private Location location;

    private RecyclerView recyclerView;

    public MovieListFragment() {
        movies = new ArrayList<>();
    }

    public static MovieListFragment newInstance(Location location) {
        MovieListFragment fragment = new MovieListFragment();
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
            Toast.makeText(getContext(), "Location:" + location.getFullName(), Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View fragmentView = inflater.inflate(R.layout.fragment_list, container, false);

        recyclerView = (RecyclerView) fragmentView.findViewById(R.id.recyclerList);

        return fragmentView;
    }

}
