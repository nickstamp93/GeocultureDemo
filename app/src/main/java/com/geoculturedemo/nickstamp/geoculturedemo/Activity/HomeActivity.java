package com.geoculturedemo.nickstamp.geoculturedemo.Activity;

import android.content.Intent;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.geoculturedemo.nickstamp.geoculturedemo.Callback.OnLocationFound;
import com.geoculturedemo.nickstamp.geoculturedemo.Model.Location;
import com.geoculturedemo.nickstamp.geoculturedemo.R;
import com.geoculturedemo.nickstamp.geoculturedemo.Utils.AnimationUtils;
import com.geoculturedemo.nickstamp.geoculturedemo.Utils.FontUtils;
import com.geoculturedemo.nickstamp.geoculturedemo.Utils.GPSUtils;
import com.geoculturedemo.nickstamp.geoculturedemo.Utils.GeocodeWebService;
import com.geoculturedemo.nickstamp.geoculturedemo.Utils.HistoryUtils;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.android.gms.maps.model.LatLngBounds;

import java.util.ArrayList;
import java.util.Locale;

public class HomeActivity extends AppCompatActivity implements View.OnClickListener, OnLocationFound, GPSUtils.LocationCallback {

    private static final int PLACE_PICKER_REQUEST = 1;
    private LatLngBounds lastBounds = null;

    //View groups
    private View cardNoLocation, cardLocationFound, cardPickLocation,
            cardButtonPickLocation, llRetry, llSearchLocation,
            cardRecentPlaces, cardRecentSearches;
    LinearLayout llPlaces, llSearches;
    //Views
    private Button bPickLocation, bExploreCustom, bExploreLocal, bRetry;
    private TextView tvCurrentLocation, tvCustomLocation;


    //Location objects for current location and custom location
    private Location currentLocation, customLocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        customLocation = null;
        currentLocation = null;

        //set the font all over the activity
        FontUtils.setFont(this, getWindow().getDecorView());

        //initialize the UI views
        initViews();

        //fill the history cards
        initHistoryCards();

        GPSUtils.searchCurrentLocation(this, this);

    }

    /**
     * Init the history cards according to the saved searches and places in the shared prefs file
     */
    private void initHistoryCards() {

        ArrayList<Location> recentPlaces = HistoryUtils.getRecentPlaces(this);
        ArrayList<Location> recentSearches = HistoryUtils.getRecentSearches(this);

        Typeface typeface = Typeface.createFromAsset(getAssets(), "fonts/Roboto-Regular.ttf");

        int padding_in_dp = 8;  // 12 dps
        final float scale = getResources().getDisplayMetrics().density;
        int textSize = 16;
        int textPadding = (int) (padding_in_dp * scale + 0.5f);

        int[] attrs = new int[]{R.attr.selectableItemBackground};
        TypedArray typedArray = obtainStyledAttributes(attrs);
        int backgroundResource = typedArray.getResourceId(0, 0);
        typedArray.recycle();

        llPlaces.removeAllViews();

        if (recentPlaces.size() > 0) {
            cardRecentPlaces.setVisibility(View.VISIBLE);
            for (final Location location : recentPlaces) {

                String sPlace = location.getFullName();

                final TextView tv = new TextView(this);
                tv.setTextSize(TypedValue.COMPLEX_UNIT_SP, textSize);
                tv.setText(sPlace);
                tv.setTypeface(typeface);
                tv.setPadding(0, textPadding, 0, textPadding);

                tv.setClickable(true);
                tv.setBackgroundResource(backgroundResource);

                tv.setGravity(Gravity.START);

                tv.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));

                tv.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (GPSUtils.isNetworkEnabled(HomeActivity.this)) {
                            Intent i = new Intent(HomeActivity.this, TabsActivity.class);
                            i.putExtra("location", location);
                            startActivity(i);
                        } else {
                            Snackbar.make(tv, getString(R.string.text_no_internet), Snackbar.LENGTH_SHORT).show();
                        }
                    }
                });

                llPlaces.addView(tv);

            }
        }

        llSearches.removeAllViews();

        if (recentSearches.size() > 0) {
            cardRecentSearches.setVisibility(View.VISIBLE);
            for (final Location location : recentSearches) {

                String sSearch = location.getFullName();

                final TextView tv = new TextView(this);
                tv.setTextSize(TypedValue.COMPLEX_UNIT_SP, textSize);
                tv.setText(sSearch);
                tv.setTypeface(typeface);
                tv.setPadding(0, textPadding, 0, textPadding);

                tv.setClickable(true);
                tv.setBackgroundResource(backgroundResource);

                tv.setGravity(Gravity.START);

                tv.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));

                tv.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (GPSUtils.isNetworkEnabled(HomeActivity.this)) {
                            Intent i = new Intent(HomeActivity.this, TabsActivity.class);
                            i.putExtra("location", location);
                            startActivity(i);
                        } else {
                            Snackbar.make(tv, getString(R.string.text_no_internet), Snackbar.LENGTH_SHORT).show();
                        }

                    }
                });


                llSearches.addView(tv);

            }
        }
    }

    /**
     * Initializes the UI views such as the cards , buttons etc
     */
    private void initViews() {
        llSearchLocation = findViewById(R.id.llSearchingLocation);
        llRetry = findViewById(R.id.llRetry);

        cardNoLocation = findViewById(R.id.cardNoLocation);
        cardLocationFound = findViewById(R.id.cardLocation);
        cardPickLocation = findViewById(R.id.cardPickLocation);

        cardButtonPickLocation = findViewById(R.id.cardbuttonPickLocation);
        cardButtonPickLocation.setOnClickListener(this);

        bPickLocation = (Button) findViewById(R.id.bPickLocation);
        bPickLocation.setOnClickListener(this);

        bExploreCustom = (Button) findViewById(R.id.bExploreCustom);
        bExploreCustom.setOnClickListener(this);

        bExploreLocal = (Button) findViewById(R.id.bExploreLocal);
        bExploreLocal.setOnClickListener(this);

        bRetry = (Button) findViewById(R.id.bRetry);
        bRetry.setOnClickListener(this);

        tvCurrentLocation = (TextView) findViewById(R.id.tvCurrentLocation);
        tvCustomLocation = (TextView) findViewById(R.id.tvCustomLocation);

        cardRecentPlaces = findViewById(R.id.cardRecentPlaces);
        cardRecentSearches = findViewById(R.id.cardRecentSearches);

        llPlaces = (LinearLayout) findViewById(R.id.llRecentPlaces);
        llSearches = (LinearLayout) findViewById(R.id.llRecentSearches);

    }

    @Override
    public void onClick(View v) {

        Intent i;

        switch (v.getId()) {
            case R.id.bRetry:
                llSearchLocation.setVisibility(View.VISIBLE);
                llRetry.setVisibility(View.INVISIBLE);
                GPSUtils.searchCurrentLocation(this, this);
                break;
            case R.id.bExploreLocal:
                i = new Intent(HomeActivity.this, TabsActivity.class);
                i.putExtra("location", currentLocation);
                startActivity(i);
                break;
            case R.id.cardbuttonPickLocation:
                launchPlacePicker();
                break;
            case R.id.bPickLocation:
                launchPlacePicker();
                break;
            case R.id.bExploreCustom:
                i = new Intent(HomeActivity.this, TabsActivity.class);
                i.putExtra("location", customLocation);
                startActivity(i);
                break;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_item_favorites:
                startActivity(new Intent(this, FavoritesActivity.class));
                break;
        }
        return true;
    }

    /**
     * Launches an intent with a Place picker Activity, so the user can search and pick
     * a custom location to explore
     */
    private void launchPlacePicker() {
        try {

            PlacePicker.IntentBuilder intentBuilder =

                    new PlacePicker.IntentBuilder();

            //starting position should be the last one picked, if any
            if (lastBounds != null)
                intentBuilder.setLatLngBounds(lastBounds);
            Intent intent = intentBuilder.build(HomeActivity.this);

            startActivityForResult(intent, PLACE_PICKER_REQUEST);

        } catch (GooglePlayServicesRepairableException e) {
            Toast.makeText(this, "Something went wrong with Google Play Services", Toast.LENGTH_LONG).show();
            e.printStackTrace();

        } catch (GooglePlayServicesNotAvailableException e) {
            Toast.makeText(this, "This app needs Google Play Services to run properly", Toast.LENGTH_LONG).show();

            e.printStackTrace();

        }
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PLACE_PICKER_REQUEST) {
            if (resultCode == RESULT_OK) {

                Place place = PlacePicker.getPlace(this, data);
                lastBounds = PlacePicker.getLatLngBounds(data);

                GeocodeWebService.ReverseGeocode reverseGeocode = new GeocodeWebService.ReverseGeocode(this, this, false, Locale.getDefault());
                reverseGeocode.execute(place.getLatLng().latitude, place.getLatLng().longitude);

            }
        }
    }

    @Override
    public void onLocationFound(Location location, boolean isCurrentLocation) {


        if (location != null) {

            if (isCurrentLocation) {
                currentLocation = location;

                HistoryUtils.updateRecentPlaces(HomeActivity.this, currentLocation);

                tvCurrentLocation.setText(currentLocation.getFullName());

                AnimationUtils.switchCards(cardLocationFound, cardNoLocation);

            } else {
                boolean previous = false;
                if (customLocation != null) {
                    //there was a previous custom location, change the animation
                    previous = true;
                }

                customLocation = location;

                HistoryUtils.updateRecentSearches(HomeActivity.this, customLocation);

                tvCustomLocation.setText(customLocation.getFullName());

                if (!previous)
                    AnimationUtils.switchCards(cardPickLocation, cardButtonPickLocation);

            }

            initHistoryCards();
        } else {
            if (isCurrentLocation) {

                Snackbar.make(cardNoLocation, getString(R.string.snackbar_no_location), Snackbar.LENGTH_SHORT).show();

                llRetry.setVisibility(View.VISIBLE);
                llSearchLocation.setVisibility(View.INVISIBLE);
            } else {
                Toast.makeText(HomeActivity.this, getString(R.string.toast_service_unavailable), Toast.LENGTH_SHORT).show();
                cardButtonPickLocation.setVisibility(View.VISIBLE);
                cardPickLocation.setVisibility(View.GONE);
            }
        }

    }

    @Override
    public void onNewLocationAvailable(GPSUtils.GPSCoordinates location) {

        GeocodeWebService.ReverseGeocode reverseGeocode = new GeocodeWebService.ReverseGeocode(this, this, true, Locale.getDefault());
        reverseGeocode.execute(location.latitude, location.longitude);

    }

    @Override
    public void onGPSError() {
        GPSUtils.showGPSErrorDialog(this);
        llRetry.setVisibility(View.VISIBLE);
        llSearchLocation.setVisibility(View.INVISIBLE);
    }

    @Override
    public void onNetworkError() {
        GPSUtils.showInternetErrorDialog(this);
        llRetry.setVisibility(View.VISIBLE);
        llSearchLocation.setVisibility(View.INVISIBLE);
    }
}
