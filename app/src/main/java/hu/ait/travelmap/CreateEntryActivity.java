package hu.ait.travelmap;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import hu.ait.travelmap.data.EntryData;

public class CreateEntryActivity extends AppCompatActivity implements
        MyLocationMonitor.MyLocationListener {

    @BindView(R.id.editTitle)
    EditText editTitle;
    @BindView(R.id.editDate)
    EditText editDate;
    @BindView(R.id.currentLocChecked)
    CheckBox clChecked;
    @BindView(R.id.editLocation)
    EditText editLocation;
    @BindView(R.id.editRating)
    EditText editRating;
    @BindView(R.id.editType)
    EditText editType;
    @BindView(R.id.editDescription)
    EditText editDescription;

    private MyLocationMonitor myLocationMonitor;
    private double currentLat;
    private double currentLon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_entry);

        ButterKnife.bind(this);

        myLocationMonitor = new MyLocationMonitor(this, this);

        requestNeededPermission();


    }

    private void requestNeededPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) !=
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
            } else {
                Toast.makeText(this, R.string.permissionNotGranted, Toast.LENGTH_SHORT).show();
            }
        }
    }

    @OnClick(R.id.btnPostEntry)
    public void postEntryClick() {
        if (!checkFields(editTitle.getText().toString()) ||
                !checkFields(editDate.getText().toString()) ||
                !checkFields(editType.getText().toString()) ||
                !checkFields(editDescription.getText().toString())) {
            Toast.makeText(CreateEntryActivity.this, R.string.fillFieldsError, Toast.LENGTH_SHORT).show();
        } else if (!checkRating(editRating.getText().toString())) {
            Toast.makeText(CreateEntryActivity.this, R.string.enterIntError, Toast.LENGTH_SHORT).show();
        } else if (!checkLocation(editLocation.getText().toString())) {
            Toast.makeText(CreateEntryActivity.this, R.string.enterLocationError, Toast.LENGTH_SHORT).show();
        } else {
            uploadEntry();
        }
    }

    private void uploadEntry() {
        String key = FirebaseDatabase.getInstance().
                getReference().child("entry").push().getKey();

        EntryData entry = new EntryData(
                FirebaseAuth.getInstance().getCurrentUser().getUid(),
                editTitle.getText().toString(),
                editDate.getText().toString(),
                Integer.parseInt(editRating.getText().toString()),
                editType.getText().toString(),
                editDescription.getText().toString(),
                clChecked.isChecked(),
                determineLatitude(),
                determineLongitude(),
                key
        );



        FirebaseDatabase.getInstance().getReference().
                child("entry").child(key).setValue(entry).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                Toast.makeText(CreateEntryActivity.this, R.string.entryCreated, Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(CreateEntryActivity.this, getString(R.string.errorEntry) + e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        finish();
    }

    @OnClick(R.id.btnCancelEntry)
    public void cancelEntryClick() {
        super.onBackPressed();
    }


    @Override
    public void newLocationReceived(Location location) {
        currentLat = location.getLatitude();
        currentLon = location.getLongitude();
    }

    private double determineLatitude() {
        if (clChecked.isChecked()) {
            return currentLat;
        } else {
            return currentLat;
        }
    }

    private double determineLongitude() {
        if (clChecked.isChecked()) {
            return currentLon;
        } else {
            return currentLon;
        }
    }

    private Boolean checkRating(String rating) {
        if ((TextUtils.isEmpty(rating)) || !(rating.matches("^[0-9]+$")) || (Integer.parseInt(rating) < 0) || (Integer.parseInt(rating) > 10)) {
            return false;
        } else {
            return true;
        }
    }

    private Boolean checkFields(String field) {
        if (TextUtils.isEmpty(field)) {
            return false;
        } else {
            return true;
        }
    }

    private Boolean checkLocation(String location) {
        if ((!TextUtils.isEmpty(location)) || (clChecked.isChecked())) {
            return true;
        } else {
            return false;
        }
    }


    @Override
    protected void onDestroy() {
        myLocationMonitor.stopLocationMonitoring();

        super.onDestroy();
    }
}
