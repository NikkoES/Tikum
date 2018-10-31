package com.tokosepeda.tikum.activity.auth;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.tokosepeda.tikum.R;
import com.tokosepeda.tikum.activity.MainActivity;
import com.tokosepeda.tikum.activity.admin.DashboardAdminActivity;
import com.tokosepeda.tikum.firebase.FirebaseApplication;
import com.tokosepeda.tikum.utils.DialogUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class LoginActivity extends AppCompatActivity {

    @BindView(R.id.et_email)
    EditText etEmail;
    @BindView(R.id.et_password)
    EditText etPassword;

    String email, password;

    private FirebaseAuth mAuth;

    private ProgressDialog loading;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);

        loading = DialogUtils.showProgressDialog(this, "Loading", "Checking Data");

        mAuth = ((FirebaseApplication) getApplication()).getFirebaseAuth();
        SharedPreferences pref = getSharedPreferences("session", Activity.MODE_PRIVATE);

        if(pref.getString("user", "").equalsIgnoreCase("admin")){
            startActivity(new Intent(this, DashboardAdminActivity.class));
        }
        else{
            ((FirebaseApplication) getApplication()).checkUserLogin(this);
        }
    }

    @OnClick(R.id.btn_login)
    public void login() {
        loading.show();
        if (TextUtils.isEmpty(etEmail.getText().toString()) || TextUtils.isEmpty(etPassword.getText().toString())) {
            Toast.makeText(this, getString(R.string.data_kosong), Toast.LENGTH_SHORT).show();
            loading.dismiss();
        }
        else {
            email = etEmail.getText().toString();
            password = etPassword.getText().toString();

            if(email.equalsIgnoreCase("admin") && password.equalsIgnoreCase("admin")){
                SharedPreferences.Editor editor = getSharedPreferences("session", MODE_PRIVATE).edit();
                editor.putString("user", "admin");
                editor.apply();
                startActivity(new Intent(this, DashboardAdminActivity.class));
            }
            mAuth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (!task.isSuccessful()) {
                                Toast.makeText(LoginActivity.this, "Failed to login", Toast.LENGTH_SHORT).show();
                            }
                            else {
                                Intent profileIntent = new Intent(LoginActivity.this, MainActivity.class);
                                startActivity(profileIntent);
                                finish();

                                loading.dismiss();
                            }
                        }
                    });
        }
    }

    @OnClick(R.id.btn_to_register)
    public void toRegister() {
        startActivity(new Intent(this, RegisterActivity.class));
        finish();
        overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
    }

    @OnClick(R.id.btn_to_forgot_password)
    public void toForgotPassword() {
        startActivity(new Intent(getApplicationContext(), ForgotPasswordActivity.class));
        finish();
        overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
    }
}
