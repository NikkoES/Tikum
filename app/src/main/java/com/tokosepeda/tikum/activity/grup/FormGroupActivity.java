package com.tokosepeda.tikum.activity.grup;

import android.app.ProgressDialog;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.tokosepeda.tikum.R;
import com.tokosepeda.tikum.activity.admin.toko.FormTokoActivity;
import com.tokosepeda.tikum.firebase.FirebaseApplication;
import com.tokosepeda.tikum.model.Grup;
import com.tokosepeda.tikum.model.Toko;
import com.tokosepeda.tikum.model.User;
import com.tokosepeda.tikum.utils.DialogUtils;

import java.util.Random;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class FormGroupActivity extends AppCompatActivity {

    @BindView(R.id.et_id)
    EditText etId;
    @BindView(R.id.et_id_grup)
    EditText etIdGrup;
    @BindView(R.id.et_nama_grup)
    EditText etNamaGrup;
    @BindView(R.id.et_deskripsi_grup)
    EditText etDeskripsiGrup;

    FirebaseAuth mAuth;
    DatabaseReference dbGrup, dbAnggota, dbUser;

    private ProgressDialog loading, loading2;

    Grup grup;
    String idUser, id, idGrup, namaGrup, deskripsiGrup;
    String mode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form_group);
        ButterKnife.bind(this);

        loading = DialogUtils.showProgressDialog(this, "Loading", "Saving data..");
        loading2 = DialogUtils.showProgressDialog(this, "Loading", "Loading data..");

        mAuth = ((FirebaseApplication) getApplication()).getFirebaseAuth();
        idUser = mAuth.getCurrentUser().getUid();

        mode = getIntent().getStringExtra("mode");
        id = getIntent().getStringExtra("id_grup");

        dbGrup = FirebaseDatabase.getInstance().getReference("groups");
        dbAnggota = FirebaseDatabase.getInstance().getReference("member_group");
        dbUser = FirebaseDatabase.getInstance().getReference("users").child(idUser);

        initToolbar();
        if (mode.equalsIgnoreCase("edit")) {
            initUI(id);
        }
        else {
            etIdGrup.setVisibility(View.GONE);
        }
    }

    @OnClick(R.id.btn_simpan)
    public void buttonSimpan(){
        loading.show();

        if (!TextUtils.isEmpty(etIdGrup.getText().toString())) {
            idGrup = etIdGrup.getText().toString();
        } else {
            idGrup = String.format("%04d", new Random().nextInt(10000));
        }
        if (!TextUtils.isEmpty(etNamaGrup.getText().toString())) {
            namaGrup = etNamaGrup.getText().toString();
        } else {
            Toast.makeText(this, "Nama Grup tidak boleh kosong !", Toast.LENGTH_SHORT).show();
            return;
        }
        if (!TextUtils.isEmpty(etDeskripsiGrup.getText().toString())) {
            deskripsiGrup = etDeskripsiGrup.getText().toString();
        } else {
            deskripsiGrup = "";
        }
        if (mode.equalsIgnoreCase("edit")) {
            dbGrup.setValue(new Grup(idGrup, namaGrup, deskripsiGrup));
        }
        else {
            final String idNew = dbGrup.push().getKey();
            final Grup grup = new Grup(idGrup, namaGrup, deskripsiGrup);
            grup.setId(idNew);
            dbGrup = FirebaseDatabase.getInstance().getReference("groups").child(idNew);
            dbGrup.setValue(grup);
            dbUser.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    User user = dataSnapshot.getValue(User.class);
                    dbAnggota = FirebaseDatabase.getInstance().getReference("member_group").child(idNew).child(idUser);
                    dbAnggota.setValue(user);
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }
        loading.dismiss();
        Toast.makeText(FormGroupActivity.this, "Data berhasil disimpan !", Toast.LENGTH_SHORT).show();
        finish();
    }

    private void initUI(String id) {
        loading2.show();
        dbGrup = FirebaseDatabase.getInstance().getReference("groups").child(id);
        dbGrup.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                grup = dataSnapshot.getValue(Grup.class);
                etId.setText(grup.getId());
                etIdGrup.setText(grup.getIdGrup());
                etNamaGrup.setText(grup.getNamaGrup());
                etDeskripsiGrup.setText(grup.getDeskripsiGrup());
                loading2.dismiss();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                loading2.dismiss();
            }
        });
    }

    private void initToolbar() {
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        if (mode.equalsIgnoreCase("add")) {
            getSupportActionBar().setTitle("Tambah Grup");
        } else {
            getSupportActionBar().setTitle("Ubah Grup");
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}
