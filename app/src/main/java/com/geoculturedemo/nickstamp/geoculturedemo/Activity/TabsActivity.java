package com.geoculturedemo.nickstamp.geoculturedemo.Activity;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.MenuItem;

import com.geoculturedemo.nickstamp.geoculturedemo.Callback.OnMovieClicked;
import com.geoculturedemo.nickstamp.geoculturedemo.Callback.OnSongClicked;
import com.geoculturedemo.nickstamp.geoculturedemo.Fragment.MovieFragment;
import com.geoculturedemo.nickstamp.geoculturedemo.Fragment.MovieListFragment;
import com.geoculturedemo.nickstamp.geoculturedemo.Fragment.SongFragment;
import com.geoculturedemo.nickstamp.geoculturedemo.Fragment.SongListFragment;
import com.geoculturedemo.nickstamp.geoculturedemo.Model.Location;
import com.geoculturedemo.nickstamp.geoculturedemo.Model.Movie;
import com.geoculturedemo.nickstamp.geoculturedemo.Model.Song;
import com.geoculturedemo.nickstamp.geoculturedemo.R;

import java.util.HashMap;

public class TabsActivity extends AppCompatActivity implements OnMovieClicked, OnSongClicked {


    private static final String TAG_MOVIE_LIST = "movielist";
    private static final String TAG_MOVIE_DETAILS = "moviedetails";
    private static final String TAG_SONG_LIST = "songlist";
    private static final String TAG_SONG_DETAILS = "songdetails";

    private SectionsPagerAdapter mSectionsPagerAdapter;

    private ViewPager mViewPager;
    private TabLayout tabLayout;

    private Fragment movieTab, songTab;
    private HashMap<String, Fragment> components;
    private FragmentManager manager;

    private String currentMovieTag, currentSongTag;
    private Location location;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tabs);

        init();

        setUpToolbar();

        setUpTabs();


    }

    private void init() {
        components = new HashMap<>();

        location = (Location) getIntent().getSerializableExtra("location");

        MovieListFragment movieListFragment = new MovieListFragment().newInstance(location);

        movieListFragment.setOnMovieClickedListener(this);

        components.put(TAG_MOVIE_LIST, movieListFragment);

        currentMovieTag = TAG_MOVIE_LIST;

        SongListFragment songListFragment = new SongListFragment().newInstance(location);
        songListFragment.setOnSongClicked(this);

        components.put(TAG_SONG_LIST, songListFragment);

        currentSongTag = TAG_SONG_LIST;

        manager = getSupportFragmentManager();
    }

    private void setUpTabs() {
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);
        tabLayout.getTabAt(0).setIcon(R.drawable.ic_music_note_white_24dp);
        tabLayout.getTabAt(1).setIcon(R.drawable.ic_movie_white_24dp);
    }

    private void setUpToolbar() {

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
        }

        return true;
    }

    @Override
    public void onMovie(Movie movie) {

        //remove the movie list fragment and create a movie details fragment with the movie selected
        manager.beginTransaction().remove(components.get(currentMovieTag)).commit();
        currentMovieTag = TAG_MOVIE_DETAILS;
        components.remove(TAG_MOVIE_DETAILS);
        components.put(TAG_MOVIE_DETAILS, MovieFragment.newInstance(movie));
        movieTab = components.get(currentMovieTag);

        mSectionsPagerAdapter.notifyDataSetChanged();

    }

    @Override
    public void onSong(Song song) {

        //remove the movie list fragment and create a movie details fragment with the movie selected
        manager.beginTransaction().remove(components.get(currentSongTag)).commit();
        currentSongTag = TAG_SONG_DETAILS;
        components.remove(TAG_SONG_DETAILS);
        components.put(TAG_SONG_DETAILS, SongFragment.newInstance(song));
        songTab = components.get(currentSongTag);

        mSectionsPagerAdapter.notifyDataSetChanged();
    }

    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {

            switch (position) {
                case 1:
                    if (movieTab == null)
                        movieTab = components.get(currentMovieTag);

                    return movieTab;

                case 0:
                    if (songTab == null)
                        songTab = components.get(currentSongTag);

                    return songTab;
            }
            return null;

        }

        @Override
        public int getCount() {
            // Show 2 total pages.
            return 2;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 1:
                    return getString(R.string.text_movies);
                case 0:
                    return getString(R.string.text_songs);
            }
            return null;
        }

        @Override
        public int getItemPosition(Object object) {

            return POSITION_NONE;

            /*if (object instanceof MovieListFragment && movieTab instanceof MovieFragment) {
                return POSITION_NONE;
            }
            if (object instanceof MovieFragment && movieTab instanceof MovieListFragment) {
                return POSITION_NONE;
            }
            if (object instanceof MovieListFragment && movieTab instanceof MovieListFragment) {
                return POSITION_NONE;
            }
            if (object instanceof PlaceholderFragment) {
                return POSITION_UNCHANGED;
            }
            return POSITION_UNCHANGED;*/

        }

    }

    @Override
    public void onBackPressed() {

        //if we are in the movie tab
        if (mViewPager.getCurrentItem() == 1) {

            //and currently movie details is shown
            if (movieTab instanceof MovieFragment) {

                //shut down the fragment's async in order to avoid a crash
                ((MovieFragment) movieTab).shutDownAsyncTask();

                //go back to the movie list
                manager.beginTransaction().remove(components.get(currentMovieTag)).commit();
                currentMovieTag = TAG_MOVIE_LIST;
                movieTab = components.get(currentMovieTag);

                mSectionsPagerAdapter.notifyDataSetChanged();
            } else {

                ((MovieListFragment) movieTab).shutDownAsyncTask();

                //else finish the activity
                super.onBackPressed();
            }

        } else if (mViewPager.getCurrentItem() == 0) {

            //and currently movie details is shown
            if (songTab instanceof SongFragment) {

                //shut down the fragment's async in order to avoid a crash
                ((SongFragment) songTab).shutDownAsyncTask();

                //go back to the movie list
                manager.beginTransaction().remove(components.get(currentSongTag)).commit();
                currentSongTag = TAG_SONG_LIST;
                songTab = components.get(currentSongTag);

                mSectionsPagerAdapter.notifyDataSetChanged();
            } else {
                //else finish the activity
                super.onBackPressed();
            }

        } else {

            ((SongListFragment) songTab).shutDownAsyncTask();

            //else finish the activity
            super.onBackPressed();
        }

        mSectionsPagerAdapter.notifyDataSetChanged();

    }

}
