package com.tokosepeda.tikum.activity.admin.user;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.tokosepeda.tikum.R;
import com.tokosepeda.tikum.activity.MainActivity;
import com.tokosepeda.tikum.activity.account.UbahProfilActivity;
import com.tokosepeda.tikum.activity.auth.RegisterActivity;
import com.tokosepeda.tikum.firebase.FirebaseApplication;
import com.tokosepeda.tikum.model.Toko;
import com.tokosepeda.tikum.model.User;
import com.tokosepeda.tikum.utils.DialogUtils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.Random;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class FormUserActivity extends AppCompatActivity {

    @BindView(R.id.image_foto)
    ImageView imageFoto;
    @BindView(R.id.et_id)
    EditText etId;
    @BindView(R.id.et_nama)
    EditText etNama;
    @BindView(R.id.et_email)
    EditText etEmail;
    @BindView(R.id.et_no_hp)
    EditText etNoHp;
    @BindView(R.id.et_password)
    EditText etPassword;
    @BindView(R.id.et_jenis_sepeda)
    EditText etJenisSepeda;

    User user;
    String id, idUser, nama, email, noHp, password, jenisSepeda;
    String mode;

    FirebaseAuth mAuth;
    DatabaseReference dbUser;

    private ProgressDialog loading, loading2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form_user);

        ButterKnife.bind(this);

        loading = DialogUtils.showProgressDialog(this, "Loading", "Saving data..");
        loading2 = DialogUtils.showProgressDialog(this, "Loading", "Loading data..");

        mAuth = ((FirebaseApplication) getApplication()).getFirebaseAuth();
        mode = getIntent().getStringExtra("mode");
        id = getIntent().getStringExtra("id_user");

        initToolbar();

        dbUser = FirebaseDatabase.getInstance().getReference("user");

        if(mode.equalsIgnoreCase("edit")){
            initUI(id);
            etEmail.setFocusable(false);
            etPassword.setVisibility(View.GONE);
        }
        else{
            idUser = String.format("%04d", new Random().nextInt(10000));
            etId.setText(idUser);
        }
    }

    private void initUI(String id) {
        loading2.show();
        dbUser = FirebaseDatabase.getInstance().getReference("users").child(id);
        dbUser.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                user = dataSnapshot.getValue(User.class);
                Glide.with(FormUserActivity.this).load(user.getFoto()).into(imageFoto);
                etId.setText(user.getIdUser());
                etNama.setText(user.getNamaUser());
                etEmail.setText(user.getEmail());
                etNoHp.setText(user.getNomorHp());
                etJenisSepeda.setText(user.getSepeda());
                loading2.dismiss();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                loading2.dismiss();
            }
        });
    }

    @OnClick(R.id.btn_simpan)
    public void buttonSimpan() {
        loading.show();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        idUser = etId.getText().toString();
        if(mode.equalsIgnoreCase("edit")){
            email = etEmail.getText().toString();
        }
        else{
            if (!TextUtils.isEmpty(etEmail.getText().toString())) {
                email = etEmail.getText().toString();
            } else {
                Toast.makeText(this, "Email tidak boleh kosong !", Toast.LENGTH_SHORT).show();
                return;
            }
            if (!TextUtils.isEmpty(etPassword.getText().toString())) {
                password = etPassword.getText().toString();
            } else {
                Toast.makeText(this, "Password tidak boleh kosong !", Toast.LENGTH_SHORT).show();
                return;
            }
        }
        if (!TextUtils.isEmpty(etNama.getText().toString())) {
            nama = etNama.getText().toString();
        } else {
            nama = "";
        }
        if (!TextUtils.isEmpty(etNoHp.getText().toString())) {
            noHp = etNoHp.getText().toString();
        } else {
            noHp = "";
        }
        if (!TextUtils.isEmpty(etJenisSepeda.getText().toString())) {
            jenisSepeda = etJenisSepeda.getText().toString();
        } else {
            jenisSepeda = "";
        }
        if(mode.equalsIgnoreCase("edit")){
            dbUser.setValue(new User(id, idUser, nama, email, noHp, "", jenisSepeda));
        }
        else{
            mAuth.createUserWithEmailAndPassword(email, password).
                    addOnCompleteListener(FormUserActivity.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (!task.isSuccessful()) {
                                Toast.makeText(FormUserActivity.this, "Authentication failed.", Toast.LENGTH_SHORT).show();
                            } else {
                                FirebaseUser user = mAuth.getCurrentUser();
                                String id = user.getUid();

                                dbUser = FirebaseDatabase.getInstance().getReference("users");
                                dbUser.child(id).setValue(new User(id,
                                        idUser,
                                        nama,
                                        email,
                                        noHp,
                                        "",
                                        jenisSepeda));
                            }
                        }
                    });
        }
        loading.dismiss();
        Toast.makeText(this, "Saved !", Toast.LENGTH_SHORT).show();
        finish();
    }

    private void initToolbar() {
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        if(mode.equalsIgnoreCase("add")){
            getSupportActionBar().setTitle("Tambah User");
        }
        else{
            getSupportActionBar().setTitle("Ubah User");
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
