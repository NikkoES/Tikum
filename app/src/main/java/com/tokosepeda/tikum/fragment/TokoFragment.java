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

    public TokoFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_toko, container, false);
        ButterKnife.bind(this, v);

        initLocation();
        initTokoData();
        initRecyclerView();

        return v;
    }

    private void initTokoData() {
        listAllToko.add(new Toko("1", "Toko Sepeda 1", "toko@gmail.com", "08226227436","Cipadung, Bandung", "-6.92746", "107.71706",  "list sparepart", ""));
        listAllToko.add(new Toko("2", "Toko Sepeda 2", "toko@gmail.com", "08988190546","Cinunuk, Bandung", "-6.93760", "107.72246",  "list sparepart", ""));
        listAllToko.add(new Toko("3", "Toko Sepeda 3", "toko@gmail.com", "08965552374","Ujung Berung, Bandung", "-6.93972", "107.71205",  "list sparepart", ""));
        listAllToko.add(new Toko("4", "Toko Sepeda 4", "toko@gmail.com", "0857826893","Cilengkrang, Bandung", "-6.92775", "107.73265",  "list sparepart", ""));
        listAllToko.add(new Toko("5", "Toko Sepeda 5", "toko@gmail.com", "0899471774","Cipadung, Bandung", "-6.92937", "107.71878",  "list sparepart", ""));
        listAllToko.add(new Toko("6", "Toko Sepeda 6", "toko@gmail.com", "08787765473","Manisi, Bandung", "-6.92707", "107.72376",  "list sparepart", ""));

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
