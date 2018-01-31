package inlove.barbaloira.inlove.model;

import com.firebase.geofire.GeoLocation;
import com.google.android.gms.maps.model.Marker;

/**
 * Created by marco on 18/01/2018.
 */

public class User {
    private String key;
    private String name;
    private GeoLocation geoLocation;
    private Marker marker;


    public User() {
    }

    public User(String name) {
        this.name = name;
    }

    public User(String key, String name, GeoLocation geoLocation) {
        this.key = key;
        this.name = name;
        this.geoLocation = geoLocation;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public GeoLocation getGeoLocation() {
        return geoLocation;
    }

    public void setGeoLocation(GeoLocation geoLocation) {
        this.geoLocation = geoLocation;
    }

    public Marker getMarker() {
        return marker;
    }

    public void setMarker(Marker marker) {
        this.marker = marker;
    }
}
