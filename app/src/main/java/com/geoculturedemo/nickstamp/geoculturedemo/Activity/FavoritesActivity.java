package com.geoculturedemo.nickstamp.geoculturedemo.Activity;

import android.annotation.TargetApi;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.transition.ChangeBounds;
import android.transition.ChangeImageTransform;
import android.transition.ChangeTransform;
import android.transition.Fade;
import android.transition.Slide;
import android.transition.Transition;
import android.transition.TransitionSet;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;

import com.geoculturedemo.nickstamp.geoculturedemo.Callback.OnFavoriteDelete;
import com.geoculturedemo.nickstamp.geoculturedemo.Callback.OnMovieClicked;
import com.geoculturedemo.nickstamp.geoculturedemo.Callback.OnSongClicked;
import com.geoculturedemo.nickstamp.geoculturedemo.Fragment.FavoriteListFragment;
import com.geoculturedemo.nickstamp.geoculturedemo.Fragment.MovieFragment;
import com.geoculturedemo.nickstamp.geoculturedemo.Fragment.SongFragment;
import com.geoculturedemo.nickstamp.geoculturedemo.Model.Movie;
import com.geoculturedemo.nickstamp.geoculturedemo.Model.Song;
import com.geoculturedemo.nickstamp.geoculturedemo.R;

public class FavoritesActivity extends AppCompatActivity implements OnMovieClicked, OnSongClicked, OnFavoriteDelete {

    private static final String TAG_FAVORITE_LIST = "favorite_list";
    private static final String TAG_MOVIE_DETAILS = "movie_details";
    private static final String TAG_SONG_DETAILS = "song_details";


    private static final int transitionDuration = 800;

    FragmentManager fragmentManager;
    FragmentTransaction fragmentTransaction;
    FavoriteListFragment favoriteListFragment;
    private Menu menu;
    private MovieFragment movieFragment;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorites);

        fragmentManager = getSupportFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();

        favoriteListFragment = FavoriteListFragment.newInstance();
        favoriteListFragment.setOnMovieClickedListener(this);
        favoriteListFragment.setOnSongClickedListener(this);

        fragmentTransaction.add(R.id.container, favoriteListFragment, TAG_FAVORITE_LIST).commit();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        this.menu = menu;
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


    @Override
    public void onMovie(Movie movie, ImageView movieImage) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            movieImage.setTransitionName(movie.getTitle());
        }

        movieFragment = MovieFragment.newInstance(movie, true);
        movieFragment.setOnFavoriteDelete(this);


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            movieFragment.setSharedElementEnterTransition(new DetailsTransition().setDuration(transitionDuration));
            movieFragment.setEnterTransition(new Fade().setDuration(transitionDuration));
            movieFragment.setExitTransition(new Fade().setDuration(transitionDuration));
            movieFragment.setSharedElementReturnTransition(new DetailsTransition().setDuration(transitionDuration));
        }
        fragmentManager
                .beginTransaction()
                .replace(R.id.container, movieFragment, TAG_MOVIE_DETAILS)
                .addToBackStack(null)
                .addSharedElement(movieImage, movie.getTitle())
                .commit();

    }

    @Override
    public void onSong(Song song) {
        SongFragment songFragment = SongFragment.newInstance(song, true);
        songFragment.setOnFavoriteDelete(this);
        fragmentManager.beginTransaction().replace(R.id.container, songFragment, TAG_SONG_DETAILS).addToBackStack(null).commit();

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        menu.clear();

    }

    @Override
    public void onDelete(Movie movie) {

        //change the animation to be
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {

            Transition slide = new Slide().setDuration(transitionDuration);

            movieFragment.setSharedElementEnterTransition(slide);
            movieFragment.setExitTransition(slide);
            movieFragment.setEnterTransition(slide);
            movieFragment.setSharedElementReturnTransition(slide);

        }
        favoriteListFragment.remove(movie);
        fragmentManager.popBackStack();
    }

    @Override
    public void onDelete(Song song) {

        fragmentManager.popBackStack();
        favoriteListFragment.remove(song);
    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    public class DetailsTransition extends TransitionSet {
        @TargetApi(Build.VERSION_CODES.LOLLIPOP)
        public DetailsTransition() {
            setOrdering(ORDERING_TOGETHER);
            addTransition(new ChangeBounds()).
                    addTransition(new ChangeTransform()).
                    addTransition(new ChangeImageTransform());
        }
    }

}
