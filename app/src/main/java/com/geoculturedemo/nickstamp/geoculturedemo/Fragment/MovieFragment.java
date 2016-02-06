package com.geoculturedemo.nickstamp.geoculturedemo.Fragment;


import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.geoculturedemo.nickstamp.geoculturedemo.Callback.OnFavoriteDelete;
import com.geoculturedemo.nickstamp.geoculturedemo.Callback.OnMovieDetailsDownloaded;
import com.geoculturedemo.nickstamp.geoculturedemo.Database.Database;
import com.geoculturedemo.nickstamp.geoculturedemo.GeoCultureApplication;
import com.geoculturedemo.nickstamp.geoculturedemo.Model.Movie;
import com.geoculturedemo.nickstamp.geoculturedemo.Parser.MovieDetailsParser;
import com.geoculturedemo.nickstamp.geoculturedemo.R;
import com.geoculturedemo.nickstamp.geoculturedemo.Utils.AnimationUtils;
import com.geoculturedemo.nickstamp.geoculturedemo.Utils.FontUtils;
import com.squareup.picasso.Picasso;

import java.io.File;

public class MovieFragment extends Fragment implements View.OnClickListener, OnMovieDetailsDownloaded {

    private static final String ARG_MOVIE = "movie";
    private static final String ARG_OFFLINE = "offline";

    private boolean isOffline;

    private TextView tvMovieTitle, tvMovieGenre, tvMovieCast, tvMovieDirector, tvMovieWriter, tvMovieRating, tvMovieRuntime, tvMovieSynopsis;
    private ImageView ivMovieImage;
    private ProgressBar pbImage;

    private Movie movie;
    private View fragmentView;
    private MovieDetailsParser movieDetailsParser;

    private FloatingActionButton fab;
    private Database database;

    private boolean isSaved;
    private OnFavoriteDelete onFavoriteDelete;

    public MovieFragment() {

    }

    public static MovieFragment newInstance(Movie movie1, boolean isOffline) {
        MovieFragment fragment = new MovieFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_MOVIE, movie1);
        args.putBoolean(ARG_OFFLINE, isOffline);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            movie = (Movie) getArguments().getSerializable(ARG_MOVIE);
            isOffline = getArguments().getBoolean(ARG_OFFLINE);
        }
        setHasOptionsMenu(isOffline);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        if (fragmentView == null) {

            fragmentView = inflater.inflate(R.layout.fragment_movie_details, container, false);

            ivMovieImage = (ImageView) fragmentView.findViewById(R.id.ivMovieImage);
            pbImage = (ProgressBar) fragmentView.findViewById(R.id.pbImage);

            tvMovieTitle = (TextView) fragmentView.findViewById(R.id.tvMovieTitle);
            tvMovieGenre = (TextView) fragmentView.findViewById(R.id.tvMovieGenre);
            tvMovieCast = (TextView) fragmentView.findViewById(R.id.tvMovieCast);
            tvMovieDirector = (TextView) fragmentView.findViewById(R.id.tvMovieDirector);
            tvMovieWriter = (TextView) fragmentView.findViewById(R.id.tvMovieWriter);
            tvMovieRating = (TextView) fragmentView.findViewById(R.id.tvMovieRating);
            tvMovieRuntime = (TextView) fragmentView.findViewById(R.id.tvMovieRuntime);
            tvMovieSynopsis = (TextView) fragmentView.findViewById(R.id.tvMovieSynopsis);

            FontUtils.setFont(getContext(), fragmentView);

            tvMovieRating.setText(movie.getRating());
            tvMovieRuntime.setText(movie.getRuntime());
            tvMovieGenre.setText(movie.getGenre());
            tvMovieDirector.setText(movie.getDirector());
            tvMovieCast.setText(movie.getCast());

            if (movie.getCast() == null || movie.getCast().trim().length() == 0) {
                movie.setCast(getString(R.string.text_not_available));
                tvMovieCast.setText(movie.getCast());
            }
            if (movie.getDirector() == null || movie.getDirector().trim().length() == 0) {
                movie.setDirector(getString(R.string.text_not_available));
                tvMovieDirector.setText(movie.getDirector());
            }

            fab = (FloatingActionButton) fragmentView.findViewById(R.id.fab);
            fab.setOnClickListener(this);

            database = ((GeoCultureApplication) getActivity().getApplication()).getDatabase();

            //if it's not offline, must download the extra details of the item
            if (!isOffline) {
                movieDetailsParser = new MovieDetailsParser(movie, this);
                movieDetailsParser.execute();
            } else {
                pbImage.setVisibility(View.INVISIBLE);
                ivMovieImage.setVisibility(View.VISIBLE);
                Picasso.with(getContext())
                        .load(new File(movie.getImgUrl()))
                        .into(ivMovieImage);

                tvMovieTitle.setText(movie.getTitle());
                tvMovieWriter.setText(movie.getWriter());
                tvMovieSynopsis.setText(movie.getSynopsis());

            }

        }

        return fragmentView;
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_favorite_fragment, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.action_delete_favorite:
                database.delete(movie);
                onFavoriteDelete.onDelete(movie);
                break;
        }
        return true;
    }

    public void setOnFavoriteDelete(OnFavoriteDelete onFavoriteDelete) {
        this.onFavoriteDelete = onFavoriteDelete;
    }

    @Override
    public void onClick(View v) {

        YoYo.with(Techniques.Pulse)
                .duration(500)
                .playOn(fab);
        if (isSaved) {
            database.delete(movie);
            fab.setImageResource(R.drawable.ic_star_outline);
            Snackbar.make(fragmentView, "Removed from favorites", Snackbar.LENGTH_SHORT).show();
        } else {
            database.insert(movie);
            fab.setImageResource(R.drawable.ic_star);
            Snackbar.make(fragmentView, "Saved", Snackbar.LENGTH_SHORT).show();
        }
        isSaved = !isSaved;
    }

    public void shutDownAsyncTask() {
        movieDetailsParser.cancel(true);
    }

    @Override
    public void onDownload(Movie movie) {
        this.movie.setTitle(movie.getTitle());
        this.movie.setImgUrl(movie.getImgUrl());
        this.movie.setSynopsis(movie.getSynopsis());
        this.movie.setWriter(movie.getWriter());

        Picasso.with(getContext())
                .load(this.movie.getImgUrl())
                .into(ivMovieImage);

        AnimationUtils.crossfade(ivMovieImage, pbImage);

        tvMovieTitle.setText(this.movie.getTitle());
        tvMovieWriter.setText(this.movie.getWriter());
        tvMovieSynopsis.setText(this.movie.getSynopsis());

        if (this.movie.getWriter() == null || this.movie.getWriter().trim().length() == 0) {
            this.movie.setWriter(getString(R.string.text_not_available));
            tvMovieWriter.setText(this.movie.getWriter());
        }
        if (this.movie.getSynopsis() == null || this.movie.getSynopsis().trim().length() == 0) {
            this.movie.setSynopsis(getString(R.string.text_not_available));
            tvMovieSynopsis.setText(this.movie.getSynopsis());
        }

        isSaved = database.isSaved(this.movie);
        if (isSaved)
            fab.setImageResource(R.drawable.ic_star);
        else
            fab.setImageResource(R.drawable.ic_star_outline);
        fab.show();
    }


}
