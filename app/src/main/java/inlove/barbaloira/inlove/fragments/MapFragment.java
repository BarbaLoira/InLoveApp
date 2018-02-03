package inlove.barbaloira.inlove.fragments;


import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.firebase.geofire.GeoFire;
import com.firebase.geofire.GeoLocation;
import com.firebase.geofire.GeoQuery;
import com.firebase.geofire.GeoQueryEventListener;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import inlove.barbaloira.inlove.Adapter.PopupFlirtAdapter;
import inlove.barbaloira.inlove.MainActivity;
import inlove.barbaloira.inlove.R;
import inlove.barbaloira.inlove.Shared.Util;
import inlove.barbaloira.inlove.model.FlirtLove;
import inlove.barbaloira.inlove.model.PopupFlirt;
import inlove.barbaloira.inlove.model.User;


/**
 * A simple {@link Fragment} subclass.
 */
public class MapFragment extends Fragment implements OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, com.google.android.gms.location.LocationListener {
    private FirebaseAuth mAuth;
    FloatingActionButton mLogout;
    GoogleApiClient mGoogleApliClient;
    Location mLastLocation;
    LocationRequest mLocationRequest;
    final int LOCATION_REQUEST_CODE = 1;
    private GoogleMap mMap;
    private SupportMapFragment mapFragment;
    GeoFire geofire;
    String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
    DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
    private int radius = 10000;
    DatabaseReference refClosesPersons = FirebaseDatabase.getInstance().getReference().child("users").child("infoMap");
    GeoFire geoFirerefClosesPersons = new GeoFire(refClosesPersons);
    GeoQuery geoQuery;
    ListView listViewPopupFlirt;
    private View v;
    boolean gps_enabled = false;
    boolean network_enabled = false;
    boolean network_bad_accuracy = false;
    private List<PopupFlirt> itemFlirt = Util.returnDataIteFlirt();

    public MapFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        v = inflater.inflate(R.layout.fragment_map, container, false);
        ((MainActivity) getActivity()).setToolbarBottomVisible(true);
        LocationManager lm = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);

        try {
            gps_enabled = lm.isProviderEnabled(LocationManager.GPS_PROVIDER);
        } catch (Exception ex) {
        }

        try {
            network_enabled = lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        } catch (Exception ex) {
        }

        if (!gps_enabled && !network_enabled) {
            AlertDialog.Builder dialog = new AlertDialog.Builder(getActivity());
            dialog.setMessage("gps network nao abilitado");
            dialog.setPositiveButton("habilitar localizacao", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                    Intent myIntent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                    getActivity().startActivity(myIntent);
                }
            });
            dialog.setNegativeButton("Cancelar", null);
            dialog.show();
        }


        mapFragment = (SupportMapFragment) getChildFragmentManager()
                .findFragmentById(R.id.map);

        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_REQUEST_CODE);

        } else {
            mapFragment.getMapAsync(this);


            mAuth = FirebaseAuth.getInstance();

            mLogout = (FloatingActionButton) v.findViewById(R.id.fab_map_logout);
            mLogout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    finishGeoFIre();
                    mGoogleApliClient.disconnect();
                    mMap.clear();
                    mMap.stopAnimation();

                    mAuth.signOut();
                    getFragmentManager().beginTransaction().replace(R.id.rl_content_main, new LoginFragment()).addToBackStack(null).commit();

                }
            });

        }
        return v;
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

        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_REQUEST_CODE);

        } else {
            buildGoogleApiClient();
            mMap.setMyLocationEnabled(true);
        }

    /*    // Add a marker in Sydney and move the camera
        LatLng sydney = new LatLng(-34, 151);
        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));*/
    }

    protected synchronized void buildGoogleApiClient() {
        mGoogleApliClient = new GoogleApiClient.Builder(getContext())
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        mGoogleApliClient.connect();
    }

    Marker lastMarker = null;
    String myKey = null;

    @Override
    public void onLocationChanged(Location location) {
        //getgetaplicationcontext!=null
        if (FirebaseAuth.getInstance().getCurrentUser() != null && location.getAccuracy() < 90) {
            mLastLocation = location;

            LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());

            mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));

            float distanceTo = mLastLocation.distanceTo(location);
            if (distanceTo < 50)
                mMap.animateCamera(CameraUpdateFactory.zoomTo(40));


            //   if (lastMarker != null)
            //        lastMarker.remove();
            //    lastMarker = mMap.addMarker(new MarkerOptions().position(latLng).title("Me")
            //            .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_VIOLET))).;


            final String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
            DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("users").child("infoMap");

            geofire = new GeoFire(ref);
            geofire.setLocation(userId, new GeoLocation(location.getLatitude(), location.getLongitude()));

            if (myKey == null)
                myKey = FirebaseAuth.getInstance().getCurrentUser().getUid();

            // Set a listener for marker click.
            mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                @Override
                public boolean onMarkerClick(final Marker marker) {
                    if (v != null) {


                        AlertDialog.Builder alertDialog = new AlertDialog.Builder(getActivity());
                        LayoutInflater inflater = getLayoutInflater();
                        View convertView = (View) inflater.inflate(R.layout.popup_flirt, null);
                        alertDialog.setView(convertView);
                        ListView lv = (ListView) convertView.findViewById(R.id.lv_item_popup_flirt);

                        AlertDialog dialog_card = alertDialog.create();
                        lv.setAdapter(new PopupFlirtAdapter(getActivity(), itemFlirt));
                        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                                for (User u : userIntoRadius) {
                                    Log.d("[ITEM POPUP CLICK]", "User id - "
                                            + u.getMarker().getSnippet() + " marker id - " + marker.getSnippet());
                                    if (u.getMarker().getSnippet().contentEquals(marker.getSnippet())) {
                                        DatabaseReference dbRefMarkerItemPopupFlirt = FirebaseDatabase.getInstance().getReference().child("users").child("infoFlirts");

                                        FlirtLove flirtLove = new FlirtLove(u.getKey(), i);
                                        dbRefMarkerItemPopupFlirt.child(u.getKey()).setValue(flirtLove);

                                    }
                                }
                            }
                        });

                        // dlgAlert.requestWindowFeature(Window.FEATURE_NO_TITLE);
                        // WindowManager.LayoutParams WMLP =
                        dialog_card.getWindow().setGravity(Gravity.BOTTOM);

                        dialog_card.show();


                    }
                    return false;
                }
            });

            getClosesPersons();
            getMarkerFlirt();
        } else if (network_bad_accuracy == false) {
            network_bad_accuracy = true;
            AlertDialog.Builder dialog = new AlertDialog.Builder(getActivity());
            dialog.setMessage("Para uma melhor usabilidade ultilize uma boa coneccao de ineternet");
            dialog.setPositiveButton("Entendi", null);
            dialog.show();
        }

    }

    private void getMarkerFlirt() {
        DatabaseReference dbRefFetMarkerFlirt = FirebaseDatabase.getInstance().getReference().child("users").child("infoFlirts");

        for (final User u : userIntoRadius) {
            dbRefFetMarkerFlirt.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot snapshot) {
                    if (snapshot.hasChild(u.getKey())) {
                     //   u.getMarker().set
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }
    }



    @Override
    public void onConnected(@Nullable Bundle bundle) {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(1000);
        mLocationRequest.setFastestInterval(1000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);


        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_REQUEST_CODE);

        } else {
            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApliClient, mLocationRequest, this);
        }
    }


    @Override
    public void onStop() {

        super.onStop();

        finishGeoFIre();
    }


    private void finishGeoFIre() {
        geofire = new GeoFire(ref);
        geofire.removeLocation(userId);
        if (geoQuery != null)
            geoQuery.removeAllListeners();

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode) {
            case LOCATION_REQUEST_CODE: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    mapFragment.getMapAsync(this);
                } else {
                    Toast.makeText(getActivity(), "Please provide the permission", Toast.LENGTH_SHORT);
                }
            }
        }


    }

    private List<User> userIntoRadius;

    private void getClosesPersons() {
        if (refClosesPersons == null)
            refClosesPersons = FirebaseDatabase.getInstance().getReference().child("users").child("infoMap");
        if (geoFirerefClosesPersons == null)
            geoFirerefClosesPersons = new GeoFire(refClosesPersons);

        geoQuery = geoFirerefClosesPersons.queryAtLocation(new GeoLocation(mLastLocation.getLatitude(), mLastLocation.getLongitude()), radius);

        geoQuery.addGeoQueryEventListener(new GeoQueryEventListener() {

            // new person into radius
            @Override
            public void onKeyEntered(String key, GeoLocation location) {
                if (!myKey.contentEquals(key)) {
                    boolean newUser = true;
                    if (userIntoRadius == null)
                        userIntoRadius = new ArrayList<>();
                    if (userIntoRadius.size() == 0) {

                        User user = new User(key, getUserName(key), location);
                        userIntoRadius.add(user);
                        markerUser(user);
                    } else {
                        for (User u : userIntoRadius) {
                            if (u.getKey().contentEquals(key)) {

                                markerUser(u);
                                newUser = false;
                            }
                        }
                        if (newUser) {
                            User user = new User(key, getUserName(key), location);
                            userIntoRadius.add(user);
                            markerUser(user);
                        }
                    }
                }
            }

            // a person out radius
            @Override
            public void onKeyExited(String key) {
                if (!myKey.contentEquals(key)) {
                    userIntoRadius.remove(key);
                    for (User u : userIntoRadius
                            ) {
                        if (u.getKey().contentEquals(key))
                            u.getMarker().remove();
                    }
                }
            }


            // person move
            @Override
            public void onKeyMoved(String key, GeoLocation location) {
                if (!myKey.contentEquals(key)) {
                    for (User u : userIntoRadius
                            ) {
                        if (u.getKey().contentEquals(key))
                            u.setGeoLocation(location);
                        markerUser(u);
                    }
                }
            }

            @Override
            public void onGeoQueryReady() {

            }

            @Override
            public void onGeoQueryError(DatabaseError error) {

            }
        });

    }

    Marker lastMarkerUser = null;

    private void markerUser(User user) {

        LatLng l = new LatLng(user.getGeoLocation().latitude, user.getGeoLocation().longitude);
        //  if (user.getMarker() != null)
        //     user.getMarker().remove();
        if (lastMarkerUser != null)
            lastMarkerUser.remove();

        lastMarkerUser = mMap.addMarker(new MarkerOptions().position(l).title("other user")
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)));
        lastMarkerUser.setIcon(BitmapDescriptorFactory.fromResource(R.drawable.location));
        //  user.setMarker(mMap.addMarker(new MarkerOptions().position(l).title(user.getName())));
        lastMarkerUser.setSnippet(user.getKey());
        user.setMarker(lastMarkerUser);

    }

    private String getUserNameProp;

    private String getUserName(String idUser) {

        DatabaseReference userLocationRef = FirebaseDatabase.getInstance().getReference().child("users").child(idUser).child("infoPerfil").child("name");
        userLocationRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String prevChildKey) {
                getUserNameProp = "";
                if (dataSnapshot != null)
                    getUserNameProp = (String) dataSnapshot.getValue();

            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }

            // ...
        });

        return getUserNameProp;
    }


}
