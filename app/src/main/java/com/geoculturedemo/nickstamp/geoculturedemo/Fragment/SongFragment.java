package com.geoculturedemo.nickstamp.geoculturedemo.Fragment;


import android.content.Intent;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.geoculturedemo.nickstamp.geoculturedemo.Callback.OnSongDetailsDownloaded;
import com.geoculturedemo.nickstamp.geoculturedemo.Database.Database;
import com.geoculturedemo.nickstamp.geoculturedemo.GeoCultureApp;
import com.geoculturedemo.nickstamp.geoculturedemo.Model.Song;
import com.geoculturedemo.nickstamp.geoculturedemo.Parser.SongDetailsParser;
import com.geoculturedemo.nickstamp.geoculturedemo.R;
import com.geoculturedemo.nickstamp.geoculturedemo.Utils.AnimationUtils;
import com.geoculturedemo.nickstamp.geoculturedemo.Utils.FontUtils;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class SongFragment extends Fragment implements View.OnClickListener, OnSongDetailsDownloaded {

    private static final String ARG_SONG = "song";
    private static final String ARG_OFFLINE = "offline";

    private boolean isOffline;

    private Song song;

    private TextView tvLyricsBy, tvTitle, tvMusicBy, tvLyrics;
    private LinearLayout llArtists;
    private ProgressBar pbArtists, pbLyrics;

    private Typeface typeface;
    private View fragmentView;

    private FloatingActionButton fab;
    private boolean isSaved;
    private Database database;
    private SongDetailsParser songDetailsParser;

    public SongFragment() {

    }

    public static SongFragment newInstance(Song song1, boolean isOffline) {

        SongFragment fragment = new SongFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_SONG, song1);
        args.putBoolean(ARG_OFFLINE, isOffline);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            song = (Song) getArguments().getSerializable(ARG_SONG);
            isOffline = getArguments().getBoolean(ARG_OFFLINE);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        if (fragmentView == null) {
            fragmentView = inflater.inflate(R.layout.fragment_song_details, container, false);

            tvTitle = (TextView) fragmentView.findViewById(R.id.tvSongTitle);
            tvMusicBy = (TextView) fragmentView.findViewById(R.id.tvSongMusicBy);
            tvLyricsBy = (TextView) fragmentView.findViewById(R.id.tvSongLyricsBy);
            tvLyrics = (TextView) fragmentView.findViewById(R.id.tvSongLyrics);

            llArtists = (LinearLayout) fragmentView.findViewById(R.id.llArtists);

            pbArtists = (ProgressBar) fragmentView.findViewById(R.id.pbArtists);
            pbLyrics = (ProgressBar) fragmentView.findViewById(R.id.pbLyrics);

            FontUtils.setRobotoFont(getContext(), fragmentView);

            tvTitle.setText(song.getTitle());
            tvMusicBy.setText(song.getMusicCreator());
            tvLyricsBy.setText(song.getLyricsCreator());

            fab = (FloatingActionButton) fragmentView.findViewById(R.id.fab);
            fab.setOnClickListener(this);

            database = ((GeoCultureApp) getActivity().getApplication()).getDatabase();

            if (!isOffline) {
                songDetailsParser = new SongDetailsParser(song, this);
                songDetailsParser.execute();
            }
        }


        return fragmentView;

    }

    @Override
    public void onClick(View v) {

        YoYo.with(Techniques.Pulse)
                .duration(500)
                .playOn(fab);
        if (isSaved) {
            database.delete(song);
            fab.setImageResource(R.drawable.ic_star_outline);
            Snackbar.make(fragmentView, "Removed from favorites", Snackbar.LENGTH_SHORT).show();
        } else {
            database.insert(song);
            fab.setImageResource(R.drawable.ic_star);
            Snackbar.make(fragmentView, "Saved", Snackbar.LENGTH_SHORT).show();
        }

        isSaved = !isSaved;

    }

    public void shutDownAsyncTask() {
        songDetailsParser.cancel(true);
    }


    @Override
    public void onDownload(final Song song) {

        this.song.setArtist(song.getArtist());
        this.song.setLinks(song.getLinks());
        this.song.setLyrics(song.getLyrics());

        AnimationUtils.crossfade(llArtists, pbArtists);
        AnimationUtils.crossfade(tvLyrics, pbLyrics);

        tvLyrics.setText(this.song.getLyrics());

        String[] artists = this.song.getArtist().split("\\|");
        String[] links = this.song.getLinks().split("\\|");
        for (int i = 0; i < artists.length; i++) {
            final String currentArtist = artists[i];

            TextView tv = new TextView(getContext());
            tv.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
            tv.setText(currentArtist);
            typeface = Typeface.createFromAsset(getContext().getAssets(), "fonts/Roboto-Regular.ttf");
            tv.setTypeface(typeface);

                /*int padding_in_dp = 8;  // 12 dps
                final float scale = getResources().getDisplayMetrics().density;
                int padding_in_px = (int) (padding_in_dp * scale + 0.5f);
                tv.setPadding(0, padding_in_px, 0, padding_in_px);*/

            int[] attrs = new int[]{R.attr.selectableItemBackground};
            TypedArray typedArray = getActivity().obtainStyledAttributes(attrs);
            int backgroundResource = typedArray.getResourceId(0, 0);
            tv.setClickable(true);
            tv.setBackgroundResource(backgroundResource);
            typedArray.recycle();

            tv.setGravity(Gravity.CENTER_VERTICAL);
            if (links[i].trim().length() > 0
                    && !links[i].equals("-")) {
                final String link = links[i];

                Log.i("nikos", "link:" + link);

                tv.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(link)));
                    }
                });
                tv.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.mipmap.ic_tube, 0);
                tv.setCompoundDrawablePadding(4);
            } else {


                tv.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        Intent intent = new Intent(Intent.ACTION_SEARCH);
                        intent.setPackage("com.google.android.youtube");

                        String s = currentArtist.substring(2, currentArtist.length());

                        intent.putExtra("query", song.getTitle() + " " + s);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);

                    }
                });
                tv.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.mipmap.ic_search, 0);
                tv.setCompoundDrawablePadding(4);
            }

            tv.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
            llArtists.addView(tv);
        }

        isSaved = database.isSaved(song);
        if (isSaved)
            fab.setImageResource(R.drawable.ic_star);
        else
            fab.setImageResource(R.drawable.ic_star_outline);
        fab.show();
    }
}


