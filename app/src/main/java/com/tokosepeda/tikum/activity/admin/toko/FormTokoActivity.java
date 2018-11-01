package com.tokosepeda.tikum.activity.admin.toko;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.tokosepeda.tikum.R;
import com.tokosepeda.tikum.activity.account.UbahProfilActivity;
import com.tokosepeda.tikum.activity.admin.user.FormUserActivity;
import com.tokosepeda.tikum.firebase.FirebaseApplication;
import com.tokosepeda.tikum.model.Toko;
import com.tokosepeda.tikum.model.User;
import com.tokosepeda.tikum.utils.DialogUtils;

import java.io.IOException;
import java.util.UUID;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class FormTokoActivity extends AppCompatActivity {

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

    private int PLACE_PICKER_REQUEST = 1;

    Toko toko;
    String id, namaToko, email, nomorToko, alamat, latitude, longitude, sparepart, image;
    String mode;

    FirebaseAuth mAuth;
    DatabaseReference dbToko;

    FirebaseStorage storage;
    StorageReference storageReference;

    private Uri filePath;

    private ProgressDialog loading, loading2;

    private final int PICK_IMAGE_REQUEST = 71;

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

        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();

        dbToko = FirebaseDatabase.getInstance().getReference("toko");

        initToolbar();
        if (mode.equalsIgnoreCase("edit")) {
            initUI(id);
        }
    }

    @OnClick(R.id.et_alamat)
    public void getLocation() {
        PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();
        try {
            //menjalankan place picker
            startActivityForResult(builder.build(FormTokoActivity.this), PLACE_PICKER_REQUEST);
        } catch (GooglePlayServicesRepairableException e) {
            e.printStackTrace();
        } catch (GooglePlayServicesNotAvailableException e) {
            e.printStackTrace();
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
        if(imageToko.getDrawable()==null){
            loading.dismiss();
            Toast.makeText(this, "Image belum diisi !", Toast.LENGTH_SHORT).show();
        }
        else{
            uploadImage(new MyCallback() {
                @Override
                public void onCallback(String image) {
                    if (mode.equalsIgnoreCase("edit")) {
                        dbToko.setValue(new Toko(id, namaToko, email, nomorToko, alamat, latitude, longitude, sparepart, image));
                    } else {
                        String idNew = dbToko.push().getKey();
                        Toko toko = new Toko(idNew, namaToko, email, nomorToko, alamat, latitude, longitude, sparepart, image);
                        dbToko = FirebaseDatabase.getInstance().getReference("toko").child(idNew);
                        dbToko.setValue(toko);
                    }
                    loading.dismiss();
                    Toast.makeText(FormTokoActivity.this, "Data berhasil disimpan !", Toast.LENGTH_SHORT).show();
                    finish();
                }
            });
        }
    }

    private void initUI(String id) {
        loading2.show();
        dbToko = FirebaseDatabase.getInstance().getReference("toko").child(id);
        dbToko.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                toko = dataSnapshot.getValue(Toko.class);
                Glide.with(getApplicationContext()).load(toko.getImageToko()).into(imageToko);
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

    @OnClick(R.id.btn_ganti_foto)
    public void buttonGantiFoto() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PLACE_PICKER_REQUEST) {
            if (resultCode == RESULT_OK) {
                Place place = PlacePicker.getPlace(data, this);
                etAlamat.setText(place.getAddress());
                etLatitude.setText(String.valueOf(place.getLatLng().latitude));
                etLongitude.setText(String.valueOf(place.getLatLng().longitude));
            }
        } else if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK
                && data != null && data.getData() != null) {
            filePath = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                imageToko.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public interface MyCallback {
        void onCallback(String value);
    }

    private void uploadImage(final MyCallback myCallback) {

        if (filePath != null) {
            final ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setTitle("Uploading...");
            progressDialog.show();

            StorageReference ref = storageReference.child("toko/" + UUID.randomUUID().toString());
            ref.putFile(filePath)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            progressDialog.dismiss();
                            myCallback.onCallback(taskSnapshot.getDownloadUrl().toString());
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            progressDialog.dismiss();
                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                            double progress = (100.0 * taskSnapshot.getBytesTransferred() / taskSnapshot
                                    .getTotalByteCount());
                            progressDialog.setMessage("Uploaded " + (int) progress + "%");
                        }
                    });
        }
    }

    private void initToolbar() {
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        if (mode.equalsIgnoreCase("add")) {
            getSupportActionBar().setTitle("Tambah Toko");
        } else {
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
