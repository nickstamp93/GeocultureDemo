package com.geoculturedemo.nickstamp.geoculturedemo.Activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.geoculturedemo.nickstamp.geoculturedemo.Model.Location;
import com.geoculturedemo.nickstamp.geoculturedemo.R;
import com.geoculturedemo.nickstamp.geoculturedemo.Utils.GPSUtils;
import com.geoculturedemo.nickstamp.geoculturedemo.Utils.LocationUtils;

public class HomeActivity extends AppCompatActivity implements View.OnClickListener {

    View cardLoading, cardNoLocation, cardLocationFound, cardPickLocation, cardButtonPickLocation;


    Button bPickLocation, bExploreCustom, bExploreLocal, bRetry;

    private GPSUtils gpsUtils;
    private LocationUtils locationUtils;

    private Location location;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

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

        locationUtils = new LocationUtils();

        gpsUtils = new GPSUtils(this);
        manageGPSCards();



    }

    private void manageGPSCards() {
        if (gpsUtils.canGetLocation()) {
            cardLoading.setVisibility(View.GONE);
            cardNoLocation.setVisibility(View.GONE);
            cardLocationFound.setVisibility(View.VISIBLE);

            //TODO get location info from the coordinates found

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
                Toast.makeText(HomeActivity.this, "You Explore locally!!!", Toast.LENGTH_LONG).show();
                break;
            case R.id.cardbuttonPickLocation:
                cardPickLocation.setVisibility(View.VISIBLE);
                cardButtonPickLocation.setVisibility(View.GONE);
                break;
            case R.id.bExploreCustom:
                Toast.makeText(HomeActivity.this, "You Explore Custom location!!!", Toast.LENGTH_LONG).show();
                break;
        }
    }


}
