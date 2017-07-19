package hu.ait.travelmap;

import android.*;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

import hu.ait.travelmap.data.EntryData;

public class FullMapsActivity extends FragmentActivity implements MyLocationMonitor.MyLocationListener, OnMapReadyCallback {

    private GoogleMap mMap;
    private MyLocationMonitor myLocationMonitor;
    private Marker markerCityPosition = null;
    private Marker markerMyPostion = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_full_maps);

        myLocationMonitor = new MyLocationMonitor(this, this);

        requestNeededPermission();

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    private void requestNeededPermission() {
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) !=
                PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, android.Manifest.permission.ACCESS_FINE_LOCATION)) {
            }

            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, 1001);
        } else {
            myLocationMonitor.startLocationMonitoring();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == 1001) {
            if (grantResults.length > 0 &&
                    grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                Toast.makeText(this, R.string.permissionGranted, Toast.LENGTH_SHORT).show();
                myLocationMonitor.startLocationMonitoring();
            } else {
                Toast.makeText(this, R.string.permissionNotGranted, Toast.LENGTH_SHORT).show();
            }
        }
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        mMap.getUiSettings().setZoomControlsEnabled(true);
        mMap.getUiSettings().setAllGesturesEnabled(true);

        DatabaseReference entryDbs = FirebaseDatabase.getInstance().getReference().child("entry");
        entryDbs.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot snapshot: dataSnapshot.getChildren()) {
                    MarkerOptions markerOptions = new MarkerOptions().
                            position(new LatLng(Double.valueOf((snapshot.child("latitude").getValue()).toString()),
                                    Double.valueOf((snapshot.child("longitude").getValue()).toString()))).
                            title((snapshot.child("title").getValue()).toString()).
                            icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_room));

                    markerCityPosition = mMap.addMarker(markerOptions);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                Intent intentOpenEntry = new Intent();
                intentOpenEntry.setClass(FullMapsActivity.this, EntryViewActivity.class);
                FullMapsActivity.this.startActivity(intentOpenEntry);

                return true;
            }
        });

    }

    @Override
    public void newLocationReceived(Location location) {

        setCurrentMarker(new LatLng(location.getLatitude(), location.getLongitude()));

    }

    private void setCurrentMarker(LatLng myPosition) {

        if (markerMyPostion == null) {
            MarkerOptions markerOptions = new MarkerOptions().
                    position(myPosition).
                    title(getString(R.string.im_here)).icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_face));

            markerMyPostion = mMap.addMarker(markerOptions);
        } else {
            markerMyPostion.setPosition(myPosition);
        }
    }
}
