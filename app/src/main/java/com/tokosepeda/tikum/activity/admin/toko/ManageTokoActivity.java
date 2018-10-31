package com.tokosepeda.tikum.activity.admin.toko;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;

import com.tokosepeda.tikum.R;
import com.tokosepeda.tikum.adapter.TokoAdminAdapter;
import com.tokosepeda.tikum.model.Toko;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ManageTokoActivity extends AppCompatActivity {

    @BindView(R.id.rv_toko)
    RecyclerView rvToko;

    private TokoAdminAdapter mAdapter;
    List<Toko> listAllToko = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_toko);

        ButterKnife.bind(this);

        initToolbar();
        initTokoData();
        initRecyclerView();
    }

    @OnClick(R.id.btn_add)
    public void buttonAdd(){
        Intent i = new Intent(this, FormTokoActivity.class);
        i.putExtra("mode", "add");
        startActivity(i);
    }

    private void initToolbar() {
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Manage Toko");
    }

    private void initTokoData() {
        listAllToko.add(new Toko("1", "Toko Sepeda 1", "toko@gmail.com", "08226227436","Cipadung, Bandung", "-6.92746", "107.71706",  "list sparepart", ""));
        listAllToko.add(new Toko("2", "Toko Sepeda 2", "toko@gmail.com", "08988190546","Cinunuk, Bandung", "-6.93760", "107.72246",  "list sparepart", ""));
        listAllToko.add(new Toko("3", "Toko Sepeda 3", "toko@gmail.com", "08965552374","Ujung Berung, Bandung", "-6.93972", "107.71205",  "list sparepart", ""));
        listAllToko.add(new Toko("4", "Toko Sepeda 4", "toko@gmail.com", "0857826893","Cilengkrang, Bandung", "-6.92775", "107.73265",  "list sparepart", ""));
        listAllToko.add(new Toko("5", "Toko Sepeda 5", "toko@gmail.com", "0899471774","Cipadung, Bandung", "-6.92937", "107.71878",  "list sparepart", ""));
        listAllToko.add(new Toko("6", "Toko Sepeda 6", "toko@gmail.com", "08787765473","Manisi, Bandung", "-6.92707", "107.72376",  "list sparepart", ""));
    }

    private void initRecyclerView() {
        rvToko.setHasFixedSize(true);
        rvToko.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new TokoAdminAdapter(this, listAllToko);
        rvToko.setAdapter(mAdapter);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}
