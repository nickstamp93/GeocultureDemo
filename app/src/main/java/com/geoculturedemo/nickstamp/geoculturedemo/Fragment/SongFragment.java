package com.geoculturedemo.nickstamp.geoculturedemo.Fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.geoculturedemo.nickstamp.geoculturedemo.Model.Song;
import com.geoculturedemo.nickstamp.geoculturedemo.R;

public class SongFragment extends Fragment {

    private static final String ARG_SONG = "song";
    private Song song;

    public SongFragment() {
        // Required empty public constructor
    }

    public static SongFragment newInstance(Song song1) {

        SongFragment fragment = new SongFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_SONG, song1);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            song = (Song) getArguments().getSerializable(ARG_SONG);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Toast.makeText(getContext(), "Song:" + song.getTitle(), Toast.LENGTH_SHORT).show();
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_song_details, container, false);
    }

}
