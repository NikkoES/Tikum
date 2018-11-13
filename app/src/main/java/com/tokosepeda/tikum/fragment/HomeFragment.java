package com.tokosepeda.tikum.fragment;


import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.tokosepeda.tikum.R;
import com.tokosepeda.tikum.activity.friend.DetailTemanActivity;
import com.tokosepeda.tikum.firebase.FirebaseApplication;
import com.tokosepeda.tikum.model.Teman;
import com.tokosepeda.tikum.model.Toko;
import com.tokosepeda.tikum.model.User;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;

import static android.content.Context.LOCATION_SERVICE;

/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment implements OnMapReadyCallback, LocationListener {

    final static int PERMISSION_ALL = 1;
    final static String[] PERMISSIONS = {android.Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION};

    LocationManager locationManager;
    Location myLocation;

    DatabaseReference dbUser, dbTeman, dbPertemanan;
    private FirebaseAuth mAuth;
    String id;
    User user;

    private MapView mapView;
    private GoogleMap mMap;

    private UiSettings mUiSettings;

    List<User> listUser = new ArrayList<>();
    List<Teman> listTeman = new ArrayList<>();

    private FragmentActivity myContext;

    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(Activity activity) {
        myContext = (FragmentActivity) activity;
        super.onAttach(activity);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_home, container, false);

        ButterKnife.bind(this, v);

        mAuth = ((FirebaseApplication) getActivity().getApplication()).getFirebaseAuth();
        id = mAuth.getCurrentUser().getUid();

        dbUser = FirebaseDatabase.getInstance().getReference("users").child(id);
        dbPertemanan = FirebaseDatabase.getInstance().getReference("users");
        dbTeman = FirebaseDatabase.getInstance().getReference("friends").child(id);

        updateData(new MyCallback() {
            @Override
            public void onCallback(User value) {
                dbUser.setValue(value);
            }
        });
        initMapFragment(v, savedInstanceState);

        return v;
    }

    private void initMapFragment(View v, Bundle savedInstanceState) {
        mapView = (MapView) v.findViewById(R.id.map);
        mapView.onCreate(savedInstanceState);
        mapView.onResume();

        try {
            MapsInitializer.initialize(getActivity().getApplicationContext());
        } catch (Exception e) {
            e.printStackTrace();

        }

        mapView.getMapAsync(this);

        locationManager = (LocationManager) getContext().getSystemService(LOCATION_SERVICE);
        if (Build.VERSION.SDK_INT >= 23 && !isPermissionGranted())
            requestPermissions(PERMISSIONS, PERMISSION_ALL);
        else
            requestLocation();
        if (!isLocationEnabled())
            showAlert(1);
    }

    @Override
    public void onResume() {
        super.onResume();
        updateData(new MyCallback() {
            @Override
            public void onCallback(User value) {
                dbUser.setValue(value);
            }
        });
    }

    public void updateData(final MyCallback myCallback) {
        locationManager = (LocationManager) getContext().getSystemService(LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        myLocation = new Location("");
        if (location == null) {
            myLocation.setLatitude(0);
            myLocation.setLongitude(0);
        } else {
            myLocation.setLatitude(location.getLatitude());
            myLocation.setLongitude(location.getLongitude());
        }

        dbUser.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                user = dataSnapshot.getValue(User.class);
                if (myLocation.getLatitude() == 0) {
                    user.setLatitude("");
                    user.setLongitude("");
                } else {
                    user.setLatitude(String.valueOf(myLocation.getLatitude()));
                    user.setLongitude(String.valueOf(myLocation.getLongitude()));
                }

                myCallback.onCallback(user);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public interface MyCallback {
        void onCallback(User value);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

        mMap = googleMap;

        mUiSettings = mMap.getUiSettings();

        mUiSettings.setZoomControlsEnabled(true);
        mUiSettings.setMyLocationButtonEnabled(true);
//        mUiSettings.setMapToolbarEnabled();
        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
//        mMap.setMyLocationEnabled(true);

        int height = 70;
        int width = 70;

//        icon marker
        BitmapDrawable bitToko = (BitmapDrawable) getResources().getDrawable(R.drawable.marker_bike);
        BitmapDrawable bitMine = (BitmapDrawable) getResources().getDrawable(R.drawable.marker_mine);
        BitmapDrawable bitTeman = (BitmapDrawable) getResources().getDrawable(R.drawable.marker_teman);
        Bitmap bToko = bitToko.getBitmap();
        Bitmap bMine = bitMine.getBitmap();
        Bitmap bTeman = bitTeman.getBitmap();
        final Bitmap smallMarkerToko = Bitmap.createScaledBitmap(bToko, width, height, false);
        Bitmap smallMarkerMine = Bitmap.createScaledBitmap(bMine, width, height, false);
        final Bitmap smallMarkerTeman = Bitmap.createScaledBitmap(bTeman, width, height, false);

        //init my location
        locationManager = (LocationManager) getContext().getSystemService(LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        final Location myLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        LatLng lokasiMember;
        if (myLocation != null) {
            lokasiMember = new LatLng(myLocation.getLatitude(), myLocation.getLongitude());
        } else {
            lokasiMember = new LatLng(-6.914744, 107.609810);
        }

        //tampil marker teman
        dbTeman.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                listTeman.clear();
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    Teman teman = postSnapshot.getValue(Teman.class);
                    listTeman.add(teman);
                }
                dbPertemanan.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        listUser.clear();
                        for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                            User user = postSnapshot.getValue(User.class);
                            for (int i = 0; i < listTeman.size(); i++) {
                                Teman teman = listTeman.get(i);
                                if (teman.getId().equalsIgnoreCase(user.getId())) {
                                    Double latitude, longitude;
                                    if (user.getLatitude().equalsIgnoreCase("")) {
                                        latitude = 0.0;
                                    } else {
                                        latitude = Double.parseDouble(user.getLatitude());
                                    }
                                    if (user.getLongitude().equalsIgnoreCase("")) {
                                        longitude = 0.0;
                                    } else {
                                        longitude = Double.parseDouble(user.getLongitude());
                                    }
                                    LatLng lokasiTeman = new LatLng(latitude, longitude);
                                    mMap.addMarker(new MarkerOptions().position(lokasiTeman).title(user.getNamaUser()).snippet("#" + user.getIdUser()).icon(BitmapDescriptorFactory.fromBitmap(smallMarkerToko)));
                                } else {
                                    Double latitude, longitude;
                                    if (user.getLatitude().equalsIgnoreCase("")) {
                                        latitude = 0.0;
                                    } else {
                                        latitude = Double.parseDouble(user.getLatitude());
                                    }
                                    if (user.getLongitude().equalsIgnoreCase("")) {
                                        longitude = 0.0;
                                    } else {
                                        longitude = Double.parseDouble(user.getLongitude());
                                    }
                                    LatLng lokasiTeman = new LatLng(latitude, longitude);
                                    mMap.addMarker(new MarkerOptions().position(lokasiTeman).title(user.getNamaUser()).snippet("#" + user.getIdUser()).icon(BitmapDescriptorFactory.fromBitmap(smallMarkerTeman)));
                                }
                            }
                            listUser.add(user);
                        }

                        mMap.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {

                            @Override
                            public View getInfoWindow(Marker arg0) {
                                return null;
                            }

                            @Override
                            public View getInfoContents(Marker marker) {

                                LinearLayout info = new LinearLayout(getContext());
                                info.setOrientation(LinearLayout.VERTICAL);

                                TextView title = new TextView(getContext());
                                title.setTextColor(Color.BLACK);
                                title.setGravity(Gravity.CENTER);
                                title.setTypeface(null, Typeface.BOLD);
                                title.setText(marker.getTitle());

                                TextView snippet = new TextView(getContext());
                                snippet.setTextColor(Color.GRAY);
                                snippet.setGravity(Gravity.CENTER);
                                snippet.setText(marker.getSnippet());

                                info.addView(title);
                                info.addView(snippet);

                                return info;
                            }
                        });

                        mMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
                            @Override
                            public void onInfoWindowClick(Marker marker) {
                                for (int i = 0; i < listUser.size(); i++) {
                                    User user = listUser.get(i);
                                    if (marker.getTitle().equalsIgnoreCase(user.getNamaUser())) {
                                        Intent intent = new Intent(getContext(), DetailTemanActivity.class);
                                        intent.putExtra("detail_teman", user);
                                        startActivity(intent);
                                        break;
                                    }
                                }
                            }
                        });
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        mMap.addMarker(new MarkerOptions().position(lokasiMember).title("Lokasi saya").icon(BitmapDescriptorFactory.fromBitmap(smallMarkerMine)));
        CameraPosition cameraPosition = new CameraPosition.Builder().target(lokasiMember).zoom(15).build();
        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));

    }

    @Override
    public void onLocationChanged(Location location) {

    }

    private void requestLocation() {
        Criteria criteria = new Criteria();
        criteria.setAccuracy(Criteria.ACCURACY_FINE);
        criteria.setPowerRequirement(Criteria.POWER_HIGH);
        String provider = locationManager.getBestProvider(criteria, true);
        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        locationManager.requestLocationUpdates(provider, 10000, 10, this);
    }

    private boolean isLocationEnabled() {
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) ||
                locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
    }

    private boolean isPermissionGranted() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (getContext().checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED || getContext().checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED) {
                Log.v("mylog", "Permission is granted");
                return true;
            } else {
                Log.v("mylog", "Permission not granted");
                return false;
            }
        }
        return false;
    }

    private void showAlert(final int status) {
        String message, title, btnText;
        if (status == 1) {
            message = "Your Locations Settings is set to 'Off'.\nPlease Enable Location to " +
                    "use this app";
            title = "Enable Location";
            btnText = "Location Settings";
        } else {
            message = "Please allow this app to access location!";
            title = "Permission access";
            btnText = "Grant";
        }
        final AlertDialog.Builder dialog = new AlertDialog.Builder(getContext());
        dialog.setCancelable(false);
        dialog.setTitle(title)
                .setMessage(message)
                .setPositiveButton(btnText, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                        if (status == 1) {
                            getActivity().finish();
                            Intent myIntent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                            startActivity(myIntent);
                        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                            requestPermissions(PERMISSIONS, PERMISSION_ALL);
                        }
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                        getActivity().finish();
                    }
                });
        dialog.show();
    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {

    }

    @Override
    public void onProviderEnabled(String s) {

    }

    @Override
    public void onProviderDisabled(String s) {

    }

}
