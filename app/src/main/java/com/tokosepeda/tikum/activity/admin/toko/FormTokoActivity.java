package com.tokosepeda.tikum.activity.admin.toko;

import android.app.ProgressDialog;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.tokosepeda.tikum.R;
import com.tokosepeda.tikum.activity.account.UbahProfilActivity;
import com.tokosepeda.tikum.firebase.FirebaseApplication;
import com.tokosepeda.tikum.model.Toko;
import com.tokosepeda.tikum.model.User;
import com.tokosepeda.tikum.utils.DialogUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class FormTokoActivity extends AppCompatActivity {

    //TODO: tambahin place picker android di alamat

    @BindView(R.id.image_toko)
    ImageView imageToko;
    @BindView(R.id.et_nama_toko)
    EditText etNamaToko;
    @BindView(R.id.et_email)
    EditText etEmail;
    @BindView(R.id.et_no_hp)
    EditText etNomorToko;
    @BindView(R.id.et_alamat)
    EditText etAlamat;
    @BindView(R.id.et_latitude)
    EditText etLatitude;
    @BindView(R.id.et_longitude)
    EditText etLongitude;
    @BindView(R.id.et_sparepart)
    EditText etSparepart;

    Toko toko;
    String id, namaToko, email, nomorToko, alamat, latitude, longitude, sparepart, image;
    String mode;

    FirebaseAuth mAuth;
    DatabaseReference dbToko;

    private ProgressDialog loading, loading2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form_toko);
        ButterKnife.bind(this);

        loading = DialogUtils.showProgressDialog(this, "Loading", "Saving data..");
        loading2 = DialogUtils.showProgressDialog(this, "Loading", "Loading data..");

        mAuth = ((FirebaseApplication) getApplication()).getFirebaseAuth();
        mode = getIntent().getStringExtra("mode");
        id = getIntent().getStringExtra("id_toko");

        initToolbar();
        if(mode.equalsIgnoreCase("edit")){
            initUI(id);
        }
    }

    @OnClick(R.id.btn_simpan)
    public void buttonSimpan() {
        loading.show();

        if (!TextUtils.isEmpty(etNamaToko.getText().toString())) {
            namaToko = etNamaToko.getText().toString();
        } else {
            Toast.makeText(this, "Nama Toko tidak boleh kosong !", Toast.LENGTH_SHORT).show();
            return;
        }
        if (!TextUtils.isEmpty(etEmail.getText().toString())) {
            email = etEmail.getText().toString();
        } else {
            email = "";
        }
        if (!TextUtils.isEmpty(etNomorToko.getText().toString())) {
            nomorToko = etNomorToko.getText().toString();
        } else {
            nomorToko = "";
        }
        if (!TextUtils.isEmpty(etAlamat.getText().toString())) {
            alamat = etAlamat.getText().toString();
        } else {
            alamat = "";
        }
        if (!TextUtils.isEmpty(etLatitude.getText().toString())) {
            latitude = etLatitude.getText().toString();
        } else {
            latitude = "";
        }
        if (!TextUtils.isEmpty(etLongitude.getText().toString())) {
            longitude = etLongitude.getText().toString();
        } else {
            longitude = "";
        }
        if (!TextUtils.isEmpty(etSparepart.getText().toString())) {
            sparepart = etSparepart.getText().toString();
        } else {
            sparepart = "";
        }
        if(mode.equalsIgnoreCase("edit")){
            dbToko.setValue(new Toko(id, namaToko, email, nomorToko, alamat, latitude, longitude, sparepart, ""));
        }
        else{
            String idNew = dbToko.push().getKey();
            Toko toko = new Toko(idNew, namaToko, email, nomorToko, alamat, latitude, longitude, sparepart, "");
            dbToko = FirebaseDatabase.getInstance().getReference("toko").child(idNew);
            dbToko.setValue(toko);
        }
        loading.dismiss();
        Toast.makeText(this, "Data berhasil disimpan !", Toast.LENGTH_SHORT).show();
        finish();
    }

    private void initUI(String id) {
        loading2.show();
        dbToko = FirebaseDatabase.getInstance().getReference("toko").child(id);
        dbToko.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                toko = dataSnapshot.getValue(Toko.class);
                Glide.with(FormTokoActivity.this).load(toko.getImageToko()).into(imageToko);
                etNamaToko.setText(toko.getNamaToko());
                etEmail.setText(toko.getEmailToko());
                etNomorToko.setText(toko.getNoHp());
                etAlamat.setText(toko.getAlamatToko());
                etLatitude.setText(toko.getLatToko());
                etLongitude.setText(toko.getLongToko());
                etSparepart.setText(toko.getSparePart());
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
        if(mode.equalsIgnoreCase("add")){
            getSupportActionBar().setTitle("Tambah Toko");
        }
        else{
            getSupportActionBar().setTitle("Ubah Toko");
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
