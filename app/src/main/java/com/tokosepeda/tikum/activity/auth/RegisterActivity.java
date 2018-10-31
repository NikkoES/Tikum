package com.tokosepeda.tikum.activity.auth;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.tokosepeda.tikum.R;
import com.tokosepeda.tikum.activity.MainActivity;
import com.tokosepeda.tikum.model.User;
import com.tokosepeda.tikum.utils.DialogUtils;

import java.util.Random;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class RegisterActivity extends AppCompatActivity {

    @BindView(R.id.et_nama)
    EditText etNama;
    @BindView(R.id.et_email)
    EditText etEmail;
    @BindView(R.id.et_no_hp)
    EditText etPhoneNumber;
    @BindView(R.id.et_password)
    EditText etPassword;

    String nama, email, phone, password;

    private ProgressDialog loading;

    private FirebaseAuth mAuth;

    DatabaseReference databaseUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        ButterKnife.bind(this);
        loading = DialogUtils.showProgressDialog(this, "Loading", "Registering your account");

        mAuth = FirebaseAuth.getInstance();
    }

    @OnClick(R.id.btn_register)
    public void register() {
        if (TextUtils.isEmpty(etNama.getText().toString()) || TextUtils.isEmpty(etEmail.getText().toString()) || TextUtils.isEmpty(etPhoneNumber.getText().toString())|| TextUtils.isEmpty(etPassword.getText().toString())) {
            Toast.makeText(this, getString(R.string.data_belum_lengkap), Toast.LENGTH_SHORT).show();
        } else {
            nama = etNama.getText().toString();
            email = etEmail.getText().toString();
            phone = etPhoneNumber.getText().toString();
            password = etPassword.getText().toString();
            registerData(nama, email, phone, password);
        }
    }

    private void registerData(final String nama, final String email, final String phone, String password) {
        loading.show();
        mAuth.createUserWithEmailAndPassword(email, password).
                addOnCompleteListener(RegisterActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (!task.isSuccessful()) {
                            Toast.makeText(RegisterActivity.this, "Authentication failed.", Toast.LENGTH_SHORT).show();
                        } else {
                            FirebaseUser user = mAuth.getCurrentUser();
                            String id = user.getUid();
                            String idUser = String.format("%04d", new Random().nextInt(10000));

                            databaseUser = FirebaseDatabase.getInstance().getReference("users");
                            databaseUser.child(id).setValue(new User(id,
                                    idUser,
                                    nama,
                                    email,
                                    phone,
                                    "",
                                    "",
                                    "",
                                    "",
                                    "",
                                    "",
                                    ""));

                            Toast.makeText(RegisterActivity.this, "Registration Successful", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(RegisterActivity.this, MainActivity.class));
                            finish();
                            loading.dismiss();
                        }
                    }
                });

    }

    @OnClick(R.id.btn_to_login)
    public void toLogin() {
        startActivity(new Intent(this, LoginActivity.class));
        finish();
        overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(this, LoginActivity.class));
        finish();
        overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
    }
}
