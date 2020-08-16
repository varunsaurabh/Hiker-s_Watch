package com.example.hikerswatch;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {
    TextView notice;
    Button switchToMap;
    LocationManager locationManager;
    LocationListener locationListener;


    public void Switch(View view){
        //Log.i("info","Botton pressed");
        Intent intent = new Intent(getApplicationContext(),MapsActivity.class);
        startActivity(intent);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        switchToMap = findViewById(R.id.SwitchToMap);
        locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        notice = (TextView)findViewById(R.id.noticeText);
        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                updateLocationInfo(location);
            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {

            }

            @Override
            public void onProviderEnabled(String provider) {

            }

            @Override
            public void onProviderDisabled(String provider) {

            }
        };
        if(ContextCompat.checkSelfPermission(this,Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION},1);
            notice.setVisibility(View.VISIBLE);

        }else{

            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,0,0,locationListener);
            Location lastknownLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            if(lastknownLocation != null){
                updateLocationInfo(lastknownLocation);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
            notice.setVisibility(View.INVISIBLE);
            startListening();
        }
    }
    public void startListening(){
        if(ContextCompat.checkSelfPermission(this,Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED){
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,0,0,locationListener);
        }
    }

    public void updateLocationInfo(Location location){
        //Log.i("Location",location.toString());
        TextView latText = (TextView)findViewById(R.id.latTextView);
        TextView longText = (TextView)findViewById(R.id.longTextView);
        TextView altText = (TextView)findViewById(R.id.altTextView);
        TextView accText = (TextView)findViewById(R.id.accTextView);
        TextView addressText = (TextView)findViewById(R.id.addTextView);

        latText.setText("Lattitude : " + Double.toString(location.getLatitude()));
        longText.setText("Longitude : " + Double.toString(location.getLongitude()));
        altText.setText("Altitude : " + Double.toString(location.getAltitude()));
        accText.setText("Accuracy : " + Double.toString(location.getAccuracy()));

        String address = "Could not find address!";

        Geocoder geocoder = new Geocoder(this, Locale.getDefault());

        try{
            List<Address> addressList = geocoder.getFromLocation(location.getLatitude(),location.getLongitude(),1);
            if(addressList != null && addressList.size() > 0){
                address = "";
                if(addressList.get(0).getThoroughfare() != null){
                    address += addressList.get(0).getThoroughfare() + "\n";
                }
                if(addressList.get(0).getLocality() != null){
                    address += addressList.get(0).getLocality() + "\n";
                }
                if(addressList.get(0).getPostalCode() != null){
                    address += addressList.get(0).getPostalCode() + "\n";
                }
                if(addressList.get(0).getAdminArea() != null){
                    address += addressList.get(0).getAdminArea();
                }
            }
        }catch(Exception e){
            e.printStackTrace();
        }

        addressText.setText(address);

        }
    }
