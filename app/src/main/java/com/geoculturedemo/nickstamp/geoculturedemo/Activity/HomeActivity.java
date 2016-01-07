package com.geoculturedemo.nickstamp.geoculturedemo.Activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import com.geoculturedemo.nickstamp.geoculturedemo.R;
import com.geoculturedemo.nickstamp.geoculturedemo.Utils.GPSUtils;

public class HomeActivity extends AppCompatActivity implements View.OnClickListener {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        GPSUtils gpsUtils = new GPSUtils(this);
        if (gpsUtils.canGetLocation())
            Toast.makeText(this, "Long:" + gpsUtils.getLongitude() + " Lat: " + gpsUtils.getLatitude(), Toast.LENGTH_SHORT).show();
        else
            gpsUtils.showGPSErrorDialog();


    }

    @Override
    public void onClick(View v) {

    }


}
