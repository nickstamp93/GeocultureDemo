package com.geoculturedemo.nickstamp.geoculturedemo.Fragment;


import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
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
import android.widget.Toast;

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
import com.geoculturedemo.nickstamp.geoculturedemo.Utils.Constants;
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
    private View fragmentView, bFullInfo;
    private MovieDetailsParser movieDetailsParser = null;

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
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                ivMovieImage.setTransitionName(movie.getTitle());
            }
            pbImage = (ProgressBar) fragmentView.findViewById(R.id.pbImage);

            tvMovieTitle = (TextView) fragmentView.findViewById(R.id.tvMovieTitle);
            tvMovieGenre = (TextView) fragmentView.findViewById(R.id.tvMovieGenre);
            tvMovieCast = (TextView) fragmentView.findViewById(R.id.tvMovieCast);
            tvMovieDirector = (TextView) fragmentView.findViewById(R.id.tvMovieDirector);
            tvMovieWriter = (TextView) fragmentView.findViewById(R.id.tvMovieWriter);
            tvMovieRating = (TextView) fragmentView.findViewById(R.id.tvMovieRating);
            tvMovieRuntime = (TextView) fragmentView.findViewById(R.id.tvMovieRuntime);
            tvMovieSynopsis = (TextView) fragmentView.findViewById(R.id.tvMovieSynopsis);

            bFullInfo = fragmentView.findViewById(R.id.bFullInfo);
            bFullInfo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String url = movie.getUrl();
                    Intent i = new Intent(Intent.ACTION_VIEW);
                    i.setData(Uri.parse(url));
                    startActivity(i);
                }
            });

            FontUtils.setFont(getContext(), fragmentView);

            tvMovieTitle.setText(movie.getTitle() + " " + movie.getYear());
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
            isSaved = database.isSaved(this.movie);

            //if it's not offline, must download the extra details of the item
            if (!isOffline && !isSaved) {
                movieDetailsParser = new MovieDetailsParser(movie, this);
                movieDetailsParser.execute();
            } else {
                if (!isOffline) {
                    movie = database.get(movie);
                    fab.setImageResource(R.drawable.ic_star);
                    fab.show();
                }

                pbImage.setVisibility(View.INVISIBLE);
                ivMovieImage.setVisibility(View.VISIBLE);
                Picasso.with(getContext())
                        .load(new File(movie.getImgUrl()))
                        .into(ivMovieImage);

                tvMovieTitle.setText(movie.getTitle() + " " + movie.getYear());
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
                if (database.delete(movie)) {
                    Toast.makeText(getContext(), "\"" + movie.getTitle() + "\" " + getString(R.string.text_deleted), Toast.LENGTH_LONG).show();
                }
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
        //if the location permission is not granted, ask the user
        if (ActivityCompat.checkSelfPermission(
                getActivity(),
                Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {

            //request permission to use location
            ActivityCompat.requestPermissions(getActivity(),
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    Constants.REQUEST_CODE_PERMISSION_STORAGE
            );

            //else request location update
        } else {
            YoYo.with(Techniques.Pulse)
                    .duration(500)
                    .playOn(fab);
            if (isSaved) {
                database.delete(movie);
                fab.setImageResource(R.drawable.ic_star_outline);
                Snackbar.make(fragmentView, getString(R.string.snackbar_deleted_saved), Snackbar.LENGTH_SHORT).show();
            } else {
                database.insert(movie);
                fab.setImageResource(R.drawable.ic_star);
                Snackbar.make(fragmentView, getString(R.string.snackbar_saved), Snackbar.LENGTH_SHORT).show();
            }
            isSaved = !isSaved;
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case Constants.REQUEST_CODE_PERMISSION_STORAGE: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    if (isSaved) {
                        database.delete(movie);
                        fab.setImageResource(R.drawable.ic_star_outline);
                        Snackbar.make(fragmentView, getString(R.string.snackbar_deleted_saved), Snackbar.LENGTH_SHORT).show();
                    } else {
                        database.insert(movie);
                        fab.setImageResource(R.drawable.ic_star);
                        Snackbar.make(fragmentView, getString(R.string.snackbar_saved), Snackbar.LENGTH_SHORT).show();
                    }
                    isSaved = !isSaved;

                } else {
                    Toast.makeText(getContext(), "You can't save movies until you grant the storage permission", Toast.LENGTH_LONG).show();
                }
                return;
            }

        }
    }

    public void shutDownAsyncTask() {
        if (movieDetailsParser != null)
            movieDetailsParser.cancel(true);
    }

    @Override
    public void onDownload(Movie movie) {
//        this.movie.setTitle(movie.getTitle());
        this.movie.setImgUrl(movie.getImgUrl());
        this.movie.setSynopsis(movie.getSynopsis());
        this.movie.setWriter(movie.getWriter());

        Picasso.with(getContext())
                .load(this.movie.getImgUrl())
                .into(ivMovieImage);

        AnimationUtils.crossfade(ivMovieImage, pbImage);

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

        if (isSaved)
            fab.setImageResource(R.drawable.ic_star);
        else
            fab.setImageResource(R.drawable.ic_star_outline);
        fab.show();
    }


}
