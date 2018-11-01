package com.tokosepeda.tikum.activity.admin.toko;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
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

    DatabaseReference dbToko;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_toko);

        ButterKnife.bind(this);

        dbToko = FirebaseDatabase.getInstance().getReference("toko");

        initToolbar();
        initRecyclerView();
    }

    @Override
    protected void onStart() {
        super.onStart();

        dbToko.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                listAllToko.clear();
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    Toko toko = postSnapshot.getValue(Toko.class);
                    listAllToko.add(toko);
                }
                mAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
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
