package com.geoculturedemo.nickstamp.geoculturedemo.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
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

public class HomeActivity extends AppCompatActivity implements View.OnClickListener {


    int PLACE_PICKER_REQUEST = 1;

    View cardLoading, cardNoLocation, cardLocationFound, cardPickLocation, cardButtonPickLocation;


    Button bPickLocation, bExploreCustom, bExploreLocal, bRetry;
    private TextView tvCurrentLocation, tvCustomLocation;

    private GPSUtils gpsUtils;
    private LocationUtils locationUtils;

    private Location currentLocation, customLocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        initViews();

        locationUtils = new LocationUtils(this);
        gpsUtils = new GPSUtils(this);

        manageGPSCards();


    }

    private void initViews() {
        cardLoading = findViewById(R.id.cardSearching);
        cardNoLocation = findViewById(R.id.cardNoLocation);
        cardLocationFound = findViewById(R.id.cardLocation);
        cardPickLocation = findViewById(R.id.cardPickLocation);

        cardButtonPickLocation = findViewById(R.id.cardbuttonPickLocation);
        cardButtonPickLocation.setOnClickListener(this);

        bPickLocation = (Button) findViewById(R.id.bPickLocation);

        bExploreCustom = (Button) findViewById(R.id.bExploreCustom);
        bExploreCustom.setOnClickListener(this);

        bExploreLocal = (Button) findViewById(R.id.bExploreLocal);
        bExploreLocal.setOnClickListener(this);

        bRetry = (Button) findViewById(R.id.bRetry);
        bRetry.setOnClickListener(this);

        tvCurrentLocation = (TextView) findViewById(R.id.tvCurrentLocation);
        tvCustomLocation = (TextView) findViewById(R.id.tvCustomLocation);

    }

    private void manageGPSCards() {
        if (gpsUtils.canGetLocation()) {
            cardLoading.setVisibility(View.GONE);

            currentLocation = locationUtils.getLocation(gpsUtils.getLatitude(), gpsUtils.getLongitude());
            if (currentLocation != null) {
                //if currentLocation was found,

                tvCurrentLocation.setText(currentLocation.getFullName() + "\n");
                tvCurrentLocation.append(locationUtils.toGreekLocale(currentLocation).getFullName());

                cardNoLocation.setVisibility(View.GONE);
                cardLocationFound.setVisibility(View.VISIBLE);
            } else {
                cardNoLocation.setVisibility(View.VISIBLE);
                cardLocationFound.setVisibility(View.GONE);
            }

        } else {
            gpsUtils.showGPSErrorDialog();
            cardLoading.setVisibility(View.GONE);
            cardNoLocation.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bRetry:
                gpsUtils.getLocation();
                manageGPSCards();
                break;
            case R.id.bExploreLocal:
//                Toast.makeText(HomeActivity.this, "You Explore locally!!!", Toast.LENGTH_LONG).show();
                //TODO launch tabs activity with the content for the current currentLocation
                startActivity(new Intent(HomeActivity.this, TabsActivity.class));
                break;
            case R.id.cardbuttonPickLocation:
//                cardPickLocation.setVisibility(View.VISIBLE);
//                cardButtonPickLocation.setVisibility(View.GONE);
                //TODO launch place picker activity for the user to pick a currentLocation
                try {

                    PlacePicker.IntentBuilder intentBuilder =

                            new PlacePicker.IntentBuilder();

                    Intent intent = intentBuilder.build(HomeActivity.this);

                    startActivityForResult(intent, PLACE_PICKER_REQUEST);

                } catch (GooglePlayServicesRepairableException e) {
                    Toast.makeText(this, "This app needs Google Play services to run properly", Toast.LENGTH_LONG).show();
                    e.printStackTrace();

                } catch (GooglePlayServicesNotAvailableException e) {
                    Toast.makeText(this, "This app needs Google Play services to run properly", Toast.LENGTH_LONG).show();

                    e.printStackTrace();

                }

                break;
            case R.id.bExploreCustom:
//                Toast.makeText(HomeActivity.this, "You Explore Custom currentLocation!!!", Toast.LENGTH_LONG).show();
                //TODO launch tabs activity with the content for the custom currentLocation
                startActivity(new Intent(HomeActivity.this, TabsActivity.class));
                break;
        }
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PLACE_PICKER_REQUEST) {
            if (resultCode == RESULT_OK) {

                Place place = PlacePicker.getPlace(data, this);

                customLocation = locationUtils.getLocation(place.getLatLng().latitude, place.getLatLng().longitude);
                if (customLocation != null) {
                    //if custom location was found,

                    tvCustomLocation.setText(customLocation.getFullName() + "\n");
                    tvCustomLocation.append(locationUtils.toGreekLocale(customLocation).getFullName());

                    cardButtonPickLocation.setVisibility(View.GONE);
                    cardPickLocation.setVisibility(View.VISIBLE);
                }

            }
        }
    }


}
