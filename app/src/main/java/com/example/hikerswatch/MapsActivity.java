package com.example.hikerswatch;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.List;
import java.util.Locale;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    LocationManager locationManager2;
    LocationListener locationListener2;

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                    locationManager2.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener2);
                }
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        mMap.setMapType(googleMap.MAP_TYPE_HYBRID);

        locationManager2 = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        locationListener2 = new LocationListener() {
            @Override
            public void onLocationChanged(Location location2) {
                // Add a marker in Sydney and move the camera
                mMap.clear();
                final LatLng userLocation2 = new LatLng(location2.getLatitude(), location2.getLongitude());
                mMap.addMarker(new MarkerOptions().position(userLocation2).title("Your Location"));
                mMap.moveCamera(CameraUpdateFactory.newLatLng(userLocation2));

                /*Geocoder geocoder = new Geocoder(getApplicationContext(), Locale.getDefault());
                try {
                    List<Address> adressList = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
                    if (adressList != null && adressList.size() > 0) {
                        String address = "";
                        if (adressList.get(0).getThoroughfare() != null) {
                            address += adressList.get(0).getThoroughfare() + " ";
                        }

                        if (adressList.get(0).getLocality() != null) {
                            address += adressList.get(0).getLocality() + " ";
                        }
                        if (adressList.get(0).getPostalCode() != null) {
                            address += adressList.get(0).getPostalCode() + " ";
                        }
                        if (adressList.get(0).getAdminArea() != null) {
                            address += adressList.get(0).getAdminArea();
                        }

                        //Toast.makeText(MapsActivity.this, address, Toast.LENGTH_SHORT).show();
                        //Log.i("Address", address);

                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }*/
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

        if (Build.VERSION.SDK_INT < 23) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return;
            }
            locationManager2.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener2);
        }else{
            if(ContextCompat.checkSelfPermission(this,Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED){
                ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION},1);
            }else{
                locationManager2.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener2);
                Location lastKnownLocation2 = locationManager2.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                mMap.clear();
                final LatLng userLocation = new LatLng(lastKnownLocation2.getLatitude(), lastKnownLocation2.getLongitude());
                mMap.addMarker(new MarkerOptions().position(userLocation).title("Your Location"));
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(userLocation, 15));

            }
        }

    }
}
