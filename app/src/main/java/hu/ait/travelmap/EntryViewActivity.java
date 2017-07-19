package hu.ait.travelmap;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.auth.FirebaseAuth;

import java.util.List;

import butterknife.OnClick;
import hu.ait.travelmap.data.EntryData;

public class EntryViewActivity extends AppCompatActivity implements OnMapReadyCallback {

    private TextView entryTitle;
    private TextView entryDate;
    private TextView entryLat;
    private TextView entryLong;
    private TextView entryRating;
    private TextView entryType;
    private TextView entryDescription;

    private GoogleMap mMap;
    private Marker markerEntry = null;

    String title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_entry_view);

        entryTitle = (TextView) findViewById(R.id.entryTitle);
        entryDate = (TextView) findViewById(R.id.entryDate);
        entryLat = (TextView) findViewById(R.id.entryLat);
        entryLong = (TextView) findViewById(R.id.entryLong);
        entryRating = (TextView) findViewById(R.id.entryRating);
        entryType = (TextView) findViewById(R.id.entryType);
        entryDescription = (TextView) findViewById(R.id.entryDescription);

        title = "";

        if (getIntent().hasExtra(EntryAdaptor.ENTRY_NAME)) {
            title = getIntent().getStringExtra(EntryAdaptor.ENTRY_NAME);
        }

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

        mMap = googleMap;

        mMap.getUiSettings().setZoomControlsEnabled(true);
        mMap.getUiSettings().setAllGesturesEnabled(true);

        EntryData entryData = EntryModel.getInstance().getEntryData();

        entryTitle.setText(entryData.getTitle());
        entryLat.setText(" " + entryData.getLatitude());
        entryLong.setText(" " + entryData.getLongitude());
        entryDate.setText(" " + entryData.getDate());
        entryRating.setText(" " + ""+entryData.getRating());
        entryType.setText(" " + ""+entryData.getType());
        entryDescription.setText(entryData.getDescription());

        setMyMarker(new LatLng(entryData.getLatitude(), entryData.getLongitude()));

        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                Intent intentLargeMap = new Intent();
                intentLargeMap.setClass(EntryViewActivity.this, FullMapsActivity.class);

                EntryViewActivity.this.startActivity(intentLargeMap);
            }
        });
    }

    public void setMyMarker(LatLng cityPosition) {

        MarkerOptions markerOptions = new MarkerOptions().
                position(cityPosition).
                title(title).
                icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_room));

        markerEntry = mMap.addMarker(markerOptions);

        mMap.animateCamera(CameraUpdateFactory.newLatLng(cityPosition));
    }
}
