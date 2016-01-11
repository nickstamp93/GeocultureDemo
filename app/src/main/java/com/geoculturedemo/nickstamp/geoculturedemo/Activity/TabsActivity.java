package com.geoculturedemo.nickstamp.geoculturedemo.Activity;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.geoculturedemo.nickstamp.geoculturedemo.Callback.OnMovieClicked;
import com.geoculturedemo.nickstamp.geoculturedemo.Fragment.MovieFragment;
import com.geoculturedemo.nickstamp.geoculturedemo.Fragment.MovieListFragment;
import com.geoculturedemo.nickstamp.geoculturedemo.Model.Location;
import com.geoculturedemo.nickstamp.geoculturedemo.Model.Movie;
import com.geoculturedemo.nickstamp.geoculturedemo.R;

import java.util.HashMap;

public class TabsActivity extends AppCompatActivity implements OnMovieClicked {


    private static final String TAG_MOVIE_LIST = "movielist";
    private static final String TAG_MOVIE_DETAILS = "moviedetails";

    private SectionsPagerAdapter mSectionsPagerAdapter;

    private ViewPager mViewPager;
    private TabLayout tabLayout;

    private Fragment movieTab;
    private HashMap<String, Fragment> components;
    private FragmentManager manager;

    private String currentMovieTag;
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
        if (location == null) {
            location = new Location();
            location.setCity("Thessaloniki");
            location.setArea("Thessaloniki");
            location.setCountry("Greece");
        }
        MovieListFragment movieListFragment = new MovieListFragment().newInstance(location);

        movieListFragment.setOnMovieClickedListener(this);

        components.put(TAG_MOVIE_LIST, movieListFragment);

        currentMovieTag = TAG_MOVIE_LIST;

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
    }

    private void setUpToolbar() {

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public void onMovie(Movie movie) {

        //remove the movie list fragment and create a movie details fragment with the movie selected
        manager.beginTransaction().remove(components.get(currentMovieTag)).commit();
        currentMovieTag = TAG_MOVIE_DETAILS;
        components.remove(TAG_MOVIE_DETAILS);
        components.put(TAG_MOVIE_DETAILS, new MovieFragment().newInstance(movie));
        movieTab = components.get(currentMovieTag);

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
                    return PlaceholderFragment.newInstance(position + 1);
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
                    return "MOVIES";
                case 0:
                    return "SONGS";
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

                //go back to the movie list
                manager.beginTransaction().remove(components.get(currentMovieTag)).commit();
                currentMovieTag = TAG_MOVIE_LIST;
                movieTab = components.get(currentMovieTag);

                mSectionsPagerAdapter.notifyDataSetChanged();
            } else {
                //else finish the activity
                super.onBackPressed();
            }

        } else {
            //else finish the activity
            super.onBackPressed();
        }

        mSectionsPagerAdapter.notifyDataSetChanged();

    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";

        public PlaceholderFragment() {
        }

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_view_pager, container, false);
            TextView textView = (TextView) rootView.findViewById(R.id.section_label);
            textView.setText(getString(R.string.section_format, getArguments().getInt(ARG_SECTION_NUMBER)));
            return rootView;
        }
    }

}
