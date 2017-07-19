package hu.ait.travelmap.data;

import android.location.Location;

import com.google.android.gms.maps.model.LatLng;

import io.realm.RealmModel;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class EntryData {

    private String uid;
    private String title;
    private String date;
    private int rating;
    private String type;
    private String description;
    private double latitude;
    private double longitude;
    private Boolean clChecked;
    private String key;

    public EntryData(){
    }

    public EntryData (String uid, String title, String date, int rating, String type, String description,
                      Boolean clChecked, double lat, double lng, String key) {
        this.uid = uid;
        this.title = title;
        this.date = date;
        this.rating = rating;
        this.type = type;
        this.description = description;
        this.clChecked =clChecked;
        this.latitude = lat;
        this.longitude = lng;
        this.key = key;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Boolean getClChecked(Boolean clChecked) {
        return clChecked;
    }

    public void setClChecked() {
        this.clChecked = clChecked;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude() {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude() {
        this.longitude = longitude;
    }

    public String getKey() {
        return key;
    }

    public void setKey() {
        this.key = key;
    }



}
