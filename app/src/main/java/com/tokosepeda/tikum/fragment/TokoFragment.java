package com.tokosepeda.tikum.fragment;


import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.tokosepeda.tikum.R;
import com.tokosepeda.tikum.adapter.TokoAdapter;
import com.tokosepeda.tikum.model.Toko;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static android.content.Context.LOCATION_SERVICE;

/**
 * A simple {@link Fragment} subclass.
 */
public class TokoFragment extends Fragment {

    @BindView(R.id.rv_toko)
    RecyclerView rvPesantren;

    private TokoAdapter mAdapter;
    List<Toko> listAllToko = new ArrayList<>();
    List<Toko> listToko = new ArrayList<>();

    Location myLocation;
    LocationManager locationManager;
    LocationListener listener;

    DatabaseReference dbToko;

    public TokoFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_toko, container, false);
        ButterKnife.bind(this, v);

        dbToko = FirebaseDatabase.getInstance().getReference("toko");

        initLocation();
        initTokoData();
        initRecyclerView();

        return v;
    }

    private void initTokoData() {
        dbToko.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                listAllToko.clear();
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    Toko toko = postSnapshot.getValue(Toko.class);
                    listAllToko.add(toko);
                }
                for (int i = 0; i < listAllToko.size(); i++) {
                    final Toko toko = listAllToko.get(i);

                    Location tokoLocation = new Location("");
                    tokoLocation.setLatitude(Double.parseDouble(toko.getLatToko()));
                    tokoLocation.setLongitude(Double.parseDouble(toko.getLongToko()));

                    float distance = myLocation.distanceTo(tokoLocation);

                    if (distance < 3000) {
                        listToko.add(listAllToko.get(i));
                    }
                }
                mAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void initLocation() {
        locationManager = (LocationManager) getContext().getSystemService(LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        myLocation = new Location("");
        myLocation.setLatitude(location.getLatitude());
        myLocation.setLongitude(location.getLongitude());
    }

    private void initRecyclerView() {
        rvPesantren.setHasFixedSize(true);
        rvPesantren.setLayoutManager(new LinearLayoutManager(getContext()));
        mAdapter = new TokoAdapter(getContext(), listToko, myLocation);
        rvPesantren.setAdapter(mAdapter);
    }

}
