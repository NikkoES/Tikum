package com.tokosepeda.tikum.activity.account;

import android.app.DatePickerDialog;
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
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.bumptech.glide.Glide;
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
import com.mikhaellopez.circularimageview.CircularImageView;
import com.tokosepeda.tikum.R;
import com.tokosepeda.tikum.activity.MainActivity;
import com.tokosepeda.tikum.firebase.FirebaseApplication;
import com.tokosepeda.tikum.fragment.HomeFragment;
import com.tokosepeda.tikum.model.User;
import com.tokosepeda.tikum.utils.DialogUtils;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.UUID;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class UbahProfilActivity extends AppCompatActivity {

    @BindView(R.id.image_foto)
    CircularImageView imageFoto;
    @BindView(R.id.et_id)
    EditText etId;
    @BindView(R.id.et_nama)
    EditText etNama;
    @BindView(R.id.et_email)
    EditText etEmail;
    @BindView(R.id.et_no_hp)
    EditText etNoHp;
    @BindView(R.id.rg_jenis_kelamin)
    RadioGroup rgJenisKelamin;
    @BindView(R.id.rb_laki)
    RadioButton rbLaki;
    @BindView(R.id.rb_perempuan)
    RadioButton rbPerempuan;
    @BindView(R.id.et_alamat)
    EditText etAlamat;
    @BindView(R.id.et_tempat_lahir)
    EditText etTempatLahir;
    @BindView(R.id.et_tanggal_lahir)
    EditText etTanggalLahir;
    @BindView(R.id.et_jenis_sepeda)
    EditText etJenisSepeda;
    @BindView(R.id.et_jenis_outfit)
    EditText etJenisOutfit;

    User user;
    String id, idUser, nama, email, noHp, jenisKelamin, alamat, tempatLahir, tanggalLahir, jenisSepeda, jenisOutfit;

    FirebaseAuth mAuth;
    DatabaseReference dbUser;

    FirebaseStorage storage;
    StorageReference storageReference;

    private ProgressDialog loading, loading2;

    private Uri filePath;

    private final int PICK_IMAGE_REQUEST = 71;

    Calendar myCalendar = Calendar.getInstance();
    DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            myCalendar.set(Calendar.YEAR, year);
            myCalendar.set(Calendar.MONTH, monthOfYear);
            myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

            String myFormat = "dd/MM/yyyy";
            SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

            etTanggalLahir.setText(sdf.format(myCalendar.getTime()));
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ubah_profil);
        ButterKnife.bind(this);
        initToolbar();

        loading = DialogUtils.showProgressDialog(this, "Loading", "Saving data..");
        loading2 = DialogUtils.showProgressDialog(this, "Loading", "Loading data..");

        mAuth = ((FirebaseApplication) getApplication()).getFirebaseAuth();
        id = mAuth.getCurrentUser().getUid();
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();

        initUI(id);
    }

    private void initUI(String id) {
        loading2.show();
        dbUser = FirebaseDatabase.getInstance().getReference("users").child(id);
        dbUser.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                user = dataSnapshot.getValue(User.class);
                Glide.with(getApplicationContext()).load(user.getFoto()).into(imageFoto);
                etId.setText(user.getIdUser());
                etNama.setText(user.getNamaUser());
                etEmail.setText(user.getEmail());
                etNoHp.setText(user.getNomorHp());
                if (user.getJenisKelamin().equalsIgnoreCase("Laki-laki")) {
                    rbLaki.setChecked(true);
                } else if (user.getJenisKelamin().equalsIgnoreCase("Perempuan")) {
                    rbPerempuan.setChecked(true);
                }
                etAlamat.setText(user.getAlamat());
                etTempatLahir.setText(user.getTempatLahir());
                etTanggalLahir.setText(user.getTanggalLahir());
                etJenisSepeda.setText(user.getSepeda());
                etJenisOutfit.setText(user.getOutfit());
                loading2.dismiss();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                loading2.dismiss();
            }
        });
    }

    @OnClick(R.id.et_tanggal_lahir)
    public void getTanggalLahir() {
        new DatePickerDialog(this, date, myCalendar
                .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                myCalendar.get(Calendar.DAY_OF_MONTH)).show();
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
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK
                && data != null && data.getData() != null) {
            filePath = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                imageFoto.setImageBitmap(bitmap);
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

            StorageReference ref = storageReference.child("users/" + UUID.randomUUID().toString());
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

    @OnClick(R.id.btn_simpan)
    public void buttonSimpan() {
        loading.show();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        idUser = etId.getText().toString();
        if (!TextUtils.isEmpty(etNama.getText().toString())) {
            nama = etNama.getText().toString();
        } else {
            nama = "";
        }
        if (!TextUtils.isEmpty(etEmail.getText().toString())) {
            email = etEmail.getText().toString();
            user.updateEmail(email);
        } else {
            Toast.makeText(this, "Email tidak boleh kosong !", Toast.LENGTH_SHORT).show();
        }
        if (!TextUtils.isEmpty(etNoHp.getText().toString())) {
            noHp = etNoHp.getText().toString();
        } else {
            noHp = "";
        }
        int genderId = rgJenisKelamin.getCheckedRadioButtonId();
        if (genderId == R.id.rb_laki) {
            jenisKelamin = "Laki-laki";
        } else if (genderId == R.id.rb_perempuan) {
            jenisKelamin = "Perempuan";
        } else {
            jenisKelamin = "";
        }
        if (!TextUtils.isEmpty(etAlamat.getText().toString())) {
            alamat = etAlamat.getText().toString();
        } else {
            alamat = "";
        }
        if (!TextUtils.isEmpty(etTempatLahir.getText().toString())) {
            tempatLahir = etTempatLahir.getText().toString();
        } else {
            tempatLahir = "";
        }
        if (!TextUtils.isEmpty(etTanggalLahir.getText().toString())) {
            tanggalLahir = etTanggalLahir.getText().toString();
        } else {
            tanggalLahir = "";
        }
        if (!TextUtils.isEmpty(etJenisSepeda.getText().toString())) {
            jenisSepeda = etJenisSepeda.getText().toString();
        } else {
            jenisSepeda = "";
        }
        if (!TextUtils.isEmpty(etJenisOutfit.getText().toString())) {
            jenisOutfit = etJenisOutfit.getText().toString();
        } else {
            jenisOutfit = "";
        }
        if (imageFoto.getDrawable()==null){
            loading.dismiss();
            Toast.makeText(this, "Image belum diisi !", Toast.LENGTH_SHORT).show();
        }
        else {
            uploadImage(new MyCallback() {
                @Override
                public void onCallback(String image) {
                    dbUser.setValue(new User(id, idUser, nama, email, noHp, jenisKelamin, alamat, tempatLahir, tanggalLahir, image, jenisSepeda, jenisOutfit));
                    loading.dismiss();
                    Toast.makeText(UbahProfilActivity.this, "Data berhasil disimpan !", Toast.LENGTH_SHORT).show();
                    finish();
                }
            });
        }
    }

    private void initToolbar() {
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Ubah Profil");
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}
