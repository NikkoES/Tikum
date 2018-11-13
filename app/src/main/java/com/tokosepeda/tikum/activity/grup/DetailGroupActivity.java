package com.tokosepeda.tikum.activity.grup;

import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.tokosepeda.tikum.R;
import com.tokosepeda.tikum.adapter.GrupAdapter;
import com.tokosepeda.tikum.adapter.TemanAdapter;
import com.tokosepeda.tikum.firebase.FirebaseApplication;
import com.tokosepeda.tikum.model.Grup;
import com.tokosepeda.tikum.model.User;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class DetailGroupActivity extends AppCompatActivity {

    @BindView(R.id.rv_anggota)
    RecyclerView rvAnggota;
    @BindView(R.id.txt_nama_grup)
    TextView txtNamaGrup;
    @BindView(R.id.txt_id_grup)
    TextView txtIdGrup;
    @BindView(R.id.txt_deskripsi_grup)
    TextView txtDeskripsiGrup;
    @BindView(R.id.btn_join)
    Button btnJoin;

    String idUser, id;
    boolean anggota;

    Grup grup;

    TemanAdapter mAdapter;

    List<User> listAnggota = new ArrayList<>();

    DatabaseReference dbAnggota, dbGrup, dbUser;
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_group);
        ButterKnife.bind(this);

        grup = (Grup) getIntent().getSerializableExtra("grup");

        mAuth = ((FirebaseApplication) getApplication()).getFirebaseAuth();
        idUser = mAuth.getCurrentUser().getUid();

        dbAnggota = FirebaseDatabase.getInstance().getReference("member_group").child(grup.getId());
        dbGrup = FirebaseDatabase.getInstance().getReference("groups");
        dbUser = FirebaseDatabase.getInstance().getReference("users").child(idUser);

        initToolbar();
        initUI();
        initRecyclerView();
    }

    @Override
    protected void onResume() {
        super.onResume();
        dbAnggota.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                listAnggota.clear();
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    User user = postSnapshot.getValue(User.class);
                    if(idUser.equalsIgnoreCase(user.getId())){
                        anggota = true;
                    }
                    listAnggota.add(user);
                }
                if(anggota){
                    btnJoin.setText("keluar grup");
                }
                else{
                    btnJoin.setText("gabung grup");
                }
                mAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    @OnClick(R.id.btn_join)
    public void buttonJoin(){
        if(btnJoin.getText().toString().equalsIgnoreCase("gabung grup")){
            dbUser.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    User user = dataSnapshot.getValue(User.class);
                    dbAnggota = FirebaseDatabase.getInstance().getReference("member_group").child(id).child(idUser);
                    dbAnggota.setValue(user);
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
            btnJoin.setText("keluar grup");
        }
        else{
            dbAnggota = FirebaseDatabase.getInstance().getReference("member_group").child(id).child(idUser);
            dbAnggota.removeValue();
            btnJoin.setText("gabung grup");
        }
        finish();
    }

    private void initRecyclerView() {
        rvAnggota.setHasFixedSize(true);
        rvAnggota.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new TemanAdapter(this, listAnggota);
        rvAnggota.setAdapter(mAdapter);
    }

    private void initUI() {
        id = grup.getId();
        txtIdGrup.setText("#"+grup.getIdGrup());
        txtNamaGrup.setText(grup.getNamaGrup());
        txtDeskripsiGrup.setText(grup.getDeskripsiGrup());
    }

    private void initToolbar() {
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Detail Grup");
        getSupportActionBar().setSubtitle(grup.getNamaGrup());
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}
