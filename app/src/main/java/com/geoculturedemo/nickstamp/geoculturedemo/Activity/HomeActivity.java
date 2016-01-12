package com.geoculturedemo.nickstamp.geoculturedemo.Activity;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.geoculturedemo.nickstamp.geoculturedemo.Model.Location;
import com.geoculturedemo.nickstamp.geoculturedemo.R;
import com.geoculturedemo.nickstamp.geoculturedemo.Utils.GPSUtils;
import com.geoculturedemo.nickstamp.geoculturedemo.Utils.LocationUtils;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.android.gms.maps.model.LatLngBounds;

public class HomeActivity extends AppCompatActivity implements View.OnClickListener {


    private static final int PLACE_PICKER_REQUEST = 1;
    private LatLngBounds lastBounds = null;
    private int animDuration = 800;

    //Views
    private View cardNoLocation, cardLocationFound, cardPickLocation, cardButtonPickLocation, llRetry, llSearchLocation;
    private Button bPickLocation, bExploreCustom, bExploreLocal, bRetry;
    private TextView tvCurrentLocation, tvCustomLocation;

    //Gps utilities
    private GPSUtils gpsUtils;
    //Location Utilities
    private LocationUtils locationUtils;

    //Location objects for current location and custom location
    private Location currentLocation, customLocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_home);


//        setUpToolbar();

        //initialize the UI vies
        initViews();

        //initialize the utility objects
        locationUtils = new LocationUtils(this);

        //hide/show appropriate cards
//        manageGPSCards();
        new AsyncFindLocation().execute();

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

    }


    @Override
    public void onClick(View v) {

        Intent i;

        switch (v.getId()) {
            case R.id.bRetry:
//                gpsUtils.getLocation();
//                manageGPSCards();
                new AsyncFindLocation().execute();
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

                Place place = PlacePicker.getPlace(data, this);
                lastBounds = PlacePicker.getLatLngBounds(data);

                //create a location object from the coordinates picked by the user in the place picker
                customLocation = locationUtils.getLocation(place.getLatLng().latitude, place.getLatLng().longitude);
                if (customLocation != null) {
                    //if custom location was found,

                    tvCustomLocation.setText(customLocation.getFullName());

                    cardButtonPickLocation.animate()
                            .translationX(cardButtonPickLocation.getWidth())
                            .setDuration(animDuration)
                            .setListener(new AnimatorListenerAdapter() {
                                @Override
                                public void onAnimationEnd(Animator animation) {
                                    super.onAnimationEnd(animation);
                                    cardButtonPickLocation.setVisibility(View.GONE);
                                }
                            });
                    if (cardPickLocation.getVisibility() != View.VISIBLE)
                        cardPickLocation.animate()
                                .alpha(0.0f)
                                .setDuration(animDuration)
                                .setListener(new AnimatorListenerAdapter() {
                                    @Override
                                    public void onAnimationEnd(Animator animation) {
                                        super.onAnimationEnd(animation);
                                        cardPickLocation.setVisibility(View.VISIBLE);
                                    }
                                });
                } else {
                    cardButtonPickLocation.setVisibility(View.VISIBLE);
                    cardPickLocation.setVisibility(View.GONE);
                }

            }
        }
    }

    public class AsyncFindLocation extends AsyncTask<Void, Void, Void> {

        private boolean locationFound;

        public AsyncFindLocation() {
            locationFound = false;
            gpsUtils = new GPSUtils(HomeActivity.this);
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            cardNoLocation.setVisibility(View.VISIBLE);

            llRetry.setVisibility(View.INVISIBLE);
            llSearchLocation.setVisibility(View.VISIBLE);

        }

        @Override
        protected Void doInBackground(Void... params) {

            boolean success = false;
            for (int i = 0; !success && i < 5; i++) {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                gpsUtils.getLocation();
                if (gpsUtils.canGetLocation()) {
                    currentLocation = locationUtils.getLocation(gpsUtils.getLatitude(), gpsUtils.getLongitude());
                    if (currentLocation != null) {
                        locationFound = true;
                        return null;
                    }
                } else {
                    return null;
                }

            }

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);


            if (locationFound) {
                tvCurrentLocation.setText(currentLocation.getFullName());

                cardNoLocation.animate()
                        .translationX(cardNoLocation.getWidth())
                        .setDuration(animDuration)
                        .setListener(new AnimatorListenerAdapter() {
                            @Override
                            public void onAnimationEnd(Animator animation) {
                                super.onAnimationEnd(animation);
                                cardNoLocation.setVisibility(View.GONE);
                            }
                        });
                cardLocationFound.animate()
                        .alpha(0.0f)
                        .setDuration(animDuration)
                        .setListener(new AnimatorListenerAdapter() {
                            @Override
                            public void onAnimationEnd(Animator animation) {
                                super.onAnimationEnd(animation);
                                cardLocationFound.setVisibility(View.VISIBLE);
                            }
                        });
//                cardNoLocation.setVisibility(View.GONE);
//                cardLocationFound.setVisibility(View.VISIBLE);
            } else {

                if (!gpsUtils.canGetLocation()) {
                    gpsUtils.showGPSErrorDialog();
                } else {
                    Snackbar.make(cardNoLocation, "Location not found yet.Please try again in a while.", Snackbar.LENGTH_SHORT).show();
                }
                llRetry.setVisibility(View.VISIBLE);
                llSearchLocation.setVisibility(View.INVISIBLE);
            }
        }
    }

}
